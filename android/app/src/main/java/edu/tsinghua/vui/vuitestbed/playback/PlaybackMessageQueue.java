package edu.tsinghua.vui.vuitestbed.playback;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PlaybackMessageQueue {
    // currently nothing more than a Concurrent Queue

    private ConcurrentLinkedQueue<String> list;
    private boolean stopped;
    
    public PlaybackMessageQueue() {
        list = new ConcurrentLinkedQueue<String>();
        stopped = false;
    }

    public synchronized void addMessage(String text) {
        list.add(text);
    }

    public synchronized String getMessage() {
        return list.poll();
    }

    public synchronized void stop() {
        this.stopped = true;
    }

    public synchronized boolean stopped() {
        return this.stopped;
    }
}