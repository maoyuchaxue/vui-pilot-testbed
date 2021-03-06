package edu.tsinghua.vui.vuitestbed.playback;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.util.Properties;

import edu.tsinghua.vui.vuitestbed.util.MessageQueue;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

public class PlaybackManager {
    private MessageQueue messageQueue;
    private Thread handlerThread;
    private PlaybackHandler handler;
    private MultiModalResponseHandler responseHandler;
    private Handler messageToUIHandler;
   
    public PlaybackManager(MultiModalResponseHandler responseHandler, Handler messageToUIHandler, Properties properties, String cuid) {
        String appKey = properties.getProperty("app.appKey");
        String secretKey = properties.getProperty("app.appSecret");
        handler = new TTSPlaybackHandler(appKey, secretKey);
        messageQueue = new MessageQueue();
        handlerThread = new Thread(new PlaybackThread(handler, messageQueue, cuid));
        this.responseHandler = responseHandler;
        this.messageToUIHandler = messageToUIHandler;
    }

    public void start() {
        handlerThread.start();
    }

    public void addResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json.has("text")) {
                String text = json.getString("text");
                if (responseHandler.hasVoiceFeedback()) {
                    messageQueue.addMessage(text);
                }
                if (!json.has("hide")) {
                    responseHandler.onAddText(text);
                }
            }
            if (json.has("img")) {
                String graphURL = json.getString("img");
                if (graphURL.length() == 0) {
                    responseHandler.onAddGraph("");
                } else {
                    responseHandler.onAddGraph(NetConfig.getNetUrl() + graphURL);
                }
            }
            Message message = messageToUIHandler.obtainMessage(0, responseHandler);
            messageToUIHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        messageQueue.stop();
    }
}