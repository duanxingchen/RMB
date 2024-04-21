<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="选股指标"></el-descriptions>
          <el-table
            :data="tableData"
            height="750"
            border
            style="width: 100%"
            :default-sort = "{prop: 'reportDate', order: 'descending'}"
          >

            <el-table-column
              prop="code"
              label="代码">
            </el-table-column>
            <el-table-column
              prop="name"
              label="名称">
            </el-table-column>

            <el-table-column
              prop="mainForce"
              label="主力">
            </el-table-column>
            <el-table-column
              prop="priceIncrease"
              label="主力利润">
            </el-table-column>
            <el-table-column
              prop="flowMarket"
              label="流动市值">
            </el-table-column>
            <el-table-column
              prop="industryAdvanced"
              label="行业先进">
            </el-table-column>
            <el-table-column
              prop="privateFund"
              label="私募基金">
            </el-table-column>

            <el-table-column
              prop="industryLeader"
              label="细分行业龙头">
            </el-table-column>
            <el-table-column
              prop="policyBlock"
              label="政策板块">
            </el-table-column>
            <el-table-column
              prop="lowTurnoverRate"
              label="换手率低">
            </el-table-column>
            <el-table-column
              prop="threeRed"
              label="底部三红兵">
            </el-table-column>
            <el-table-column
              prop="heatLeadStock"
              label="板块龙头股票热度">
            </el-table-column>
            <el-table-column
              prop="event"
              label="最近事件"
              sortable>
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
    name: 'stockSelection',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
        fetch("http://192.168.1.5:28003/recommend/stockSelection/selectAll")
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["code"] = re.code;
            obj["name"] = re.name;
            obj["mainForce"] = re.mainForce;
            obj["priceIncrease"] = re.priceIncrease;
            obj["flowMarket"] = re.flowMarket;
            obj["industryAdvanced"] = re.industryAdvanced;
            obj["privateFund"] = re.privateFund;
            obj["industryLeader"] = re.industryLeader;
            obj["policyBlock"] = re.policyBlock;
            obj["lowTurnoverRate"] = re.lowTurnoverRate;
            obj["threeRed"] = re.threeRed;
            obj["heatLeadStock"] = re.heatLeadStock;
            obj["event"] = re.event;

            this.tableData.push(obj);
          }
        })
    },
    methods:{
      deleteStock: function (row) {
        fetch("http://192.168.1.5:28003/recommend/deleteStock/"+row.code)
          .then(res => res.text()).then(res =>
            console.info(res)
        )
      }
    }
  }
</script>

