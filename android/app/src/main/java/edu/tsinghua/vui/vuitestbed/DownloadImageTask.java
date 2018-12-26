package edu.tsinghua.vui.vuitestbed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap res = null;
        try {
            res = ImageCacheMap.get(url);
            if (res == null) {
                InputStream in = new java.net.URL(url).openStream();
                res = BitmapFactory.decodeStream(in);
                ImageCacheMap.put(url, res);
            }
        } catch (Exception e) {
            Log.e("VUI", e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        imageView.setVisibility(View.VISIBLE);
    }
}
