<template>
  <div>
    <el-row style="height: 50px">
      <el-col :span="12">
        <el-input type="text" style="width: 200px" placeholder="板块名称" suffix-icon="el-icon-search" v-model="dongCaiInput"> {{dongCaiInput}}</el-input>
        <el-button class="ml-5" type="primary" @click="dongCaiSubmit">过滤</el-button>
      </el-col>
      <el-col :span="12">
        <el-input type="text" style="width: 200px" placeholder="概念名称" suffix-icon="el-icon-search" v-model="conceptInput"> {{conceptInput}}</el-input>
        <el-button class="ml-5" type="primary" @click="conceptSubmit">过滤</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    import {changeXtoDate} from "../../fun/echartsUtil";

    export default {
      name: "filterIndustry",
      props:{
        fatherRes:{}
      },
      data(){
          return{
            dongCaiInput:"",
            conceptInput:""
          }
      },
      inject:['dongCaiExtracted','conceptExtracted'],
      methods: {
        dongCaiSubmit: function () {
          console.info(this.fatherRes)
          let xAxi = changeXtoDate(this.fatherRes.xAxi);
          let dongCaiIndustry = this.fatherRes.dongCaiIndustry;

          if (this.dongCaiInput === '') {
            this.dongCaiExtracted(xAxi, dongCaiIndustry);
            return;
          }

          let filter = [];
          for (let i = 0; i < dongCaiIndustry.length; i++) {
            if (dongCaiIndustry[i].name.indexOf(this.dongCaiInput) !== -1) {
              filter.push(dongCaiIndustry[i]);
            }
          }

          this.dongCaiExtracted(xAxi, filter);
        },
        conceptSubmit: function () {
          let xAxi = changeXtoDate(this.fatherRes.xAxi);
          let concept = this.fatherRes.concept;

          if (this.conceptInput === '') {
            this.conceptExtracted(xAxi, concept);
            return;
          }

          let filter = [];
          for (let i = 0; i < concept.length; i++) {
            if (concept[i].name.indexOf(this.conceptInput) !== -1) {
              filter.push(concept[i]);
            }
          }

          this.conceptExtracted(xAxi, filter);
        }
      }
    }
</script>

<style scoped>

</style>
