<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="股东人数变化"></el-descriptions>
          <holder-form @filterFun = "showFun"></holder-form>
          <el-table
            :data="tableData"
            height="750"
            border
            highlight-current-row = "true"
            size = "medium"
            width = "10px"
            style="width: 100%; color:#b328a0"
            :default-sort = "{prop: 'sort12', order: 'descending'}"
          >
            <el-table-column
            type="index"
            width="50"
            label="序号">
            </el-table-column>

            <el-table-column
              prop="name"
              label="股票名称">
            </el-table-column>
            <el-table-column
              prop="reportDate"
              label="报告时间"
              :filters="[{text: '最近1个月', value: '1'},{text: '最近2个月', value: '2'},{text: '最近3个月', value: '3'}]"
              :filter-method="filterDaysHandler"
              sortable>
            </el-table-column>

            <el-table-column
              prop="priceRatio"
              label="主力利润"
              sortable>
            </el-table-column>
            <el-table-column
              prop="makerVsSanHuRatio"
              label="庄家比散户"
              sortable>
            </el-table-column>
            <el-table-column
              prop="holderNum"
              label="股东人数">
            </el-table-column>
            <el-table-column
              prop="price"
              label="股价">
            </el-table-column>
            <el-table-column
              prop="noticePrice"
              label="通知价格">
            </el-table-column>



            <el-table-column
              prop="market"
              label="总流通市值">
            </el-table-column>
            <el-table-column
              prop="flowMarket"
              label="交易市值">
            </el-table-column>

            <el-table-column
              prop="tenFlowHolderRatio"
              label="前十大股东占比"
              sortable>
            </el-table-column>

            <el-table-column
              prop="makersRatio"
              label="庄家持股比例"
              sortable>

            </el-table-column>
            <el-table-column
              prop="sanHuRatio"
              label="散户持股比例"
              sortable>
            </el-table-column>
            <el-table-column
              prop="oderNum3"
              label="3年排名"
              sortable>
            </el-table-column>
            <el-table-column
              prop="oderNum5"
              label="5年排名"
              sortable>
            </el-table-column>

            <el-table-column
              prop="sort"
              label="sort合"
              sortable>
            </el-table-column>

            <el-table-column
              prop="sort12"
              label="sort12"
              sortable>
            </el-table-column>
            <el-table-column
              prop="sort13"
              label="sort13"
              sortable>
            </el-table-column>

            <el-table-column
              prop="sort14"
              label="sort14"
              sortable>
            </el-table-column>
            <el-table-column
              prop="sort15"
              label="sort15"
              sortable>
            </el-table-column>
            <el-table-column
              prop="sort16"
              label="sort16"
              sortable>
            </el-table-column>
            <el-table-column
              prop="holderName"
              label="私募">
            </el-table-column>

            <el-table-column
              prop="dongCaiIndustry"
              label="东财行业">
            </el-table-column>

            <el-table-column
              prop="industryBig"
              label="大行业">
            </el-table-column>

            <el-table-column
              prop="industryMiddle"
              label="中行业">
            </el-table-column>

            <el-table-column
              prop="listingDate"
              label="上市时间"
              >
            </el-table-column>
          </el-table>

        </div>
        <el-descriptions title="统计">
        <el-descriptions-item label="股票总数量：" v-bind:value = stockCount >{{stockCount}}</el-descriptions-item>
        </el-descriptions>
      </el-main>

    </el-container>
  </div>
</template>

<script>
  import {dateFormat} from "../../fun/timeUtil.js";
  import {keepTwoDecimal} from "../../fun/mathUtil.js";

  import HolderForm from "./holderForm";
  import HolderChart from "./holderBlockChart";

  export default {
    name: 'holderAll',
    components: {HolderChart, HolderForm},
    data() {
      return {
        tableData:[],
        stockCount:0
      }
    },
    created() {

    },
    methods:{
      showFun(data){
          console.info(data);
         fetch("http://127.0.0.1:28003/recommend/selectHolderAll",
           {
             method: 'post',
             body: JSON.stringify(data),
             headers: {
               'Content-Type': 'application/json'
             }}).then(res =>res.json()).then(res => {
          console.log(res);
            this.stockCount = res.length;
           this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["code"] = re.code;
            obj["name"] = re.name;
            obj["reportDate"] = dateFormat(re.reportDate);
            obj["holderNum"] = re.holderNum;
            obj["price"] = re.price;
            obj["oderNum3"] = re.oderNum3;
            obj["oderNum5"] = re.oderNum5;
            obj["noticePrice"] = re.noticePrice;
            obj["priceRatio"] = re.priceRatio;
            obj["sort"] = keepTwoDecimal(re.sort12 + re.sort13 + re.sort14 + re.sort15 + re.sort16);
            obj["sort12"] = re.sort12;
            obj["sort13"] = re.sort13;
            obj["sort14"] = re.sort14;
            obj["sort15"] = re.sort15;
            obj["sort16"] = re.sort16;
            obj["market"] = re.market;
            obj["flowMarket"] = re.flowMarket;
            obj["tenFlowHolderRatio"] = re.tenFlowHolderRatio;
            obj["makersRatio"] = re.makersRatio;
            obj["makerVsSanHuRatio"] = re.makerVsSanHuRatio;
            obj["makersCost"] = re.makersCost;
            obj["sanHuRatio"] = re.sanHuRatio;
            obj["holderName"] = re.holderName;
            obj["dongCaiIndustry"] = re.dongCaiIndustry;
            obj["industryBig"] = re.industryBig;
            obj["industryMiddle"] = re.industryMiddle;
            obj["listingDate"] = dateFormat(re.listingDate);
            this.tableData.push(obj);
          }
           this.$eventBus.$emit('holderStockChart',res);

         })
        },
      filterDaysHandler: function (value, row, column) {
        const property = column['property'];
        let reportDay = row[property];
        if((new Date().getTime() - Date.parse(reportDay)) < value*30*24*60*60*1000){
          return true;
        }
        return false;
      }
    }
  }
</script>

