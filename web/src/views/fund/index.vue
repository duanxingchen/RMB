<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar />
    </el-aside>

    <el-container>
      <el-header style="font-size: 30px; border-bottom: 1px solid #ccc; line-height: 30px; display: flex">
        <fundSearch/>
      </el-header>
      <el-main>
        <fund-detail/>
        <Coordinate/>
        <self-fund/>
      </el-main>
    </el-container>
    </el-container>
</template>

<script>

import navbar from "../layout/navbar";
import fundSearch from "./fundSearch";
import FundDetail from "./fundDetail";
import Coordinate from "./coordinate";
import SelfFund from "./selfFund";

  export default {
    name: 'fund',
    components: {SelfFund, Coordinate, FundDetail, fundSearch, navbar},
    data() {
      return {

      }
    },
    created() {
      document.title = '私募基金';
      // 请求分页查询数据
      this.load()
    },
    methods: {
    }
  }
</script>

