package edu.tsinghua.vui.screen.displayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.tsinghua.vui.screen.displayer.net.IDevNetHandler;

public class WakeupButtonFragment extends Fragment {

    IDevNetHandler iDevNetHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wakeup_button_fragment, container, false);
        iDevNetHandler = new IDevNetHandler();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDevNetHandler.screenWakeup();
            }
        });

        Thread wakeupThread = new Thread(iDevNetHandler);
        wakeupThread.start();
        return view;
    }

}
