package com.baidu.aip.demotest;

import javax.sound.sampled.*;

public class AudioCapturer {

    final public static int packageDurationInMs = 40;
    final public static int sampleRate = 8000;
    final public static int sampleSizeInBits = 16;

    private TargetDataLine line;

    public AudioCapturer() {
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
            format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Error: no valid DataLine found");
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException ex) {
            System.out.println("Error: cannot open capture device");
        }
    }

    public void start() {
        line.start();
    }

    public byte[] read() {
        int len = packageDurationInMs * sampleSizeInBits;
        byte[] bytes = new byte[len];
        int bytesRead = line.read(bytes, 0, len);

        if (bytesRead == 0) {
            return null;
        } else {
            return bytes;
        }
    }

    public void stop() {
        line.stop();
    }
}