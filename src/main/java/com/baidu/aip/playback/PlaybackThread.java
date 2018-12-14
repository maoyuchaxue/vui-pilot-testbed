package com.baidu.aip.playback;

import com.baidu.aip.playback.PlaybackHandler;
import com.baidu.aip.playback.PlaybackMessageQueue;

public class PlaybackThread implements Runnable {

    private PlaybackHandler playbackHandler;
    private PlaybackMessageQueue messageQueue;
    private String cuid;

    public PlaybackThread(PlaybackHandler playbackHandler, PlaybackMessageQueue messageQueue, String cuid) {
        this.playbackHandler = playbackHandler;
        this.messageQueue = messageQueue;
        this.cuid = cuid;
    }

    public void run() {
        while (!messageQueue.stopped()) {
            String text = messageQueue.getMessage();
            if (text != null) {
                System.out.println("now playing: " + text);
                playbackHandler.play(text, cuid);
            }
        }
    }
}