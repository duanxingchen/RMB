<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <navbar/>
    </el-aside>

    <el-container>
      <el-main>
        <holder-all/>
        <holder-stock-chart/>
        <holderBlockChart/>

      </el-main>
    </el-container>
  </el-container>
</template>

<script>

  import navbar from "../layout/navbar";
  import HolderAll from "./holderAll";
  import HolderStockChart from "./holderStockChart";
  import holderBlockChart from "./holderBlockChart"

  export default {
    name: 'holderCharts',
    components: {HolderStockChart, HolderAll, navbar,holderBlockChart},
    data() {
      return {
      }
    },
    created() {
      document.title = '股东人数直观图';
    }
  }
</script>

