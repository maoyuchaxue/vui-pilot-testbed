package edu.tsinghua.vui.vuitestbed.playback;

public class PlaybackThread implements Runnable {

    private final static int mills = 20;

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
                playbackHandler.register(text, cuid);
                while ((!playbackHandler.ended()) && (messageQueue.isEmpty())) {
                    playbackHandler.play(mills);
                }
            }
        }
    }
}