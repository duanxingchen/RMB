<template>
  <div>
  <el-container style=" border: 1px solid #eee">
      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="东财板块异常统计">
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
              width="180"
              :filters="[{text: '最近1天', value: '1'},{text: '最近2天', value: '2'},{text: '最近3天', value: '3'},{text: '最近5天', value: '5'}]"
              :filter-method="filterDaysHandler">
            </el-table-column>
            <el-table-column
              prop="plateName"
              label="板块名称">
            </el-table-column>
            <el-table-column
              prop="rocketLaunch"
              label="火箭发射"
              sortable>
            </el-table-column>

            <el-table-column
              prop="rapidRebound"
              label="快速反弹"
              sortable>
            </el-table-column>

            <el-table-column
              prop="highDiving"
              label="高台跳水"
              sortable>
            </el-table-column>
            <el-table-column
              prop="pallingUp"
              label="竞价上涨"
              sortable>
            </el-table-column>

            <el-table-column
              prop="name"
              label="个股">
            </el-table-column>
            <el-table-column
              prop="socketType"
              label="个股类型">
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
    name: 'plateBlockFromDongCai',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
      fetch("http://127.0.0.1:28003/monitor/selectPlateBlockFromDongCai/")
        .then(res =>res.json()).then(res => {
        console.log(res);
        this.tableData = [];
        for (let i = 0; i < res.length; i++) {
          let re = res[i];
          let obj = {};
          obj["reportDate"] = dateFormat(re.reportDate);
          obj["plateName"] = re.plateName;
          obj["name"] = re.name;
          obj["socketType"] = re.socketType;
          obj["rocketLaunch"] = re.rocketLaunch;
          obj["rapidRebound"] = re.rapidRebound;
          obj["highDiving"] = re.highDiving;
          obj["pallingUp"] = re.pallingUp;
          this.tableData.push(obj);
        }
      })
    },
    methods: {
      filterDaysHandler: function (value, row, column) {
        const property = column['property'];
        let reportDay = row[property];
        let no = new Date();
        if((new Date().getTime() - Date.parse(reportDay)) < value*24*60*60*1000){
          return true;
        }
        return false;

      }
    }
  }
</script>

