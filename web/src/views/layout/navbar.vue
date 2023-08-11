<template>
      <el-menu >
        <router-link tag="a" target="_blank" v-for="item of navs" :to="item.path">
            <template>
              <div >
                <span slot="title">{{ item.name }}</span>
              </div>
            </template>
      </router-link>
    </el-menu>
</template>

<script>
  export default {
    name:"navbar",
    data() {
      return {
        navs: [
          {
          name: '条件推荐',
          path: 'recommend'
          },
          {
            name: '个股信息',
            path: 'info'
          },
          {
            name: '私募基金',
            path: 'fund'
          },
          {
            name: '异常监控',
            path: 'monitor'
          },
          {
            name: '历史统计',
            path: 'history'
          }
          ,
          {
            name: '盘口技术',
            path: 'handicap'
          },
          {
            name: '异常直观图',
            path: 'plateCharts'
          },
          {
            name: '交易直观图',
            path: 'tranCharts'
          },
          {
            name: '股东人数直观图',
            path: 'holderCharts'
          }],
      };
    },
    methods: {
    }
  }
</script>

