<template>
  <div>
    <el-header>
      <Header 
      :activeIndex="activeIndex" 
      @changeIndex="changeIndex" 
      @showLogin="showLogin"
      :avatar="avatar" 
      :nickName="nickName" 
      :hasIn="false"
      ref="header"
      ></Header>
    </el-header>
    <Login 
    v-show="showBoxId==1"
    @hideBox="hideBox" 
    @showRegister="showRegister" 
    @editPassword="editPassword"></Login>
    <Register
    v-show="showBoxId==2" 
    @showLogin="showLogin"></Register>
    <EditPasswd
    v-show="showBoxId==3" 
    @showLogin="showLogin"
    @resetPassword="resetPassword"></EditPasswd>
    <Reset
    v-show="showBoxId==4" 
    @editPassword="editPassword"></Reset>
  </div>
</template>
<script>
import Header from '../components/Header.vue'
import Login from '../components/Login.vue'
import Register from '../components/Register.vue'
import Reset from '../components/Reset.vue'
import EditPasswd from '../components/EditPasswd.vue'
export default {
  name: "testLogin",
  mounted(){

  },
  components:{
    Header,
    Login,
    Register,
    Reset,
    EditPasswd
  },
  props:{
    
  },
  data() {
    return {
      activeIndex:'1',
      showBoxId:'0',
      avatar: '/avatar/avatar.png',
      nickName: '未登录'
    };
  },
  methods: {
    resetPassword(){
      this.showBoxId = '4'
    },
    editPassword(){
      this.showBoxId = '3'
    },
    showRegister(){
      this.showBoxId = '2'
    },
    showLogin(){
      this.showBoxId = '1'
    },
    hideBox(){
      this.showBoxId = '0'
    },
    changeIndex(index){
      var _that = this
      this.activeIndex=index
    },
  }
};
</script>
<style scoped>

</style>