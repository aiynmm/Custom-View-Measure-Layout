package com.sd.flowlayouttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.sd.flowlayouttest.util.ConverUtil;

public class Measure1Activity extends AppCompatActivity {
    private FrameLayout imgContainerLayout;
    private LinearLayout seekControlLayout;
    private AppCompatSeekBar wSeek;
    private AppCompatSeekBar hSeek;


    private float minWidth = ConverUtil.dp2px(100);
    private float minHeight = ConverUtil.dp2px(200);
    private float screenW;
    private float screenH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure1);
        getSupportActionBar().setTitle("正方形ImageView");

        screenW = getResources().getDisplayMetrics().widthPixels;
        screenH = getResources().getDisplayMetrics().heightPixels;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusH = 0;
        if (resourceId > 0) {
            statusH = getResources().getDimensionPixelSize(resourceId);
        }
        int actionBarH = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarH = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        final float contentH = screenH - actionBarH - statusH;

        imgContainerLayout = findViewById(R.id.img_container_layout);
        seekControlLayout = findViewById(R.id.bottom_control_layout);
        wSeek = findViewById(R.id.w_seek);
        hSeek = findViewById(R.id.h_seek);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViewGroup.LayoutParams layoutParams = imgContainerLayout.getLayoutParams();
                layoutParams.width = (int) (minWidth + (screenW
                        - minWidth) * wSeek.getProgress() / 100);
                layoutParams.height = (int) (minHeight + (contentH
                        - seekControlLayout.getHeight() - minHeight) * hSeek.getProgress() / 100);
                imgContainerLayout.setLayoutParams(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        wSeek.setOnSeekBarChangeListener(listener);
        hSeek.setOnSeekBarChangeListener(listener);
    }
}
