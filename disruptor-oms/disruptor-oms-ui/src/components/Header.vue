<template>
  <div class="header">
  <!--折叠按钮 -->
  <div class="collapse-btn"  @click="collapseChange">
    <i v-if="!collapsed">
      <el-icon>
        <!-- 动态生成图标-->
        <component :is="$icon['Expand']"></component>
      </el-icon>
<!--      <el-icon>-->
<!--        <Expand />-->
<!--      </el-icon>-->
    </i>
    <i v-else>
      <el-icon>
        <Fold/>
      </el-icon>
    </i>
  </div>
  <!-- LOGO -->
  <div class="logo">Disruptor OMS</div>
  <!--用户信息 + 下拉菜单-->
  <div class="header-right">
    <div class="header-user-con">
      <el-dropdown class="user-name" trigger="click">
        <span class="el-dropdown-link">
          {{username}}
          <i>
                  <el-icon>
                    <CaretBottom />
                  </el-icon>
          </i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>Logout</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
  </div>
</template>

<script>
// import { Search, Fold} from "@element-plus/icons-vue"
import bus from 'vue3-eventbus'
export default {
  name: "Header",
  setup() {
    const expandIconName = 'Expand'
    return {expandIconName}
  },
  // components: {
  //   Search,
  //   Fold
  // },
  data(){
    return {
      //username: 'guest', //转成计算属性 -> computed: {  username(){} }
      collapsed: true,
    }
  },
  computed: {
    username(){
      let acc = sessionStorage.getItem("uid");
      return acc ? acc : 'guest'
    }
  },
  methods:{
    // 用户名下拉菜单选择事件
    handleCommand(command) {
      if (command == 'loginout') {
        //TODO logout
        // logout();
      }
    },
    collapseChange(){
      this.collapsed = !this.collapsed;

      // 通过 Event Bus 进行跨组件之间（header组件 <-> sidebar组件） 通信，来折叠侧边栏  -> vue3停止对eventBus支持，导入vue3-eventbus
      // 折叠事件影响3个组件页面 - Header / sidebar / 主页面（中间）页面
      bus.emit("collapse", this.collapsed); //发送collapse消息 -》  sidebar  接收并处理
    }
  },
  // 第五步  右侧下拉菜单
  mounted() {
    if (document.body.clientWidth < 1500) {
      this.collapseChange();
    }
  }
}
</script>

<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}

.header .logo {
  float: left;
  width: 250px;
  line-height: 70px;
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}

.btn-bell .el-icon-bell {
  color: #fff;
}

.user-name {
  margin-left: 10px;
}

.user-avator {
  margin-left: 20px;
}

.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}
</style>