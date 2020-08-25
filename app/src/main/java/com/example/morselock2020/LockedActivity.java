package com.example.morselock2020;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.morselock2020.Util.MorseInputUtil;
import com.example.morselock2020.Util.SaveSharedPreference;
import com.example.morselock2020.databinding.ActivityLockedBinding;

import java.util.concurrent.locks.Lock;

public class LockedActivity extends AppCompatActivity {

    private ActivityLockedBinding binding;

    private MorseInputUtil morseInputUtil;
    private float sensitivity;
    private String morseAnswer, recoveryPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_locked);
        binding.setInput("");
        binding.setUnlock(false);

        binding.morseInputTxt.setClickable(false);
        binding.morseInputTxt.setFocusable(false);  // EditText 비활성화

        sensitivity = SaveSharedPreference.getPrefTime(this);
        morseAnswer = SaveSharedPreference.getPrefMorse(this);
        recoveryPW = SaveSharedPreference.getPrefPW(this);

        if (sensitivity > 0)
            morseInputUtil = new MorseInputUtil(binding, sensitivity);
        else
            morseInputUtil = new MorseInputUtil(binding);

        // InputButton
        binding.morseInputBtn.setOnTouchListener(onButtonTouchListener);


        // ForgotText
        binding.forgotTxt.setOnClickListener(v -> {
            EditText editText = new EditText(LockedActivity.this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning").setMessage("보조 비밀번호를 입력하세요.\n주의: 등록해놓은 정보가 삭제됩니다.");
            builder.setView(editText);
            builder.setPositiveButton("확인", (dialog, which) -> {
                if (recoveryPW.equals(editText.getText().toString())) {

                    SaveSharedPreference.setMorseCode(this, "");
                    SaveSharedPreference.setPW(this, "");
                    Toast.makeText(this, "모든 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockedActivity.this, MainActivity.class);
                    intent.putExtra("isUnLock", true);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(this, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("취소", ((dialog, which) -> {}));
            builder.show();
        });


        // InputText
        binding.morseInputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (morseAnswer.equals(s.toString())) {
                    binding.setUnlock(true);
                    //Toast.makeText(LockedActivity.this, "잠금해제 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockedActivity.this, MainActivity.class);
                    intent.putExtra("isUnLock", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);  // 화면 전환 애니메이션 -> 오른쪽
                    finish();
                } else if (s.toString().length() >= morseAnswer.length()) {
                    // 좌우로 흔드는 애니메이션
                    binding.setUnlock(false);
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

    /*@Override
    protected void onPause() {
        super.onPause();
        finish();
    }*/

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