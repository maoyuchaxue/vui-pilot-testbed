package edu.tsinghua.vui.vuitestbed.testctrl;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioCapturer {

    final public static int packageDurationInMs = 40;
    final public static int sampleRate = 8000;
    final public static int sampleSizeInBits = 16;

    private AudioRecord audioRecord;

    public AudioCapturer() {
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, packageDurationInMs * sampleSizeInBits * 1000);
    }

    public void start() {
        audioRecord.startRecording();
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    public byte[] read() {
        int len = packageDurationInMs * sampleRate * sampleSizeInBits / (1000 * 16);
        short[] data = new short[len];
        int shortsRead = 0;
        while (shortsRead != len) {
            shortsRead += audioRecord.read(data, shortsRead, len - shortsRead);
        }

        return short2byte(data);
    }

    public void stop() {
        audioRecord.stop();
    }
}