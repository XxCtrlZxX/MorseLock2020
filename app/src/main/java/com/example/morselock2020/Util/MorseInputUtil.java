package com.example.morselock2020.Util;

import com.example.morselock2020.databinding.ActivityLockedBinding;
import com.example.morselock2020.databinding.ActivityMainBinding;

public class MorseInputUtil {
    enum CurrentBinding { main, locked }

    private CurrentBinding currentBinding;

    private ActivityMainBinding mainBinding;
    private ActivityLockedBinding lockedBinding;

    private boolean isBtnDown, isSensitivity, isCanAppend;
    private float pressTime, sensitivity;

    //-------- Constructors ---------//
    public MorseInputUtil(ActivityMainBinding binding) {
        currentBinding = CurrentBinding.main;
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
    public MorseInputUtil(ActivityLockedBinding binding) {
        currentBinding = CurrentBinding.locked;
        lockedBinding = binding;
        isBtnDown = false;
        isSensitivity = false;
        pressTime = 0;
    }
    public MorseInputUtil(ActivityLockedBinding binding, float time) {
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
                            appendText("-");
                            isCanAppend = false;
                            isBtnDown = false;
                            pressTime = 0;
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
            appendText("·");
        }
    }

    //-------- appendText function ----------//
    private void appendText(final String code) {
        switch (currentBinding) {
            case main:
                mainBinding.morseInputTxt.append(code);
                break;
            case locked:
                lockedBinding.morseInputTxt.append(code);
                break;
        }
    }
}
