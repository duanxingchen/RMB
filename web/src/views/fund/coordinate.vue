<template>
  <div>
  <el-container style=" border: 1px solid #eee">

      <el-main>
        <div style="margin-bottom: 30px">
          <el-descriptions title="协同机构"></el-descriptions>
          <el-table
            :data="tableData"
            height="350"
            border
            style="width: 100%"
            :default-sort = "{prop: 'coordinateCount', order: 'descending'}"
          >
            <el-table-column
              prop="parentOrgName"
              label="机构组织名称"
              width="180"
              sortable>
            </el-table-column>
            <el-table-column
              prop="holderName"
              label="机构名称"
              :filters="[{text: '私募', value: '私募'},{text: '社保', value: '社保'}]"
              :filter-method="filterHandler">
            </el-table-column>
            <el-table-column
              prop="coordinateCount"
              label="协调次数">
            </el-table-column>

          </el-table>

        </div>

      </el-main>

    </el-container>
  </div>
</template>

<script>

  export default {
    name: 'coordinate',
    data() {
      return {
        tableData:[]
      }
    },
    created() {
      this.$eventBus.$on( 'fund' , (holderName)=>{
        fetch("http://192.168.1.5:28003/fund/coordinate/" + holderName)
          .then(res =>res.json()).then(res => {
          console.log(res);
          this.tableData = [];
          for (let i = 0; i < res.length; i++) {
            let re = res[i];
            let obj = {};
            obj["parentOrgName"] = re.parentOrgName;
            obj["holderName"] = re.holderName;
            obj["coordinateCount"] = re.coordinateCount;
            this.tableData.push(obj);
          }
        })
      } )
    },
    methods:{
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

