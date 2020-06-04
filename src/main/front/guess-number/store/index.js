import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		islogin: false,
		username: "游客",
		openid: "-1",
		auth: false,
		urlpath:"106.55.36.70"
		// urlpath:"localhost"
	},
	mutations: {
		authok(state, auth) {
			state.auth = true;
		},
		loginname(state, username) {
			state.username = username;
		},
		loginopenid(state, openid) {
			state.openid = openid;
		},
		login(state) {
			state.islogin = true;
			wx.request({
				url: 'http://localhost:8800',
				data: {
					login: state.openid,
					
				},
				success: res => {
					
				}
			});
		}
	}
})

export default store
