<template>
  <div>
  <el-container style=" border: 1px solid #eee">
      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="自选股票异常">
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
              :filters="[{text: '最近三天', value: '3'},{text: '最近五天', value: '5'},{text: '最近十天', value: '10'},{text: '最近二十天', value: '20'}]"
              :filter-method="filterDaysHandler">
            </el-table-column>
            <el-table-column
              prop="name"
              label="名称">
            </el-table-column>
            <el-table-column
              prop="plate"
              label="异常类型"
              :filters="[{text: '涨', value: 'up'},{text: '跌', value: 'down'}]"
              :filter-method="filterHandler">
            </el-table-column>
            <el-table-column
              prop="pchg"
              label="涨跌幅">
            </el-table-column>
            <el-table-column
              prop="vo"
              label="成交量">
            </el-table-column>
          </el-table>
        </div>

      </el-main>

    </el-container>
  </div>
</template>

<script>
  import {timeFormat} from "../../fun/timeUtil.js";
  export default {
    name: 'plateSelfStock',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
      fetch("http://192.168.1.6:28003/monitor/selectPlateSelfStock/")
        .then(res =>res.json()).then(res => {
        console.log(res);
        this.tableData = [];
        for (let i = 0; i < res.length; i++) {
          let re = res[i];
          let obj = {};
          obj["reportDate"] = timeFormat(re.reportDate);
          obj["name"] = re.name;
          obj["plate"] = re.plate;
          obj["pchg"] = re.pchg;
          obj["vo"] = re.vo;
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

      },
      filterHandler(value, row, column) {
        let upList = ["火箭发射"];
        let downList = ["高台跳水"];

        const property = column['property'];
        let v = row[property];
        console.log(row[property].search(value))
        if(value === "up"){
          for (let i = 0; i < upList.length; i++) {
            if(v === upList[i]){
              return true;
            }
          }
        }

        if(value === "down"){
          for (let i = 0; i < downList.length; i++) {
            if(v === downList[i]){
              return true;
            }
          }
        }

        return false;
      }
    }
  }
</script>

