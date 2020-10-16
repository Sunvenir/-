00.<template>
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
        <div style="width: 100%;margin-top: 60px;float:left;">
        	<div style="width: 800px;margin-left:100px;float:left;box-shadow:0 0 10px #666666;">
            <div class="width:100%;height:70px;background-color=red;">
              <div class="head-img"></div>
              <div class="head-line"></div>
            </div>
        		<div style="width: 380px;margin:10px;height:260px;float:left;border-radius:5px;overflow:hidden;">
        			<el-carousel style="box-shadow:0 0 10 #666666;cursor:pointer;" height="260px">
        				<el-carousel-item v-for="(item,index) in recommendList" :key="index">
        					<el-image :src="item.coursecover" fit="fill" style="height:100%;width:100%;" @click="enterClass(recommendList[index])"></el-image>
        				</el-carousel-item>
        			</el-carousel>
        		</div>
        		<div style="width: 400px;height:280px;float:left;">
              <div v-for="(item,index) in recommendList" @click="enterClass(item)">
        			<Display
        			style="box-shadow:0 0 10px #666666;cursor:pointer;" 
        			:title="item.coursename"
        			:cover="item.coursecover"
        			:teacherName="recommendTList[index].username"
        			:watches="item.participant"
        			:brief="item.courseintro" >
        			</Display>
              </div>
        		</div>
        		<div class="bar">
        			<div v-for="(item,index) in barList" class="barItem" @click="getVideo(item)">{{item}}
        				<div class="check" v-show="item==selected" style="pointer-events:none;"></div>
        			</div>
        		</div>
        		<div class="line"></div>
        		<div style="width:800px;">
              <div @click="enterClass(item)" style="cursor:pointer;" v-for="(item,index) in searchList">
        			<Display
        			style="box-shadow:0 0 10px #666666;" 
        			:title="item.coursename"
        			:cover="item.coursecover"
        			:teacherName="searchTList[index]"
        			:watches="item.participant"
        			:brief="item.courseintro"
              >
        			</Display>
              </div>
              <div class="no_result" v-show="searchList.length==0">暂无搜索结果</div>
        		</div>
        	</div>
        </div>
        </div>
  	</div>
  </el-container>
</template>
<script>
import Header from "./components/Header";
import Display from "./components/Display.vue"
export default {
  components: {
    Header,
    Display
  },
  mounted() {
    var _that = this
    this.resize()
    window.onresize = function(){
      _that.resize()
    }
    if(window.sessionStorage.getItem("uid")==null){
    	window.sessionStorage.setItem("uid","")
    	this.$refs.header.resetAvatar('/avatar/avatar.png')
    }
    this.init()
  },
  data() {
    return {
      activeIndex:"1",
      page: '/cover.png',
      // loginBg: "",
      avatar: '/avatar/avatar.png',
      nickName: '未登录',
      cover:'/cover.png',
      teacherName: "郑孝信",
      watches: 10000,
      brief:"兔兔兔兔兔兔兔兔兔",
      title:"数学分析",
      recommendList:[],
      recommendTList:[],
      searchList:[],
      searchTList:[],
      barList:['数学','工科','理科','文科','医科','法学','经济','外语','艺术'],
      selected: '数学',
      // cpAvatar: "",
      // cpNickName: "",
      // cpStatus: "",
      // chating: false,
      // image1: "",
      // class1: "",
      // onshowPersonal: false
    };
  },
  methods: {
    searchByType(type){
      console.log(index)
    },
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
    init(){
    	this.$axios.post("/api/recommend",{})
      .then(res => {
        console.log(res.data)
        if(res.data.code === 200){
          this.recommendList=res.data.data
          this.recommendTList=res.data.detail
        }
        else{
          this.$message.error(res.data.message);
        }
      })
      .catch(failResponse=>{
        console.log(failResponse);
        this.$message.error('网络错误');
      })
      this.$axios.post("/api/searchcourse2",{
        key:'4',
        keyword:this.getType(this.selected)
      })
      .then(res => {
        console.log(res.data)
        if(res.data.code === 200){
          this.searchList=res.data.detail
          this.searchTList=res.data.data        
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
    getType(str){
      if(str=='数学'){
        return 'shu'
      }
      else if(str=='工科'){
        return 'gong'
      }
      else if(str=='理科'){
        return 'li'
      }
      else if(str=='文科'){
        return 'wen'
      }
      else if(str=='医科'){
        return 'yi'
      }
      else if(str=='法学'){
        return 'fa'
      }
      else if(str=='经济'){
        return 'jing'
      }
      else if(str=='艺术'){
        return 'art'
      }
    },
    getVideo(item){
    	//获取分类推荐视频的函数
    	this.selected = item;
      this.init()
    },
    enterClass(item){
      window.sessionStorage.setItem("class", item.courseid)
      this.$router.push('/play')
      // window.sessionStorage.setItem("class", this.recommendList[index].)
    }
    // getSocket(){
    //   var _that = this
    //   this.$socket.openSocket(window.sessionStorage.getItem("UID"),function(msg) {
    //     if(msg.data.substring(0, 1)!='{'&&msg.data.substring(0, 1)!='['){
    //       if(msg.data == "连接成功") return;
    //       _that.$message.success(msg.data);
    //     }
    //     else {
    //       var data = eval('('+msg.data+')')
    //       if(data.message!=undefined&&_that.chating){
    //         console.log(data)
    //         _that.$refs.chat.addNewMes(data)
    //       }
    //       if(data.cid!=undefined){
    //         window.sessionStorage.setItem('CID',data.cid)
    //         _that.getUserInfo(data.cid)
    //         _that.cpStatus = data.status
    //       }
    //     }
    //   });
    // },
    // getUserInfo(cid){
    //   this.$axios.post('/user/getInfo',{
    //     uid: cid
    //   })
    //   .then(res=>{
    //     if(res.data.code==200){
    //       this.cpAvatar = res.data.data.avatar;
    //       this.cpNickName = res.data.data.nickName;
    //     }
    //     else{
    //       this.$message.error(res.data.message)
    //     }
    //   })
    //   .catch(res=>{
    //     this.$message.error('未知错误！')
    //   })
    // }
    // changeIndex(index){
    //   if(index == '2'){
    //     this.$router.replace('/search')
    //   }
    //   else{
    //     this.activeIndex=index
    //   }
    // },
    // handleSelect(){

    // }
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
.head-img{
  height: 70px;
  width: 70px;
  background: url('./assets/icon/recommend.png') no-repeat;
  background-size: contain;
}
.head-line{
  position: relative;
  left: 70px;
  top:-24px;
  height: 24px;
  width: 720px;
  background: url('./assets/icon/line.png') no-repeat;
  background-size: contain;
}
.inner{
  width: 1017px;
  overflow: scroll;
}
.line{
	background: url(./assets/icon/line.png) no-repeat;
	background-size: contain;
	float: left;
	width: 800px;
	height: 27px;
}
.bar{
	margin-top: 30px;
	width: 800px;
	float: left;
	height: 40px;
	font-family: xingkai;
	src: url(/font/STXINGKA.TTF);
	font-size: 25px;
}
.barItem{
	line-height: 30px;
	float: left;
	margin-left: 34px;
	cursor: pointer;
	float: left;
}
.check{
	background: url(./assets/icon/select.png) no-repeat;
	background-size: contain;
	pointer-events: none;
	margin-top: -20px;
	margin-left: 10px;
	height: 40px;
	width: 40px;
}
.mainPage{
  position: fixed;
  opacity: 0.8;
  left: 10%;
  top: 60px;
  height: 700px;
  width: 80%;
  color: white;
  background-color: white;
  z-index: 1;
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
  float: left;
}
.el-carousel__item:nth-child(2n) {
  background-color: #dddddd;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #dddddd;
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
