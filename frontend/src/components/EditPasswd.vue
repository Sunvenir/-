<template>
  <div class="login" clearfix>
    <div class="login-wrap" id="warp">
      <el-row type="flex" justify="center" id="user">
        <el-form ref="loginForm" :rules="rules" :model="user" label-position="left" label-width="80px">
          <el-form-item prop="username" label="学号">
            <el-input v-model="user.username" placeholder="请输入学号" prefix-icon></el-input>
          </el-form-item>
          <el-form-item prop="opassword" id="opassword" label="旧密码">
            <el-input v-model="user.opassword" show-password placeholder="请输入旧密码"></el-input>
          </el-form-item>
          <el-form-item prop="password" id="password" label="新密码">
            <el-input v-model="user.password" show-password placeholder="请输入新密码"></el-input>
          </el-form-item>
          <el-form-item id="last">
            <a style="font-size:12px;color:black;cursor:pointer;" @click="resetPassword">重置密码</a>
            <a style="font-size:12px;color:black;cursor:pointer;" @click="showLogin">返回</a>
          </el-form-item>
        </el-form>
      </el-row>
      <el-row>
        <div class="login_bt" @click="doLogin('loginForm')"></div>
      </el-row>
    </div>
  </div>
</template>
<script>
export default {
  name: "EditPasswd",
  props:{
  },
  mounted(){
    this.user.username=''
    this.user.password=''
    this.user.opassword=''
  },
  data: function() {
    return {
      user: {
        username: "",
        password: "",
        opassword: ""
      },
      rules: {
        username: [
            {required:true,message:"请输入学号", trigger:'blur'},
            {pattern:/^[0-9A-Za-z]{1,8}$/,message:"请输入合法学号！", trigger:'blur'}
        ],
        password: [
            {required:true,message:"请输入密码", trigger:'blur'},
            {pattern:/^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/,message:"密码最少6位且包含大写字母小写字母和数字", trigger:'blur'}
        ],
        opassword: [
            {required:true,message:"请输入密码", trigger:'blur'},
            {pattern:/^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/,message:"密码最少6位且包含大写字母小写字母和数字", trigger:'blur'}
        ]
      }
    }
  },
  methods:{
      doLogin(formName){
        this.$refs[formName].validate((valid) => {
        if(valid){
          this.$axios
          .post("/api/changepassword",{
            id:this.user.username,
            password:this.user.opassword,
            newpassword:this.user.password
          })
          .then(res => {
            if(res.data.code === 200){
              this.$message({
                message:'修改密码成功',
                type:'success'
              });
            }
            else{
              this.$message.error(res.data.message);
            }
            this.user.username=''
            this.user.password=''
            this.user.opassword=''
          })
          .catch(failResponse=>{
            console.log(failResponse);
            this.$message.error('网络错误');
            this.user.username=''
            this.user.password=''
            this.user.opassword=''
          })
        }
        else{
          this.$message.error('信息不合法');
          this.user.username=''
          this.user.password=''
          this.user.opassword=''
        }
        })
      },
      resetPassword(){
        this.$parent.resetPassword()
        this.user.username=''
        this.user.password=''
        this.user.opassword=''
      },
      showLogin(){
        this.$parent.showLogin()
        this.user.username=''
        this.user.password=''
        this.user.opassword=''
      }
  }
};
</script>

<style scoped>
.login {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  zoom: 1;
  background-repeat: no-repeat;
  background-size: cover;
  -webkit-background-size: cover;
  -o-background-size: cover;
  background-position: center 0;
  color: white;
}
.login-wrap{
  background: url('../assets/login.png') no-repeat;
  background-color: gray;
  color: #222222;
  background-size: cover;
  width: 400px;
  height: 350px;
  overflow: hidden;
  padding-top: 10px;
  padding-bottom: 20px;
  line-height: 40px;
  border-radius: 5px;
  text-align: center;
  box-shadow: 0 0 5px #222222; 
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}
a {
  margin-right: 10px;
  font-size: 5px;
}
#user{
    padding-top:30px;
}
#last{
  position: relative;
    left: 60px;
}
.el-form-item{
    margin:20px;
}
/deep/ .el-form-item__label{
  color: white;
  text-shadow: 0 0 2px black;
}
.login_bt{
  background: url('../assets/icon/resetInfo.png') no-repeat;
  background-size: contain;
  height: 80px;
  width: 80px;
  border-radius: 40px;
  cursor: pointer;
  position: absolute;
  top: -10px;
  left: 160px;
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
