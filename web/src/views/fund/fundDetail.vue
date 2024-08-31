<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="历史持股明细"></el-descriptions>
          <el-table
            :data="tableData"
            height="350"
            border
            style="width: 100%"
            :default-sort = "{prop: 'reportDate', order: 'descending'}"
          >

            <el-table-column
              prop="parentOrgName"
              label="机构组织名称"
              width="180">
            </el-table-column>
            <el-table-column
              prop="holderName"
              label="机构名称">
            </el-table-column>
            <el-table-column
              prop="reportDate"
              label="报告日期"
              width="180">
            </el-table-column>
            <el-table-column
              prop="code"
              label="股票代码">
            </el-table-column>
            <el-table-column
              prop="name"
              label="股票名称">
            </el-table-column>
            <el-table-column
              prop="totalShares"
              label="持股总数">
            </el-table-column>
            <el-table-column
              prop="totalSharesRatio"
              label="占总股本比例(%)">
            </el-table-column>
          </el-table>

        </div>

      </el-main>

    </el-container>
  </div>
</template>

<script>
  import {dateFormat} from "../../fun/timeUtil.js";

  export default {
    name: 'fundDetail',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
      this.$eventBus.$on( 'fund' , (holderName)=>{
        fetch("http://127.0.0.1:28003/fund/fundDetail/" +holderName)
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["reportDate"] = dateFormat(re.reportDate);
            obj["parentOrgName"] = re.parentOrgName;
            obj["holderName"] = re.holderName;
            obj["code"] = re.code;
            obj["name"] = re.name;
            obj["totalShares"] = re.totalShares;
            obj["totalSharesRatio"] = re.totalSharesRatio;
            this.tableData.push(obj);
          }
        })
      } )
    }
  }
</script>

