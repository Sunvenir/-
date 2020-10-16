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
  		<div class="inner" ref="inner" @scroll="scrollChange">
        <div style="width:100%;height:200px;margin-top:80px;">
          <div class="search_logo"></div>
          <el-input v-model="keyword" placeholder="请输入搜索内容" class="search_input"></el-input>
          <div class="search_button" @click="getResult"></div>
          <div class="search_line"></div>
        </div>
        <div style="width:100%;height:80px;">
          <div style="height:79px;width:920px;position:relative;left:40px;">
          <div class="type" @click="search('class')">课程<div class="select" v-show="searchIndex==1"></div></div>
          <div class="type" @click="search('teacher')">老师<div class="select" v-show="searchIndex==2"></div></div>
          <div class="type" @click="search('school')">学校<div class="select" v-show="searchIndex==3"></div></div> 
          <div class="type" @click="search('user')">用户<div class="select" v-show="searchIndex==4"></div></div>  
          </div>
          <div style="height:1px;width:920px;position:relative;left:40px;background-color:#cccccc;box-shadow:0 0 10px #666666"></div>
        </div>
        <div style="width:920px;margin-left:40px;">
          <div style="width:100%;" v-if="searchIndex==1||searchIndex==2||searchIndex==3">
            <div v-for="(item,index) in result" class="video_box" @click="play(item.courseid)">
              <SearchDisplay
              :cover='item.coursecover' 
              :title="item.coursename"
              :par='item.participant'
              :teacherName="teacher[index]"
              ></SearchDisplay>
            </div>
            <div style="float:left;width:100%;" v-show="result.length!=0">
              <el-pagination
              background
              layout="prev, pager, next"
              :total="1"
              style="margin-top:30px;margin-bottom:30px;"
              v-if="searchIndex==1||searchIndex==2||searchIndex==3">
              </el-pagination>
            </div>
          </div>
          <div class="no_result" v-show="result.length==0">暂无搜索结果</div>
        </div>
      </div>
  	</div>
    <div ref="toTop" class="toTop" v-show="!hideTop" @mouseout="outTop" @mousedown="beforeTop" @mouseup="toTop"></div>
  </el-container>
</template>
<script>
import Header from "./components/Header";
import SearchDisplay from "./components/SearchDisplay.vue"
export default {
  components: {
    Header,
    SearchDisplay
  },
  mounted() {
    var _that = this
    this.resize()
    window.onresize = function(){
      _that.resize()
    }
  },
  data() {
    return {
      activeIndex:"2",
      page: "page1",
      hideTop: true,
      // loginBg: "",
      avatar: '/avatar/avatar.png',
      nickName: '未登录',
      searchIndex: '1',
      keyword: '',
      result:[],
      teacher:[]
    };
  },
  methods: {
    changeIndex(index){
      if(index == '1'){
        this.$router.replace('/')
      }
      else{
        this.activeIndex=index
      }
    },
    scrollChange(){
      this.hideTop=this.$refs.inner.scrollTop<200
    },
    resize(){
      this.$refs.bg.style.height = window.innerHeight + 'px'
      this.$refs.inner.style.height = window.innerHeight+17 + 'px'
      var left = (window.innerWidth - 1000)/2
      left = left>0?left:0
      this.page = left>0?'page1':'page2'
      this.$refs.bg.style.left = left + 'px'
    },
    search(index){
      if(index=='class'){
        this.searchIndex='1'
      }
      else if(index=='teacher'){
        this.searchIndex='2'
      }
      else if(index=='school'){
        this.searchIndex='3'
      }
      else if(index=='user'){
        this.searchIndex='4'
      }
    },
    getResult(){
      if(this.searchIndex=='4'){
        this.$message.error("功能暂未开放！")
        return
      }
      var key = this.keyword==""?'0':this.searchIndex
      this.$axios.post("/api/searchcourse2",{
        key:key,
        keyword:this.keyword
      })
      .then(res => {
        console.log(res.data)
        if(res.data.code === 200){
          this.result=res.data.detail
          this.teacher=res.data.data        
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
    beforeTop(){
      this.$refs.toTop.style.transform = 'scale(0.8)'
    },
    outTop(){
      console.log('hhh')
      this.$refs.toTop.style.transform = ''
    },
    toTop(){
      this.$refs.toTop.style.transform = ''
      var _that = this
      setTimeout(()=>{
        _that.$refs.inner.scrollTop=0
      }, 30)
    },
    play(i){
      window.sessionStorage.setItem("class", i)
      this.$router.push('/play')
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
  width: 340px;
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
  background: url('./assets/icon/search.png');
  cursor: pointer;
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
.type{
  font-family: xingkai;
  font-size: 25px;
  float: left;
  margin-left: 130px;
  margin-top: 30px;
  width: 50px;
  height: 25px;
  cursor: pointer;
  src: url(/font/STXINGKA.TTF);
  text-shadow: 0 0 3px #aaaaaa;
}
.select{
  height: 40px;
  width: 50px;
  position: relative;
  bottom: 15px;
  background: url('./assets/icon/select.png') no-repeat;
  background-size: contain;
}
.video_box{
  box-shadow: 0 0 10px #666666;
  float: left;
  margin-top: 20px;
  height: 280px;
  width: 205px;
  margin-left: 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: transform 0.3s; 
}
.video_box:hover{
  transform: scale(1.02);
}
.toTop{
  height: 70px;
  width: 70px;
  border-radius: 35px;
  position: fixed;
  bottom: 30px;
  right: 30px;
  background: url('./assets/icon/toTop.png');
  background-size: contain;
  cursor: pointer;
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
  width: 830px;
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
  background: url('./assets/icon/watch.png');
  background-size: contain;
}
.cash{
  position:fixed;
  top:0;
  width:100%;
}
/deep/ .el-pagination.is-background .el-pager li{
  box-shadow: 0 0 5px #666666;
}
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #444444;
  color: #fff;
}
/deep/ .el-pagination.is-background .el-pager li.active {
  color: #fff;
  cursor: default;
}
/deep/ .el-pagination.is-background .el-pager li:hover {
  color: white;
  background: #aaaaaa;
}
/deep/ .el-pagination.is-background .el-pager li:not(.disabled):hover {
  color: white;
}
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active:hover {
  background-color: #444444;
  color: #FFF;
}
/deep/ .btn-next{
  box-shadow: 0 0 5px #666666;
}
/deep/ .btn-next:hover{
  box-shadow: 0 0 5px #666666;
}
/deep/ .btn-prev{
  box-shadow: 0 0 5px #666666;
}
/deep/ .btn-prev:hover{
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
