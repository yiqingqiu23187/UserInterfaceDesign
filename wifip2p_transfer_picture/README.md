# WifiP2P 多端传输

> 赵宇飞 https://gitee.com/yunlicangliu



通过WiFi P2P实现Android设备之间的图片互传以及同一张图片的跨屏显示。跨屏显示需要一个设备处在发送页面，另一个设备处在接收页面。

wifi 数据传输总是搞不定，于是在参考文档的基础上进行了一些修改，实现了跨屏显示的功能。



### 参考文档

Android 实现无网络传输文件： https://www.jianshu.com/p/f5d66e15fbdf

### 使用方法

1. 下载apk安装应用

2. 授予权限

   <img src="C:\Users\Lenovo\AppData\Local\Temp\WeChat Files\5dc3c789bbdd21d680325ea2e51ca56.jpg" alt="5dc3c789bbdd21d680325ea2e51ca56" style="zoom:33%;" />

   使用app时需要打开位置权限和读写sdk的权限，否则无法进行设备连接

3. 点击**接收端**按钮，进入接收页面

   <img src="C:\Users\Lenovo\AppData\Local\Temp\WeChat Files\8ea624dcf25e4c20e31f9e95315d7f4.jpg" alt="8ea624dcf25e4c20e31f9e95315d7f4" style="zoom:25%;" />

   接收端需要**创建通道**，由发送端连接，建立传输通道，通过socket进行数据传输

   

4. 点击**发送端**按钮，进入发送页面

   <img src="C:\Users\Lenovo\AppData\Local\Temp\WeChat Files\224312379be114e20b1f10c5ab75855.jpg" alt="224312379be114e20b1f10c5ab75855" style="zoom:25%;" />

   在发送页面点击**搜索**（需要打开wifi），在**设备列表**中点击接收端的设备，两个设备建立起连接之后就可以图片互传啦！

5. **点击选择文件**，相册中选择一张图片

   在发送端左右拖动图片就可以在两个设备上跨屏显示了。

