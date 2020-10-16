<template>
<div>
  <el-menu 
  :default-active="activeIndex" 
  class="header"
  mode="horizontal" 
  style="position:fixed;top:0;width:100%;left:0;z-index:1000;"
  active-text-color="red"
  background-color="#cccccc"
  text-color="black">
    <el-menu-item>
      <el-image :fit="fit" src="/logo/logo.png" style="width:150px;height:50px;"></el-image>
    </el-menu-item>
    <el-menu-item index="1" @click="chHome()" style="font-family:xingkai;font-size:20px;">首页</el-menu-item>
    <el-menu-item index="2" @click="chSearch()" style="font-family:xingkai;font-size:20px;">搜索</el-menu-item>
    <el-menu-item index="3" @click="chLive()" style="font-family:xingkai;font-size:20px;">直播</el-menu-item>
    <el-menu-item index="4" @click="chUpload()" style="font-family:xingkai;font-size:20px;">上传</el-menu-item>
    <el-menu-item index="5" @click="chManage()" style="font-family:xingkai;font-size:20px;" v-show="isManager">管理</el-menu-item>
    <el-menu-item style="float:right;">
      <el-dropdown>
        <span class="el-dropdown-link">
          <el-upload style="float:left;height:42px;width:42px;margin-right:15px;" :before-upload="editAvatar" action="#">
            <el-image :src="avatar" class="avatar"></el-image>
          </el-upload>
          <span>{{nickName}}</span>
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu>
          <el-dropdown-item>
            <!-- <div v-if="hasIn" style="color:red;" @click="showPersonal()">
              修改信息
              <i class="el-icon-caret-right"></i>
            </div> -->
            <div style="color:red;" @click="showLogin()">
              登录
              <i class="el-icon-caret-right"></i>
            </div>
          </el-dropdown-item>
          <el-dropdown-item  v-show="!isManager">
            <div style="color:red;" @click="teacherConfirm()">
              教师认证
              <i class="el-icon-caret-right"></i>
            </div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div style="color:red;" @click="toMyCourse()">
              我的课程
              <i class="el-icon-caret-right"></i>
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-menu-item>
  </el-menu>
  <Login v-if="onshow=='login'"></Login>
  <Register v-if="onshow=='register'"></Register>
  <EditPassword v-if="onshow=='editPass'"></EditPassword>
  <Reset v-if="onshow=='reset'"></Reset>
  <AvatarSelect v-show="showCutter" ref="avatarSelect"></AvatarSelect>
</div>
</template>
<script>
import Login from "./Login.vue"
import Register from "./Register.vue"
import EditPassword from "./EditPasswd.vue"
import Reset from "./Reset.vue"
import AvatarSelect from "./AvatarSelect.vue"
export default {
  name: "Header",
  components:{
    Login,
    Register,
    EditPassword,
    Reset,
    AvatarSelect
  },
  props:{
    activeIndex:String
  },
  mounted(){
    if(window.sessionStorage.getItem("nickName")!=null){
      this.resetNickname(window.sessionStorage.getItem("nickName"))
    }
    if(window.sessionStorage.getItem("avatar")!=null){
      this.resetAvatar(window.sessionStorage.getItem("avatar"))
    }
  },
  data() {
    return {
      avatar: "/avatar/avatar.png",
      nickName: "未登录",
      hasIn: false,
      isTeacher: false,
      showCutter: false,
      fit: "scale-down",
      onshow: "none",
      isManager: window.sessionStorage.getItem("uid")=="12345678"||window.sessionStorage.getItem("power")=="1",
      isTeacher: window.sessionStorage.getItem("power")=="1"
    };
  },
  methods: {
    // showPersonal() {
    //   this.$emit("showPersonal",1);
    // },
    chHome(){
      this.$router.replace('/')
    },
    chSearch(){
      this.$router.replace('/search')
    },
    chLive(){
      if(window.sessionStorage.getItem("uid")==""){
        this.$message.error("请先登录！")
        this.chHome
        return
      }
      console.log('Live')
    },
    chUpload(){
      this.$router.replace('/upload')
    },
    chManage(){
      if(window.sessionStorage.getItem("uid")=="12345678"){
        this.$router.replace('/manageUsers')
      }
      else if(window.sessionStorage.getItem("power")=="1"){
        this.$router.replace('/manageStudents')
      }
    },
    getPower(){
      return window.sessionStorage.getItem("uid")=="12345678"||window.sessionStorage.getItem("power")=="1"
    },
    showLogin(){
      this.onshow='login'
    },
    editPassword(){
      this.onshow='editPass'
    },
    showRegister(){
      this.onshow='register'
    },
    hideBox(){
      this.onshow='none'
    },
    resetPassword(){
      this.onshow='reset'
    },
    resetAvatar(avatar){
      this.avatar = avatar
    },
    resetNickname(name){
      this.nickName = name
    },
    toMyCourse(){
      if(window.sessionStorage.getItem("uid")==""||window.sessionStorage.getItem("uid")==null){
        this.$message.error('请先登录！')
        return
      }
      this.$router.replace('/course')
    },
    teacherConfirm(){
      if(window.sessionStorage.getItem("uid")==""||window.sessionStorage.getItem("uid")==null){
        this.$message.error('请先登录！')
        return
      }
      this.$alert('认证教师需要提交相关证明材料，确定提交申请？', '教师认证', {
        confirmButtonText: '确定',
        callback: action => {
          if(action=='cancel') return
          this.$axios.post("/api/authsubmit",{
            userid:window.sessionStorage.getItem("uid"),
            text:''
          })
          .then(res => {
            if(res.data.code === 200){
              this.$message.success('请关注您的邮箱，管理员将会与您联系~');
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
    editAvatar(file){
      if(window.sessionStorage.getItem("uid")==""||window.sessionStorage.getItem("uid")==null){
        this.$message.error('请先登录！')
        return false
      }
      console.log('ok')
      const isJPG = file.type === 'image/jpeg'||file.type === 'image/png';//判定格式
      this.type = file.type;
      if (!isJPG) {
        this.$message.error('封面图片只能是 JPG 或 PNG 格式!');
        return false
      }
      this.$refs.avatarSelect.parseFile(file)
      this.showCutter = true
      this.$refs.avatarSelect.resize()
      return false
    },
    hideCutter(){
      this.showCutter=false
    },
    renewAvatar(data){
      this.$axios.post("/api/edit",{
        avatar:data,
        id:window.sessionStorage.getItem("uid")
      })
      .then(res => {
        if(res.data.code === 200){
          this.$message.success('头像更改成功！');
          window.sessionStorage.setItem("avatar", data)
          this.avatar = data
          this.showCutter = false
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
    resize(){
      this.$parent.resize()
    }
  }
};
</script>
<style scoped>
.header{
  box-shadow: 2px 2px 2px #888888;
}
.avatar {
  height: 42px;
  width: 42px;
  border-radius: 21px;
  transform: rotate(0deg);
  transition: transform;
  -ms-transform: rotate(0deg);
  -moz-transform: rotate(0deg);
  -webkit-transform: rotate(0deg);
  -o-transform: rotate(0deg);
  transition-duration: 1.5s;

}
.avatar:hover {
  transform: rotate(360deg);
  -ms-transform: rotate(360deg);
  -moz-transform: rotate(360deg);
  -webkit-transform: rotate(360deg);
  -o-transform: rotate(360deg);
}
.froms{
  z-index: 1000
}
@font-face {
    font-family: xingkai;
    src: url(/font/STXINGKA.TTF)
}
</style>