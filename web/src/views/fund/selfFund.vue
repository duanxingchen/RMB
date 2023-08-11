<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="自选私募基金"></el-descriptions>
          <el-table
            :data="tableData"
            height="350"
            border
            style="width: 100%"
            :default-sort = "{prop: 'holderName', order: 'descending'}"
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
              label="当前报告时间"
              width="180">
            </el-table-column>
            <el-table-column
              prop="code"
              label="当前股票代码">
            </el-table-column>
            <el-table-column
              prop="name"
              label="当前股票名称">
            </el-table-column>
            <el-table-column
              prop="totalShares"
              label="当前持股总数">
            </el-table-column>
            <el-table-column
              prop="totalSharesRatio"
              label="当前占总股本比例(%)">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="100">
              <template slot-scope="scope">
                <el-button @click="handleClick(scope.row)" type="text" size="small">删除</el-button>
              </template>
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
    name: 'selfFund',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
        fetch("http://localhost:28003/fund/selfFund/selectAll")
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
    },
    methods:{
      handleClick(row) {
        console.log(row);
      }
    }
  }
</script>

