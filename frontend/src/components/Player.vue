<template>
  <div class="container" style="background-color:green;box-shadow: 5px 5px 10px #888888;" ref="container">
    <video :src="videoSrc" ref="video" style="top:0;left:0;height:100%;width:100%;background-color:black;cursor:pointer;" @click="playing?pause():play()"></video>
    <div style="top:0;left:0;width:100%;height:100%;pointer-events:none;position:relative;top:-100%" ref="dan">
      <div style="width:100%;">
        <div v-for="(item,i) in dan" class="dan" :id="forid(i)" v-show="item.show">
          <div style="float:left;" :id="innerId(i)">{{item.text}}</div>
        </div>
      </div>
    </div>
    <div style="top:0;left:0;width:100%;height:100%;position:relative;pointer-events:none;top:-200%" ref="mask">
      <div style="top:0;left:0;width:100%;height:40px;" ref="title_p">
        <div ref="title" class="title" v-show="show||!playing">{{this.title}}</div>
      </div>
      <div style="width:100%;height:440px;" ref="icon_p">
        <div ref="start_icon" class="start_icon" v-show="!playing" style="width:100px;height:100px;z-index:1;cursor:pointer;" @click="play">
        </div>
      </div>
      <div style="bottom:0;left:0;position:relative;width:100%;height:70px;" ref="ctrl_p">
        <div class="control" v-show="show||!playing" ref="control" style="pointer-events:auto;">
          <div :class="ct_src" ref="ct_img" style="width:50px;height:50px;border-radius: 25px;" @click="playing?pause():play()"></div>
          <div class="pr_box" ref="pr_box">
            <div class="time">{{this.writeTime}}</div>
            <div class="time_line" ref="time_line">
              <div class="pre_line" ref="pre_line"></div>
            </div>
            <div class="slider" ref="slider"></div>
          </div>
          <div class="sp_box" @mouseover="showSpeed=true" @mouseout="showSpeed=false">
            <div v-show="showSpeed" @mouseover="showSpeed=true" @mouseout="showSpeed=false" style="background-color:white;border-radius:7px;border:black solid 1px;position:relative;height:100px;bottom:250%;display:block;width:40px;">
              <el-slider :max="2" :min="0.5" :step="0.25" v-model="speed" style="position:relative;bottom:-10%;height:80%;left:0px;" vertical></el-slider>
            </div>
          </div>
          <div class="vl_box" @mouseover="showVolume=true" @mouseout="showVolume=false">
            <div v-show="showVolume" @mouseover="showVolume=true" @mouseout="showVolume=false" style="background-color:white;border-radius:7px;border:black solid 1px;position:relative;height:100px;bottom:250%;display:block;width:40px;">
              <el-slider :max="1" :min="0" :step="0.01" v-model="volume" style="position:relative;bottom:-10%;height:80%;left:0px;" vertical></el-slider>
            </div>
          </div>
          <div :class="full_src" @click="fullsc?back():full()"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "Player",
  props:{
    videoId:String,
    videoSrc:String,
    title: String,
    hasIn: Boolean
  },
  data() {
    return {
      show: false,
      playing: false,
      ct_src: '/logo/start.png',
      current: 0,
      total: 0,
      fullsc: false,
      full_src: 'full_box',
      showVolume: false,
      showSpeed: false,
      speed: 1,
      volume: 0.5,
      writeTime: '0:0/0:0',
      ct_src: 'start_box',
      dan: [],
      start: 0,
      updateTime:0
    };
  },
  mounted(){
    console.log(this.videoId)
    this.$refs.video.load();
    var _that = this
    var t = 0
    var onControl = false
    window.onresize = function(){
      _that.resize()
    }
    setInterval(function(){
      if(onControl) return
      if(t>0){
        t = t - 100
      }
      else{
        _that.show = false
        _that.showVolume=false
      }
    }, 100);
    this.$refs.container.onmousemove = function () {
      t=800
      _that.show = true
      if(_that.fullsc){
        _that.resize()
      }
    }
    this.$refs.control.onmouseover = function(){
      onControl = true
    }
    this.$refs.control.onmouseout = function(){
      onControl = false
    }
    this.$refs.time_line.onclick = function(ev){
      var event = ev || event;
      _that.$refs.video.currentTime = event.offsetX/_that.$refs.time_line.clientWidth*_that.$refs.video.duration
    }
    this.$refs.video.addEventListener("timeupdate", function(){
      if(_that.$refs.video==undefined) return;
      _that.$refs.video.volume = _that.volume
      _that.$refs.video.playbackRate = _that.speed
      var len = _that.$refs.video.currentTime/_that.$refs.video.duration
      if(_that.$refs.pre_line!=undefined)
        _that.$refs.pre_line.style.width = (len*100)+'%'
      if(_that.$refs.slider!=undefined)
        _that.$refs.slider.style.left = (len*100)+'%'
      if(_that.$refs.video.duration!=undefined&&!isNaN(_that.$refs.video.duration)){
        _that.writeTime = parseInt(_that.$refs.video.currentTime/60)+':'+parseInt(_that.$refs.video.currentTime%60)+' / '+parseInt(_that.$refs.video.duration/60)+':'+parseInt(_that.$refs.video.duration%60)
      }
      if(_that.$refs.video.currentTime>=_that.$refs.video.duration){
        _that.update()
        _that.play()
        _that.pause()
      }
      _that.update()
      for(var i=0;i<_that.dan.length;i++){
        var id = 'dan'+i
        var inner = 'inner'+i
        document.getElementById(id).style.left = _that.$refs.container.clientWidth-(_that.$refs.video.currentTime-_that.dan[i].time)*150+'px'
        if(!_that.dan[i].show){
          var show_cnt = 0;
          for(var j=0;j<_that.dan.length;j++){
            if(_that.dan[j].show){
              show_cnt++
            }
          }
          document.getElementById(id).style.top = (_that.dan[i].line -show_cnt) *30 + 'px'
        }
        _that.dan[i].show = true
        // var offsetStr = document.getElementById(id).style.left
        // var offsetLeft = parseInt(offsetStr.substring(0, offsetStr.length-2))
        // var widthStr = document.getElementById(inner).clientWidth
        // if(offsetLeft+widthStr<0){
        //   _that.dan[i].show = false
        // }
      }
      // if(_that.index+1<_that.word.length&&_that.$refs.guideVoice.currentTime+0.5>=_that.word[_that.index+1].time){
      //   _that.index++;
      //   _that.currentText = _that.word[_that.index].text
      // }
      // _that.current=parseInt(_that.$refs.guideVoice.currentTime)
    },false)
    document.onkeydown=function(event){//键盘事件，为弹幕发送做准备
      var e = event || window.event || arguments.callee.caller.arguments[0];
      if(e && e.keyCode==13){
        //回车
      }
      if(e && e.keyCode==37){
        //左键
        _that.$refs.video.currentTime=_that.$refs.video.currentTime>3?_that.$refs.video.currentTime-3:0;
        t=800
        _that.show = true
      }
      if(e && e.keyCode==39){
        //右键
        _that.$refs.video.currentTime=_that.$refs.video.currentTime+3>_that.$refs.video.duration?_that.$refs.video.duration:_that.$refs.video.currentTime+3;
        t=800
        _that.show = true
      }
      if(e && e.keyCode==38){
        //上键
        _that.volume=_that.volume+0.1>1?1:_that.volume+0.1;
        t=800
        _that.show = true
        _that.showVolume=true
        return false
      }
      if(e && e.keyCode==40){
        //下键
        _that.volume=_that.volume-0.1<0?0:_that.volume-0.1;
        t=800
        _that.show = true
        _that.showVolume=true
        return false
      }
      if(e && e.keyCode==32){
        //空格
        _that.playing?_that.pause():_that.play()
      }
    }
  },
  methods: {
    resize(){
      if(!this.fullsc){
        return
      }
      this.$refs.container.style.height = window.innerHeight + 'px'
      this.$refs.container.style.width = window.innerWidth +'px'
      this.$refs.title.style.width = window.innerWidth +'px'
      this.$refs.control.style.width = window.innerWidth + 'px'
      this.$refs.ctrl_p.style.top = window.innerHeight-550 + 'px'
      this.$refs.pr_box.style.width = (window.innerWidth - 230) + 'px'
      if(this.$refs.start_icon == undefined) return
      this.$refs.start_icon.style.left = (window.innerWidth/2 - 50) + 'px'
      this.$refs.start_icon.style.top = (window.innerHeight/2 - 50) + 'px'
    },
    play(){
      this.$refs.video.play()
      this.ct_src='pause_box'
      this.playing=true
    },
    pause(){
      this.$refs.video.pause()
      this.ct_src='start_box'
      this.playing=false
    },
    full(){
      this.fullsc = true
      this.full_src = 'back_box'
      this.resize()
      this.$emit("FullScreen");
    },
    reload(){
      this.$refs.video.load();
      this.start = 0;
      this.updateTime = 0;
    },
    back(){
      this.fullsc = false
      this.full_src = 'full_box'
      this.$refs.container.style.height = '550px'
      this.$refs.container.style.width = '750px'
      this.$refs.control.style.width = '750px'
      this.$refs.title.style.width = '750px'
      if(this.$refs.start_icon == undefined) return
      this.$refs.start_icon.style.left = '325px'
      this.$refs.start_icon.style.top = '205px'
      this.$refs.pr_box.style.width = '510px'
      this.$refs.ctrl_p.style.top = '0px'
      this.resize()
      this.$emit("ExitFullScreen");
    },
    sendDan(text){
      var _that = this
      var dan = {
        text:text,
        time:_that.$refs.video.currentTime,
        line:1,
        show:false,
        offset:_that.$refs.container.clientWidth
      }
      var arr = [true,true,true,true,true,true,true,true,true,true,true,true]
      for(var i=0;i<_that.dan.length;i++){
        var id = _that.forid(i)
        var inner = _that.innerId(i)
        var scwidth = _that.$refs.container.clientWidth
        var offsetStr = document.getElementById(id).style.left
        var offsetLeft = parseInt(offsetStr.substring(0, offsetStr.length-2))
        if(offsetLeft+document.getElementById(inner).clientWidth+30>scwidth){
          // console.log('offset:'+offsetLeft)
          // console.log('width'+document.getElementById(inner).clientWidth)
          arr[_that.dan[i].line-1] = false
        }
      }
      for(var i=0;i<arr.length;i++){
        if(arr[i]){
          dan.line = i+1
          _that.dan.push(dan)
          return
        }
      }
      dan.line = Math.floor(Math.random()*10+1)
      _that.dan.push(dan)
      return
    },
    forid(index){
      return 'dan'+index
    },
    innerId(index){
      return 'inner'+index
    },
    update(){
      if(!this.hasIn||this.videoId=="") return
      var d = (this.$refs.video.currentTime-this.start-this.updateTime)*1000
      if(d>0&&d<1000){
        if(this.updateTime*1000>2000){
          this.$axios.post("/api/updatetime",{
            userid:window.sessionStorage.getItem("uid"),
            videoid:this.videoId,
            time:parseInt(this.updateTime*1000)
          })
          .then(res => {
            this.$emit("updateProc")
            this.start = this.updateTime
            this.updateTime = 0
          })
          .catch(failResponse=>{
            console.log(failResponse);
            this.$message.error('网络错误');
          })
        }
        else{
          this.updateTime = d/1000 + this.updateTime
        }
      }
      else{
        this.$axios.post("/api/updatetime",{
            userid:window.sessionStorage.getItem("uid"),
            videoid:this.videoId,
            time:parseInt(this.updateTime*1000)
          })
          .then(res => {
            this.$emit("updateProc")
            this.start = this.$refs.video.currentTime
            this.updateTime = 0
          })
          .catch(failResponse=>{
            console.log(failResponse);
            this.$message.error('网络错误');
          })
      }
    }
  }
};
</script>
<style scoped>
.container{
  height:550px;
  width:750px;
  overflow: hidden;
}
.control{
  height: 70px;
  width: 750px;
  background-color: black;
  opacity: 0.8;
  box-shadow: 0px -20px 50px black;
  z-index: 1;
  float: left;
}
.title{
  text-align: center;
  width: 750px;
  height: 40px;
  line-height: 40px;
  background-color: black;
  color: white;
  opacity: 0.8;
  box-shadow: 0px 10px 50px black;
  float: left;
}
.start_icon{
  width: 100px;
  height: 100px;
  left: 325px;
  top: 205px;
  position: relative;
  border-radius: 75px;
  box-shadow: 0 0 30px black;
  background: url('../assets/icon/start.png') no-repeat;
  background-size: contain;
  float: left;
}
.start_box{
  margin-left: 10px;
  height: 50px;
  margin-top: 10px;
  width: 50px;
  border-radius: 25px;
  cursor: pointer;
  background: url('../assets/icon/start.png') no-repeat;
  background-size: contain;
  float: left;
}
.pause_box{
  margin-left: 10px;
  height: 50px;
  margin-top: 10px;
  width: 50px;
  border-radius: 25px;
  cursor: pointer;
  background: url('../assets/icon/pause.png') no-repeat;
  background-size: contain;
  float: left;
}
.pr_box{
  margin-top: 10px;
  height: 50px;
  margin-left: 10px;
  width: 510px;
  float: left;
}
.time{
  margin-left: 20px;
  width: 100%;
  height: 16px;
  color: white;
  text-align: left;
  cursor: default;
  float: left;
}
.slider{
  width: 30px;
  height: 30px;
  margin-top:-18px;
  border-radius: 20px;
  cursor: pointer;
  background: url('../assets/icon/slider.png') no-repeat;
  background-size: contain;
  pointer-events: none;
  float: left;
  position: relative;
  left: 0;
}
.pre_line{
  height: 8px;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
  float: left;
}
.time_line{
  width: 100%;
  height: 8px;
  margin-left: 10px;
  margin-right: 10px;
  border-radius: 4px;
  background-color: black;
  border: 1px solid gray;
  margin-top: 10px;
  cursor: pointer;
  float: left;
}
.sp_box{
  margin-top: 15px;
  margin-left: 25px;
  width: 40px;
  height: 40px;
  right: 95px;
  border-radius: 20px;
  cursor: pointer;
  background: url('../assets/icon/speed.png') no-repeat;
  background-size: contain;
  float: left;
}
.vl_box{
  margin-top: 15px;
  margin-left: 5px;
  width: 40px;
  height: 40px;
  right: 50px;
  border-radius: 20px;
  cursor: pointer;
  background: url('../assets/icon/vl.png') no-repeat;
  background-size: contain;
  float: left;
}
.full_box{
  margin-top: 15px;
  margin-left: 5px;
  width: 40px;
  height: 40px;
  right: 5px;
  border-radius: 20px;
  cursor: pointer;
  background: url('../assets/icon/full.png') no-repeat;
  background-size: contain;
  float: left;
}
.back_box{
  margin-top: 15px;
  width: 40px;
  height: 40px;
  right: 5px;
  border-radius: 20px;
  cursor: pointer;
  background: url('../assets/icon/back.png') no-repeat;
  background-size: contain;
  float: left;
}
.dan{
  position:relative;
  font-size:20px;
  line-height:30px;
  height:30px;
  color:white;
  text-shadow: 3px 3px 3px #FF0000;
  overflow: hidden;
  max-height: 30px;
  text-align: left;
}
.full{
  position: fixed;
}
.unFull{}
/deep/ .el-slider__runway {
  background-color: gray;
}
/deep/ .el-slider__bar {
  background-color: black;
}
/deep/ .el-slider__button {
  background-color: white;
  border: solid 2px black;
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