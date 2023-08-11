import Vue from 'vue'
import Router from 'vue-router'
import info from "../views/info/index";
import history from "../views/history/index";
import monitor from "../views/monitor/index";
import recommend from "../views/recommend/index";
import fund from "../views/fund/index";
import handicap from "../views/handicap/index";
import plateCharts from "../views/plateCharts/index";
import tranCharts from "../views/tranCharts/index";
import holderCharts from "../views/holderCharts/index";
Vue.use(Router)


export default new Router({
  routes: [
    {
      path: '/',
      name: 'info',
      component: info
    },
    {
      path: '/info',
      name: 'info',
      component: info
    },
    {
      path: '/fund',
      name: 'fund',
      component: fund
    },
    {
      path: '/recommend',
      name: 'recommend',
      component: recommend
    },
    {
      path: '/monitor',
      name: 'monitor',
      component: monitor
    },
    {
      path: '/history',
      name: 'history',
      component: history
    }
    ,
    {
      path: '/handicap',
      name: 'handicap',
      component: handicap
    }
    ,
    {
      path: '/plateCharts',
      name: 'plateCharts',
      component: plateCharts
    }
    ,
    {
      path: '/tranCharts',
      name: 'tranCharts',
      component: tranCharts
    } ,
    {
      path: '/holderCharts',
      name: 'holderCharts',
      component: holderCharts
    }
  ]
})
