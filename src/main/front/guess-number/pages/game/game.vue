<template>
	<view>
		<view :class="[is_round==true?'':'bg-brown']">
			<view class="cu-list grid col-1 no-border card-menu margin-top shadow bg-gray" v-if="is_matched">
				<view class="cu-item">
					<text class="sm cuIcon-favor">{{otheruserscore}}</text>{{otherUsername}}
				</view>
				<view class="cu-item">
					<view class="grid col-2">
						<view class="padding-sm">
							<view class="padding radius text-center shadow bg-green">
								<view class="margin-top-sm text-Abc">
									{{othera}}
								</view>
							</view>
						</view>
						<view class="padding-sm">
							<view class="padding radius text-center shadow bg-orange">
								<view class="margin-top-sm text-Abc">
									{{otherb}}
								</view>
							</view>
						</view>
					</view>
				</view>
				<view>
					<view class='cu-tag round' :class="[othera>result&&otherb>result?'bg-brown':'']">偏大</view>
					<view class='cu-tag round' :class="[othera<result&&otherb<result?'bg-cyan':'']">偏小</view>
					<view class='cu-tag round' :class="[(othera<result&&otherb>result)||(othera>result&&otherb<result)?'bg-orange':'']">之间</view>
					<view class='cu-tag round' :class="[othera==result||otherb==result?'bg-green':'']">成功</view>
				</view>
			</view>
		</view>
		<view class="cu-list grid col-2 no-border card-menu margin-top shadow bg-gray" v-if="is_matched">
			<view class="cu-item">
				<view class='cu-tag round'>0 ~ 100</view>
			</view>
		</view>
		<view :class="[is_round==false?'':'bg-gradual-green']">
			<view class="cu-list grid col-1 no-border card-menu margin-top shadow bg-gray">
				<view>
					<view class='cu-tag round' :class="[mya>result&&myb>result?'bg-brown':'']">偏大</view>
					<view class='cu-tag round' :class="[mya<result&&myb<result?'bg-cyan':'']">偏小</view>
					<view class='cu-tag round' :class="[(mya<result&&myb>result)||(mya>result&&myb<result)?'bg-orange':'']">之间</view>
					<view class='cu-tag round' :class="[mya==result||myb==result?'bg-green':'']">成功</view>
				</view>
				<view class="cu-item">
					<view class="grid col-2">
						<view class="padding-sm">
							<view class="padding radius text-center shadow bg-green">
								<view class="margin-top-sm text-Abc">
									<input placeholder="数字1" placeholder-class="text-white text-sm" @input="inputA" v-if="is_round"></input>
									<view v-if="!is_round">{{mya}}</view>
								</view>
							</view>
						</view>
						<view class="padding-sm">
							<view class="padding radius text-center shadow bg-orange">
								<view class="margin-top-sm text-Abc">
									<input placeholder="数字2" placeholder-class="text-white text-sm" @input="inputB" v-if="is_round"></input>
									<view v-if="!is_round">{{myb}}</view>
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="padding flex flex-direction">
					<button class="cu-btn bg-gradual-blue lg shadow-blur" @click="clickRequest" v-if="is_init">开始游戏</button>
					<button class="cu-btn bg-gradual-blue lg shadow-blur" @click="confirmnumber" v-if="is_matched&&is_round">确定数字</button>
					<button class="cu-btn block bg-gradual-blue margin-tb-sm lg" disabled="true" loading v-if="is_matched&&!is_round">等待对手...</button>
					<button class="cu-btn block bg-gradual-blue margin-tb-sm lg" disabled="true" loading v-if="is_matching">匹配中...</button>
				</view>
			</view>
		</view>

		<view class="cu-modal" :class="modalName=='Image'?'show':''">
			<view class="cu-dialog">
				<view class="bg-img" style="background-image: url('https://ossweb-img.qq.com/images/lol/web201310/skin/big91012.jpg');height:200px;">
					<view class="cu-bar justify-end text-white">
						<view class="action" @tap="hideModal">
							<text class="cuIcon-close "></text>
						</view>
					</view>
				</view>
				<view class="cu-bar bg-white">
					<view class="action margin-0 flex-sub  solid-left" @tap="hideModal">匹配成功</view>
				</view>
			</view>
		</view>

		<view class="cu-modal" :class="modalName=='win'?'show':''">
			<view class="cu-dialog">
				<view class="bg-img" style="background-image: url('https://ossweb-img.qq.com/images/lol/web201310/skin/big91012.jpg');height:200px;">
					<view class="cu-bar justify-end text-white">
						<view class="action" @tap="hideModal">
							<text class="cuIcon-close "></text>
						</view>
					</view>
				</view>
				<view class="cu-bar bg-white">
					<view class="action margin-0 flex-sub  solid-left" @tap="hideModal">游戏胜利！结果是{{result}}</view>
				</view>
			</view>
		</view>

		<view class="cu-modal" :class="modalName=='lose'?'show':''">
			<view class="cu-dialog">
				<view class="bg-img" style="background-image: url('https://ossweb-img.qq.com/images/lol/web201310/skin/big91012.jpg');height:200px;">
					<view class="cu-bar justify-end text-white">
						<view class="action" @tap="hideModal">
							<text class="cuIcon-close "></text>
						</view>
					</view>
				</view>
				<view class="cu-bar bg-white">
					<view class="action margin-0 flex-sub  solid-left" @tap="hideModal">游戏失败！结果是{{result}}</view>
				</view>
			</view>
		</view>

		<view class="cu-modal" :class="modalName=='error'?'show':''">
			<view class="cu-dialog">
				<view class="bg-img" style="background-image: url('https://ossweb-img.qq.com/images/lol/web201310/skin/big91012.jpg');height:200px;">
					<view class="cu-bar justify-end text-white">
						<view class="action" @tap="hideModal">
							<text class="cuIcon-close "></text>
						</view>
					</view>
				</view>
				<view class="cu-bar bg-white">
					<view class="action margin-0 flex-sub  solid-left" @tap="hideModal">您的对手退出了游戏！</view>
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
		computed: mapState(['islogin', 'openid', 'username', 'auth', 'urlpath']),
		data() {
			return {
				is_open_socket: false,
				socketTask: null,
				is_init: true,
				is_matching: false,
				is_matched: false,
				is_round: false,
				modalName: null,
				resdata: null,
				result: 50,
				otherUsername: "旗鼓相当的对手",
				otherUserOpenId: -1,
				otheruserscore: 0,
				mya: -1,
				myb: 101,
				myina: -1,
				myinb: 101,
				othera: 101,
				otherb: -1
			}
		},
		onLoad() {
			// 进入这个页面的时候创建websocket连接
		},
		mounted() {
			console.log("mounted")
			this.connectSocket();
		},
		// 关闭websocket【必须在实例销毁之前关闭,否则会是underfined错误】
		beforeDestroy() {
			this.closeSocket();
		},
		methods: {
			connectSocket() {
				var path = "ws://" + this.urlpath + ":8800/guessNumber"
				this.socketTask = uni.connectSocket({
					url: path,
					success(data) {
						console.log("websocket连接成功");
					},
				});
				let that = this
				var loginstr = "login={\"openid\":\"" + this.openid + "\",\"nickname\":\"" + this.username + "\"}"
				this.socketTask.onOpen((res) => {
					console.log("WebSocket连接正常打开中...");
					this.is_open_socket = true;
					this.socketTask.send({
						data: loginstr,
						async success() {
							console.log("连接建立成功...")
						}
					});
					this.socketTask.onMessage((e) => {
						console.log("收到服务器消息");
						if (e.data == "loginok") {
							//说明是第一次请求login的响应
						} else if (e.data == "win" || e.data == "lose" || e.data == "error") {
							if (e.data == "error") {
								that.modalName = "error"
							}
							//游戏结束回到初始状态
							that.modalName = e.data
							that.is_init = true
							that.is_matching = false
							that.is_matched = false
							that.is_round = false
							that.mya = -1
							that.myb = 101
							that.othera = 101
							that.otherb = -1
							that.otherUsername = "旗鼓相当的对手"
							that.otherUserOpenId = -1
							that.otheruserscore = 0
							that.otherUserOpenId = -1
						} else {
							that.resdata = e.data
							let data = JSON.parse(that.resdata);
							console.log(data)
							if (data.code == 1) {
								console.log(data)
								that.modalName = "Image"
								that.is_matching = false;
								that.is_matched = true;
								that.result = data.res;
								that.otherUserOpenId = data.openid;
								that.otherUsername = data.nickname;
								that.otheruserscore = data.score;
								if (that.otherUserOpenId < that.openid) {
									// openid大的先手
									that.is_round = true
								}
							} else if (data.code == 2) {
								// 来自对手的回合信息
								that.is_round = true;
								that.othera = data.numbera;
								that.otherb = data.numberb;
							}
						}
					});
				})
				this.socketTask.onClose(() => {
					console.log("已经被关闭了")
				})
			},
			closeSocket() {
				this.socketTask.close({
					success(res) {
						this.is_open_socket = false;
						console.log("websocket关闭成功", res)
					},
					fail(err) {
						console.log("websocket关闭失败", err)
					}
				})
			},
			clickRequest() {
				let that = this
				if (this.is_open_socket) {
					this.is_init = false;
					this.is_matching = true;
					let matchStr = "match={\"openid\":\"" + this.openid + "\",\"nickname\":\"" + this.username + "\"}"
					this.socketTask.send({
						data: matchStr,
						async success(e) {

						},
					});
				}
			},
			confirmnumber() {
				this.mya = this.myina
				this.myb = this.myinb
				this.is_round = !this.is_round
				console.log(this.mya + " " + this.myb)
				if (this.mya == this.result || this.myb == this.result) {
					let resuldStr = "win={\"openid\":\"" + this.openid + "\"}";
					this.socketTask.send({
						data: resuldStr,
						async success(e) {

						},
					});
				} else {
					let numberStr = "number={\"openid\":\"" + this.otherUserOpenId + "\",\"numbera\":\"" + this.mya +
						"\",\"numberb\":\"" + this.myb + "\"}"
					this.socketTask.send({
						data: numberStr,
						async success(e) {

						},
					});
				}
			},
			inputA(e) {
				this.myina = e.target.value
			},
			inputB(e) {
				this.myinb = e.target.value
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
