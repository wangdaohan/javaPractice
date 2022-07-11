<template>
  <div class="wrapper">
    <!-- 引入头部导航栏-->
    <v-header/>
    <!-- 引入侧边导航栏-->
    <v-sidebar></v-sidebar>
<!--    <router-view></router-view>-->
    <!-- 主页面业务 -->
    <div class="content-box" :class="{'content-collapse':collapse}"> <!-- 当收起侧边栏时（collapse=true），主页面应向左扩充显示 ，占满空白部分，生成侧边栏折叠效果-->
      <div class="content">
        <transition name="move" mode="out-in"> <!-- 页面平滑过渡效果 -->
          <router-view></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
// <!-- 为什么要用vHeader标签，因为HTML本身有header标签 <v-header></v-header>-->
import vHeader from '../components/Header'
import vSidebar from '../components/Sidebar'
import bus from 'vue3-eventbus'
export default {
  name: 'Home',
  components:{
    vHeader,
    vSidebar
  },
  data() {
    return {
      collapse: false,
    }
  },
  created() {
    bus.on('collapse-content', msg => {
      this.collapse = msg;
    });
  },
  beforeDestroy() {
    //取消订阅collapse消息
    bus.off('collapse-content', msg => {
          this.collapse = msg;
        }
    );
  }
}
</script>
