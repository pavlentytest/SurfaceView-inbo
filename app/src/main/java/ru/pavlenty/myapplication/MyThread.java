package ru.pavlenty.myapplication;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

public class MyThread extends Thread {

    // частота обновления экрана
    private final int REDRAW_TIME = 1000;

    // время анимации
    private final int ANIMATION_TIME = 5000;

    // флаг для управления потоком
    private boolean flag;

    // время начала анимации
    private long startTime;

    // предыдущее время перерисовки
    private long prevRedrawTime;

    private Paint paint;

    // переменная (объект) для интерполирования
    private ArgbEvaluator argbEvaluator;
    // указатель на holder для получения canvas
    private SurfaceHolder surfaceHolder;


    MyThread(SurfaceHolder holder) {
        surfaceHolder = holder;
        flag = false;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
    }

    public long getTime() {
        return System.nanoTime()/1000; // микросек.
    }

    @Override
    public void run() {
        //super.run();
        Canvas canvas;
        startTime = getTime();
        while (flag) {
            long currentTime = getTime();
            long elapsedTime = currentTime - prevRedrawTime;
            // логика рисования
            if (elapsedTime < REDRAW_TIME) {
                continue;
            }
         //   try {
            //    Thread.sleep(2000);
           // } catch (InterruptedException e) {
          //      e.printStackTrace();
          //  }
            // получаем результат из Canvas
            canvas = surfaceHolder.lockCanvas();

            draw(canvas);

            // очищаем Canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
            // обновляем время
            prevRedrawTime = getTime();
        }
    }

    public void draw(Canvas c) {
        long currTime = getTime() - startTime;

        int width = c.getWidth();
        int height = c.getHeight();

        c.drawColor(Color.BLACK);
        int centerX = width/2;
        int centerY = height/2;

        float radius = Math.min(width,height)/2;

        /*Random r = new Random();
        int color = Color.argb(r.nextInt(255),
                                r.nextInt(255),
                                r.nextInt(255),
                                r.nextInt(255));*/

        //  шаг для интерполирования
        float fraction = (float)(currTime%ANIMATION_TIME)/ANIMATION_TIME;
        Log.e("RRRRR",fraction+"");
        int color = (int)argbEvaluator.evaluate(fraction,Color.RED,Color.BLACK);
        paint.setColor(color);

        c.drawCircle(centerX,centerY,radius*fraction,paint);



    }


    public void setRunning(boolean b) {
        flag = b;
        prevRedrawTime = getTime();
    }
}
