package com.baidu.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class ConnUtil {

    public static String urlEncode(String str) {
        String result = null;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResponseString(HttpURLConnection conn) throws IOException, NetException {
        return new String(getResponseBytes(conn));
    }

    public static byte[] getResponseBytes(HttpURLConnection conn) throws IOException, NetException {
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.err.println("http return code error:" + responseCode);
            if (responseCode == 401) {
                System.err.println("appkey appSecret might be wrong");
            }
            throw new NetException("http response code is" + responseCode);
        }

        InputStream inputStream = conn.getInputStream();
        byte[] result = getInputStreamContent(inputStream);
        return result;
    }

    public static byte[] getInputStreamContent(InputStream is) throws IOException {
        byte[] b = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len = 0;
        while (true) {
            len = is.read(b);
            if (len == -1) {
                break;
            }
            byteArrayOutputStream.write(b, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
