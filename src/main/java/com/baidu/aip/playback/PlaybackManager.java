package com.baidu.aip.playback;

public class PlaybackManager {
    private PlaybackMessageQueue messageQueue;
    private Thread handlerThread;
    private PlaybackHandler handler;
    
    public PlaybackManager() {
        handler = new TTSPlaybackHandler();
        messageQueue = new PlaybackMessageQueue();
        handlerThread = new Thread(new PlaybackThread(handler, messageQueue));
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