package com.basic.operations.math.mathoperations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Menu_Activity extends Activity {

    Button[] menu_view_level_buttons = null;
    Button[] menu_view_option_buttons = null;
    Button clear_score_make_it_zero = null;
    public static int[] play_pause_pictures;
    TextView level_heading_main;
    TextView results_score_menu;
    static Intent game_activity = null;
    int button_size = 50;
    int play_music_option = 1; // 1 = playing music & 0 = not playing or stop
    int option_timer_on_off = 0; // 0 = no timer & 1 = timer on
    Animation pushed_in_options_button = null;
    Animation scale_out_level_buttons = null;
    boolean buttons_enabled = true;
    String current_score = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent set_variables_from_game_activity = getIntent();
        play_music_option = set_variables_from_game_activity.getIntExtra("music_variable", 1);
        option_timer_on_off = set_variables_from_game_activity.getIntExtra("timer_variable", 0);

        menu_view_level_buttons = new Button[3];
        menu_view_option_buttons = new Button[2];

        pushed_in_options_button = AnimationUtils.loadAnimation(getBaseContext(), R.anim.button_alpha_pushed_in);
        scale_out_level_buttons = AnimationUtils.loadAnimation(getBaseContext(), R.anim.button_scale_pushed_out);

        level_heading_main = (TextView) findViewById(R.id.menu_heading);
        results_score_menu = (TextView) findViewById(R.id.menu_results);

        menu_view_level_buttons[0] = (Button) findViewById(R.id.easy_level);
        menu_view_level_buttons[1] = (Button) findViewById(R.id.medium_level);
        menu_view_level_buttons[2] = (Button) findViewById(R.id.hard_level);

        clear_score_make_it_zero = (Button) findViewById(R.id.trash_score);

        final Typeface custom_name_font = Typeface.createFromAsset(getAssets(), "menu_heading_font.ttf");
        final Typeface custom_heading_font = Typeface.createFromAsset(getAssets(), "data_heading_font.ttf");

        results_score_menu.setTypeface(custom_name_font);

        SharedPreferences sharePref = this.getSharedPreferences(getString(R.string.shared_preferences_files_name), Context.MODE_PRIVATE);
        current_score = sharePref.getString(getString(R.string.shared_preferences_key_name), null);

        if (current_score == null) {
            results_score_menu.setText("Score: 0 %");
        }
        else {
            results_score_menu.setText("Score: " + current_score + " %");
        }

        setTypeFace_font(level_heading_main, custom_name_font);
        setTypeFace_font(menu_view_level_buttons[0], custom_heading_font);
        setTypeFace_font(menu_view_level_buttons[1], custom_heading_font);
        setTypeFace_font(menu_view_level_buttons[2], custom_heading_font);

        menu_view_option_buttons[0] = (Button) findViewById(R.id.play_and_stop_button);
        menu_view_option_buttons[1] = (Button) findViewById(R.id.timer_button);

        menu_view_option_buttons[0].setWidth(button_size);
        menu_view_option_buttons[0].setHeight(button_size);
        menu_view_option_buttons[1].setWidth(button_size);
        menu_view_option_buttons[1].setHeight(button_size);

        play_pause_pictures = new int[2];

        play_pause_pictures[0] = getResources().getIdentifier("pause", "drawable", getPackageName());
        play_pause_pictures[1] = getResources().getIdentifier("play", "drawable", getPackageName());

        if (play_music_option == 1) {
            startService(new Intent(getBaseContext(), Main_Music.class));
            menu_view_option_buttons[0].setBackgroundResource(play_pause_pictures[1]);
        }
        else if (play_music_option == 0) {
            stopService(new Intent(getBaseContext(), Main_Music.class));
            menu_view_option_buttons[0].setBackgroundResource(play_pause_pictures[0]);
        }

        game_activity = new Intent(getBaseContext(), Game_Activity.class);

        menu_view_option_buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(pushed_in_options_button);

                if (play_music_option == 0) {
                    startService(new Intent(getBaseContext(), Main_Music.class));
                    menu_view_option_buttons[0].setBackgroundResource(play_pause_pictures[1]);
                    play_music_option = 1;
                    v.setPressed(false);
                    v.setSelected(false);
                }
                else if (play_music_option == 1) {
                    stopService(new Intent(getBaseContext(), Main_Music.class));
                    menu_view_option_buttons[0].setBackgroundResource(play_pause_pictures[0]);
                    play_music_option = 0;
                    v.setPressed(true);
                    v.setSelected(true);
                }
            }
        });

        menu_view_option_buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(pushed_in_options_button);

                if (option_timer_on_off == 0) {
                    option_timer_on_off = 1;
                    v.setPressed(false);
                    v.setSelected(false);
                } else if (option_timer_on_off == 1) {
                    option_timer_on_off = 0;
                    v.setPressed(true);
                    v.setSelected(true);
                }

                createOptions_toast(option_timer_on_off);
            }
        });

        clear_score_make_it_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(pushed_in_options_button);

                if (current_score != null) {

                    v.setPressed(true);
                    v.setSelected(true);

                    clear_score_and_update();

                }
                else if (current_score == null) {

                    v.setPressed(false);
                    v.setSelected(false);

                    score_already_has_been_cleared();
                }
            }
        });

        menu_view_level_buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_enabled) {
                    game_activity.putExtra("music_variable", play_music_option);
                    game_activity.putExtra("timer_variable", option_timer_on_off);
                    game_activity.putExtra("level_variable", 0);
                    v.startAnimation(scale_out_level_buttons);
                    buttons_enabled = false;
                }
            }
        });

        menu_view_level_buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_enabled) {
                    game_activity.putExtra("music_variable", play_music_option);
                    game_activity.putExtra("timer_variable", option_timer_on_off);
                    game_activity.putExtra("level_variable", 1);
                    v.startAnimation(scale_out_level_buttons);
                    buttons_enabled = false;
                }
            }
        });

        menu_view_level_buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_enabled) {
                    game_activity.putExtra("music_variable", play_music_option);
                    game_activity.putExtra("timer_variable", option_timer_on_off);
                    game_activity.putExtra("level_variable", 2);
                    v.startAnimation(scale_out_level_buttons);
                    buttons_enabled = false;
                }
            }
        });

        scale_out_level_buttons.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(500);
                    finish();
                    startActivity(game_activity);
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out);
                }
                catch (Exception e) {
                    finish();
                    startActivity(game_activity);
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void clear_score_and_update() {
        results_score_menu.setText("Score: 0 %");
        current_score = null;

        SharedPreferences sharePref = this.getSharedPreferences(getString(R.string.shared_preferences_files_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString(getString(R.string.shared_preferences_key_name), current_score);
        editor.putString(getString(R.string.shared_preferences_key_name_score), current_score);
        editor.putString(getString(R.string.shared_preferences_key_name_score_outof), current_score);
        editor.commit();

        Toast toast = Toast.makeText(this, R.string.message_score_clear, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 240);
        toast.show();
    }

    private void score_already_has_been_cleared() {
        Toast toast = Toast.makeText(this, R.string.message_already_score_clear, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 240);
        toast.show();
    }

    private TextView setTypeFace_font(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);

        return textView;
    }

    private void createOptions_toast(int value) {
        if (value == 0) {
            Toast toast = Toast.makeText(this, R.string.timer_stopped, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 240);
            toast.show();
        }
        else if (value == 1) {
            Toast toast = Toast.makeText(this, R.string.timer_started, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 240);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (play_music_option == 1) {
            startService(new Intent(getBaseContext(), Main_Music.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(getBaseContext(), Main_Music.class));
    }

}
