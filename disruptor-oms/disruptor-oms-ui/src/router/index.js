import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: HomeView,
    children:[
      {
        path: '/dashboard',
        name:'Dashboard',
        component:()=>import('../views/Dashboard.vue')
      },
      {
        path: '/pwdreset',
        name: 'Pwdreset',
        component: ()=> import('../views/PwdSetting.vue'),
        meta: {requiredAuth: true} //路由拦截器
      },
      {
        //银证转账
        path: '/transfer',
        component: () => import( '../views/Transfer.vue'),
        meta: {requireAuth: false}
      },
      {
        //银证转账查询
        path: '/transferquery',
        component: () => import( '../views/TransferQuery.vue'),
        meta: {requireAuth: false}
      },
      {
        //银证转账
        path: '/orderquery',
        component: () => import( '../views/OrderQuery.vue'),
        meta: {requireAuth: false}
      },
      {
        //当日成交
        path: '/tradequery',
        component: () => import( '../views/TradeQuery.vue'),
        meta: {requireAuth: false}
      },
      {
        //历史委托
        path: '/hisorderquery',
        component: () => import( '../views/HisOrderQuery.vue'),
        meta: {requireAuth: false}
      },
      {
        //历史成交
        path: '/histradequery',
        component: () => import( '../views/HisTradeQuery.vue'),
        meta: {requireAuth: false}
      },
    ]
  },
  {
    path: '/',
    name: 'login',
    component: Login
  },

  {
    path: '/404',
    name: '404',
    component:()=>import('../views/404.vue')
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/404',
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

//路由拦截器
router.beforeEach((to, from, next)=> {
  if(to.meta.requiredAuth) { //如果前往页面需要验证
    if(Boolean(sessionStorage.getItem("uid"))) {
      next();
    } else {
      next({
        path: '/',
      })
    }
  } else {
    next();
  }
})

export default router
