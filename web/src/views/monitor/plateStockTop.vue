<template>
  <div>
  <el-container style=" border: 1px solid #eee">
      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="今日异动推荐">
          </el-descriptions>
          <el-table
            :data="tableData"
            height="850"
            border
            style="width: 100%"
         >
            <el-table-column
              prop="reportDate"
              label="报告日期">
            </el-table-column>
            <el-table-column
              prop="name"
              label="名称">
            </el-table-column>
            <el-table-column
              prop="code"
              label="代码">
            </el-table-column>

            <el-table-column
              prop="dongcaiIndustry"
              label="所属东财行业"
              sortable>
            </el-table-column>
            <el-table-column
              prop="flowMarket"
              label="实际流通值"
              sortable>
            </el-table-column>

            <el-table-column
              prop="fireTimes"
              label="火箭发射次数"
              sortable>
            </el-table-column>

            <el-table-column
              label="火箭发射时间间隔">
              <el-table-column
                prop="fireDays"
                label="距上次"
                sortable>
              </el-table-column>
              <el-table-column
                prop="fireDays1"
                label="距上上"
                sortable>
              </el-table-column>
            </el-table-column>


            <el-table-column
              label="累计涨跌">
              <el-table-column
                prop="pchg"
                label="今日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="pchg3"
                label="最近3日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="pchg5"
                label="最近5日"
                sortable>
              </el-table-column>
            </el-table-column>

            <el-table-column
              label="累计有效换手率">
              <el-table-column
                prop="changHands"
                label="今日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="changHands3"
                label="最近3日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="changHands5"
                label="最近5日"
                sortable>
              </el-table-column>
            </el-table-column>

          </el-table>
          <el-table
            :data="tableDataCount"
            height="350"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="dongcaiIndustryCount"
              label="所属东财行业"
              sortable>
            </el-table-column>
            <el-table-column
              prop="avgFlowMarket"
              label="行业平均实际流通值"
              sortable>
            </el-table-column>
            <el-table-column
              prop="stockNum"
              label="股票数量"
              sortable>
            </el-table-column>

            <el-table-column
              prop="fireAvgTimes"
              label="火箭发射平均次数"
              sortable>
            </el-table-column>

            <el-table-column
              label="累计涨跌">
              <el-table-column
                prop="pchgCount"
                label="今日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="pchg3Count"
                label="最近3日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="pchg5Count"
                label="最近5日"
                sortable>
              </el-table-column>
            </el-table-column>

            <el-table-column
              label="累计有效换手率">
              <el-table-column
                prop="changHandsCount"
                label="今日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="changHands3Count"
                label="最近3日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="changHands5Count"
                label="最近5日"
                sortable>
              </el-table-column>
            </el-table-column>

          </el-table>

        </div>

      </el-main>

    </el-container>
  </div>
</template>

<script>
  import {dateFormat} from "../../fun/timeUtil.js";
  import {keepTwoDecimal} from "../../fun/mathUtil.js";
  export default {
    name: 'plateStockTop',
    data() {
      return {
        tableData:[],
        tableDataCount:[]
      }
    },
    created() {
      fetch("http://localhost:28003/monitor/selectPlateStockTop/")
        .then(res =>res.json()).then(res => {
        console.log(res);
        this.addData(res);
        this.groupBY(res);
      })
    },
    methods: {
      addData(res){
        this.tableData = [];
        for (let i = 0; i < res.length; i++) {
          let re = res[i];
          let obj = {};
          obj["reportDate"] = dateFormat(re.reportDate);
          obj["name"] = re.name;
          obj["code"] = re.code;
          obj["dongcaiIndustry"] = re.dongcaiIndustry;
          obj["flowMarket"] = re.flowMarket;
          obj["fireTimes"] = re.fireTimes;
          obj["fireDays"] = re.fireDays;
          obj["fireDays1"] = re.fireDays1;
          obj["pchg"] = re.pchg;
          obj["pchg3"] = re.pchg3;
          obj["pchg5"] = re.pchg5;
          obj["changHands"] = re.changHands;
          obj["changHands3"] = re.changHands3;
          obj["changHands5"] = re.changHands5;
          this.tableData.push(obj);
        }
      },
      groupBY: function(res){
        this.tableDataCount = [];
        let gd = res.reduce(function (acc,obj) {
          let key = obj.dongcaiIndustry;
          if(!acc[key]){
            acc[key] = [];
          }
          acc[key].push(obj);
          return acc;
        },{});
        for(let key in gd){
          let gdElement = gd[key];
          let len = gdElement.length;

          let avgFlowMarket = 0;
          let stockNum = 0;
          let fireAvgTimes = 0;
          let pchgCount = 0.0;
          let pchg3Count = 0.0;
          let pchg5Count = 0.0;
          let changHandsCount = 0.0;
          let changHands3Count = 0.0;
          let changHands5Count = 0.0;

          for (let i = 0; i < len; i++) {
            let element = gdElement[i];
            avgFlowMarket = avgFlowMarket + element.flowMarket;
            stockNum++;
            fireAvgTimes = fireAvgTimes + element.fireTimes;
            pchgCount = pchgCount + element.pchg;
            pchg3Count = pchg3Count + element.pchg3;
            pchg5Count = pchg5Count + element.pchg5;
            changHandsCount = changHandsCount + element.changHands;
            changHands3Count = changHands3Count + element.changHands3;
            changHands5Count = changHands5Count + element.changHands5;
          }

          let obj = {};
          obj["dongcaiIndustryCount"] = key;
          obj["avgFlowMarket"] = keepTwoDecimal(avgFlowMarket/len);
          obj["stockNum"] = stockNum;
          obj["fireAvgTimes"] = keepTwoDecimal(fireAvgTimes/len);
          obj["pchgCount"] = keepTwoDecimal(pchgCount/len);
          obj["pchg3Count"] = keepTwoDecimal(pchg3Count/len);
          obj["pchg5Count"] = keepTwoDecimal(pchg5Count/len);
          obj["changHandsCount"] = keepTwoDecimal(changHandsCount/len);
          obj["changHands3Count"] = keepTwoDecimal(changHands3Count/len);
          obj["changHands5Count"] = keepTwoDecimal(changHands5Count/len);
          this.tableDataCount.push(obj);
        }
      },
      filterHandler(value, row, column) {

      }
    }
  }
</script>

