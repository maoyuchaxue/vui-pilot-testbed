package edu.tsinghua.vui.vuitestbed.playback;

import java.util.Properties;

public class PlaybackManager {
    private PlaybackMessageQueue messageQueue;
    private Thread handlerThread;
    private PlaybackHandler handler;
   
    public PlaybackManager(Properties properties, String cuid) {
        String appKey = properties.getProperty("app.appKey");
        String secretKey = properties.getProperty("app.appSecret");
        handler = new TTSPlaybackHandler(appKey, secretKey);
        messageQueue = new PlaybackMessageQueue();
        handlerThread = new Thread(new PlaybackThread(handler, messageQueue, cuid));
    }

    public void start() {
        handlerThread.start();
    }

    public void addMessage(String text) {
        messageQueue.addMessage(text);
    }

    public void stop() {
        messageQueue.stop();
    }
}