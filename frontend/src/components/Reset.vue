<template>
  <div class="login" clearfix>
    <div class="login-wrap" id="warp">
      <el-row type="flex" justify="center" id="user">
        <el-form ref="loginForm" :rules="rules" :model="user" label-position="left" label-width="80px">
          <el-form-item>
            <div style="color:#cc3366;text-shadow: 0 0 1px green;position:relative;right:20px;">系统重置密码将发送至您的邮箱~</div>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="user.email" placeholder="请输入邮箱" prefix-icon></el-input>
          </el-form-item>
          <el-form-item id="last">
            <a style="font-size:12px;color:black;cursor:pointer;" @click="editPassword">返回</a>
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
  name: "Reset",
  props:{
  },
  mounted(){
    this.user.email = ''
  },
  data: function() {
    return {
      user:{
        email: ''
      },
      rules: {
        email: [
            {required:true,message:"请输入邮箱", trigger:'blur'},
            {pattern:/^([A-Za-z0-9_\-.])+@([A-Za-z0-9_\-.])+.([A-Za-z]{2,4})$/,message:"请输入合法邮箱！", trigger:'blur'}
        ]
      }
    }
  },
  methods:{
      doLogin(formName){
        this.$refs[formName].validate((valid) => {
        if(valid){
          this.$axios
          .post("/user/resetPass",{
            email:this.user.email
          })
          .then(res => {
            if(res.data.code === 200){
              this.$message({
                message:'重置密码已发送至您的邮件！',
                type:'success'
              });
            }
            else{
              this.$message.error(res.data.message);
            }
            this.email=''
          })
          .catch(failResponse=>{
            console.log(failResponse);
            this.$message.error('网络错误');
            this.email=''
          })
        }
        else{
          this.$message.error('信息不合法');
          this.email=''
        }
        })
      },
      editPassword(){
        this.$parent.editPassword()
        this.email=''
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
  height: 300px;
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
  margin-right: -50px;
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
  background: url('../assets/icon/resetPasswd.png') no-repeat;
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
