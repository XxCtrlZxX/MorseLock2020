package com.example.morselock2020;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.morselock2020.Util.MorseInputUtil;
import com.example.morselock2020.Util.SaveSharedPreference;
import com.example.morselock2020.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MorseInputUtil morseInputUtil;
    private float sensitivity;
    private String recoveryPW;
    private boolean isLocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        sensitivity = SaveSharedPreference.getPrefTime(this);
        recoveryPW = SaveSharedPreference.getPrefPW(this);
        binding.setSensitivity(sensitivity);
        binding.setIsValid(false);
        binding.setIsCanReset(false);
        binding.setMorse("");

        binding.morseInputTxt.setClickable(false);
        binding.morseInputTxt.setFocusable(false);  // EditText

        if (SaveSharedPreference.getPrefMorse(this).length() > 0) {
            isLocked = true;
            binding.unLockSwitch.setText("Locked");
            binding.unLockSwitch.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            isLocked = false;
            binding.unLockSwitch.setText("UnLocked");
            binding.unLockSwitch.setTextColor(getResources().getColor(R.color.materialDarkBlack));
        }

        if (getIntent().getBooleanExtra("isUnLock", false)) {
            binding.morseInputTxt.setText("---------");
            binding.morseInputTxt.setText("");
            binding.splashBtn.setVisibility(View.GONE);
        }

        // SplashButton
        binding.splashBtn.setOnClickListener(v -> {
            if (SaveSharedPreference.getPrefMorse(this).length() <= 0) {
                binding.morseInputTxt.setText("---------");
                binding.morseInputTxt.setText("");
                binding.splashBtn.setVisibility(View.GONE);
            } else {
                startActivity(new Intent(MainActivity.this, LockedActivity.class));
                finish();
            }
        });

        // InputButton
        CreateInstance();
        binding.morseInputBtn.setOnTouchListener(onButtonTouchListener);


        // UnLockSwitch
        binding.unLockSwitch.setChecked(isLocked);
        binding.unLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (binding.getIsValid())
                        GotoPasswordActivity();
                    else {
                        binding.unLockSwitch.setChecked(false);
                        Toast.makeText(MainActivity.this, "네 글지 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    EditText editText = new EditText(MainActivity.this);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("보조 비밀번호 입력");
                    builder.setView(editText);
                    builder.setPositiveButton("확인", (dialog, which) -> {
                        if (recoveryPW.equals(editText.getText().toString())) {

                            SaveSharedPreference.setMorseCode(MainActivity.this, "");
                            SaveSharedPreference.setPW(MainActivity.this, "");
                            Toast.makeText(MainActivity.this, "모스부호와 비밀번호가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            binding.unLockSwitch.setText("UnLocked");
                            binding.unLockSwitch.setTextColor(getResources().getColor(R.color.materialDarkBlack));

                        } else {
                            binding.unLockSwitch.setChecked(true);
                            Toast.makeText(MainActivity.this, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("취소", ((dialog, which) -> binding.unLockSwitch.setChecked(true)));
                    builder.show();
                }
            }
        });


        // ResetButton
        binding.resetBtn.setOnClickListener(v -> binding.morseInputTxt.setText(""));


        // SettingButton
        binding.settingBtn.setOnClickListener(v -> GotoPasswordActivity());


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
        binding.timeSeekBar.setProgress((int)(sensitivity * 100));
        binding.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivity = (float)progress / 100;
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

    private void GotoPasswordActivity() {
        String morse = binding.getMorse();
        Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
        intent.putExtra("morse", morse);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);  // 화면 전환 애니메이션 -> 오른쪽
        finish();
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