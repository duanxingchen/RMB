<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar/>
    </el-aside>

    <el-container>
      <el-main>
        <stock-selection/>
        <departure/>
      </el-main>
    </el-container>
    </el-container>
</template>

<script>

import navbar from "../layout/navbar";
import StockSelection from "./stockSelection";
import Departure from "./departure";


  export default {
    name: 'recommend',
    components: { Departure, StockSelection, navbar},
    data() {
      return {
      }
    },
    created() {
      document.title = '条件推荐';
    }
  }
</script>

