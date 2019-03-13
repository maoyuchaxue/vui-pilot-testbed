package edu.tsinghua.vui.screen.displayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import edu.tsinghua.vui.screen.displayer.net.NetConfig;
import edu.tsinghua.vui.screen.displayer.net.ODevNetHandler;

public class WakeupActivity extends AppCompatActivity {

    private ODevNetHandler oDevNetHandler;
    private HorizonFragment horizonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String serverIP = intent.getStringExtra("server_ip");
        NetConfig.setServerIP(serverIP);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wakeup);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ODevNetHandler.UNWAKEUP:
                        setUnwakeup();
                        break;
                    case ODevNetHandler.VISUAL_WAKEUP:
                        setWakeup();
                        break;
                }
            }
        };
        oDevNetHandler = new ODevNetHandler(handler);
        Thread wakeupThread = new Thread(oDevNetHandler);
        wakeupThread.start();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new WakeupButtonFragment()).commit();
    }

    private void setWakeup() {
        horizonFragment = new HorizonFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, horizonFragment).commit();
    }

    private void setUnwakeup() {
        if (horizonFragment != null) {
            horizonFragment.stop();
            horizonFragment = null;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new WakeupButtonFragment()).commit();
    }
}
