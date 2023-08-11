<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar />
    </el-aside>

    <el-container>
      <el-header style="font-size: 30px; border-bottom: 1px solid #ccc; line-height: 30px; display: flex">
        <handicapSearch />
      </el-header>
      <el-main>
        <oneHandCountChart/>
      </el-main>
    </el-container>
    </el-container>
</template>

<script>

import navbar from '../layout/navbar';
import handicapSearch from "./handicapSearch";
import oneHandCountChart from "./oneHandCountChart"

  export default {
    name: 'handicap',
    components: {handicapSearch, navbar, oneHandCountChart},
    data() {
      return {

      }
    },
    created() {
      document.title = '盘口技术';
      // 请求分页查询数据
      this.load()
    },
    methods: {
    }
  }
</script>

