<template>
    <div class="sidebar">
        <el-menu
                class="sidebar-el-menu"
                :default-active="onRoutes"
                :collapse="collapse"
                background-color="#324157"
                text-color="#bfcbd9"
                active-text-color="#20a0ff"
                unique-opened
                router
        >
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-sub-menu :index="item.index" :key="item.index">
                        <template slot="title">
                              <el-icon>
                                <!-- 动态生成图标-->
                                <component :is="$icon[item.icon]"></component>
                              </el-icon>
                            <span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-sub-menu
                                    v-if="subItem.subs"
                                    :index="subItem.index"
                                    :key="subItem.index"
                            >
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item
                                        v-for="(threeItem,i) in subItem.subs"
                                        :key="i"
                                        :index="threeItem.index"
                                >{{ threeItem.title }}
                                </el-menu-item>
                            </el-sub-menu>
                            <el-menu-item
                                    v-else
                                    :index="subItem.index"
                                    :key="subItem.index"
                            >{{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-sub-menu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i>
                          <el-icon>
                            <!-- 动态生成图标-->
                            <component :is="$icon[item.icon]"></component>
                          </el-icon>
                        </i>
                        <span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import bus from 'vue3-eventbus'
    export default {
        data() {
            return {
                collapse: false,
                items: [
                    {
                        icon: 'PieChart',
                        index: 'dashboard',
                        title: '资金股份'
                    },
                    {
                        icon: 'Shop',
                        index: 'buy',
                        title: '买入'
                    },
                    {
                        icon: 'Sell',
                        index: 'sell',
                        title: '卖出'
                    },
                    {
                        icon: 'Search',
                        index: '3',
                        title: '查询',
                        subs: [
                            {
                                index: 'orderquery',
                                title: '当日委托'
                            },
                            {
                                index: 'tradequery',
                                title: '当日成交'
                            },
                            {
                                index: 'hisorderquery',
                                title: '历史委托'
                            },
                            {
                                index: 'histradequery',
                                title: '历史成交'
                            },
                        ]
                    },

                    {
                        icon: 'CreditCard',
                        index: '4',
                        title: '银证业务',
                        subs: [
                            {
                                index: 'transfer',
                                title: '银证转账'
                            },
                            {
                                index: 'transferquery',
                                title: '转账查询'
                            },
                        ]
                    },
                    {
                        icon: 'Setting',
                        index: 'pwdreset',
                        title: '修改密码'
                    },


                ]
            };
        },
        computed: {
            onRoutes() {
                return this.$route.path.replace('/', '');  //this.$route.path -> /dashboard -> replace('/', '') -> dashboard
            }
        },
        created() {
            // 通过 Event Bus 进行跨组件之间（header组件 <-> sidebar组件） 通信，来折叠侧边栏
            // 需要先安装vue-bus去使用$bus功能

          //订阅collapse消息
          //开发过程中 on和off肯定是成对出现的，即订阅和取消订阅是成对出现的。
            bus.on('collapse', msg => {
                this.collapse = msg;
                bus.emit('collapse-content', msg); //发送collapse消息 -》  主页面HomeView.vue 去接收并处理
            });
        },
      beforeDestroy() {
          //取消订阅collapse消息
          bus.off('collapse', msg => {
              this.collapse = msg;
              bus.emit('collapse-content', msg); //发送collapse消息 -》  主页面HomeView.vue 去接收并处理
            }
          );
      }
    };
</script>

<style lang="scss" >
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
        overflow-y: scroll;


        .el-menu-item {
            min-width: 150px;
        }

        li {
            text-align: left;
            .el-tooltip {
                width: auto ! important;
            }
        }

        /*下拉导航菜单多出1px*/
        .el-menu {
            border-right-width: 0;
        }

        .el-menu--collapse{
            width: auto ! important;
        }
    }

    .sidebar::-webkit-scrollbar {
        width: 0;
    }

    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 150px;
    }

    .sidebar > ul {
        height: 100%;
    }




</style>
