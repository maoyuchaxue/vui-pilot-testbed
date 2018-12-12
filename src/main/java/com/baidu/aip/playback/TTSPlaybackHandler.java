package com.baidu.aip.playback;

import com.baidu.aip.playback.PlaybackHandler;
import com.baidu.tts.TTSNetService;
import javax.sound.sampled.*;

public class TTSPlaybackHandler implements PlaybackHandler {

    private TTSNetService netService;
    final public static int sampleRate = 16000;
    final public static int sampleSizeInBits = 16;

    private SourceDataLine line;

    public TTSPlaybackHandler(String appKey, String secretKey) {
        netService = new TTSNetService(appKey, secretKey);
        
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("No output device detected");
        }
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(String text, String cuid) {
        byte[] bytes = null;
        try {
            bytes = netService.tts(text, cuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(text);
        if (bytes != null) {
            System.out.println(bytes.length);
            line.start();
            line.write(bytes, 0, bytes.length);
            line.drain();
            line.stop();
        }
    }
}