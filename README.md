# 介绍

收款监听


本项目主要解决：无企业资质的个人，想要收款通知的功能，无法申请到微信支付宝接口的困境


实现思路：安卓手机监听支付宝付款通知，将收款消息发于服务端处理


实现方法：NotificationListenerService接收通知，并将其发于服务器


本文只实现支付宝，微信监听可仿照支付宝添加

