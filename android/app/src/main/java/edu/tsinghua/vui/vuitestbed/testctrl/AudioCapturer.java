package edu.tsinghua.vui.vuitestbed.testctrl;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioCapturer {

    final public static int packageDurationInMs = 40;
    final public static int sampleRate = 8000;
    final public static int sampleSizeInBits = 16;

    private AudioRecord audioRecord;

    public AudioCapturer() {
        int bufsize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, packageDurationInMs * sampleSizeInBits * 5);
    }

    public void start() {
        audioRecord.startRecording();
    }

    public byte[] read() {
        int len = packageDurationInMs * sampleSizeInBits;
        byte[] bytes = new byte[len];
        int bytesRead = audioRecord.read(bytes, 0, len);

        if (bytesRead == 0) {
            return null;
        } else {
            return bytes;
        }
    }

    public void stop() {
        audioRecord.stop();
    }
}