package com.example.morselock2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.morselock2020.Util.SaveSharedPreference;
import com.example.morselock2020.databinding.ActivityPasswordBinding;

public class PasswordActivity extends AppCompatActivity {

    private ActivityPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        binding.setPassword("");
        binding.setIsValid(false);

        Intent intent = getIntent();
        binding.setMorse(intent.getStringExtra("morse"));


        // BackButton
        binding.backBtn.setOnClickListener(v -> {
            startActivity(new Intent(PasswordActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);  // 화면 전환 애니메이션 -> 왼쪽
            finish();
        });


        // NextButton
        binding.nextBtn.setOnClickListener(v -> {
            Toast.makeText(this, binding.getMorse() + ", " + binding.getPassword() + " 설정되었습니다.", Toast.LENGTH_SHORT).show();

            SaveSharedPreference.setMorseCode(this, binding.getMorse());
            SaveSharedPreference.setPW(this, binding.getPassword());

            startActivity(new Intent(PasswordActivity.this, MainActivity.class));
            finish();
        });

        binding.pwInputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 4)
                    binding.setIsValid(true);
                else
                    binding.setIsValid(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}