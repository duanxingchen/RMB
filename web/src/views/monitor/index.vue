<template>
  <el-container style=" border: 1px solid #eee">
    <el-aside :width="180 + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41/35%);">
      <navbar/>
    </el-aside>


    <el-container>
      <el-main>
        <plate-self-stock/>
        <plate-stock-top/>
        <plate-block-from-dong-cai/>
        <plateBlockCount/>
        <monitor-search/>
        <plate-block-detail/>
        <plate-block-chart/>

      </el-main>
    </el-container>

    </el-container>
</template>

<script>


  import navbar from "../layout/navbar";
  import PlateSelfStock from "./plateSelfStock";
  import plateBlockCount from "./plateBlockCount";
  import MonitorSearch from "./monitorSearch";
  import MonitorFlash from "./monitorFlash";
  import PlateBlockChart from "./plateBlockChart";
  import PlateBlockDetail from "./plateBlockDetail";
  import PlateBlockFromDongCai from "./plateBlockFromDongCai";
  import PlateStockTop from "./plateStockTop";
  import PlateBlockHotChart from "../plateCharts/plateBlockHotChart";

  export default {
    name: 'monitor',
    components: {
      PlateBlockHotChart,
      PlateStockTop,
      PlateBlockFromDongCai,
      PlateBlockDetail, PlateBlockChart, MonitorFlash, MonitorSearch, plateBlockCount, PlateSelfStock, navbar},
    data() {
      return {
      }
    },
    created() {
      document.title = '异常监控';
    }
  }
</script>

