package edu.tsinghua.vui.vuitestbed.playback;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import edu.tsinghua.vui.vuitestbed.tts.TTSNetService;

public class TTSPlaybackHandler implements PlaybackHandler {

    private TTSNetService netService;
    final public static int sampleRate = 16000;
    final public static int sampleSizeInBits = 16;

    private AudioTrack audioTrack;
    private byte[] bytes;
    private int length;
    private int current;

    public TTSPlaybackHandler(String appKey, String secretKey) {
        netService = new TTSNetService(appKey, secretKey);

        int bufsize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Log.e("VUI", new Integer(bufsize).toString());

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, //sample rate
                AudioFormat.CHANNEL_OUT_MONO, //1 channel
                AudioFormat.ENCODING_PCM_16BIT, // 16-bit
                bufsize,
                AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    public void register(String text, String cuid) {
        synchronized (this) {
            bytes = null;
            try {
                bytes = netService.tts(text, cuid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            current = 0;
            length = bytes.length;
        }
    }

    public void play(int mills) {
        synchronized (this) {
            if (bytes != null) {
                int curLength = mills * 16;
                if (curLength + current > length) {
                    curLength = length - current;
                }
                audioTrack.write(bytes, current, curLength);
                audioTrack.flush();
                current += curLength;
            }
        }
    }

    public boolean ended() {
        return current == length;
    }
}