<template>
	<view class="content">
		<view class="bg-gradual-blue radius shadow margin">
			<view class="block cf">
				<view class="radius fl padding-sm flex flex-direction">
					<view class="cu-avatar xl round cuIcon-friend bg-cyan"></view>
					<view class="margin-top-sm text-center">{{username}}</view>
					<view class="margin-top-sm text-center">{{openid}}</view>
				</view>
				<view class="radius fr padding-sm align-center">
					<view class="margin-tb-sm text-center">
						<button class="cu-btn round bg-blue shadow" @click="toLogin()" v-if="!islogin&&!isLoginning">立即登录</button>
						<button class="cu-btn round bg-orange shadow" disabled="true" loading="true" v-if="isLoginning&&!islogin">正在登陆</button>
						<button class="cu-btn round bg-orange shadow" disabled="true" v-if="islogin&&!isLoginning">已登录</button>
					</view>
				</view>
			</view>
		</view>
		<view class=" bg-gray cu-list grid col-2 no-border card-menu margin-top shadow">
			<view class="cu-item">
				<text class="lg text-gray cuIcon-favor">1000</text>
				<text class="text-lg">当前积分</text>
			</view>
			<view class="cu-item">
				<text class="lg text-gray cuIcon-favorfill">100</text>
				<text class="text-lg">可领取积分</text>
			</view>
		</view>
		<view class=" bg-gray cu-list menu card-menu margin-top shadow">
			<view class="cu-item arrow">
				<view class="content block cf">
					<text class="cuIcon-friend text-grey"></text>
					<text class="text-grey">GITHUB</text>
				</view>
			</view>
			<view class="cu-item arrow">
				<view class="content block cf">
					<text class="cuIcon-friend text-grey"></text>
					<text class="text-grey">分享赚积分！</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'

	export default {
		computed: mapState(['islogin', 'openid', 'username', 'auth','urlpath']),
		data() {
			return {
				modalName: null,
				isLoginning: false
			}
		},
		methods: {
			...mapMutations(['authok', 'loginname', 'loginopenid', 'login']),
			toLogin() {
				let that = this
				this.isLoginning = true
				wx.getSetting({
					success: function(res) {
						if (res.authSetting['scope.userInfo']) {
							that.authok()
							wx.getUserInfo({
								success: function(userdata) {
									let name = userdata.userInfo.nickName;
									wx.login({
										success(res) {
											if (res.code) {
												var path = "http://" + that.urlpath + ":8800/guessNumber"
												wx.request({
													url: path,
													data: {
														code: res.code
													},
													success: res => {
														console.log("用户的openid:" + res.data);
														that.loginname(name);
														that.loginopenid(res.data);
														that.login();
														that.isLoginning = false
													}
												});
											}
										}
									})
								}
							});
						} else {
							wx.openSetting({
								success(res) {
									wx.getSetting({
										success(e) {
											console.log(e)
											if (res.authSetting['scope.userInfo']) {
												that.authok()
											}
										}
									})
								}
							})
						}
					}
				})
			},
			showModal(e) {
				this.modalName = e.currentTarget.dataset.target
			},
			hideModal(e) {
				this.modalName = null
			}
		}
	}
</script>

<style>

</style>
