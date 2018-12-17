package edu.tsinghua.vui.vuitestbed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.util.Properties;

import edu.tsinghua.vui.vuitestbed.testctrl.SingleTest;
import edu.tsinghua.vui.vuitestbed.util.NetConfig;

public class SingleTestActivity extends AppCompatActivity {

    private SingleTest test = null;
    private Properties properties = null;
    private boolean hasVoiceFeedback;
    private boolean hasTextFeedback;
    private boolean hasGraphFeedback;
    private String cuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        hasVoiceFeedback = intent.getBooleanExtra("has_voice_feedback", true);
        hasTextFeedback = intent.getBooleanExtra("has_text_feedback", false);
        hasGraphFeedback = intent.getBooleanExtra("has_graph_feedback", false);
        // TODO: do something with multi modal configs!

        cuid = intent.getStringExtra("test_id");
        String serverURL = intent.getStringExtra("server_url");
        NetConfig.setNetUrl(serverURL);

        try {
            properties = getProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }

        test = new SingleTest(properties, cuid);

        Thread thread = new Thread(test);

        setContentView(R.layout.activity_single_test);
        Button endTestButton = (Button) findViewById(R.id.end_test_button);
        endTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.stop();
            }
        });

        thread.run();
    }

    private static Properties getProperties() throws Exception {
        String fullFilename = System.getProperty("user.dir") + "../conf/sdk.properties";
        Properties properties = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(fullFilename);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }
}
