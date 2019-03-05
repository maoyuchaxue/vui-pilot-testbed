本项目是一个用于VUI Wizard-of-Oz相关实验的实验平台。主要可以进行的实验流程包括对话流程和唤醒实验两个部分，见以下说明。

## 对话流程

![对话方式图](https://github.com/maoyuchaxue/vui-pilot-testbed/blob/master/wiki/user-graph.png)

用户使用安卓手机进行实验，被告知其面对的是智能语音助手。语音输入被采集后经调用[百度实时语音识别](http://ai.baidu.com/sdk#itma)服务，转为文字并实时发送到Wizard所在的web端，Wizard可根据事先准备的脚本选择回答、并发送回用户，经由[百度TTS服务](http://ai.baidu.com/sdk#tts)转为语音播放。这样即完成了应答流程。

安卓端与服务器间使用HTTP传输数据，向服务器定期(每40ms)POST音频数据，并GET回复。由于本实验工具用于实验室环境，且基本不存在并发问题，在这里并未考虑带宽过低或延迟太高等问题。服务器将记录音频数据和全部对话文本，便于后续分析。

## 唤醒实验

![唤醒方式图](https://github.com/maoyuchaxue/vui-pilot-testbed/blob/master/wiki/wakeup-graph.png)

本部分用于唤醒方式与唤醒反馈相关的实验。不同的唤醒输入设备（包括触屏、实体按钮等，仅存在交互方式概念无具体实现时也可由Wizard负责），以及不同的唤醒输出设备（安卓端的振动、图标显示、语音应答，还有有待后续实现的其他设备）使用简单的socket协议与服务器连接，而实验人员可在后台配置输入输出设备之间互相触发的关联关系，由此可以进行唤醒方式的user study。

以上两部分功能目前是相互耦合的，安卓端既用于对话，也作为唤醒流程的设备（触屏输入、振动图标语音反馈）使用。Wizard的web端也同时用于两个实验。