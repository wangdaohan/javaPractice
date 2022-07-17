import { createStore } from 'vuex'

export default createStore({
  state: {
    posiData:[],
    orderData:[],
    tradeData:[],
    balance:0,
  },
  getters: {
  },
  mutations: {
    updatePosi(state, posiInfo) {
      state.posiData = posiInfo;
    },
    updateOrder(state, orderData) {
      state.orderData = orderData;
    },
    updateTrade(state, tradeData){
      state.tradeData = tradeData;
    },
    updateBalance(state, balance) {
      console.log("set state.balance " + balance);
      state.balance = balance;
    },
  },
  //与mutations类似，但是异步操作
  // actions: {
  // },
  //与state类似，以modules形式保存对象
  // modules: {
  // }
})
