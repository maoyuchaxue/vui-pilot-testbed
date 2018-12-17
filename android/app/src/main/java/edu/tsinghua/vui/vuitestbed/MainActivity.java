package edu.tsinghua.vui.vuitestbed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button startButton = (Button) findViewById(R.id.start_button);
        final RadioGroup modalsRadioGroup = (RadioGroup) findViewById(R.id.modals_radio_group);
        final EditText testIdTextView = (EditText) findViewById(R.id.test_id_textview);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SingleTestActivity.class);

                int modalsId = modalsRadioGroup.getCheckedRadioButtonId();
                boolean hasVoiceFeedback = true;
                boolean hasTextFeedback = false;
                boolean hasGraphFeedback = false;
                switch (modalsId) {
                    case R.id.voice_only_modal:
                        break;
                    case R.id.text_only_modal:
                        hasVoiceFeedback = false;
                        hasTextFeedback = true;
                        break;
                    case R.id.voice_and_text_modal:
                        hasTextFeedback = true;
                        break;
                    case R.id.voice_and_graph_modal:
                        hasGraphFeedback = true;
                        break;
                    case R.id.voice_graph_text_modal:
                        hasTextFeedback = true;
                        hasGraphFeedback = true;
                        break;
                    default:
                        break;
                }
                intent.putExtra("has_voice_feedback", hasVoiceFeedback);
                intent.putExtra("has_text_feedback", hasTextFeedback);
                intent.putExtra("has_graph_feedback", hasGraphFeedback);
                intent.putExtra("test_id", testIdTextView.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
    }
}
