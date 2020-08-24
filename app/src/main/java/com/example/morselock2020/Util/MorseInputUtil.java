package com.example.morselock2020.Util;

import com.example.morselock2020.databinding.ActivityMainBinding;

public class MorseInputUtil {

    private ActivityMainBinding mainBinding;

    private boolean isBtnDown, isSensitivity, isCanAppend;
    private float pressTime, sensitivity;
    private String morse = "";

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

                    //TODO: 너무 길게 누르면 에러남
                    if (isSensitivity && pressTime >= sensitivity) {
                        //morse.append("-");
                        appendDash(mainBinding);
                        isCanAppend = false;
                        isBtnDown = false;
                        break;
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
        //mainBinding.morseInputTxt.setText("안녕");
    }

    public void OnMorseBtnUp() {
        isBtnDown = false;
        if (isCanAppend) {
            //morse.append("·");
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
