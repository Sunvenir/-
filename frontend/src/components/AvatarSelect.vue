<template>
    <!-- <div class="avatar">
    <el-upload class="avatar-uploader" :before-upload="beforeAvatarUpload" action="#">
      <img v-if="showImg" :src="dataUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    </div> -->
  <div id="editBox" ref="container">
    <canvas id="canvas"></canvas>
    <div id = "cutter" :style="getCutterPos()" @mousedown=drag($event)>
      <canvas id="inner"></canvas>
    </div>
    <div class="block">
      <span class="demonstration">缩放:</span>
      <el-slider class="slider" v-model="value" :min="minValue" :max="maxValue" :disabled="!hasImg"></el-slider>
    </div>
    <div class="ok" @click="showImage"></div>
    <div class="exit" @click="exit"></div>
  </div>
</template>
<script>
export default {
  name: "AvatarSelect",
  mounted(){
    this.initCanvas();
  },
  props:{
    
  },
  data() {
    return {
      minValue: 0,//缩放条的最小值，依据传入图片改动
      value: 0,//缩放条的值
      maxValue: 200,//缩放条的最大值，依据传入图片改动
      imageUrl: '',//所选图片的路径
      commitable: false,
      img: null,//图片文件
      hasImg:false,//画布上是否有图
      canvas: null,//外部canvas
      context: null,//外部canvas的ctx
      showImg: false,
      X: 0,//截取框位移
      Y: 0,//截取框位移
      type: '',//文件类型
      dataUrl: ''//base64
    };
  },
  watch:{
    value(val,oldVal){//动态缩放图片
      this.context.fillRect(0,0,750,450);
      var scale = val/100;
      this.context.drawImage(this.img,0,0,this.img.width*scale,this.img.height*scale);
    }
  },
  methods: {
    resize(){
      this.$refs.container.style.left = (window.innerWidth-750)/2+'px'
    },
    drag(ev){//截取框拖动，判定边界不超过外部canvas（750*450）
      var event = ev || event;
      var startX = event.clientX
      var startY = event.clientY
      var _that = this
      var PX = _that.X;
      var PY = _that.Y;
      document.onmousemove = function(ev){
        var event = ev || event;
        if(PX+event.clientX-startX>=0&&PX+event.clientX-startX<=510){
          _that.X = PX+event.clientX-startX;
        }
        if(PY+event.clientY-startY>=0&&PY+event.clientY-startY<=290){
          _that.Y = PY+event.clientY-startY;
        }
      }
      document.onmouseup = function () {
        document.onmousemove = null;
        document.onmouseup = null;
      }
      return false;
    },
    getCutterPos(){
      return "top:"+(this.Y-2)+"px;left:"+(this.X-2)+"px;"
    },
    initCanvas(){//初始化外部canvas
      this.canvas = document.getElementById("canvas")
      this.canvas.height = 450;
      this.canvas.width = 750;
      this.context = this.canvas.getContext("2d");
      this.context.fillStyle = "#FFF";
      this.context.fillRect(0,0,750,450);
    },
    parseFile(file) {
      this.imageUrl = URL.createObjectURL(file)//创建图片链接
      this.startCanvas();
    },
    startCanvas(){//外部canvas开始工作
      this.hasImg = false;
      this.context.fillRect(0,0,750,450);
      if(this.imageUrl=='') return;
      this.img = new Image();
      this.img.src = this.imageUrl;
      var _that = this
      var canvas = _that.canvas
      var img = this.img
      this.img.onload = function(){
        _that.hasImg = true;
        var Max_height = canvas.height;//不论上传的图片多大，保证图片被包含在画布内，以此为依据更改缩放条的最大值
        var Max_width = canvas.width;
        _that.maxValue = (Max_width/img.width<Max_height/img.height?Max_width/img.width:Max_height/img.height)*100;
        _that.value = 100<_that.maxValue?100:_that.maxValue;
        var scale = _that.value/100;
        _that.context.drawImage(img,0,0,img.width*scale,img.height*scale);
      }
    },
    showImage(){
      this.hasImg = false;
      var canvas = document.getElementById("inner")
      canvas.height = 150;//内部canvas和截取框等大
      canvas.width = 150;
      var context = canvas.getContext("2d");
      var img = new Image();
      img.src = this.imageUrl;
      var _that = this
      img.onload = function(){
        var scale = _that.value/100;
        context.drawImage(img,-_that.X,-_that.Y,img.width*scale,img.height*scale);//实现截图
        _that.dataUrl = canvas.toDataURL(_that.type);//获取base64
        _that.showImg = true;//显示图片
        _that.$parent.renewAvatar(_that.dataUrl)
        _that.hasImg = true;
        canvas.height = 0;
        canvas.width = 0;
      }
    },
    exit(){
      this.$parent.hideCutter()
    }
  }
};//CSS一定要采用绝对定位！！！
</script>
<style scoped>
#canvas{
  position: absolute;
  left: 0px;
  top: 0px;
}
#editBox{
  border: #888888 solid 20px;
  background-color: #888888;
  position: absolute;
  left: 400px;
  top: 100px;
  height: 500px;
  width: 750px;
  border-radius: 5px;
  box-shadow: 0 0 10px black;
  z-index: 1000;
}
.demonstration{
  position: absolute;
  bottom: 10px;
  left: 10px;
  color: white;
}
.slider{
  position: absolute;
  left: 70px;
  bottom: 0px;
  width: 550px;
}
.block{
  position: absolute;
  bottom: 0px;
  width: 700px;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 50%;
}
.ok{
  cursor: pointer;
  position: absolute;
  right: 65px;
  bottom:-2px;
  height: 40px;
  width: 40px;
  font-size: 20px;
  line-height: 40px;
  color: white;
  background: url(../assets/icon/cut.png) no-repeat;
  background-size: contain;
}
.exit{
  cursor: pointer;
  position: absolute;
  right: 15px;
  bottom:-2px;
  height: 40px;
  width: 40px;
  font-size: 20px;
  line-height: 40px;
  color: white;
  background: url(../assets/icon/back.png) no-repeat;
  background-size: contain;
}
#cutter{
  position: absolute;
  border: 2px solid red;
  height: 150px;
  width: 150px;
  border-radius: 75px;
}
#inner{
  position: absolute;
  left: 0px;
  top: 0px;
}
/deep/ .el-slider__runway {
  background-color: white;
}
/deep/ .el-slider__bar {
  background-color: black;
}
/deep/ .el-slider__button {
  background-color: white;
  border: solid 2px black;
}
</style>