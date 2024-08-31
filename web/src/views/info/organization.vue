<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="机构明细">
          </el-descriptions>
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
              prop="parentOrgName"
              label="机构组织名称"
              width="180">
            </el-table-column>
            <el-table-column
              prop="holderName"
              label="机构名称"
              :filters="[{text: '私募', value: '私募'},{text: '社保', value: '社保'}]"
              :filter-method="filterHandler">
            </el-table-column>
            <el-table-column
              prop="orgType"
              label="机构类型">
            </el-table-column>
            <el-table-column
              prop="freeSharesRatio"
              label="占流通股本比例">
            </el-table-column>
            <el-table-column
              prop="holdMarketCap"
              label="持股市值">
            </el-table-column>
            <el-table-column
              prop="totalShares"
              label="持股总数">
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
    name: 'organization',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
      this.$eventBus.$on( 'info' , (code)=>{
        fetch("http://127.0.0.1:28003/info/organization/" +code)
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["reportDate"] = dateFormat(re.reportDate);
            obj["parentOrgName"] = re.parentOrgName;
            obj["holderName"] = re.holderName;
            obj["orgType"] = re.orgType;
            obj["freeSharesRatio"] = re.freeSharesRatio;
            obj["holdMarketCap"] = re.holdMarketCap;
            obj["totalShares"] = re.totalShares;
            this.tableData.push(obj);
          }

        })
      } )
    },
    methods: {
      filterHandler(value, row, column) {
        console.log(value)
        console.log(row)
        console.log(column)
        const property = column['property'];
        console.log(row[property].search(value))
        return row[property].search(value) !== -1;
      }
    }
  }
</script>

