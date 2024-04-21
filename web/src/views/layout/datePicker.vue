<template>
  <div class="block">
    <el-row style="height: 50px">
      <el-col :span="12">
        <span class="demonstration">查询时间段</span>
        <el-date-picker
          v-model="DateValue"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-col>
      <el-col :span="12"> <el-button icon="el-icon-search"  type="success" @click="pickerSearch"></el-button></el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
      name: "datePicker",
      inject:['submit'],
      data() {
        return {
          DateValue: '',
          pickerOptions: {
            shortcuts: [{
              text: '最近一天',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
                picker.$emit('pick', [start, end]);
              }
            },{
              text: '最近二天',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 2);
                picker.$emit('pick', [start, end]);
              }
            },
              {
                text: '最近三天',
                onClick(picker) {
                  const end = new Date();
                  const start = new Date();
                  start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
                  picker.$emit('pick', [start, end]);
                }
              },
              {
              text: '最近一周',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近一个月',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
              }
            }, {
              text: '最近三个月',
              onClick(picker) {
                const end = new Date();
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
              }
            }]
          }
        };
      },
      methods:{
        pickerSearch(){
            console.info(this.DateValue)
            this.submit(this.DateValue);
          }
      }
    }
</script>

