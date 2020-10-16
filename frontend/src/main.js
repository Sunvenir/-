import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from './axios.js'
import instanse from './axios.js'
// import Socket from './socket.js'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import store from './vuex';
import VueParticles from 'vue-particles'

Vue.use(VueParticles)
Vue.use(ElementUI);
// Vue.use(Vuex);
Vue.prototype.$axios = axios;
// Vue.prototype.$socket = Socket;
Vue.config.productionTip = false

new Vue({
  render: h => h(App), 
  router,
  store
}).$mount('#app')
