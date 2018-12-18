package edu.tsinghua.vui.vuitestbed.playback;

import org.json.JSONObject;

import java.util.Properties;

public class PlaybackManager {
    private PlaybackMessageQueue messageQueue;
    private Thread handlerThread;
    private PlaybackHandler handler;
    private MultiModalResponseHandler responseHandler;
   
    public PlaybackManager(MultiModalResponseHandler responseHandler, Properties properties, String cuid) {
        String appKey = properties.getProperty("app.appKey");
        String secretKey = properties.getProperty("app.appSecret");
        handler = new TTSPlaybackHandler(appKey, secretKey);
        messageQueue = new PlaybackMessageQueue();
        handlerThread = new Thread(new PlaybackThread(handler, messageQueue, cuid));
        this.responseHandler = responseHandler;
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
                responseHandler.onAddText(text);
            }
            if (json.has("wakeup")) {
                responseHandler.onWakeup();
            }
            if (json.has("graph")) {
                String graphURL = json.getString("url");
                responseHandler.onAddGraph(graphURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        messageQueue.stop();
    }
}