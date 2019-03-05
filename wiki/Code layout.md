本部分简单地介绍项目的结构。

+ ./android 安卓项目，用于直接和用户交互
+ ./conf ITMA SDK自带的配置目录
+ ./libs IDMA SDK带的库文件
+ ./logs 服务器端产生的log文件，记录了实验流程，由winston.js以及logback生成，其中pcm音频数据是被转为urlsafe base64格式存储的
+ ./server 服务器端，基于express.js提供的框架修改而来
+ ./src ITMA SDK的主体部分，由服务器端调用完成实时语音识别