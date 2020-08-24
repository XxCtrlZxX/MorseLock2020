package com.example.morselock2020.Util;

import com.example.morselock2020.databinding.ActivityMainBinding;

public class MorseInputUtil {

    private ActivityMainBinding mainBinding;

    private boolean isBtnDown, isSensitivity, isCanAppend;
    private float pressTime, sensitivity;

    //-------- Constructors ---------//
    public MorseInputUtil(ActivityMainBinding binding) {
        mainBinding = binding;
        isBtnDown = false;
        isSensitivity = false;
        pressTime = 0;
    }
    public MorseInputUtil(ActivityMainBinding binding, float time) {
        this(binding);
        isSensitivity = true;
        sensitivity = time;
    }

    //------------ Entering Morse Code --------------//
    private void TimerStart() {
        TouchThread kThread = new TouchThread();
        kThread.start();
    }

    private class TouchThread extends Thread {
        @Override
        public void run() {
            super.run();
            while(isBtnDown)
            {
                try {
                    Thread.sleep(5);
                    pressTime += 0.005;

                    if (isSensitivity) {    // 감도 설정 됨
                        if (pressTime >= sensitivity) {
                            appendDash(mainBinding);
                            isCanAppend = false;
                            isBtnDown = false;
                            break;
                        }
                    } else {    // 감도 설정 안 됨

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //------- public function --------//
    public void OnMorseBtnDown() {
        TimerStart();
        isBtnDown = true;
        isCanAppend = true;
        pressTime = 0;
    }

    public void OnMorseBtnUp() {
        isBtnDown = false;
        if (isCanAppend) {
            appendDot(mainBinding);
        }
    }

    //-------- setText function ----------//
    private void appendDot(ActivityMainBinding binding) {
        binding.morseInputTxt.append("·");
    }

    private void appendDash(ActivityMainBinding binding) {
        binding.morseInputTxt.append("-");
    }
}
