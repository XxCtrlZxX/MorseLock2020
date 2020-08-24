package com.example.morselock2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.morselock2020.Util.SaveSharedPreference;
import com.example.morselock2020.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private float pressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        pressTime = SaveSharedPreference.getPrefTime(MainActivity.this);
        binding.setSensitivity(pressTime);

        binding.timeSeekBar.setProgress((int)(pressTime * 500));
        // SeekBar
        binding.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pressTime = (float)progress / 500;
                if (pressTime > 0)
                    binding.timeTxt.setText(String.format("%.2f", pressTime) + "s");
                else
                    binding.timeTxt.setText("Disabled");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SaveSharedPreference.setPressTime(MainActivity.this, pressTime);
            }
        });
    }
}