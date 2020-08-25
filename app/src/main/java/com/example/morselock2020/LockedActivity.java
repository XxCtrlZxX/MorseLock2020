package com.example.morselock2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.morselock2020.Util.MorseInputUtil;
import com.example.morselock2020.Util.SaveSharedPreference;
import com.example.morselock2020.databinding.ActivityLockedBinding;

public class LockedActivity extends AppCompatActivity {

    private ActivityLockedBinding binding;

    private MorseInputUtil morseInputUtil;
    private float sensitivity;
    private String morseAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_locked);
        binding.setUnlock(false);

        binding.morseInputTxt.setClickable(false);
        binding.morseInputTxt.setFocusable(false);  // EditText 비활성화

        sensitivity = SaveSharedPreference.getPrefTime(this);
        morseAnswer = SaveSharedPreference.getPrefMorse(this);

        if (sensitivity > 0)
            morseInputUtil = new MorseInputUtil(binding, sensitivity);
        else
            morseInputUtil = new MorseInputUtil(binding);

        // InputButton
        binding.morseInputBtn.setOnTouchListener(onButtonTouchListener);


        // InputText
        binding.morseInputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(morseAnswer)) {
                    binding.setUnlock(true);
                    Toast.makeText(LockedActivity.this, "잠금해제 되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else if (s.toString().length() >= morseAnswer.length()) {
                    // 좌우로 흔드는 애니메이션
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    binding.morseInputTxt.startAnimation(shake);
                    binding.morseInputTxt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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