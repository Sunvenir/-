<template>
  <div>
    <Player ref='player' style="position:absolute;top:0px;"></Player>
    <el-input v-model="input" style="position:absolute;top:560px;left:0px;width:550px;"></el-input>
    <el-button @click="send" style="position:absolute;top:560px;left:650px;">发送</el-button>
  </div>
</template>
<script>
import Player from '../components/Player.vue'
export default {
  name: "testDan",
  components: {
    Player
  },
  data() {
    return {
      input:''
    };
  },
  methods: {
    send(){
      this.$refs.player.sendDan(this.input)
    }
  }
};
</script>
<style scoped>

</style>