package com.basic.operations.math.mathoperations.Fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.operations.math.mathoperations.R;
import com.basic.operations.math.mathoperations.Service.Results_Interface;

public class Game_Fragment extends Fragment {

    View view = null;
    Animation pushed_in_options_button = null;
    int timer_on_off; // 0ff = 0 on == 1
    int selected_level; // 0 = easy, 1 = medium, 2 == hard
    TextView timer = null;
    TextView question = null;
    TextView result = null;
    Button[] answers = new Button[4];
    ImageView answer_picture = null;
    int[] answer_pictures_resource = new int[2];
    int[] questions_first = new int[10];
    int[] questions_second = new int[10];
    int[] operator_value = new int[10]; // 1 = +, 2 = -, 3 = x, 4 = /
    int[] correct_answer_button_put_answer = new int[10]; //this is used to select which button to play correct answer
    boolean buttons_can_be_pressed = false;
    int counter_to_ten = 0;
    int[] wrong_number_array;
    int resource_button_which_is_clicked = 0;
    int button_number_id = 0;
    int correct_answer_counter = 0;
    CountDownTimer cdt;
    String[][] result_array_to_pass_on = null;
    Button get_selected_answer_string = null;
    long timer_variable = 0;
    long default_timer = 30000;

    public Results_Interface delegates = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getArguments = this.getArguments();
        timer_on_off = getArguments.getInt("timer_variable", 0);
        selected_level = getArguments.getInt("level_variable", 0);

        answer_pictures_resource[0] = getResources().getIdentifier("right", "drawable", getActivity().getPackageName());
        answer_pictures_resource[1] = getResources().getIdentifier("wrong", "drawable", getActivity().getPackageName());

        startGame_questions(selected_level);

        result_array_to_pass_on = new String[10][3]; // this gets passed and the correct answer counter
        //[0][0] < question
        //[0][1] < correct answer
        //[0][2] < your selected answer

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game, container, false);

        final Typeface custom_name_font = Typeface.createFromAsset(getActivity().getAssets(), "menu_heading_font.ttf");
        final Typeface custom_heading_font = Typeface.createFromAsset(getActivity().getAssets(), "data_heading_font.ttf");

        timer = (TextView) view.findViewById(R.id.timer_text_view);
        timer.setTypeface(custom_heading_font);
        question = (TextView) view.findViewById(R.id.math_question);
        question.setTypeface(custom_name_font);
        result = (TextView) view.findViewById(R.id.small_result);
        result.setTypeface(custom_heading_font);
        result.setText("Score: " + correct_answer_counter + " / 10");
        answer_picture = (ImageView) view.findViewById(R.id.answer_picture);

        answers[0] = (Button) view.findViewById(R.id.math_answer_one);
        answers[0].setTypeface(custom_name_font);
        answers[1] = (Button) view.findViewById(R.id.math_answer_two);
        answers[1].setTypeface(custom_name_font);
        answers[2] = (Button) view.findViewById(R.id.math_answer_three);
        answers[2].setTypeface(custom_name_font);
        answers[3] = (Button) view.findViewById(R.id.math_answer_four);
        answers[3].setTypeface(custom_name_font);

        pushed_in_options_button = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_pushed_in);

        if (timer_on_off == 0) {
            startActual_game_play(counter_to_ten);
        }
        else if (timer_on_off == 1) {
            setTimer(default_timer);
        }

        answers[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_can_be_pressed) {
                    buttons_can_be_pressed = false;
                    get_selected_answer_string = (Button) v;
                    result_array_to_pass_on[counter_to_ten - 1][2] = "" + get_selected_answer_string.getText().toString();
                    button_number_id = v.getId();
                    v.startAnimation(pushed_in_options_button);
                }
            }
        });

        answers[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_can_be_pressed) {
                    buttons_can_be_pressed = false;
                    get_selected_answer_string = (Button) v;
                    result_array_to_pass_on[counter_to_ten - 1][2] = "" + get_selected_answer_string.getText().toString();
                    button_number_id = v.getId();
                    v.startAnimation(pushed_in_options_button);
                }
            }
        });

        answers[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_can_be_pressed) {
                    buttons_can_be_pressed = false;
                    get_selected_answer_string = (Button) v;
                    result_array_to_pass_on[counter_to_ten - 1][2] = "" + get_selected_answer_string.getText().toString();
                    button_number_id = v.getId();
                    v.startAnimation(pushed_in_options_button);
                }
            }
        });

        answers[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttons_can_be_pressed) {
                    buttons_can_be_pressed = false;
                    get_selected_answer_string = (Button) v;
                    result_array_to_pass_on[counter_to_ten - 1][2] = "" + get_selected_answer_string.getText().toString();
                    button_number_id = v.getId();
                    v.startAnimation(pushed_in_options_button);
                }
            }
        });

        pushed_in_options_button.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (counter_to_ten <= 10) {
                    if (button_number_id == resource_button_which_is_clicked) {
                        correct_answer_counter++;
                        answer_picture.setImageResource(answer_pictures_resource[0]);
                        result.setText("Score: " + correct_answer_counter + " / 10");
                    } else {
                        answer_picture.setImageResource(answer_pictures_resource[1]);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (counter_to_ten < 10) {
                    try {
                        Thread.sleep(1500);
                        answer_picture.setImageDrawable(null);

                        if (timer_on_off == 0) {
                            startActual_game_play(counter_to_ten);
                        } else if (timer_on_off == 1) {
                            cdt.cancel();
                            setTimer(default_timer);
                        }
                    } catch (Exception e) {
                        answer_picture.setImageDrawable(null);

                        if (timer_on_off == 0) {
                            startActual_game_play(counter_to_ten);
                        } else if (timer_on_off == 1) {
                            cdt.cancel();
                            setTimer(default_timer);
                        }
                    }
                }
                else if (counter_to_ten == 10) {
                    try {
                        Thread.sleep(1500);
                        answer_picture.setImageDrawable(null);
                        cdt.cancel();
                        // and start the results fragment from here in a method then and finish this fragment
                        delegates.processTotal_results(correct_answer_counter, result_array_to_pass_on);
                    }
                    catch (Exception e) {
                        answer_picture.setImageDrawable(null);
                        //cdt.cancel();
                        // and start the results fragment from here in a method then and finish this fragment
                        delegates.processTotal_results(correct_answer_counter, result_array_to_pass_on);
                    }

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return view;
    }

    private void setTimer(long timer_time) {

        answer_picture.setImageDrawable(null);

        if (timer_time == 30000) {
            startActual_game_play(counter_to_ten);
        }

        cdt = new CountDownTimer(timer_time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Spanned createString = Html.fromHtml("<b>" + millisUntilFinished / 1000 + "</b> \u2B05 Timer");
                timer_variable = millisUntilFinished;
                timer.setText(createString);
            }

            @Override
            public void onFinish() {
                timer.setText("Times up!");
                buttons_can_be_pressed = false;

                answer_picture.setImageResource(answer_pictures_resource[1]);

                Handler delayhandler = new Handler();
                delayhandler.postDelayed(mUpdateTimeTask, 500);

            }
        }.start();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            // This line is necessary for the next call
            if (counter_to_ten < 10) {
                try {
                    Thread.sleep(1500);
                    cdt.cancel();
                    setTimer(default_timer);
                } catch (Exception e) {
                    cdt.cancel();
                    setTimer(default_timer);
                }
            }
            else if (counter_to_ten == 10) {
                try {
                    Thread.sleep(1500);
                    cdt.cancel();
                    delegates.processTotal_results(correct_answer_counter, result_array_to_pass_on);
                } catch (Exception e) {
                    cdt.cancel();
                    delegates.processTotal_results(correct_answer_counter, result_array_to_pass_on);
                }

            }
        }
    };

    private void startGame_questions(int level) {

        for (int x = 0; x < 10; x++) {

            switch (level) {
                case 0:  questions_first[x] = (int) Math.ceil(Math.random() * 10);
                    questions_second[x] = (int) Math.ceil(Math.random() * 10);
                    break;
                case 1:  questions_first[x] = (int) Math.ceil(Math.random() * 50) + 10;
                    questions_second[x] = (int) Math.ceil(Math.random() * 50) + 10;
                    break;
                case 2:  questions_first[x] = (int) Math.ceil(Math.random() * 100) + 50;
                    questions_second[x] = (int) Math.ceil(Math.random() * 100) + 50;
                    break;
                default: break;
            }

            operator_value[x] = (int) Math.ceil(Math.random() * 4);

            correct_answer_button_put_answer[x] = (int) Math.floor(Math.random() * 4);

        }
    }

    private void startActual_game_play(int chooser) {

        if (counter_to_ten < 10) {

            String sign = null;
            int corAns = 0;
            double corAns_divide = 0;
            Spanned q = null;

            switch (operator_value[chooser]) {
                case 1:
                    sign = "\u002B";
                    corAns = questions_first[chooser] + questions_second[chooser];
                    q = Html.fromHtml("" + questions_first[chooser] + " " + sign + " " + questions_second[chooser]);
                    break;
                case 2:
                    sign = "\u2212";

                    if (questions_first[chooser] >= questions_second[chooser]) {
                        corAns = questions_first[chooser] - questions_second[chooser];
                        q = Html.fromHtml("" + questions_first[chooser] + " " + sign + " " + questions_second[chooser]);
                    }
                    else if (questions_first[chooser] < questions_second[chooser]) {
                        corAns = questions_second[chooser] - questions_first[chooser];
                        q = Html.fromHtml("" + questions_second[chooser] + " " + sign + " " + questions_first[chooser]);
                    }

                    break;
                case 3:
                    sign = "\u00D7";
                    corAns = questions_first[chooser] * questions_second[chooser];
                    q = Html.fromHtml("" + questions_first[chooser] + " " + sign + " " + questions_second[chooser]);
                    break;
                case 4:
                    sign = "\u00F7";

                    if (questions_first[chooser] >= questions_second[chooser]) {
                        //corAns = questions_first[chooser] / questions_second[chooser];
                        corAns_divide = (double) questions_first[chooser] / (double) questions_second[chooser];
                        q = Html.fromHtml("" + questions_first[chooser] + " " + sign + " " + questions_second[chooser]);
                    }
                    else if (questions_first[chooser] < questions_second[chooser]) {
                        //corAns = questions_second[chooser] / questions_first[chooser];
                        corAns_divide = (double) questions_second[chooser] / (double) questions_first[chooser];
                        q = Html.fromHtml("" + questions_second[chooser] + " " + sign + " " + questions_first[chooser]);
                    }

                    break;
                default:
                    break;
            }

            question.setText(q);

            if (operator_value[chooser] != 4) {
                answers[correct_answer_button_put_answer[chooser]].setText("" + corAns);

                result_array_to_pass_on[chooser][0] = "" + q;
                result_array_to_pass_on[chooser][1] = "" + corAns;

                switch (correct_answer_button_put_answer[chooser]) {
                    case 0:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_one", "id", getActivity().getPackageName());
                        break;
                    case 1:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_two", "id", getActivity().getPackageName());
                        break;
                    case 2:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_three", "id", getActivity().getPackageName());
                        break;
                    case 3:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_four", "id", getActivity().getPackageName());
                        break;
                    default:
                        break;
                }

                wrong_number_array = new int[3];
                wrongButton_numbers(chooser, 0);

                int wroAns = corAns + 7;
                answers[wrong_number_array[0]].setText("" + wroAns);

                wroAns = corAns - 3;
                answers[wrong_number_array[1]].setText("" + wroAns);

                wroAns = corAns + 4;
                answers[wrong_number_array[2]].setText("" + wroAns);
            }
            else if (operator_value[chooser] == 4) {
                answers[correct_answer_button_put_answer[chooser]].setText(String.format("%.2f", corAns_divide));

                result_array_to_pass_on[chooser][0] = "" + q;
                result_array_to_pass_on[chooser][1] = String.format("%.2f", corAns_divide);

                switch (correct_answer_button_put_answer[chooser]) {
                    case 0:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_one", "id", getActivity().getPackageName());
                        break;
                    case 1:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_two", "id", getActivity().getPackageName());
                        break;
                    case 2:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_three", "id", getActivity().getPackageName());
                        break;
                    case 3:
                        resource_button_which_is_clicked = getResources().getIdentifier("math_answer_four", "id", getActivity().getPackageName());
                        break;
                    default:
                        break;
                }

                wrong_number_array = new int[3];
                wrongButton_numbers(chooser, 0);

                double wroAns = corAns_divide + (double) 5;
                answers[wrong_number_array[0]].setText(String.format("%.2f", wroAns));

                wroAns = corAns_divide - (double) 3;
                answers[wrong_number_array[1]].setText(String.format("%.2f", wroAns));

                wroAns = corAns_divide + (double) 7;
                answers[wrong_number_array[2]].setText(String.format("%.2f", wroAns));
            }
            counter_to_ten++;
            buttons_can_be_pressed = true; // open the buttons
        }
        else if (counter_to_ten == 10) {
            buttons_can_be_pressed = false; // open the buttons
        }

    }

    private void wrongButton_numbers(int chooser, int current_position_of_array) {
        int wrong_button = (int) Math.floor(Math.random() * 4);

        if (wrong_button == correct_answer_button_put_answer[chooser]) {
            wrongButton_numbers(chooser, current_position_of_array);
        } else {
            wrong_number_array[current_position_of_array] = wrong_button;
            current_position_of_array++;
            wrongButton_numbers(chooser, current_position_of_array, 0);
        }
    }

    private void wrongButton_numbers(int chooser, int current_position_of_array, int first_position) {
        int wrong_button = (int) Math.floor(Math.random() * 4);

        if (wrong_button == correct_answer_button_put_answer[chooser] || wrong_button == wrong_number_array[first_position]) {
            wrongButton_numbers(chooser, current_position_of_array, first_position);
        } else {
            wrong_number_array[current_position_of_array] = wrong_button;
            current_position_of_array++;
            int luxury = correct_answer_button_put_answer[chooser] + wrong_number_array[first_position] + wrong_number_array[1];
            wrong_number_array[current_position_of_array] = 6 - luxury;
        }
    }

    public void pause_Timer() {
        buttons_can_be_pressed = false;
        cdt.cancel();

    }

    public void resume_Timer() {
        buttons_can_be_pressed = true;
        setTimer(timer_variable);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (timer_on_off == 1) {
            cdt.cancel();
        }
    }
}
