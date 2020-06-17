# vue+netty+redis微信猜数对战小游戏

### 技术点
 - netty服务器实现长连接实时推送消息
 - redis数据库

### 需求列表
- [x] 微信登录
- [ ] 每小时一次领取积分1000
- [ ] 分享给好友赢1000积分，每微信账号一次
- [x] 积分小于-10000不可进行游戏
- [ ] 排行榜rank100，实时更新
- [ ] 链接跳转github
- [ ] 游戏帮助信息
- [x] 玩家轮流回合，随机玩家开始第一回合
- [x] 玩家选择两个数之后可确定，若选中结果则游戏结束，未选中则依据情况提示（均偏大，均偏小，在两数之间）
- [x] 玩家点击开始游戏匹配对手
- [x] 游戏主体流程

### 效果演示
 - 登录游戏：点击登录即可授权登录。会请求获取用户信息，若用户不授权则跳转设置页。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194119361.png)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194410151.png)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194425769.png)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194444325.png)
 - 匹配对手：点击开始匹配匹配对手
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194545933.png)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194609221.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjM0MDM2Ng==,size_16,color_FFFFFF,t_70)
 - 轮流进行回合
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194643108.png)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194648845.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjM0MDM2Ng==,size_16,color_FFFFFF,t_70)
 - 游戏结束：猜对了数字就结束游戏
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020060419481419.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjM0MDM2Ng==,size_16,color_FFFFFF,t_70)
 - 中途退出则提示对手
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604194846933.png)