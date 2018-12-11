package com.baidu.aip.playback;

import com.baidu.aip.playback.PlaybackHandler;
import com.baidu.aip.playback.PlaybackMessageQueue;

public class PlaybackThread implements Runnable {

    private PlaybackHandler playbackHandler;
    private PlaybackMessageQueue messageQueue;

    public PlaybackThread(PlaybackHandler playbackHandler, PlaybackMessageQueue messageQueue) {
        this.playbackHandler = playbackHandler;
        this.messageQueue = messageQueue;
    }

    public void run() {
        while (!messageQueue.stopped()) {
            String text = messageQueue.getMessage();
            if (text != null) {
                playbackHandler.play(text);
            }
        }
    }
}