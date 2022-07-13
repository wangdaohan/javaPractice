<template>
  <!--  持仓列表  -->
  <div>
    <el-row>
      <el-col :span="5">
        可用资金:{{balance}}
      </el-col>
    </el-row>

    <el-table
        :data="
                    tableData.slice
                    (
                        (query.currentPage - 1) * query.pageSize,
                        query.currentPage * query.pageSize
                    )
                "
        border
        @sort-change="changeTableSort"
    >
      <el-table-column prop="code" label="代码" align="center"
                       sortable :sort-orders="['ascending', 'descending']"
                       :formatter="codeFormatter"
      />
      <el-table-column prop="name" label="名称" align="center"/>
      <el-table-column prop="count" label="股票数量" align="center"/>
      <el-table-column prop="cost" label="总投入" align="center" :formatter="moneyFormatter"/>
      <el-table-column label="成本" align="center" :formatter="costFormatter"/>
    </el-table>

    <div class="pagination">
      <el-button round
                 type="primary" size="mini"
                 style="margin-top:2px;float: right"
                 icon="el-icon-refresh"
                 @click="queryRefresh">
        刷新
      </el-button>
      <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.currentPage"
          :page-size="query.pageSize"
          :total="dataTotalCount"
          @current-change="handlePageChange"/>
    </div>

  </div>
</template>

<script>
import {constants} from "@/api/constants";
import {codeFormat, moneyFormat} from "@/api/formatter";
import {queryPosi, queryBalance} from "@/api/orderApi";

export default {
  name: "PosiList",
  created() { //初始化函数，初始化完成后会执行的
    this.tableData = this.posiData;
    this.balance = this.balance;
  },
  computed: {
    posiData(){
      return this.$store.state.posiData;
    },
    balanceData(){
      return moneyFormat(this.$store.state.balance);
    }
  },
  watch:{
    //当Vuex store中的PosiData发生变化(store/index.js)， computed里面的计算方法posiData()就会发生变化， watch中的相关函数posiData就会执行
    posiData: function(val) {
      this.tableData = val;
      this.dataTotalCount = val.length;
    },
    balance: function (val) {
      this.balance = val;
    }
  },
  data() {
    return {
      tableData: [],
      dataTotalCount: 4,
      balance: 0,

      query: {
        currentPage: 1, // 当前页码
        pageSize: 2 // 每页的数据条数
      }
    };
  },
  methods: {
    queryRefresh(){
      queryPosi();
      queryBalance();
    },
    costFormatter(row, column) {
      return (row.cost / constants.MULTI_FACTOR / row.count).toFixed(2);
    },
    moneyFormatter(row, column) {
      return moneyFormat(row.cost);
    },
    codeFormatter(row, column) {
      return codeFormat(row.code);
    },
    // 分页导航
    handlePageChange(val) {
      this.query.currentPage = val;
    },

    //处理排序
    changeTableSort(column) {
      console.log('600886' - '000001');
      let fieldName = column.prop;
      if (column.order == "descending") {
        this.tableData = this.tableData.sort((a, b) => b[fieldName] - a[fieldName]);
      } else {
        this.tableData = this.tableData.sort((a, b) => a[fieldName] - b[fieldName]);
      }
    },

    cellStyle({row, column, rowIndex, columnIndex}) {
      return "padding:2px";
    },
  }
}
</script>

<style scoped>

</style>