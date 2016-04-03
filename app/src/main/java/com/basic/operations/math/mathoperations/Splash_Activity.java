package com.basic.operations.math.mathoperations;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash_Activity extends AppCompatActivity {

    static TextView first_name = null;
    static TextView last_name = null;
    static TextView math_signs = null;
    static Animation slider_left = null;
    static Animation slider_right = null;
    static Animation fader_in = null;
    static TextView progress_bar = null;
    static TextView progress_bar_percent = null;
    static TextView first_string_msg = null;
    static TextView second_string_msg = null;
    StringBuilder stringbuild = null;
    int percent = 0;
    Intent music_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        music_player = new Intent(getBaseContext(), Main_Music.class);

        first_name = (TextView) findViewById(R.id.firstname);
        last_name = (TextView) findViewById(R.id.secondname);
        math_signs = (TextView) findViewById(R.id.mathsigns);
        first_string_msg = (TextView) findViewById(R.id.first_string_msg);
        second_string_msg = (TextView) findViewById(R.id.second_string_msg);

        progress_bar = (TextView) findViewById(R.id.progress_bar_calculate);
        progress_bar_percent = (TextView) findViewById(R.id.progress_bar_calculate_percentage);

        stringbuild = new StringBuilder();

        slider_left = AnimationUtils.loadAnimation(this, R.anim.silder_left);
        slider_right = AnimationUtils.loadAnimation(this, R.anim.silder_right);
        fader_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        final Typeface custom_name_font = Typeface.createFromAsset(getAssets(), "Dumb_Name_Font.ttf");
        final Typeface custom_heading_font = Typeface.createFromAsset(getAssets(), "heading_font.ttf");

        progress_bar_percent.setTypeface(custom_heading_font);

        first_name.startAnimation(fader_in);
        last_name.startAnimation(fader_in);
        math_signs.startAnimation(fader_in);

        fader_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                setTypeFace_font(first_name, custom_name_font);
                setTypeFace_font(math_signs, custom_name_font);
                setTypeFace_font(last_name, custom_name_font);

                progress_bar_percent.setText("" + percent + "%");

                setProgress_bar();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                first_string_msg.startAnimation(slider_left);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slider_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setTypeFace_font(first_string_msg, custom_heading_font);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setTypeFace_font(second_string_msg, custom_heading_font);
                second_string_msg.startAnimation(slider_right);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //second_string_msg.startAnimation(slider_right);

        slider_right.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(2500);
                    final Intent menu_activity = new Intent(getBaseContext(), Menu_Activity.class);
                    menu_activity.putExtra("music_variable", 1);
                    menu_activity.putExtra("timer_variable", 0);
                    startActivity(menu_activity);
                    finish();
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out);
                }
                catch (Exception e) {
                    final Intent menu_activity = new Intent(getBaseContext(), Menu_Activity.class);
                    menu_activity.putExtra("music_variable", 1);
                    menu_activity.putExtra("timer_variable", 0);
                    startActivity(menu_activity);
                    finish();
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setProgress_bar() {
        final Thread sleepProgress = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    for (int x = 0; x < 20; x++) {
                        Thread.sleep(200);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setProgress_bar_percent();
                            }
                        });
                    }
                }
                catch (Exception e) {
                    //
                }

            }
        });

        sleepProgress.start();

    }

    private void setProgress_bar_percent() {

        stringbuild.append("TTTTt");
        percent += 5;

        progress_bar.setText(stringbuild);

        progress_bar_percent.setText("" + percent + "%");
    }

    private TextView setTypeFace_font(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
        textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));

        return textView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(music_player);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(music_player);
    }

}
