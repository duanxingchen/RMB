<template>
  <div>
    <el-form ref="form" :model="form" :inline="true"  class="demo-form-inline" >
        <el-form-item label="股票名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="东财行业">
          <el-input v-model="form.dongCaiIndustry"></el-input>
        </el-form-item>
      <el-form-item label="概念板块">
        <el-input v-model="form.concept"></el-input>
      </el-form-item>
        <el-form-item label="主力利润">
          <el-input v-model="form.priceRatio"></el-input>
        </el-form-item>



      <el-form-item label="交易市值">
        <el-input v-model="form.flowMarket"></el-input>
      </el-form-item>

      <el-form-item label="私募">
        <el-switch v-model="form.delivery"></el-switch>
      </el-form-item>

      <el-form-item label="st">
        <el-switch v-model="form.st"></el-switch>
      </el-form-item>
      <el-form-item label="上市时间">
        <el-select v-model="form.listingDate" placeholder="请选上市时间">
          <el-option label="满一年" value="1"></el-option>
          <el-option label="满两年" value="2"></el-option>
          <el-option label="满三年" value="3"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报告时间">
        <el-select v-model="form.reportDate" placeholder="请选报告时间">
          <el-option label="最近10天" value="10"></el-option>
          <el-option label="最近一个月" value="30"></el-option>
          <el-option label="最近二个月" value="60"></el-option>
          <el-option label="最近三个月" value="90"></el-option>
          <el-option label="最近四个月" value="120"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="sort12">
        <el-select v-model="form.sort12" placeholder="请选择大小">
          <el-option label="大于0.8" value="0.8"></el-option>
          <el-option label="大于0.9" value="0.9"></el-option>
          <el-option label="大于1" value="1"></el-option>
          <el-option label="大于1.1" value="1.1"></el-option>
          <el-option label="大于1.2" value="1.2"></el-option>
          <el-option label="大于1.3" value="1.3"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="sort13">
        <el-select v-model="form.sort13" placeholder="请选择大小">
          <el-option label="大于0.8" value="0.8"></el-option>
          <el-option label="大于0.9" value="0.9"></el-option>
          <el-option label="大于1" value="1"></el-option>
          <el-option label="大于1.1" value="1.1"></el-option>
          <el-option label="大于1.2" value="1.2"></el-option>
          <el-option label="大于1.3" value="1.3"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="sort14">
        <el-select v-model="form.sort14" placeholder="请选择大小">
          <el-option label="大于0.8" value="0.8"></el-option>
          <el-option label="大于0.9" value="0.9"></el-option>
          <el-option label="大于1" value="1"></el-option>
          <el-option label="大于1.1" value="1.1"></el-option>
          <el-option label="大于1.2" value="1.2"></el-option>
          <el-option label="大于1.3" value="1.3"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="sort15">
        <el-select v-model="form.sort15" placeholder="请选择大小">
          <el-option label="大于0.8" value="0.8"></el-option>
          <el-option label="大于0.9" value="0.9"></el-option>
          <el-option label="大于1" value="1"></el-option>
          <el-option label="大于1.1" value="1.1"></el-option>
          <el-option label="大于1.2" value="1.2"></el-option>
          <el-option label="大于1.3" value="1.3"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="sort16">
        <el-select v-model="form.sort16" placeholder="请选择大小">
          <el-option label="大于0.8" value="0.8"></el-option>
          <el-option label="大于0.9" value="0.9"></el-option>
          <el-option label="大于1" value="1"></el-option>
          <el-option label="大于1.1" value="1.1"></el-option>
          <el-option label="大于1.2" value="1.2"></el-option>
          <el-option label="大于1.3" value="1.3"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="最近3年排名">
        <el-select v-model="form.oderNum3" placeholder="请选择大小">
          <el-option label="前1" value="1"></el-option>
          <el-option label="前3" value="3"></el-option>
          <el-option label="前5" value="5"></el-option>
          <el-option label="前10" value="10"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="最近5年排名">
        <el-select v-model="form.oderNum5" placeholder="请选择大小">
          <el-option label="前1" value="1"></el-option>
          <el-option label="前3" value="3"></el-option>
          <el-option label="前5" value="5"></el-option>
          <el-option label="前10" value="10"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="能动排名">
        <el-select v-model="form.energy" placeholder="请选择排名">
          <el-option label="排名前100" value="100"></el-option>
          <el-option label="排名前200" value="200"></el-option>
          <el-option label="排名前300" value="300"></el-option>
          <el-option label="排名前400" value="400"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="拉升1%能力排名">
        <el-select v-model="form.detailCount" placeholder="请选择排名">
          <el-option label="排名前100" value="100"></el-option>
          <el-option label="排名前200" value="200"></el-option>
          <el-option label="排名前300" value="300"></el-option>
          <el-option label="排名前400" value="400"></el-option>
          <el-option label="清空" value=""></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: 'holderForm',
    data() {
      return {
        form: {
          name: '',
          dongCaiIndustry: '',
          concept: '',
          priceRatio: '',
          flowMarket: '',
          delivery: false,
          st: false,
          sort12: '',
          sort13: '',
          sort14: '',
          sort15: '',
          sort16: '',
          oderNum3:'',
          oderNum5:'',
          listingDate: '',
          reportDate: '',
          detailCount: '',
          energy:''
        }
      }
    },
    methods:{
      onSubmit() {
        console.log('submit!');
        this.$emit('filterFun',this.form);
        this.$eventBus.$emit('recommend',this.form);
      }
    }
  }
</script>

