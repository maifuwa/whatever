# 看看最后会变成什么 ：）

做好的功能：
1. 发送模板邮件(服务器本地渲染html后再发送给用户)
2. 获取用户信息，ip地址，位置信息，电脑信息，请求时间
3. 登录逻辑完成
4. 退出登录：使用黑名单模式踢用户下线，保存以登录的token到redis黑名单缓存中，到token过期再放出

## 网站提供的功能
1. 查看课表
2. 用户自由发帖