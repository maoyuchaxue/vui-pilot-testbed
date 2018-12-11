package com.baidu.tts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TtsMain {

    public static void main(String[] args) throws IOException, DemoException {
        (new TtsMain()).run();
    }

    private final String appKey = ""; // TODO

    private final String secretKey = ""; // TODO

    private static String text_gbk = "hello world";
    
    private static String text = "hello world";
    
    private final int per = 0;
    private final int spd = 5;
    private final int pit = 5;
    private final int vol = 5;

    private final int aue = 6;

    public final String url = "http://tsn.baidu.com/text2audio";

    private String cuid = "1234567JAVA";

    private void run() throws IOException, DemoException {
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
        System.out.println(url + "?" + params);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            String format = getFormat(aue);
            File file = new File("result." + format);
            // System.out.println( file.getAbsolutePath());
            FileOutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            System.out.println("audio file write to " + file.getAbsolutePath());
        } else {
            System.err.println("ERROR: content-type= " + contentType);
            String res = ConnUtil.getResponseString(conn);
            System.err.println(res);
        }
    }

    private String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }
}
