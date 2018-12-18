package edu.tsinghua.vui.vuitestbed.testctrl;

import android.util.Base64;

import edu.tsinghua.vui.vuitestbed.playback.PlaybackManager;
import edu.tsinghua.vui.vuitestbed.util.ConnUtil;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetRawAudioDataHandler implements RawAudioDataHandler {

    private PlaybackManager playbackManager;
    private String localNetURL;
    private String cuid;

    public NetRawAudioDataHandler(PlaybackManager playbackManager, String cuid) {
        this.playbackManager = playbackManager;
        this.cuid = cuid;
        this.localNetURL = NetConfig.getNetUrl() +  "/raw";
    }

    public void onInputReceived(byte[] input) {
        String payload = "";
        try {
            payload = Base64.encodeToString(input, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String params = "cuid=" + cuid;
        params += "&payload=" + payload;

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(localNetURL).openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(2000);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.close();
            conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot access net server");
            return ;
        }
    }
}