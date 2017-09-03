package com.yuneec.example.component.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by sushmas on 9/1/17.
 */

public class Sounds {

    public static void vibrate(Context context) {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
    }
}
