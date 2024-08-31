<template>
  <div>
  <el-container style=" border: 1px solid #eee">
      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="板块异动明细">
          </el-descriptions>
          <el-table
            :data="tableData"
            height="650"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="today"
              label="今天">
              <el-table-column
                prop="concept0"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum0"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate0"
                label="涨占比">
              </el-table-column>

            </el-table-column>
            <el-table-column
              prop="before1"
              label="最近天1">
              <el-table-column
                prop="concept1"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum1"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate1"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before2"
              label="最近天2">
              <el-table-column
                prop="concept2"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum2"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate2"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before3"
              label="最近天3">
              <el-table-column
                prop="concept3"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum3"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate3"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before5"
              label="最近天5">
              <el-table-column
                prop="concept5"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum5"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate5"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before7"
              label="最近天7">
              <el-table-column
                prop="concept7"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum7"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate7"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before10"
              label="最近天10">
              <el-table-column
                prop="concept10"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum10"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate10"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before15"
              label="最近天15">
              <el-table-column
                prop="concept15"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum15"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate15"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before20"
              label="最近天20">
              <el-table-column
                prop="concept20"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum20"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate20"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before25"
              label="最近天25">
              <el-table-column
                prop="concept25"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum25"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate25"
                label="涨占比">
              </el-table-column>
            </el-table-column>
            <el-table-column
              prop="before30"
              label="最近天30">
              <el-table-column
                prop="concept30"
                label="名称">
              </el-table-column>
              <el-table-column
                prop="plateNum30"
                label="总/涨/板块涨">
              </el-table-column>
              <el-table-column
                prop="rate30"
                label="涨占比">
              </el-table-column>
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
    name: 'plateBlockDetail',
    data() {
      return {
        tableData:[]
      }
    },
    created: function () {
      this.$eventBus.$on( 'monitor' , (blackName)=> {
        fetch("http://127.0.0.1:28003/monitor/selectPlateBlockDetail/"+blackName)
          .then(res => res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          let outlen = res[res.length - 1].data.length;
          let inlen = res.length;

          for (let j = 0; j < outlen; j++) {
            let obj = {};
            for (let i = 0; i < inlen; i++) {
              let name = res[i].name;
              let data = res[i].data;
              if (data.length > 0) {
                let conceptName = "concept" + name;
                let plateNumName = "plateNum" + name;
                let rate = "rate" + name;
                let re = data[j];
                if (re !== undefined) {
                  obj[conceptName] = re.concept;
                  obj[plateNumName] = re.plateNum + ' / ' + re.upPlate + ' / ' + re.stockNum;
                  obj[rate] = re.rate;
                }
              }
            }
            this.tableData.push(obj);
          }
        })
      })
    },
    methods: {
    }
  }
</script>

