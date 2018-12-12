package com.baidu.tts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.baidu.util.ConnUtil;
import com.baidu.util.NetException;

public class TTSNetService {
    
    private String appKey;
    private String secretKey;
    
    private final int per = 0; // character
    private final int spd = 5; // speed (0-15)
    private final int pit = 5; // pitch (0-15)
    private final int vol = 5; // volume (0-9)
    private final int aue = 4; // audio format (4: pcm-16k)
    public final String url = "http://tsn.baidu.com/text2audio";

    public TTSNetService(String appKey, String secretKey) {
        this.appKey = appKey;
        this.secretKey = secretKey;
    }

    public byte[] tts(String text) throws IOException, NetException, TTSException {
        return tts(text, "default");
    }

    public byte[] tts(String text, String cuid) throws IOException, NetException, TTSException {
        TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.TTS_SCOPE);
        holder.refresh();
        String token = holder.getToken();

        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(text));
        params += "&per=" + per;
        params += "&spd=" + spd;
        params += "&pit=" + pit;
        params += "&vol=" + vol;
        params += "&cuid=" + cuid;
        params += "&tok=" + token;
        params += "&aue=" + aue;
        params += "&lan=zh&ctp=1";

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(2000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            return ConnUtil.getResponseBytes(conn);
        } else {
            System.err.println("ERROR: content-type= " + contentType);
            String res = ConnUtil.getResponseString(conn);
            System.err.println(res);
            return null;
        }
    }
}