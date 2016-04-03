package com.basic.operations.math.mathoperations;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Game_Activity extends FragmentActivity implements Results_Interface {

    int timer_on_off; // 0 = off, 1 = on
    int music_on_off; // 1 = on, 0 = off
    int level_chooser; // 0 = easy, 1 = medium, 2 = hard
    Button music_player_on_off = null;
    Button pause_game_button = null;
    int button_size = 50;
    TextView game_heading_textview = null;
    TextView pause_text_view = null;
    Animation pushed_in_options_button = null;
    int[] play_pause_pictures = null;
    AlertDialog builder;
    Intent menu_activity = null;
    String[][] result_array_to_pass_on = null;
    int correct_answer_counter = 0;
    FragmentManager fragMng = null;
    FragmentTransaction fragTrans = null;
    boolean show_exit_message = true;
    int[] pause_game_pic = null;
    boolean pause_or_playing = true;
    Game_Fragment gf = null;
    boolean end_game_dont_show_message = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent menu_activity_variables = getIntent();
        music_on_off = menu_activity_variables.getIntExtra("music_variable", 0); // 1 = playing music & 0 = not playing or stop
        timer_on_off = menu_activity_variables.getIntExtra("timer_variable", 0); // 0 = no timer & 1 = timer on
        level_chooser = menu_activity_variables.getIntExtra("level_variable", 0); // 0 = easy, 1 = medium, 2 = hard

        music_player_on_off = (Button) findViewById(R.id.music_player_button);
        pause_game_button = (Button) findViewById(R.id.pause_game_button);

        pause_text_view = (TextView) findViewById(R.id.game_paused_text_view);

        music_player_on_off.setHeight(button_size);
        music_player_on_off.setWidth(button_size);
        pause_game_button.setHeight(button_size);
        pause_game_button.setWidth(button_size);

        pause_game_pic = new int[2];
        pause_game_pic[0] = getResources().getIdentifier("pause_game", "drawable", getPackageName());
        pause_game_pic[1] = getResources().getIdentifier("play_game", "drawable", getPackageName());

        if (timer_on_off == 1) {
            pause_game_button.setBackgroundResource(pause_game_pic[0]);
            pause_game_button.setVisibility(View.VISIBLE);
        }

        play_pause_pictures = new int[2];

        play_pause_pictures[0] = getResources().getIdentifier("pause", "drawable", getPackageName());
        play_pause_pictures[1] = getResources().getIdentifier("play", "drawable", getPackageName());

        if (music_on_off == 1) {
            startService(new Intent(getBaseContext(), Main_Music.class));
            music_player_on_off.setBackgroundResource(play_pause_pictures[1]);
        } else if (music_on_off == 0) {
            stopService(new Intent(getBaseContext(), Main_Music.class));
            music_player_on_off.setBackgroundResource(play_pause_pictures[0]);
        }

        game_heading_textview = (TextView) findViewById(R.id.game_heading);

        final Typeface custom_name_font = Typeface.createFromAsset(getAssets(), "menu_heading_font.ttf");

        game_heading_textview.setTypeface(custom_name_font);
        pause_text_view.setTypeface(custom_name_font);

        final int[] level_heading_text_array = {R.string.easy_level, R.string.medium_level, R.string.hard_level};
        game_heading_textview.setText(level_heading_text_array[level_chooser]);

        pushed_in_options_button = AnimationUtils.loadAnimation(getBaseContext(), R.anim.button_alpha_pushed_in);

        gf = new Game_Fragment();
        gf.delegates = this;
        Bundle send_data = new Bundle();
        send_data.putInt("timer_variable", timer_on_off);
        send_data.putInt("level_variable", level_chooser);
        gf.setArguments(send_data);

        fragMng = getFragmentManager();
        fragTrans = fragMng.beginTransaction();
        fragTrans.replace(R.id.game_fragment, gf, "GAME_FRAG_START");
        fragTrans.commit();

        music_player_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(pushed_in_options_button);

                if (music_on_off == 0) {
                    setMusic_player_pic();
                    v.setPressed(false);
                    v.setSelected(false);
                } else if (music_on_off == 1) {
                    setMusic_player_pic();
                    v.setPressed(true);
                    v.setSelected(true);
                }
            }
        });

        pause_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(pushed_in_options_button);

                setPause_or_play_game(pause_or_playing);
            }
        });
    }

    private void setPause_or_play_game(boolean pause_play) {

        //put in code to pause and activate game from here

        this.pause_or_playing = pause_play;

        if (pause_or_playing) {

            pause_or_playing = false;

            pause_game_button.setBackgroundResource(pause_game_pic[1]);

            gf.pause_Timer();

            pause_text_view.setVisibility(View.VISIBLE);
        } else if (!pause_or_playing) {

            pause_or_playing = true;

            gf.resume_Timer();

            pause_game_button.setBackgroundResource(pause_game_pic[0]);

            pause_text_view.setVisibility(View.GONE);
        }
    }

    private void setMusic_player_pic() {
        if (music_on_off == 0) {
            startService(new Intent(getBaseContext(), Main_Music.class));
            music_player_on_off.setBackgroundResource(play_pause_pictures[1]);
            music_on_off = 1;
        }
        else if (music_on_off == 1) {
            stopService(new Intent(getBaseContext(), Main_Music.class));
            music_player_on_off.setBackgroundResource(play_pause_pictures[0]);
            music_on_off = 0;
        }
    }

    private void createDialog_box_for_exit(boolean show_or_dont) {

        if (timer_on_off == 1 && show_or_dont) {
            setPause_or_play_game(true);
        }

        if (show_exit_message) {

            builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog_MinWidth).setTitle(R.string.exit_title)
                    .setMessage(R.string.exit_message)
                    .setPositiveButton(R.string.exit_okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            set_back_activity();
                        }
                    })
                    .setNegativeButton(R.string.exit_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Do nothing if user cancels dialog Box
                        }
                    }).show();

            final int alertTitle = getResources().getIdentifier("alertTitle", "id", "android");

            final Typeface custom_name_font = Typeface.createFromAsset(getAssets(), "menu_heading_font.ttf");
            final Typeface custom_heading_font = Typeface.createFromAsset(getAssets(), "data_heading_font.ttf");

            TextView dialog_title = (TextView) builder.findViewById(alertTitle);
            TextView dialog_message = (TextView) builder.findViewById(android.R.id.message);
            Button dialog_okay_button = (Button) builder.findViewById(android.R.id.button1);
            Button dialog_cancel_button = (Button) builder.findViewById(android.R.id.button2);

            setDialog_text(dialog_title, custom_name_font);
            setDialog_text(dialog_message, custom_heading_font);
            setDialog_text(dialog_okay_button, custom_heading_font);
            setDialog_text(dialog_cancel_button, custom_heading_font);
        }
        else if (!show_exit_message) {
            set_back_activity();
        }

    }

    private TextView setDialog_text(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));

        return textView;
    }

    private Button setDialog_text(Button button, Typeface typeface) {
        button.setTypeface(typeface);
        button.setTextColor(ContextCompat.getColor(this, R.color.white));

        return button;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (music_on_off == 1) {
            startService(new Intent(getBaseContext(), Main_Music.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(getBaseContext(), Main_Music.class));

        if (timer_on_off == 1) {
            setPause_or_play_game(true);
        }
    }

    @Override
    public void onBackPressed() {
        createDialog_box_for_exit(end_game_dont_show_message);
    }

    private void set_back_activity() {
        finish();
        menu_activity = new Intent(getBaseContext(), Menu_Activity.class);
        menu_activity.putExtra("music_variable", music_on_off);
        menu_activity.putExtra("timer_variable", timer_on_off);
        startActivity(menu_activity);
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out);
    }

    @Override
    public void processTotal_results(int total, String[][] total_array) {
        this.correct_answer_counter = total;
        this.result_array_to_pass_on = total_array;

        show_exit_message = false;
        end_game_dont_show_message = false;
        store_score_in_shared_preferences(total);
        pause_game_button.setVisibility(View.GONE);
        startResult_Fragment();
    }

    private void store_score_in_shared_preferences(int score_currently) {

        SharedPreferences sharePref = this.getSharedPreferences(getString(R.string.shared_preferences_files_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        String current_score = sharePref.getString(getString(R.string.shared_preferences_key_name), null);

        if (current_score == null) {

            double current_num = (double) score_currently / (double) 10;
            current_num *= 100;

            editor.putString(getString(R.string.shared_preferences_key_name), String.format("%.2f", current_num));
            editor.putString(getString(R.string.shared_preferences_key_name_score), "" + score_currently);
            editor.putString(getString(R.string.shared_preferences_key_name_score_outof), "" + 10);
            editor.commit();

        }
        else if (current_score != null) {

            double total_points = Double.parseDouble(sharePref.getString(getString(R.string.shared_preferences_key_name_score), null));
            double total_games_out_of = Double.parseDouble(sharePref.getString(getString(R.string.shared_preferences_key_name_score_outof), null));

            total_points += (double) score_currently;
            total_games_out_of += (double) 10;

            double current_num = total_points / total_games_out_of;
            current_num *= 100;

            editor.putString(getString(R.string.shared_preferences_key_name), String.format("%.2f", current_num));
            editor.putString(getString(R.string.shared_preferences_key_name_score), "" + total_points);
            editor.putString(getString(R.string.shared_preferences_key_name_score_outof), "" + total_games_out_of);
            editor.commit();
        }

    }

    private void startResult_Fragment() {
        final Results_Fragment rf = new Results_Fragment();
        rf.setEnd_result_total(correct_answer_counter, result_array_to_pass_on);

        final FrameLayout frame = (FrameLayout) findViewById(R.id.game_fragment);

        final Animation anmi_out = AnimationUtils.loadAnimation(this, R.anim.frag_fade_out);
        final Animation anmi_in = AnimationUtils.loadAnimation(this, R.anim.frag_fade_in);

        frame.startAnimation(anmi_out);

        anmi_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Fragment frag = getFragmentManager().findFragmentByTag("GAME_FRAG_START");
                getFragmentManager().beginTransaction().remove(frag).commit();
                frame.startAnimation(anmi_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anmi_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragTransation = fragmentManager.beginTransaction();
                fragTransation.add(R.id.game_fragment, rf);
                fragTransation.commit();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
