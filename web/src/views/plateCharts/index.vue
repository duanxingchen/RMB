<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar/>
    </el-aside>

    <el-container>
      <el-main>
        <plateBlockHotChart/>

      </el-main>
    </el-container>
    </el-container>
</template>

<script>

import navbar from "../layout/navbar";
import plateBlockHotChart from "./plateBlockHotChart";

  export default {
    name: 'plateCharts',
    components: { navbar,plateBlockHotChart},
    data() {
      return {
      }
    },
    created() {
      document.title = '异常直观图';
    }
  }
</script>

