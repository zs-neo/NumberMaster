(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/main/main"],{"209b":function(t,e,n){"use strict";(function(t){n("3300"),n("921b");o(n("66fd"));var e=o(n("24c2"));function o(t){return t&&t.__esModule?t:{default:t}}t(e.default)}).call(this,n("543d")["createPage"])},"24c2":function(t,e,n){"use strict";n.r(e);var o=n("e2bc"),u=n("b7df");for(var a in u)"default"!==a&&function(t){n.d(e,t,function(){return u[t]})}(a);var r=n("2877"),i=Object(r["a"])(u["default"],o["a"],o["b"],!1,null,null,null);e["default"]=i.exports},b7df:function(t,e,n){"use strict";n.r(e);var o=n("d8d8"),u=n.n(o);for(var a in o)"default"!==a&&function(t){n.d(e,t,function(){return o[t]})}(a);e["default"]=u.a},d8d8:function(t,e,n){"use strict";(function(t){Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var o=n("2f62");function u(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{},o=Object.keys(n);"function"===typeof Object.getOwnPropertySymbols&&(o=o.concat(Object.getOwnPropertySymbols(n).filter(function(t){return Object.getOwnPropertyDescriptor(n,t).enumerable}))),o.forEach(function(e){a(t,e,n[e])})}return t}function a(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}var r=function(){return n.e("components/colorui/components/cu-custom").then(n.bind(null,"f637"))},i={computed:(0,o.mapState)(["islogin","openid","username","auth"]),components:{cuCustom:r},data:function(){return{PageCur:"user",title:"我的",modalName:null}},mounted:function(){var t=this;wx.getSetting({success:function(e){console.log(e),e.authSetting["scope.userInfo"]?t.authok():t.modalName="Image"}})},methods:u({},(0,o.mapMutations)(["authok"]),{NavChange:function(e){this.auth&&this.islogin?(this.PageCur=e.currentTarget.dataset.cur,"rank"==this.PageCur?this.title="排行":"game"==this.PageCur?this.title="决斗":this.title="我的"):t.showModal({title:"请先登录~"})},bindGetUserInfo:function(){console.log("invoke")},showModal:function(t){this.modalName=t.currentTarget.dataset.target},hideModal:function(t){this.modalName=null}})};e.default=i}).call(this,n("543d")["default"])},e2bc:function(t,e,n){"use strict";var o=function(){var t=this,e=t.$createElement;t._self._c},u=[];n.d(e,"a",function(){return o}),n.d(e,"b",function(){return u})}},[["209b","common/runtime","common/vendor"]]]);