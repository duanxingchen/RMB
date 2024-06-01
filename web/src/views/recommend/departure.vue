<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="离场指标"></el-descriptions>
          <el-table
            :data="tableData"
            height="750"
            border
            style="width: 100%"
          >

            <el-table-column
              prop="name"
              label="名称">
            </el-table-column>
            <el-table-column
              prop="code"
              label="代码">
            </el-table-column>
            <el-table-column
              prop="upDownDays"
              label="涨跌连续天数">
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
              <el-table-column
                prop="pchg10"
                label="最近10日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="pchg30"
                label="最近30日"
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
              <el-table-column
                prop="changHands10"
                label="最近10日"
                sortable>
              </el-table-column>
              <el-table-column
                prop="changHands30"
                label="最近30日"
                sortable>
              </el-table-column>
            </el-table-column>



            <el-table-column
              prop="policyAppearance"
              label="政策出现">
            </el-table-column>

            <el-table-column
              prop="blockRoseSharp"
              label="板块全面大涨">
            </el-table-column>

            <el-table-column
              prop="highTurnoverRate"
              label="头一天换手率大">
            </el-table-column>
            <el-table-column
              prop="priceIncrease"
              label="从最底部已大幅上涨">
            </el-table-column>
            <el-table-column
              prop="continuousHighWalk"
              label="股价连续的高走">
            </el-table-column>
            <el-table-column
              prop="dragonsTigersAppears"
              label="出现龙虎榜">
            </el-table-column>

            <el-table-column
              prop="bulkConsignmentOrder"
              label="买盘出现大量托单">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="100">
              <template slot-scope="scope">
                <el-button @click="deleteStock(scope.row)" type="text" size="small">删除</el-button>
                <el-button type="text" size="small">编辑</el-button>
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
    name: 'departure',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
        fetch("http://192.168.1.6:28003/recommend/departure/selectAll")
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["code"] = re.code;
            obj["name"] = re.name;
            obj["upDownDays"] = re.upDownDays;

            obj["pchg"] = re.pchg;
            obj["pchg3"] = re.pchg3;
            obj["pchg5"] = re.pchg5;
            obj["pchg10"] = re.pchg10;
            obj["pchg30"] = re.pchg30;

            obj["changHands"] = re.changHands;
            obj["changHands3"] = re.changHands3;
            obj["changHands5"] = re.changHands5;
            obj["changHands10"] = re.changHands10;
            obj["changHands30"] = re.changHands30;

            obj["policyAppearance"] = re.policyAppearance;
            obj["blockRoseSharp"] = re.blockRoseSharp;
            obj["highTurnoverRate"] = re.highTurnoverRate;
            obj["priceIncrease"] = re.priceIncrease;
            obj["continuousHighWalk"] = re.continuousHighWalk;
            obj["dragonsTigersAppears"] = re.dragonsTigersAppears;
            obj["bulkConsignmentOrder"] = re.bulkConsignmentOrder;
            this.tableData.push(obj);
          }
        })
    },
    methods:{
      deleteStock: function (row) {
        fetch("http://192.168.1.6:28003/recommend/deleteStock/"+row.code)
          .then(res => res.text()).then(res =>
          console.info(res)
        )
      }
    }
  }
</script>

