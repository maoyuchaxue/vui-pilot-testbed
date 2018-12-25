package edu.tsinghua.vui.vuitestbed;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Properties;

import edu.tsinghua.vui.vuitestbed.playback.MultiModalConfig;
import edu.tsinghua.vui.vuitestbed.playback.MultiModalResponseHandler;
import edu.tsinghua.vui.vuitestbed.testctrl.SingleTest;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

public class SingleTestActivity extends AppCompatActivity {

    private SingleTest test = null;
    private Properties properties = null;
    private boolean hasVoiceFeedback;
    private boolean hasTextFeedback;
    private boolean hasGraphFeedback;
    private String cuid;
    private ImageView wakeupImage;
    private ImageView responseImageView;
    private TextView responseTextView;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        Intent intent = getIntent();

        hasVoiceFeedback = intent.getBooleanExtra("has_voice_feedback", true);
        hasTextFeedback = intent.getBooleanExtra("has_text_feedback", false);
        hasGraphFeedback = intent.getBooleanExtra("has_graph_feedback", false);
        // TODO: do something with multi modal configs!
        MultiModalConfig modalConfig = new MultiModalConfig(hasVoiceFeedback, hasTextFeedback, hasGraphFeedback);

        cuid = intent.getStringExtra("test_id");
        String serverURL = intent.getStringExtra("server_url");
        NetConfig.setNetUrl(serverURL);

        try {
            properties = getProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_single_test);
        Button endTestButton = (Button) findViewById(R.id.end_test_button);
        endTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.stop();
                SingleTestActivity.this.finish();
            }
        });
        wakeupImage = findViewById(R.id.wakeup_imageview);
        responseImageView = findViewById(R.id.response_imageview);
        responseTextView = findViewById(R.id.response_textview);
        clear();

        MultiModalResponseHandler responseHandler = new MultiModalResponseHandler(this, modalConfig);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                MultiModalResponseHandler resHandler = (MultiModalResponseHandler) msg.obj;
                resHandler.update();
            }
        };

        test = new SingleTest(responseHandler, handler, properties, cuid);

        Thread thread = new Thread(test);
        thread.start();
    }

    private Properties getProperties() throws Exception {
        Properties properties = new Properties();
        Resources resources = getResources();
        InputStream is = null;
        try {
            is = resources.openRawResource(R.raw.sdk);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    123);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    123);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setWakeup() {
        wakeupImage.setVisibility(View.VISIBLE);
    }

    public void setUnwakeup() {
        wakeupImage.setVisibility(View.INVISIBLE);
    }

    public void addText(String text) {
        responseTextView.setText(text);
        responseTextView.setVisibility(View.VISIBLE);
    }

    public void addGraph(String url) {
        new DownloadImageTask(responseImageView).execute(url);
    }

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    public void clear() {
        responseTextView.setText("");
        responseTextView.setVisibility(View.GONE);
        responseImageView.setVisibility(View.GONE);
        wakeupImage.setVisibility(View.INVISIBLE);
    }
}
