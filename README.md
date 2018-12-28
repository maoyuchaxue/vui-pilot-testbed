# Baidu Java ITMA SDK Demo, modified for personal use

## 需求

+ Java > 1.8
+ npm
+ node

## 使用方法

1. git clone 本项目;
1. 下载[ITMA SDK in Java](http://ai.baidu.com/sdk#itma)，将conf, gradle, libs三个文件夹复制进本项目根目录中;
1. 在conf/sdk.properties中修改appKey, appSecret以连接到百度服务;
1. 若使用服务器端, 进入server下执行npm install;

## 运行用户端

使用 sh gradlew run  --no-daemon 运行
也可以使用 sh run-test.sh 运行

## 运行服务器端

cd server
npm start

## WOz端

访问localhost:7575/agent

## todolist:

- [x] Android端移植
- [x] 添加用户端的图形界面
- [x] 添加由GUI管理的测试流程控制
    - [x] server端的测试组控制
    - [x] WOz端的流程展示
- [x] 添加GUI直接反馈（唤醒反馈）
    - [x] 唤醒的WOz
- [x] 添加log
    - [ ] 用户端
    - [x] server端
- [ ] agent端的rasa实现
- [x] WOz端的section设置
- [x] 交谈脚本的展示

## 平台trivial bug/feature list:

- [x] 退出+切换模态无法成功
- [x] WOz的界面显示易用性不佳，操作缓慢
- [x] 录音结果不正确
- [ ] 脚本需要更新
- [x] 可以添加错误反馈+记录
- [x] 停止语音
- [x] 保持唤醒