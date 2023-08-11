// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/store'
import ElementUI from 'element-ui';
import * as echarts from 'echarts'
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'

Vue.config.productionTip = false
Vue.use(ElementUI, { size: "mini" });
Vue.prototype.$eventBus = new Vue()
Vue.prototype.$echarts = echarts


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})


