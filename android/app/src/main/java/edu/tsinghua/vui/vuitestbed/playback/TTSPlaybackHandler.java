package edu.tsinghua.vui.vuitestbed.playback;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import edu.tsinghua.vui.vuitestbed.tts.TTSNetService;

public class TTSPlaybackHandler implements PlaybackHandler {

    private TTSNetService netService;
    final public static int sampleRate = 16000;
    final public static int sampleSizeInBits = 16;

    private AudioTrack audioTrack;

    public TTSPlaybackHandler(String appKey, String secretKey) {
        netService = new TTSNetService(appKey, secretKey);

        int bufsize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, //sample rate
                AudioFormat.CHANNEL_OUT_STEREO, //2 channel
                AudioFormat.ENCODING_PCM_16BIT, // 16-bit
                bufsize,
                AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    public void play(String text, String cuid) {
        byte[] bytes = null;
        try {
            bytes = netService.tts(text, cuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bytes != null) {
            System.out.println(bytes.length);
            audioTrack.write(bytes, 0, bytes.length);
            audioTrack.flush();
        }
    }
}