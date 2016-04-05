package com.example.observer.crazymath;

import java.util.Random;

/**
 * Created by observer on 4/4/16.
 */
public class Counter {
    int left;
    int right;
    int result;

    public String getResultString() {
        Random shot = new Random();
        Random choose = new Random();

        this.left = shot.nextInt(100);
        this.right = shot.nextInt(100);
        this.result = choose.nextInt(2) == 1 ? this.left + this.right : shot.nextInt(100);

        return (left + " + " + right + " = " + result);
    }
}
