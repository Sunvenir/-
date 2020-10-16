<template>
  <div class="login" clearfix>
    <div class="login-wrap" id="warp">
      <el-row type="flex" justify="center" id="user">
        <el-form ref="loginForm" :rules="rules" :model="user" label-position="left" label-width="80px">
          <el-form-item prop="id" label="学号">
            <el-input v-model="user.id" placeholder="请输入学号" prefix-icon></el-input>
          </el-form-item>
          <el-form-item prop="username" label="姓名">
            <el-input v-model="user.username" placeholder="请输入姓名" prefix-icon></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input v-model="user.password" show-password placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item prop="confirm" label="确认密码">
            <el-input v-model="user.confirm" show-password placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="user.email" placeholder="请输入邮箱" prefix-icon></el-input>
          </el-form-item>
          <el-form-item prop="phone" label="联系方式">
            <el-input v-model="user.phone" placeholder="请输入手机号" prefix-icon></el-input>
          </el-form-item>
          <el-form-item id="last">
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
  name: "Register",
  props:{
  },
  mounted(){
    this.user.username=''
    this.user.password=''
  },
  data: function() {
    var validatePass2 = (rule, value, callback) => {
        if(value === ''){
            callback(new Error('请再次输入密码'));
        }
        else if(value !== this.user.password){
            callback(new Error('两次输入密码不一致！'));
        }
        else{
            callback();
        }
      };
    return {
      user: {
        id: "",
        username: "",
        password: "",
        confirm: "",
        email: "",
        phone: ""
      },
      rules: {
        // /^[0-9\u4e00-\u9fa5]{2,15}$/
        id: [
            {required:true,message:"请输入学号", trigger:'blur'},
            {pattern:/^[0-9A-Za-z]{1,8}$/,message:"请输入合法学号！", trigger:'blur'}
        ],
        username: [
          { required: true, message: "请输入昵称", trigger:'blur' },
          { pattern: /^[0-9\u4e00-\u9fa5]{2,12}$/, message:"姓名由2到12位汉字或数字组成", trigger:'blur'}
        ],
        password: [
            {required:true,message:"请输入密码", trigger:'blur'},
            {pattern:/^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/,message:"密码最少6位且包含大写字母小写字母和数字", trigger:'blur'}
        ],
        confirm: [
            {required:true,message:"请再次输入密码", trigger:'blur'},
            {validator:validatePass2,trigger:'blur'}
        ],
        email: [
            {required:true,message:"请输入邮箱", trigger:'blur'},
            {pattern:/^([A-Za-z0-9_\-.])+@([A-Za-z0-9_\-.])+.([A-Za-z]{2,4})$/,message:"请输入合法邮箱！", trigger:'blur'}
        ],
        phone: [
            {required:true,message:"请输入手机号", trigger:'blur'},
            {pattern:/^\d{11}/,message:"请输11位手机号！", trigger:'blur'}
        ]
      }
    }
  },
  methods:{
      doLogin(formName){
        this.$refs[formName].validate((valid) => {
        if(valid){
          this.$axios
          .post("/api/register",{
            id:this.user.id,
            username:this.user.username,
          	email:this.user.email,
            password:this.user.password,
            phone:this.user.phone,
            avatar:"/avatar/avatar.png"
          })
          .then(res => {
            if(res.data.code === 200){
              this.$message({
                message:'注册成功',
                type:'success'
              });
            }
            else{
              this.$message.error(res.data.message);
            }
            this.user.id=''
            this.user.username=''
            this.user.password=''
            this.user.confirm=''
            this.user.email=''
            this.user.phone=''
          })
          .catch(failResponse=>{
            console.log(failResponse);
            this.$message.error('网络错误');
            this.user.id=''
            this.user.username=''
            this.user.password=''
            this.user.confirm=''
            this.user.email=''
            this.user.phone=''
          })
        }
        else{
          this.$message.error('信息不合法');
          this.user.id=''
          this.user.username=''
          this.user.password=''
          this.user.confirm=''
          this.user.email=''
          this.user.phone=''
        }
        })
      },
      showLogin(){
      	this.$parent.showLogin()
        this.user.id=''
        this.user.username=''
        this.user.password=''
        this.user.confirm=''
        this.user.email=''
        this.user.phone=''
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
  height: 540px;
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
	background: url('../assets/icon/register.png') no-repeat;
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
