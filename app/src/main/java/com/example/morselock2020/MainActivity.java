package com.example.morselock2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.morselock2020.Util.MorseInputUtil;
import com.example.morselock2020.Util.SaveSharedPreference;
import com.example.morselock2020.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MorseInputUtil morseInputUtil;
    private float sensitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        sensitivity = SaveSharedPreference.getPrefTime(MainActivity.this);
        binding.setSensitivity(sensitivity);
        binding.setIsValid(false);
        binding.setIsCanReset(false);
        binding.setMorse("");

        binding.morseInputTxt.setClickable(false);
        binding.morseInputTxt.setFocusable(false);  // EditText 비활성화
        binding.morseInputTxt.setText("");

        // InputButton
        CreateInstance();
        binding.morseInputBtn.setOnTouchListener(onButtonTouchListener);


        // ResetButton
        binding.resetBtn.setOnClickListener(v -> binding.morseInputTxt.setText(""));


        // SettingButton
        binding.settingBtn.setOnClickListener(v -> {
            String morse = binding.getMorse();
            Toast.makeText(this, "설정 화면으로 넘어갑니다. " + morse, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
            intent.putExtra("morse", morse);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);  // 화면 전환 애니메이션 -> 오른쪽
            finish();
        });


        // TextChangeListener
        binding.morseInputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.toString().length();
                if (len >= 4)
                    binding.setIsValid(true);
                if (len > 0)
                    binding.setIsCanReset(true);
                else {
                    binding.setIsValid(false);
                    binding.setIsCanReset(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // SeekBar
        binding.timeSeekBar.setProgress((int)(sensitivity * 500));
        binding.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivity = (float)progress / 500;
                if (sensitivity > 0)
                    binding.timeTxt.setText(String.format("%.2f", sensitivity) + "s");
                else
                    binding.timeTxt.setText("Disabled");
                CreateInstance();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SaveSharedPreference.setPressTime(MainActivity.this, sensitivity);
            }
        });
    }

    private void CreateInstance() {
        if (sensitivity > 0)
            morseInputUtil = new MorseInputUtil(binding, sensitivity);
        else
            morseInputUtil = new MorseInputUtil(binding);
    }

    private View.OnTouchListener onButtonTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    morseInputUtil.OnMorseBtnDown();
                    break;
                case MotionEvent.ACTION_UP:
                    morseInputUtil.OnMorseBtnUp();
                    break;
            }
            return false;
        }
    };
}