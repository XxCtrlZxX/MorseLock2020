package com.example.morselock2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.morselock2020.Util.SaveSharedPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // 처음 앱을 켰을 때
            /*if (SaveSharedPreference.getPrefFirst(this)) {
                // 인트로 화면으로 전환
            }*/

            if (SaveSharedPreference.getPrefMorse(this).length() <= 0) {
                // 저장된 모스부호 없음
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                // TODO: 잠금화면으로 이동
            }

        }, 1500);
    }
}