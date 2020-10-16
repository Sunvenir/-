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
        <div style="width:100%;height:200px;margin-top:80px;">
          <div class="search_logo"></div>
          <div class="classTitle">课程：</div>
          <el-select class="search_input" v-model="classId" :popper-append-to-body="false"  placeholder="请选择课程">
            <el-option
            v-for="item in classList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            </el-option>
          </el-select>
          <div class="search_button" @click="invite"></div>
          <div class="search_line"></div>
        </div>
        <div style="width:920px;margin-left:40px;">
          <div style="width:100%;">
            <div class="infoHeader" v-show="result.length!=0" style="flaot:left;width:100%;">课程申请列表</div>
            <div v-for="item in result" style="flaot:left;width:100%;height:59px;border-bottom:1px solid #cccccc;">
              <div class="userInfo">*****申请人：{{item.userid}}——{{item.username}}</div>
              <div class="know" @click="accept(item.userid)"></div>
              <div class="deny" @click="deny(item.userid)"></div>
            </div>
            <div class="infoHeader" v-show="circleList.length!=0" style="flaot:left;width:100%;">社区申请列表</div>
            <div v-for="item in circleList" style="flaot:left;width:100%;height:59px;border-bottom:1px solid #cccccc;">
              <div class="userInfo">*****申请人：{{item.userid}}——{{item.username}}</div>
              <div class="know" @click="acceptCircle(item.userid)"></div>
              <div class="deny" @click="denyCircle(item.userid)"></div>
            </div>
            <div class="infoHeader" style="border-bottom:1px solid #cccccc;flaot:left;width:100%;" v-show="member.length!=0">课程参与列表</div>
            <div v-for="(item,index) in member" style="flaot:left;width:100%;height:59px;border-bottom:1px solid #cccccc;">
              <div class="userInfo">*****参与人：{{item.userid}}——{{item.username}}
                <div style="float:right;height:100%;" :id="'time'+index">{{getProc(item.userid,index)}}</div>
              </div>
            </div>
            <div style="width:100%;height:30px;"></div>
          </div>
        </div>
        <div class="no_result" v-show="result.length==0 && member.length==0 && circleList.length==0">暂无搜索结果</div>
      </div>
    </div>
    <el-dialog title="请输入学生学号并以@符号分割" :visible.sync="dialogVisible">
      <el-input v-model.trim="idList" placeholder="输入学生学号"></el-input>
      <div class="invite_button" @click="inviteStudents"></div>
    </el-dialog>
  </el-container>
</template>
<script>
import Header from "./components/Header";
export default {
  components: {
    Header
  },
  mounted() {
    var _that = this
    this.resize()
    window.onresize = function(){
      _that.resize()
    }
    this.$axios.post("/api/searchcourse",{
        id:window.sessionStorage.getItem("uid"),
        key:3
      })
      .then(res => {
        if(res.data.code === 200){
          this.classData = res.data.detail
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
  },
  data() {
    return {
      activeIndex:"5",
      page: "page1",
      hideTop: true,
      // loginBg: "",
      avatar: '/avatar/avatar.png',
      nickName: '未登录',
      classId: '',
      classList: [],
      result:[],
      member:[],
      circleList:[],
      idList:'',
      dialogVisible: false
    };
  },
  watch:{
    classId(val){
      this.renew()
    }
  },
  methods: {
    resize(){
      this.$refs.bg.style.height = window.innerHeight + 'px'
      this.$refs.inner.style.height = window.innerHeight+17 + 'px'
      var left = (window.innerWidth - 1000)/2
      left = left>0?left:0
      this.page = left>0?'page1':'page2'
      this.$refs.bg.style.left = left + 'px'
    },
    invite(){
      if(this.classId==''){
        this.$message.error('未选择课程！')
      }
      else{
        this.dialogVisible = true
      }
    },
    inviteStudents(){
      if(this.idList==""||this.idList==null) return
      this.$axios.post("/api/studentintocourse",{
        courseid:this.classId,
        studentid:this.idList
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('成功邀请学生进入课程~');
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
      this.renew()
    },
    renew(){
      this.result=[]
      this.member=[]
      this.circleList=[]
      this.$axios.post("/api/getcourseapplylist",{
        courseid:this.classId
      })
      .then(res => {
        if(res.data.code === 200){
          this.result=res.data.detail
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })

      this.$axios.post("/api/getstudentlist",{
        courseid:this.classId
      })
      .then(res => {
        if(res.data.code === 200){
          this.member=res.data.detail
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })

      this.$axios.post("/api/getcommunityapplylist",{
        courseid:this.classId
      })
      .then(res => {
        if(res.data.code === 200){
          console.log(res.data)
          this.circleList=res.data.detail
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    },
    accept(uid){
      this.$axios.post("/api/courseapplyaccept",{
        courseid:this.classId,
        studentid:uid
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('该生已成功加入课程！');
          this.renew()
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    },
    deny(uid){
      this.$axios.post("/api/courseapplyrefuse",{
        courseid:this.classId,
        studentid:uid
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('已拒绝该生的请求！');
          this.renew()
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    },
    acceptCircle(uid){
      this.$axios.post("/api/communityapplyaccept",{
        courseid:this.classId,
        studentid:uid
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('该生已成功加入社区！');
          this.renew()
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    },
    denyCircle(uid){
      this.$axios.post("/api/communityapplyrefuse",{
        courseid:this.classId,
        studentid:uid
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('已拒绝该生加入社区！');
          this.renew()
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    },
    getProc(uid,index){
      this.$axios.post("/api/getcoursewatchtime",{
      userid:uid,
      courseid:this.classId
    })
      .then(res => {
        console.log(res.data)
        if(res.data.code === 200){
          document.getElementById('time'+ index).innerText=
          parseInt(res.data.detail.watchtime/60000)+"/"+parseInt(res.data.detail.sumtime/60000)
          +"分钟   ("+parseInt(res.data.detail.watchtime/res.data.detail.sumtime*100)+"%)"
        }
        else{
          document.getElementById('time'+ index).innerText= "0/0分钟   (0%)"
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
        document.getElementById('time'+ index).innerText= "0/0分钟   (0%)"
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
.bg{
  position: absolute;
  top: 0;
  width: 1000px;
  background-color: rgba(225,225,225,0.8);
  box-shadow: 0 0 15px black;
  overflow: hidden;
}
.inner{
  width: 1017px;
  overflow: scroll;
}
.classTitle{
  height: 20px;
  width: 50px;
  font-size: 16px;
  float: left;
  position: relative;
  left: 100px;
  top: 90px;
  font-weight: bold;
}
.search_logo{
  background: url('./assets/icon/logo.png');
  background-size: contain;
  height: 80px;
  width: 240px;
  float: left;
  position: relative;
  left: 100px;
  top: 60px;
}
.search_input{
  width: 300px;
  float: left;
  position: relative;
  left: 120px;
  top: 80px;
  box-shadow: 0 0 8px black;
}
.search_button{
  height: 80px;
  width: 80px;
  border-radius: 40px;
  float: left;
  position: relative;
  left: 140px;
  top: 55px;
  background: url('./assets/icon/invite.png');
  cursor: pointer;
  background-size: contain;
}
.invite_button{
  height: 60px;
  width: 60px;
  border-radius: 30px;
  margin: 0 auto;
  align-self: center;
  background: url('./assets/icon/invite.png');
  cursor: pointer;
  margin-top: 20px;
  background-size: contain;
}
.search_line{
  width: 990px;
  float: left;
  position: relative;
  left: 5px;
  top: 90px;
  height: 33px;
  background: url('./assets/icon/line.png') no-repeat;
  background-size: contain;
}
.infoHeader{
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF);
  font-size: 25px;
  line-height: 59px;
  overflow: hidden;
  max-height: 59px;
  text-align: center;
  margin-left: 10px;
  margin-right: 10px;
  width: 100%;
  height: 59px;
  float: left;
}
.userInfo{
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF);
  font-size: 20px;
  line-height: 59px;
  overflow: hidden;
  max-height: 59px;
  text-align: left;
  margin-left: 10px;
  margin-right: 10px;
  width: 780px;
  height: 59px;
  float: left;
}
.know{
  height: 49px;
  width: 49px;
  border-radius: 24.5px;
  float: left;
  margin: 5px;
  cursor: pointer;
  background: url('./assets/icon/accept.png');
  background-size: contain;
}
.deny{
  height: 49px;
  width: 49px;
  border-radius: 24.5px;
  float: left;
  margin: 5px;
  cursor: pointer;
  background: url('./assets/icon/deny.png');
  background-size: contain;
}
.no_result{
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF);
  font-size: 40px;
  line-height: 200px;
  text-align: center;
  width: 100%;
  height: 200px;
  text-shadow: 0 0 5px #aaaaaa;
  color: #666666;
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
/deep/ .el-icon-close:hover{
  color:red !important;
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
