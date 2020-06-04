import Vue from 'vue'
import App from './App'
import store from './store'

import game from './pages/game/game.vue'
Vue.component('game',game)

import rank from './pages/rank/rank.vue'
Vue.component('rank',rank)

import user from './pages/user/user.vue'
Vue.component('user',user)

Vue.config.productionTip = false

Vue.prototype.$store = store

App.mpType = 'app'

const app = new Vue({
    store,
    ...App
})
app.$mount()
