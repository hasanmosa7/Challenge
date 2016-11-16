package com.example.hasan.challenge;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Screen3 extends Activity {

    Bitmap b, tempb;
    int[] aa;
    ImageView v;
    int w, h;
    boolean firstFocus = true;
    long countPixels = 0;
    ProgressBar progressBar;
    int progressStatus = 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen3);

        progressBar = (ProgressBar) findViewById(R.id.progressBarPixel);
        v = (ImageView) findViewById(R.id.fullScreenImage);
        v.setImageAlpha(50);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    updateBitmapPixels(x, y);
                    v.setImageBitmap(b);
                    v.invalidate();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    int historySize = motionEvent.getHistorySize();
                    if (historySize != 0) {
                        int tempHistoricalX = (int) motionEvent.getHistoricalX(0);
                        int tempHistoricalY = (int) motionEvent.getHistoricalY(0);
                        int historicalX, historicalY;
                        for (int i = 1; i < historySize; i++) {
                            historicalX = (int) motionEvent.getHistoricalX(i);
                            historicalY = (int) motionEvent.getHistoricalY(i);
                            if (historicalX == tempHistoricalX && historicalY == tempHistoricalY)
                                break;
                            updateBitmapPixels(historicalX, historicalY);
                        }
                        v.setImageBitmap(b);
                        v.invalidate();
                    }
                }
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start_message();
            }
        }, 5000);
    }

     private void updateBitmapPixels(int x , int y){
        int rgb;
        int startX = Math.max(x - 50, 0), endX = Math.min(x + 50, w);
        int startY = Math.max(y - 50, 0), endY = Math.min(y + 50, h);
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                if(Color.alpha(b.getPixel(i,j))!=255) {
                    rgb = aa[j * w + i];
                    countPixels++;
                    b.setPixel(i, j, Color.argb(255, Color.red(rgb), Color.green(rgb), Color.blue(rgb)));
                    progressStatus = (int) (countPixels * 100) / (w * h);
                    progressBar.setProgress(progressStatus);
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if(firstFocus) {
            w = v.getWidth();
            h = v.getHeight();
            Bitmap bitmapscr = ((BitmapDrawable) v.getDrawable()).getBitmap();
            bitmapscr = Bitmap.createScaledBitmap(bitmapscr, w, h, false);
            b = bitmapscr.copy(Bitmap.Config.ARGB_8888, true);
            b.setHasAlpha(true);
            aa = new int[w * h];
            firstFocus = false;
            new Thread(new Runnable() {
                public void run() {
                    initializePixelsArray();
                }
            }).start();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initializePixelsArray() {
        b.getPixels(aa, 0, w, 0, 0, w, h);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int rgb= b.getPixel(i, j);
                b.setPixel(i, j, Color.argb(50,Color.red(rgb),Color.green(rgb),Color.blue(rgb)));
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                v.setImageAlpha(255);
                v.setImageBitmap(b);
                v.invalidate();
            }
        });

    }

    private void start_message(){
        final AlertDialog alertDialog = new AlertDialog.Builder(Screen3.this).create();
        alertDialog.setTitle("Clear Image");
        alertDialog.setMessage("Touch the screen with your finger to clear the screen");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_screen3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
