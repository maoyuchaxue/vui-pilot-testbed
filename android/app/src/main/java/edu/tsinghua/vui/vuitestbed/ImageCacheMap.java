package edu.tsinghua.vui.vuitestbed;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class ImageCacheMap {
    private static Map<String, Bitmap> cacheMap;

    public static void init() {
        cacheMap = new HashMap<>();
    }

    public static Bitmap get(String url) {
        return cacheMap.get(url);
    }

    public static void put(String url, Bitmap res) {
        cacheMap.put(url, res);
    }
}
