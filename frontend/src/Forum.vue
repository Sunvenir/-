<template>
  <el-container :class="page">
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
      <Header :activeIndex="activeIndex" ref="header"></Header>
    </el-header>
    <div class="bg" ref='bg'>
      <div class="inner" ref="inner" @scroll="scrollChange">
      	<div style="height: 100%;width: 100%;margin-top:75px">
          <Card style="z-index: 200;box-shadow:0 0 10px #666666;"
            :title="comitem2.name"
            :teacherName="comitem1.teachername"
            :studentNumber="comitem1.participant"
            :brief="comitem1.courseintro">
          </Card>
          <div class="notice" @click="show()" :style="noticestyle()">
            【规则】{{notice}}
          </div>
          <Post style="z-index: 100;box-shadow:0 0 10px #666666;" v-on:postinv="newInvs()" v-on:postnotice="refresh()" 
          :notice="notice"
          :isteacher="isTeacher" 
          :communityid="communityId"
          >
          </Post>
          <div class="menu">
            <div class="tag1" :style="newstyle(0)" @click="choose0()">
              全部
            </div>
            <div class="tag1" :style="newstyle(1)" @click="choose1()">
              置顶帖
            </div>
            <div class="tag1" :style="newstyle(2)" @click="choose2()">
              精华帖
            </div>
            <div class="tag2" :style="newstyle2(2)" @click="choose3()">
              按评论
            </div>
            <div class="tag2" :style="newstyle2(1)" @click="choose4()">
              按点赞
            </div>
            <div class="tag2" :style="newstyle2(0)" @click="choose5()">
              按时间
            </div>
          </div>
          <div style="width: 800px;margin: 0 auto;box-shadow:0 0 10px #666666;" v-for="item in Invs">
            <Inv 
              :title="item.head"
              :text="item.text"
              :type="item.isdigest"
              :avatar="item.posteravatar"
              :author="item.postername"
              :ppid="item.postid"
              :pid="item.posterid"
              :time="item.time"
              :follow="item.likes"
              :top="item.istop"
              :comnum="item.comments_number"
              :isteacher="isTeacher"
              :puid="item.userid"
              v-on:freshinv="newInvs()"
            >
            </Inv>
          </div>
        </div>
      </div>
  	</div>
  </el-container>
</template>

<script>
import Header from "./components/Header";
import Display from "./components/Display.vue";
import Tabs from "./components/Tabs.vue";
import Tags from "./components/Tags.vue";
import Tabsplus from "./components/Tabsplus.vue";
import Card from "./components/Card.vue";
import Post from "./components/Post.vue";
import Inv from "./components/Inv.vue";

export default {
  components: {
    Header,
    Display,
    Tabsplus,
    Tags,
    Tabs,
    Card,
    Post,
    Inv
  },
  mounted() {
    var _that = this
    this.resize()
    window.onresize = function(){
      _that.resize()
    }
    this.refresh()
  },
  data() {
    return {
      avatar: '/avatar/avatar.png',
      nickName: '未登录',
      Invs:[],
      activeIndex:'-1',
      menuNo:0,
      menuNo2:0,
      page:'',
      communityId: '',
      isTeacher: false,
      comitem1:[],
      comitem2:[],
      noticeView:false,
      notice:'',
    };
  },
  methods: {
    refresh(){
      this.$axios.post("/api/searchcourse2",{
        key:'8',
        keyword:window.sessionStorage.getItem("class")
      })
      .then(res => {
        console.log(res.data)
        if(res.data.code === 200){
          this.communityId = res.data.data.id+""
          window.sessionStorage.setItem("communityid",this.communityId)
          console.log(res.data.data)
          console.log(res.data.data.creator.userid==window.sessionStorage.getItem("uid"))
          this.isTeacher = (res.data.data.creator.userid==window.sessionStorage.getItem("uid"))
          this.newInvs();  
          this.comitem1=res.data.detail;
          this.comitem2=res.data.data;
          this.notice=res.data.data.rules;
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
    show(){
      this.noticeView=!this.noticeView;
    },
    noticestyle(){
      if(this.noticeView==true)
        return "max-height:none; -webkit-line-clamp: unset;box-shadow:0 0 10px #666666;background: #eeeeee;";
      else
        return "box-shadow:0 0 10px #666666;background: #eeeeee;";
    },
     resize(){
      this.$refs.bg.style.height = window.innerHeight + 'px'
      this.$refs.inner.style.height = window.innerHeight+17 + 'px'
      var left = (window.innerWidth - 1000)/2
      left = left>0?left:0
      this.page = left>0?'page1':'page2'
      this.$refs.bg.style.left = left + 'px'
    },
    newstyle(i){
      if(i==this.menuNo)
        return "background:black;color:white;box-shadow:0 0 10px #666666";
      else
        return "background:#cccccc;color:black;box-shadow:0 0 0px #666666";
    },
    newstyle2(i){
      if(i==this.menuNo2)
        return "background:black;color:white;box-shadow:0 0 10px #666666";
      else
        return "background:#cccccc;color:black;box-shadow:0 0 0px #666666";
    },
    choose0(){
      this.menuNo=0;
      this.newInvs();
    },
    choose1(){
      this.menuNo=1;
      this.newInvs();
    },
    choose2(){
      this.menuNo=2;
      this.newInvs();
    },
    choose3(){
      this.menuNo2=2;
      this.newInvs();
    },
    choose4(){
      this.menuNo2=1;
      this.newInvs();
    },
    choose5(){
      this.menuNo2=0;
      this.newInvs();
    },
    changeIndex(index){
      var _that = this
      this.activeIndex=index
      if(index==1)
      this.$router.replace('/')
    },
    scrollChange(){
      this.hideTop=this.$refs.inner.scrollTop<200
    },
    newInvs(){
      this.$axios.post("/api/showpost",{
        courseid:window.sessionStorage.getItem("class"),
        list_type:this.menuNo+1,
        sort_style:this.menuNo2+1,
        //window.sessionStorage.getItem("class")
      })
      .then(res => {
        if(res.data.code === 200){
          this.Invs=res.data.detail;
          for(var i=0;i<this.Invs.length;i++){
            if(this.Invs[i].time.length>2)
            this.Invs[i].time=this.Invs[i].time.substring(0,this.Invs[i].time.length-2);
          }
          console.log(res);
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
.notice{
  width: 770px;
  max-height:40px;
  margin: 0 auto;
  margin-top: 10px;
  padding-left: 15px;
  padding-right: 15px;
  background: white;
  font-size: 20px;
  text-align: left;
  line-height: 40px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
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
  .menu{
    margin: 0 auto;
    margin-top: 10px;
    width: 770px;
    height:30px;
    padding-left:15px;
    padding-right: 15px;
    background: #eeeeee;
    box-shadow:0 0 10px #666666;
  }
  .tag1{
    width: 80px;
    background: pink;
    height:30px;
    float: left;
    line-height: 30px;
    font-size: 15px;
    cursor: pointer;
    margin-right: 5px;
  }
  .tag2{
    width: 80px;
    background: pink;
    height:30px;
    float: right;
    line-height: 30px;
    font-size: 15px;
    cursor: pointer;
    margin-left: 5px;
  }
  /deep/ .el-button{
    background-color: black !important;
    color: white !important;
    border: 1px solid white !important;
    box-shadow: 0 0 10px #666666 !important;
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