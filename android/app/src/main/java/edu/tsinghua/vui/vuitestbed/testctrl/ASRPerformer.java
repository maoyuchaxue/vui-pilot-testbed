package edu.tsinghua.vui.vuitestbed.testctrl;

import edu.tsinghua.vui.vuitestbed.playback.PlaybackFetcher;

public class ASRPerformer {

    AudioCapturer capturer;
    PlaybackFetcher playbackFetcher;
    RawAudioDataHandler rawAudioDataHandler;
    boolean stopped;

    public ASRPerformer(PlaybackFetcher playbackFetcher, RawAudioDataHandler rawAudioDataHandler) {
        this.playbackFetcher = playbackFetcher;
        this.rawAudioDataHandler = rawAudioDataHandler;
        capturer = new AudioCapturer();
    }

    public void startCapture() {
        capturer.start();
        try {
            while (!stopped) {
                byte[] bytes = capturer.read();

                if (bytes == null) {
                    break;
                }
                rawAudioDataHandler.onInputReceived(bytes);
                playbackFetcher.fetch(); // fetch response
            };
        } finally {
            capturer.stop();
        }
    }

    public synchronized void stop() {
        stopped = true;
    }
}