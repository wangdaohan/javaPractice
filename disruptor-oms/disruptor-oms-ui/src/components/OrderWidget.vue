<template>
  <!-- 委托窗口   -->
  <div class="orderForm">
    <el-form label-width="80px">
      <el-form-item>
        <h3 :style="direction === 0 ? 'color: #F56C6C' : 'color: #67C23A'">
          {{direction === 0 ? '买入' : '卖出'}} 股票
        </h3>
      </el-form-item>
      <el-form-item label="证券代码">
        <code-input/>
      </el-form-item>
      <el-form-item label="证券名称">
        <el-input readonly v-model="name"/>
      </el-form-item>
      <el-form-item :label="'可' + (direction === 0 ? '买' : '卖') + '(股)'">
        <el-input readonly v-model="affordCount"></el-input>
      </el-form-item>
      <el-form-item label="价格">
        <el-input-number v-model="price" controls-position="right"
                         @change="handlePrice"
                         :step="0.01"
                         :min="0.01"/>
      </el-form-item>
      <el-form-item :label="(direction === 0 ? '买入' : '卖出') + '(股)'">
        <el-input-number v-model="volume" controls-position="right"
                         :min="0" :max="affordCount"/>
      </el-form-item>

      <el-form-item>
        <el-button :type="direction === 0 ? 'danger' : 'success'" style="float: right" @click="onOrder" >
          {{direction === 0 ? '买入' : '卖出'}}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import bus from 'vue3-eventbus'
import CodeInput from "./CodeInput";
import {sendOrder} from "@/api/orderApi";
import {constants} from "@/api/constants";
import * as moment from "moment";


export default {
  name: "OrderWidget",
  components: {CodeInput},
  data() {
    return {
      code: '',
      name: '',
      price: undefined,
      affordCount: undefined,
      volume: undefined,
    }
  },
  props: {
    direction: {type: Number, required: true},
  },
  methods: {
    handlePrice(){
      if (this.direction === constants.SELL) {
        let posiArr = this.$store.state.posiData;
        for(let i = 0, leng = posiArr.length; i < len; i++) {
          if(posiArr[i].code == this.code){
            this.affordCount = posiArr[i].count;
          }
        }
      } else {
        this.affordCount = parseInt(
            (this.$store.state.balance / constants.MULTI_FACTOR) / this.price
        );
      }
    },
    updateSelectCode(item) {
      this.code = item.code;
      this.name = item.name;
      this.price = undefined;
      this.volume = undefined;
      // if (this.direction === constants.SELL) {
      //     let posiArr = this.$store.state.posiData;
      //     for (let i = 0, len = posiArr.length; i < len; i++) {
      //         if (posiArr[i].code == item.code) {
      //             this.affordCount = posiArr[i].count;
      //             break;
      //         }
      //     }
      // }
    },
    handleOrderResCallback(code, msg, data){
      if (code == 0){
        this.$message.success("委托成功送往交易所");
      } else {
        this.$message.error(msg);
      }
    },
    onOrder(){
      sendOrder({
        uid: sessionStorage.getItem('uid'),
        type: constants.NEW_ORDER,
        timestamp: moment.now(),
        code: this.code,
        direction: this.direction,
        price: this.price * constants.MULTI_FACTOR,
        volume: this.volume,
        ordertype: constants.LIMIT
      }, this.handleOrderResCallback);
    }

  },
  created() {
    bus.on("codeinput-selected", this.updateSelectCode);
  },
  beforeDestroy() {
    bus.off("codeinput-selected", this.updateSelectCode);
  },
}
</script>

<style lang="scss">

.orderForm {
  input {
    text-align: center;
  }

  .el-input-number {
    width: 100%;
  }
}

</style>