# 飞秋
关于飞秋的一些知识和在Java中如何去接受和发送消息。飞秋兼容飞鸽传书协议，也有自己的传输协议。单点信息传输消息内容加密，消息发送之前先获取加密公钥，之后加密再发送，而群消息采用统一的加密方式，使用MAC地址加密，加密算法参看附录文章。

### 参看文章
* [Java监听和发送飞秋群消息](https://blog.csdn.net/pigerCC/article/details/51706027)
* [NODEJS和飞秋通讯，RSA、blowfish消息加密解密](https://blog.csdn.net/lady911/article/details/44830135)
* [破解飞秋群聊协议](http://www.cppblog.com/kevinlynx/archive/2014/11/18/139187.html)
* [IPMSG飞鸽传书——编译源代码的方法](https://www.cnblogs.com/hnrainll/archive/2011/07/05/2098308.html)
* [飞鸽飞秋协议总结](https://blog.csdn.net/zanfeng/article/details/17127035)
* [IP Messenger ](https://ipmsg.org/en/)
