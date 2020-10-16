<template>
  <el-container class="page" ref="page">
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
      v-show="!full"
    >
    </vue-particles>
    <el-header v-show="!full">
      <Header 
      @changeIndex="changeIndex" 
      :avatar="avatar" 
      :nickName="nickName" 
      :hasIn="false"
      ref="header"
      ></Header>
    </el-header>
    <div class="bg" ref='bg'>
      <div class="inner" ref="inner">
        <div class="video-header" v-show="!full">
          <div style="width: 570px;height:100%;float: left;">
          <div :class="type" style="float:left;margin:10px 0 0 30px;border-right:solid #888888 1px;"></div>
          <div class="title">{{title}}</div> 
          <div class="school">{{school}}</div>
          <div class="par">{{par}}人参与</div>
          <div class="proc" v-show="hasIn">进度：{{proc}}</div>
          <div class="proc" v-show="!hasIn">无进度</div> 
          </div>
          <div class="apply participate" @click="participate" v-show="!hasIn&&!isTeacher"></div>
          <div class="apply exit" @click="exitClass" v-show="hasIn&&!isTeacher"></div>
          <div class="apply" style="cursor:default;" v-show="isTeacher"></div>
          <div class="apply forum" @click="toForum"></div>
        </div>
        <div class="teacher-box" v-show="!full">
          <el-image class="teacher-avatar" :src="teacher_avatar"/>
          <div style="float:left;width:130px;margin:15px 0 5px 0;font-size:18px;font-weight:bold;">教师</div>
          <div class="teacher-name">{{teacher_name}}</div>
        </div>
        <div class="player-box" ref="pb">
          <Player
          @FullScreen="FullScreen"
          @ExitFullScreen="ExitFullScreen" 
          :videoSrc="videoUrl"
          :title="lessonName" 
          :hasIn="hasIn" 
          :videoId="videoId"
          ref="player" 
          @updateProc="updateProc"></Player>
          <div style="height:70px;whdth:100%;box-shadow:0 0 10px #666666;background-color:#eeeeee" v-show="!full">
            <el-input v-model="danText" placeholder="发条弹幕吧~" class="dan_input"></el-input>
            <div class="dan_button" @click="sendDan"></div>
          </div>
        </div>
        <div class="video_select" v-show="!full">
          <div class="select_header">视频课程列表</div>
          <div class="select_line"></div>
          <div class="select_outer">
            <div class="select_inner">
              <div v-for="(item,i) in videoList" :class="i==activeLesson?'select_active':'select_item'" :id="getLesson(i)" @click="select(i)">{{i+1+". "+videoList[i].videoName}}</div>
            </div>
          </div>
          <div class="select_footer">
            <div :class="hasIn?'discuss':'enter'"></div>
          </div>
        </div>
      </div>
    </div>
  </el-container>
</template>
<script>
import Header from "./components/Header";
import Player from "./components/Player.vue"
export default {
  components: {
    Header,
    Player
  },
  mounted() {
    var _that = this
    this.resize()
    window.onresize = function(){
      _that.resize()
    }
    this.$axios.post("/api/getvideo",{
      classid:window.sessionStorage.getItem("class")
    })
      .then(res => {
        if(res.data.code === 200){
          this.videoList = res.data.detail
          this.videoUrl = this.videoList[this.activeLesson].storePath
          this.videoId = this.videoList[this.activeLesson].videoId+""
          this.cover=res.data.detail[0].course.coursecover
          this.classid=res.data.detail[0].course.courseid
          this.teacher_avatar=res.data.data.avatar
          this.teacher_name=res.data.data.teacher
          this.title=res.data.detail[0].course.coursename
          this.school=res.data.detail[0].course.courseschool
          this.type=res.data.detail[0].course.coursetype
          this.par=res.data.detail[0].course.participant
          this.isTeacher=window.sessionStorage.getItem("power")=='1'
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    this.$axios.post("/api/judgestudentcourse",{
    	studentid:window.sessionStorage.getItem("uid"),
    	courseid:window.sessionStorage.getItem("class")
    })
      .then(res => {
          this.hasIn = res.data.detail;
          if(this.hasIn){
          	this.updateProc()
          }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
  },
  watch:{
  	videoList(){
  		this.lessonName=this.videoList[this.activeLesson].videoName
  	},
  	activeLesson(){
  		this.lessonName=this.videoList[this.activeLesson].videoName
  	}
  },
  data() {
    return {
      classid: '',
      videoList: [],
      videoUrl: '',
      avatar: '/avatar/avatar.png',
      nickName: '未登录',
      full: false,
      danText: '',
      activeLesson: 0,
      type:'art',
      title: '',
      school: '北京航空航天大学',
      proc: '0/0 分钟',
      hasIn: false,
      par: 100,
      teacher_avatar: '/yuan.png',
      teacher_name: '原仓周',
      isTeacher: false,
      lessonName: '',
      videoId: ''
    };
  },
  computed:{
    icon(type){
      return ''
    }
  },
  methods: {
    toForum(){
      if(window.sessionStorage.getItem("uid")==""||window.sessionStorage.getItem("uid")==null){
      	this.$message.error('您还未登录！')
      	return
      }
      if(this.hasIn){
      	this.$router.push('/Forum')
      }
      else{
      	this.$axios.post("/api/communityapplysubmit",{
        userid:window.sessionStorage.getItem("uid"),
        courseid:window.sessionStorage.getItem("class")
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('申请已提交，请等待教师审核~');
        }
        else if(res.data.code === 250){
        	this.$router.push('/Forum')
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
    },
    participate(){
    	if(window.sessionStorage.getItem("uid")==""){
    		this.$message.error('您还未登录！')
    		return
    	}
    	this.$axios.post("/api/courseapplysubmit",{
        userid:window.sessionStorage.getItem("uid"),
        courseid:window.sessionStorage.getItem("class")
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('申请已提交，请等待教师审核~');
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
    exitClass(){
      this.$axios.post("/api/exitcourse",{
        studentid:window.sessionStorage.getItem("uid"),
        courseid:window.sessionStorage.getItem("class")
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('退课成功！');
        }
        else{
          this.$message.success(res.data.message);
        }
        this.hasIn=false
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
    },
    changeIndex(index){
      if(index == '1'){
        this.$router.replace('/')
      }
      else if(index=='2'){
        this.$router.replace('/search')
      }
      else{
        this.activeIndex=index
      }
    },
    resize(){
      if(this.full){
        this.$refs.bg.style.width = window.innerWidth + 'px'
        this.$refs.inner.style.width = window.innerWidth + 17 + 'px'
      }
      this.$refs.bg.style.height = window.innerHeight + 'px'
      this.$refs.inner.style.height = window.innerHeight+17 + 'px'
      var left = (window.innerWidth - this.$refs.bg.clientWidth)/2
      left = left>0?left:0
      this.page = left>0?'page1':'page2'
      this.$refs.bg.style.left = left + 'px'
      this.$refs.player.resize()
    },
    FullScreen(){
      this.full = true;
      this.$refs.pb.style.marginLeft = '0px';
      this.$refs.pb.style.marginTop = '0px';
      // var de = document.documentElement;
      // if (de.requestFullscreen) {
      //   de.requestFullscreen();
      // } 
      // else if (de.mozRequestFullScreen) {
      //   de.mozRequestFullScreen();
      // } 
      // else if (de.webkitRequestFullScreen) {
      //   de.webkitRequestFullScreen();
      // }
      this.resize()
    },
    ExitFullScreen(){
      this.$refs.pb.style.marginLeft = '20px';
      this.$refs.pb.style.marginTop = '10px';
      this.$refs.bg.style.width = '1000px'
      this.$refs.inner.style.width = '1017px'
      this.full = false;
      // var de = document;
      // if (de.tuichu) {
      //     de.tuichu();
      // } else if (de.mozCancelFullScreen) {
      //     de.mozCancelFullScreen();
      // } else if (de.webkitCancelFullScreen) {
      //     de.webkitCancelFullScreen();
      // }
      this.resize()
    },
    sendDan(){
      this.$refs.player.sendDan(this.danText)
    },
    getLesson(index){
      return 'lesson'+index
    },
    select(index){
      this.activeLesson = index
      this.videoUrl = this.videoList[this.activeLesson].storePath
      this.videoId = this.videoList[this.activeLesson].videoId
      this.$refs.player.pause()
      this.$refs.player.reload()
      var _that = this
      setTimeout(()=>{
        _that.$refs.player.play()
      }, 1000)
    },
    updateProc(){
    	this.$axios.post("/api/getcoursewatchtime",{
    	userid:window.sessionStorage.getItem("uid"),
    	courseid:window.sessionStorage.getItem("class")
    })
      .then(res => {
      	console.log(res.data)
      	if(res.data.code === 200){
      		this.proc = parseInt(res.data.detail.watchtime/res.data.detail.sumtime*100)+"%"
        }
        else{
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
.page{
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
.cash{
  position:fixed;
  top:0;
  width:100%;
}
.video-header{
  box-shadow:0 0 10px #666666;
  background-color: #dddddd;
  margin-top: 70px;
  margin-left: 20px;
  width: 750px;
  height: 80px;
  float: left;
  position: relative;
  border-top-left-radius: 5px;
  left: 0;
}
.shu{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/shu.png) no-repeat;
  background-size: contain;
}
.li{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/li.png) no-repeat;
  background-size: contain;
}
.gong{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/gong.png) no-repeat;
  background-size: contain;
}
.yi{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/yi.png) no-repeat;
  background-size: contain;
}
.wen{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/wen.png) no-repeat;
  background-size: contain;
}
.fa{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/fa.png) no-repeat;
  background-size: contain;
}
.jing{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/jing.png) no-repeat;
  background-size: contain;
}
.wai{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/wai.png) no-repeat;
  background-size: contain;
}
.art{
  height: 60px;
  width: 60px;
  background: url(./assets/icon/art.png) no-repeat;
  background-size: contain;
}
.apply{
  height: 60px;
  width: 60px;
  float: left;
  margin:10px 0px 0 20px;
  cursor: pointer;
}
.participate{
  background: url(./assets/icon/enter.png) no-repeat;
  background-size: contain;
}
.exit{
  background: url('./assets/icon/exit.png') no-repeat;
  background-size: contain;
}
.forum{
	background: url(./assets/icon/discuss.png) no-repeat;
	background-size: contain;
}
.title{
  font-size: 20px;
  width: 449px;
  text-align: left;
  margin: 10px 20px 0 10px;
  float: left;
  font-weight: bold;
  height:25px;
}
.school{
  margin: 10px 20px 0 10px;
  text-align: left;
  float: left;
  font-size: 16px;
  height:25px;
}
.par{
  margin: 10px 20px 0 10px;
  float: left;
  text-align: left;
  font-size: 16px;
  height:25px;
}
.proc{
  margin: 10px 20px 0 10px;
  float: left;
  text-align: right;
  font-size: 16px;
  color:#888888;
  font-weight: bold;
  height:25px;
}
.teacher-box{
  box-shadow:0 0 10px #666666;
  background-color: #dddddd;
  border-top-right-radius: 5px;
  margin-top: 70px;
  width: 210px;
  height: 80px;
  float: left;
  cursor: pointer;
}
.teacher-avatar{
  height: 60px;
  width: 60px;
  border-radius: 30px;
  box-shadow:0 0 10px black;
  float: left;
  margin-top: 10px;
  margin-left: 20px;
}
.teacher-name{
  width: 130px;
  font-size: 16px;
  font-weight: bold;
  float: left;
  text-align: center;
  color: #444444;
}
.player-box{
  margin-top: 10px;
  margin-left: 20px;
  width: 750px;
  height: 640px;
  float: left;
}
.dan_input{
  width: 570px;
  float: left;
  position: relative;
  left: 30px;
  top: 15px;
  box-shadow: 0 0 8px black;
}
.dan_button{
  border-radius: 30px;
  position: relative;
  width: 60px;
  height: 60px;
  background: url('./assets/icon/dan.png') no-repeat;
  background-size: contain;
  cursor: pointer;
  top:5px;
  left: 640px;
}
.video_select{
  border-bottom-right-radius: 5px;
  border-top-left-radius: 5px;
  background-color: #dddddd;
  float: left;
  height: 620px;
  margin-top: 10px;
  margin-left: 10px;
  width: 200px;
  overflow: hidden;
  box-shadow: 0 0 10px #666666;
}
.select_header{
  width: 217px;
  float: left;
  height: 53px;
  line-height: 60px;
  text-align: center;
  font-size: 22px;
  font-family: xingkai;
  src: url(/font/STXINGKA.TTF)
}
.select_line{
  height: 7px;
  width: 200px;
  background: url(./assets/icon/line.png) no-repeat;
  background-size: contain;
  float: left;
}
.select_outer{
  height: 460px;
  width: 217px;
  overflow: hidden;
}
.select_inner{
  height: 477px;
  width: 217px;
  overflow: scroll;
}
.select_item{
  font-size: 15px;
  line-height: 39px;
  overflow: hidden;
  max-height: 39px;
  text-align: left;
  border-bottom: #aaaaaa solid 1px;
  cursor: pointer;
  width: 200px;
  height: 39px;
  padding-left: 10px;
  float: left;
  color: #666666;
  transition: transform 0.3s;
}
.select_active{
  font-size: 15px;
  line-height: 39px;
  overflow: hidden;
  max-height: 39px;
  text-align: left;
  border-bottom: #aaaaaa solid 1px;
  cursor: pointer;
  width: 200px;
  height: 39px;
  padding-left: 10px;
  color: red;
  float: left;
  transform: scale(1.05);
  transition: transform 0.3s;
  font-weight: bold;
}
.select_item:hover{
  transform: scale(1.05);
  color: red;
}
.select_footer{
  background: url(./assets/selectFooter.png) no-repeat;
  background-size: contain;
  height: 100px;
  width: 217px;
  box-shadow: 0 0 10px #666666;
  background-color: #cccccc;
}
.discuss{
  height: 50px;
  width: 50px;
  margin-left: 150px;
  margin-top: 5px;
  float: left;
  cursor: pointer;
  border-radius: 25px;
  background: url(./assets/icon/discuss.png) no-repeat;
  background-size: contain;
  opacity: 0.4;
  transition: opacity 0.3s;
}
.enter{
  height: 50px;
  width: 50px;
  margin-left: 150px;
  margin-top: 5px;
  float: left;
  cursor: pointer;
  border-radius: 25px;
  background: url(./assets/icon/enter.png) no-repeat;
  background-size: contain;
  opacity: 0.4;
  transition: opacity 0.3s;
}
.discuss:hover{
  opacity: 1;
}
.enter:hover{
  opacity: 1;
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
