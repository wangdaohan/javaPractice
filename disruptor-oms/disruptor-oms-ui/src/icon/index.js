import { reactive } from 'vue'
// 引入全部图标
// import * as Icons from "@element-plus/icons"

// 按需引入图标
import {
    Fold,
    Expand,
    PieChart,
    Shop,
    Sell,
    Search,
    CreditCard,
    Setting,
} from "@element-plus/icons-vue"

const dictIcon = reactive({
    'Fold': Fold,
    'Expand': Expand,
    'PieChart': PieChart,
    'Shop': Shop,
    'Sell': Sell,
    'Search': Search,
    'CreditCard': CreditCard,
    'Setting': Setting,
})

const installIcon = (app) => {
    // 便于模板获取
    app.config.globalProperties.$icon = dictIcon
    // 使用全部图标
    // app.config.globalProperties.$icon = Icons
}

export default installIcon