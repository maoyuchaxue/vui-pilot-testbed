package edu.tsinghua.vui.vuitestbed.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {
    // currently nothing more than a Concurrent Queue

    private ConcurrentLinkedQueue<String> list;
    private boolean stopped;
    
    public MessageQueue() {
        list = new ConcurrentLinkedQueue<>();
        stopped = false;
    }

    public synchronized void addMessage(String text) {
        list.add(text);
    }

    public synchronized String getMessage() {
        return list.poll();
    }

    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    public void stop() {
        synchronized (this) {
            this.stopped = true;
        }
    }

    public synchronized boolean stopped() {
        synchronized (this) {
            return this.stopped;
        }
    }
}