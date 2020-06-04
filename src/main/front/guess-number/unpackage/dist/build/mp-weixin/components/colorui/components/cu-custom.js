(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["components/colorui/components/cu-custom"],{"1d5c":function(t,n,a){"use strict";a.r(n);var e=a("b46e"),u=a.n(e);for(var o in e)"default"!==o&&function(t){a.d(n,t,function(){return e[t]})}(o);n["default"]=u.a},"5b72":function(t,n,a){"use strict";var e=function(){var t=this,n=t.$createElement;t._self._c},u=[];a.d(n,"a",function(){return e}),a.d(n,"b",function(){return u})},b46e:function(t,n,a){"use strict";(function(t){Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var a={data:function(){return{StatusBar:this.StatusBar,CustomBar:this.CustomBar}},name:"cu-custom",computed:{style:function(){var t=this.StatusBar,n=this.CustomBar,a=this.bgImage,e="height:".concat(n,"px;padding-top:").concat(t,"px;");return this.bgImage&&(e="".concat(e,"background-image:url(").concat(a,");")),e}},props:{bgColor:{type:String,default:""},isBack:{type:[Boolean,String],default:!1},bgImage:{type:String,default:""}},methods:{BackPage:function(){t.navigateBack({delta:1})}}};n.default=a}).call(this,a("543d")["default"])},f637:function(t,n,a){"use strict";a.r(n);var e=a("5b72"),u=a("1d5c");for(var o in u)"default"!==o&&function(t){a.d(n,t,function(){return u[t]})}(o);var c=a("2877"),r=Object(c["a"])(u["default"],e["a"],e["b"],!1,null,null,null);n["default"]=r.exports}}]);
;(global["webpackJsonp"] = global["webpackJsonp"] || []).push([
    'components/colorui/components/cu-custom-create-component',
    {
        'components/colorui/components/cu-custom-create-component':(function(module, exports, __webpack_require__){
            __webpack_require__('543d')['createComponent'](__webpack_require__("f637"))
        })
    },
    [['components/colorui/components/cu-custom-create-component']]
]);                
