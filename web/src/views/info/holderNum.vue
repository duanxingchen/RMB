<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <el-descriptions title="股东人数明细">        </el-descriptions>
        <el-table
          :data="tableData"
          height="350"
          border
          style="width: 100%"
          :default-sort = "{prop: 'reportDate', order: 'descending'}"
        >
          <el-table-column
            prop="reportDate"
            label="报告日期"
            width="180">
          </el-table-column>
          <el-table-column
            prop="holderNum"
            label="股东人数"
            width="180">
          </el-table-column>
          <el-table-column
            prop="holderNumChange"
            label="股东人数变化">
          </el-table-column>
          <el-table-column
            prop="holdNoticeDate"
            label="通知时间">
          </el-table-column>
          <el-table-column
            prop="holderNumRatio"
            label="股东人数变化率">
          </el-table-column>
          <el-table-column
            prop="changeReason"
            label="原因">
          </el-table-column>
          <el-table-column
            prop="totalAShares"
            label="总股本">
          </el-table-column>
        </el-table>
      </el-main>

    </el-container>
  </div>
</template>

<script>
  import {dateFormat} from "../../fun/timeUtil.js";

  export default {
    name: 'holderNum',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
      this.$eventBus.$on( 'info' , (code)=>{
        fetch("http://192.168.1.6:28003/info/holderNum/" +code)
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["reportDate"] = dateFormat(re.reportDate);
            obj["holderNum"] = re.holderNum;
            obj["holderNumChange"] = re.holderNumChange;
            obj["holdNoticeDate"] = dateFormat(re.holdNoticeDate);
            obj["holderNumRatio"] = re.holderNumRatio;
            obj["changeReason"] = re.changeReason;
            obj["totalAShares"] = re.totalAShares;
            this.tableData.push(obj);
          }
        })
      } )
    },
    methods: {

    }
  }
</script>

