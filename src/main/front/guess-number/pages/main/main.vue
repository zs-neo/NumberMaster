<template>
	<view>
		<cuCustom bgColor="bg-gradual-blue">
			<block slot="content">{{title}}</block>
		</cuCustom>
		<rank v-if="PageCur=='rank'"></rank>
		<game v-if="PageCur=='game'"></game>
		<user v-if="PageCur=='user'"></user>
		<view class="cu-bar tabbar bg-white shadow foot">
			<view class="action" @click="NavChange" data-cur="rank">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/rank' + [PageCur=='rank'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='rank'?'text-green':'text-gray'">排行</view>
			</view>
			<view class="action" @click="NavChange" data-cur="game">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/game' + [PageCur == 'game'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='game'?'text-green':'text-gray'">决斗</view>
			</view>
			<view class="action" @click="NavChange" data-cur="user">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/user' + [PageCur == 'user'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='user'?'text-green':'text-gray'">我的</view>
			</view>
		</view>

		<view class="cu-modal" :class="modalName=='Image'?'show':''">
			<view class="cu-dialog">
				<!-- <view class="bg-img" style="background-image: url('https://ossweb-img.qq.com/images/lol/web201310/skin/big91012.jpg');height:200px;"> -->
				<view class="bg-img" style="height:150px;">
					<view class="cu-bar text-white"></view>
					<view class="grid col-1">
						<view class="cuIcon-search text-bold">申请获取以下权限</view>
						<view class="cuIcon-scan margin-top">获得你的公开信息(昵称，头像等)</view>
					</view>
				</view>
				<view class="cu-bar bg-white">
					<view class="action margin-0 flex-sub  solid-left" @tap="hideModal">
						<view>
							<button class='cu-btn bg-green' open-type="getUserInfo" bindgetuserinfo="bindGetUserInfo">
								授权登录
							</button>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import cuCustom from '../../components/colorui/components/cu-custom.vue'
	import {
		mapState,
		mapMutations
	} from 'vuex';

	export default {
		computed: mapState(['islogin', 'openid', 'username', 'auth']),
		components: {
			cuCustom
		},
		data() {
			return {
				PageCur: "user",
				title: "我的",
				modalName: null
			}
		},
		mounted() {
			let that = this
			wx.getSetting({
				success: function(res) {
					console.log(res)
					if (res.authSetting['scope.userInfo']) {
						that.authok()
					} else {
						that.modalName = "Image"
					}
				}
			})
		},
		methods: {
			...mapMutations(['authok']),
			NavChange: function(e) {
				if (!this.auth || !this.islogin) {
					uni.showModal({
						title: '请先登录~'
					})
				} else {
					this.PageCur = e.currentTarget.dataset.cur
					if (this.PageCur == "rank") this.title = "排行"
					else if (this.PageCur == "game") this.title = "决斗"
					else this.title = "我的"
				}
			},
			bindGetUserInfo(){
				console.log("invoke")
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
