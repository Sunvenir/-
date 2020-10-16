<template>
  <el-container :class="page" ref="page">
    <vue-particles
      color="#111111"
      :particleOpacity="0.7"
      :particlesNumber="80"
      shapeType="star"
      :particleSize="4"
      linesColor="#000000"
      :linesWidth="2"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="3"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
      class="cash"
    >
    </vue-particles>
    <el-header>
      <Header 
      :activeIndex="activeIndex" 
      ref="header"
      ></Header>
    </el-header>
  	<div class="bg" ref='bg'>
  		<div class="inner" ref="inner">
        <div class="form-up">
          <div class="form">
            <div class="title">{{way=='create'?'新建课程':'更新课程'}}</div>
            <el-upload class="avatar-uploader" :before-upload="editCover" action="#" :class="'cover-'+way"
            :disabled="way!='create'">
              <el-image :src="data.cover" fit="fill"></el-image>
            </el-upload>
            <div class="list">
              <el-form label-position="left" label-width="80px" style="width:280px;margin-left:15px;">
                <el-form-item label="课程名：" :class="'create-'+(way=='create')">
                  <el-input v-model.trim="data.className" placeholder="请输入课程名称" maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="视频名：" class="create-true">
                  <el-input v-model.trim="data.videoName" placeholder="请输入视频名称" maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="类型：" :class="'create-'+(way=='create')">
                  <el-select v-model="data.type" :popper-append-to-body="false">
                    <el-option
                    v-for="item in types"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="学校：" :class="'create-'+(way=='create')">
                  <el-input v-model.trim="data.schoolName" placeholder="请输入学校" maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="简介：" :class="'create-'+(way=='create')">
                  <el-input v-model.trim="data.brief" placeholder="请输入简介" maxlength="50"></el-input>
                </el-form-item>
                <el-form-item label="课程名：" :class="'create-'+(way!='create')" style="position:relative;bottom:310px;">
                  <el-select v-model="data.classId" :popper-append-to-body="false">
                    <el-option
                    v-for="item in classList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item >
                <!-- "https://jsonplaceholder.typicode.com/posts/"  -->
                <el-form-item label="操作：" style="position:relative;bottom:50px;">
                  <el-upload
                  :popper-append-to-body="false"
                  class="upload-demo"
                  ref="upload"
                  :action="'http://localhost:8888/api/'+way" 
                  :data="data"
                  :on-preview="handlePreview"
                  :on-remove="handleRemove"
                  :file-list="fileList"
                  :auto-upload="false"
                  :on-change="onChangeFile" 
                  :before-upload="beforeUpload" 
                  :on-success="uploadSuccess" 
                  :on-error="uploadError" 
                  :on-progress="doUploading"
                  >
                    <div slot="trigger" class="file-select"></div>
                    <div @click="submitUpload" class="submit"></div>
                    <div @click="submitDelete" :class="'create-'+(way!='create')+' delete'"></div>
                  </el-upload>
                </el-form-item>
              </el-form>
            </div>
            <div class="create-text" @click='goCreate'>新建课程<div class="check" v-show="way=='create'"></div></div>
            <div class="renew-text" @click='goRenew'>更新课程<div class="check" v-show="way=='renew'"></div></div>
          </div>
        </div>
      </div>
    </div>
    <CoverSelect v-show="showCoverSelect" @hideCutter="hideCutter" ref="coverSelect" @renewCover="renewCover"></CoverSelect>
    <video v-show="false" ref="tmp"></video>
  </el-container>
</template>
<script>
import Header from "./components/Header";
import CoverSelect from "./components/CoverSelect.vue"
export default {
  components: {
    Header,
    CoverSelect
  },
  mounted() {
    var _that = this
    this.resize()
    window.onresize = function(){
      _that.resize()
    }
    if(window.sessionStorage.getItem("uid")==""||window.sessionStorage.getItem("uid")==null){
      this.$message.error("请先登录~")
      this.$router.replace("/")
      return
    }
    if(window.sessionStorage.getItem("power")!='1'||window.sessionStorage.getItem("uid")=="12345678"){
      this.$message.error("只有老师才能上传视频哦~")
      this.$router.replace("/")
      return
    }
  },
  data() {
    return {
      activeIndex:"4",
      uploading: false,
      showCoverSelect: false,
      page: 'page1',
      avatar: '/avatar/avatar.png',
      nickName: '未登录',
      video:new File(["blank"], "./assets/blank.mp4"),
      classList: [{value:'7',label:'test'}],
      classData: [],
      lessonList: [],
      lessonData: [],
      totalTime: 0,
      fileCnt: 0,
      way: 'create',
      data: {
        classId: '',
        className: '',
        schoolName: '',
        videoName: '',
        type: 'shu',
        brief: '',
        cover: '/cover.png',
        teacher: window.sessionStorage.getItem("uid")
      },
      fileList: [],
      types: [{
          value: 'shu',
          label: '数学类'
        }, {
          value: 'gong',
          label: '工科类'
        }, {
          value: 'li',
          label: '理科类'
        }, {
          value: 'wen',
          label: '文科类'
        }, {
          value: 'wai',
          label: '外语类'
        }, {
          value: 'yi',
          label: '医学类'
        },{
          value: 'fa',
          label: '法学类'
        }, {
          value: 'jing',
          label: '经济类'
        }, {
          value: 'art',
          label: '艺术类'
        }],

    };
  },
  methods: {
    resize(){
      this.$refs.bg.style.height = window.innerHeight + 'px'
      this.$refs.inner.style.height = window.innerHeight+17 + 'px'
      var left = (window.innerWidth - 1000)/2
      left = left>0?left:0
      this.page = left>0?'page1':'page2'
      this.$refs.bg.style.left = left + 'px'
      this.$refs.coverSelect.resize(left+100)
    },
    init(){
      this.data.className = ''
      this.data.schoolName = ''
      this.data.videoName = ''
      this.data.type = 'shu'
      this.data.brief = ''
      this.data.classId = ''
      this.data.time = 0
      this.data.cover = this.way=='create'?'/cover.png':'pic1.png'
      this.classList = []
      this.classData = []
      this.lessonList = []
      this.lessonData = []
      this.fileList = []
      this.$refs.upload.clearFiles();
    },
    editCover(file){
      const isJPG = file.type === 'image/jpeg'||file.type === 'image/png';//判定格式
      this.type = file.type;
      if (!isJPG) {
        this.$message.error('封面图片只能是 JPG 或 PNG 格式!');
        return false
      }
      this.$refs.coverSelect.parseFile(file)
      this.showCoverSelect = true
      return false
    },
    renewCover(url){
      if(this.way!='create'){
        return
      }
      this.data.cover=url
      this.showCoverSelect=false
    },
    goCreate(){
      this.way='create'
      this.data.cover = '/cover.png'
      this.init()
    },
    goRenew(){
      this.way='renew'
      this.data.cover='/pic1.png'
      this.$axios.post("/api/searchcourse",{
        id:window.sessionStorage.getItem("uid"),
        key:3
      })
      .then(res => {
        if(res.data.code === 200){
          this.classData = res.data.detail
          console.log(res.data.detail)
          for(var i=0;i<res.data.detail.length;i++){
            this.classList.push({
              'value':res.data.detail[i].courseid,
              'label':res.data.detail[i].coursename
            })
          }
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
      this.init()
    },
    submitUpload() {
      this.way=='create'?this.uploadCreate():this.uplaodRenew()
    },
    beforeUpload(file){
      const isMP4 = file.type === 'video/mp4';
      const isLt200M = file.size / 1024 / 1024 < 200;
      if (!isMP4) {
        this.$message.error('上传头像图片只能是 MP4 格式!');
      }
      if (!isLt200M) {
        this.$message.error('上传视频大小不能超过 200MB!');
      }
      return isMP4 && isLt200M;
    },
    uploadCreate(){
      if(this.uploading){
        this.$message.error('您有课程正在上传，请稍后！');
        return
      }
      if(this.isNull(this.data.className)){
        this.$message.error('请输入课程名称！');
        return
      }
      if(this.isNull(this.data.videoName)){
        this.$message.error('请输入视频名称！');
        return
      }
      if(this.isNull(this.data.schoolName)){
        this.$message.error('请输入学校名称！');
        return
      }
      if(this.isNull(this.data.brief)){
        this.$message.error('请输入视频简介！');
        return
      }
      if(this.fileCnt<1){
        this.$message.error('尚未传入视频文件！');
        return
      }
      this.$refs.upload.submit();
    },
    uplaodRenew(){
      if(this.uploading){
        this.$message.error('您有课程正在上传，请稍后！');
        return
      }
      if(this.isNull(this.data.classId)){
        this.$message.error('请选择课程！');
        return
      }
      if(this.isNull(this.data.videoName)){
        this.$message.error('请输入视频名称！');
        return
      }
      if(this.fileCnt<1){
        this.$message.error('尚未传入视频文件！');
        return
      }
      this.$refs.upload.submit();
    },
    submitDelete(){
      if(this.isNull(this.data.classId)){
        this.$message.error('请选择课程！');
        return
      }
      this.$alert('所有视频将被删除，确定删除课程？', '提醒', {
        confirmButtonText: '确定',
        callback: action => {
          if(action=='cancel') return
          this.$axios.post("/api/deletecourse",{
            courseid:this.data.classId,
          })
          .then(res => {
            if(res.data.code === 200){
              this.$message.success('成功删除课程！');
              this.goRenew()
            }
            else{
              this.$message.error(res.data.message);
            }
          })
          .catch(failResponse=>{
            console.log(failResponse);
            this.$message.error('网络错误');
          })
        }
      });
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    doUploading(){
      this.uploading = true
    },
    uploadSuccess(response, file, fileList){
      this.uploading = false
      if(response.code==200){
        console.log(response)
        this.createCircle(this.data.className,response.detail)
        this.$message.success('课程上传成功！');
        this.init();
      }
      else{
        this.$message.error(response.message);
      }
      fileList=[]
    },
    uploadError(err, file, fileList){
      this.uploading = false
      this.$message.error('课程上传失败！');
      fileList=[]
    },
    onChangeFile(file,fileList){
      if(fileList.length>1){
        fileList.pop()
        this.$message.error('仅支持单文件上传！');
      }
      else{
        this.$refs.tmp.src = URL.createObjectURL(file.raw)
        var _that = this
        this.$refs.tmp.onloadedmetadata = function(){
          _that.totalTime = parseInt(_that.$refs.tmp.duration*1000)
          _that.data.time = _that.totalTime
        }
        this.$refs.tmp.load()
      }
      this.fileCnt = fileList.length
    },
    isNull(str){
      if (str == "") return true;
      var regu = "^[ ]+$";
      var re = new RegExp(regu);
      return re.test(str);
    },
    hideCutter(){
      this.showCoverSelect = false
    },
    createCircle(name,courseid){
      if(this.way!='create') return
      this.$axios.post("/api/createNewCommunity",{
        creator:window.sessionStorage.getItem("uid"),
        courseid:courseid,
        name: name
      })
      .then(res => {
        if(res.data.code === 200){
          console.log(res.data)
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    }
  }
};
</script>
<style scoped>
.page1{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -10;
  overflow: hidden;
  zoom: 1;
  background-color: #fff;
  background-repeat: no-repeat;
  background-size: cover;
  -webkit-background-size: cover;
  -o-background-size: cover;
  background-position: center 0;
}
.page2{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -10;
  overflow: scroll;
  zoom: 1;
  background-color: #fff;
  background-repeat: no-repeat;
  background-size: cover;
  -webkit-background-size: cover;
  -o-background-size: cover;
  background-position: center 0;
}
.inner{
  width: 1017px;
  overflow: scroll;
}
.bg{
  position: absolute;
  top: 0;
  width: 1000px;
  background-color: rgba(225,225,225,0.8);
  box-shadow: 0 0 15px black;
  overflow: hidden;
}
.form-up{
  box-shadow: 0 0 10px #666666;
  width: 960px;
  height: 600px;
  margin-left: 20px;
  margin-top: 80px;
  border-radius: 5px;
  background-color: #eeeeee;
  float: left;
  margin-bottom: 20px;
  overflow: hidden;
}
.head-line{
  font-size: 30px;
  height: 80px;
  line-height: 80px;
  margin-top: 20px;
  width: 100%;
  text-align: center;
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF)
}
.title{
  width: 90%;
  margin-left: 5%;
  text-align: center;
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF);
  font-size:22px;
  margin-top: 10px;
  height: 22px;
  margin-bottom: 10px;
  border-bottom: solid #666666 1px;
}
.form{
  margin-left: 118px;
  margin-top: 40px;
  width: 720px;
  border: 2px solid #666666;
  height: 500px;
}
.cover-create{
  position: relative;
  left: 50px;
  top: 20px;
  float: left;
  height: 160px;
  width: 240px;
  border-radius: 5px;
  box-shadow: 0 0 10px #666666;
  cursor: pointer;
}
.cover-renew{
  position: relative;
  left: 50px;
  top: 20px;
  float: left;
  height: 160px;
  width: 240px;
  border-radius: 5px;
  box-shadow: 0 0 10px #666666;
  cursor: default;
}
.list{
  width: 350px;
  height: 400px;
  float: left;
  border-left: 1px #666666 solid;
  border-right: 1px #666666 solid;
  position: relative;
  left: 80px;
  top: 20px;
}
.create-true{
  transition: all 1s;
  -moz-transition: all 1s;
  -webkit-transition: all 1s;
  -o-transition:all 1s;
}
.create-false{
  transform: scale(0);
  transition: all 1s;
  -moz-transition: all 1s;
  -webkit-transition: all 1s;
  -o-transition:all 1s;
}
.create-text{
  font-size: 25px;
  position: relative;
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF);
  float: left;
  right: 480px;
  top: 250px;
  cursor: pointer;
  height: 80px;
}
.renew-text{
  font-size: 25px;
  position: relative;
  right: 200px;
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF);
  float: left;
  right: 480px;
  top: 260px;
  cursor: pointer;
  height: 80px;
}
.check{
  background: url(./assets/icon/select.png) no-repeat;
  background-size: contain;
  position: relative;
  left: 30px;
  bottom: 20px;
  width: 50px;
  height: 50px;
}
.file-select{
  background: url(./assets/icon/fileSelect.png) no-repeat;
  height: 50px;
  width: 50px;
  border-radius: 25px;
  background-size: contain;
  position: relative;
  right: 80px;
}
.submit{
  background: url(./assets/icon/upload.png) no-repeat;
  height: 50px;
  width: 50px;
  border-radius: 25px;
  background-size: contain;
  cursor: pointer;
  margin-left: 70px;
  position: relative;
  bottom: 65px;
}
.delete{
  background: url(./assets/icon/delete.png) no-repeat;
  height: 50px;
  width: 50px;
  border-radius: 25px;
  background-size: contain;
  cursor: pointer;
  margin-left: 145px;
  position: relative;
  bottom: 115px;
}
.cash{
  position:fixed;
  top:0;
  width:100%;
}
/deep/ .el-input__inner{
  border: none;
  box-shadow: 0 0 5px #666666;
}
/deep/ .el-select-dropdown__item.selected{
  color: red;
}
/deep/ .el-progress .el-progress--line{
  background-color: #666666;
  color: #666666;
  stroke: #666666;
}
/deep/ .el-upload-list{
  position: relative;
  bottom: 120px;
  right: 50px;
}
/deep/ .el-upload-list__item-name{
  color: #666666;
}
/deep/ .el-upload-list__item-name :hover{
  color: red !important;
}
/deep/ .el-upload-list__item :hover{
  color: red !important;
}
/deep/ .el-icon-upload-success{
  color: black !important;
}
/deep/ .el-progress-bar__inner{
  background-color: black;
  color: black;
  box-shadow: 0 0 5px #666666;
}
*{  
    -webkit-touch-callout:none;  /*系统默认菜单被禁用*/   
    -webkit-user-select:none; /*webkit浏览器*/   
    -khtml-user-select:none; /*早期浏览器*/   
    -moz-user-select:none;/*火狐*/   
    -ms-user-select:none; /*IE10*/   
    user-select:none;   
} 
input{  
  -webkit-user-select:auto; /*webkit浏览器*/     
}
textarea{
  -webkit-user-select:auto; /*webkit浏览器*/
}
</style>
