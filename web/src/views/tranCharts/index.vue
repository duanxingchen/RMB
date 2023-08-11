<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar/>
    </el-aside>

    <el-container>
      <el-main>
        <tran-hot-chart/>

      </el-main>
    </el-container>
    </el-container>
</template>

<script>

import navbar from "../layout/navbar";
import plateBlockHotChart from "./tranHotChart";
import TranHotChart from "./tranHotChart";

  export default {
    name: 'tranCharts',
    components: {TranHotChart, navbar},
    data() {
      return {
      }
    },
    created() {
      document.title = '交易直观图';
    }
  }
</script>

