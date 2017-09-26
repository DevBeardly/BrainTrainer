package info.timgraves.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button goButton;
    TextView timeLeftTextView;
    TextView promptTextView;
    TextView currentScoreTextView;
    TextView rightWrongTextView;
    Button buttonOne;
    Button buttonTwo;
    Button buttonThree;
    Button buttonFour;
    Button playAgainButton;
    Boolean gameIsActive = false;
    CountDownTimer gameTimer;
    int score;
    int attempted;
    String userScore;
    int locOfAnswer;
    ArrayList<Integer> answers = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all views for referencing in other methods

        timeLeftTextView = (TextView)findViewById(R.id.timeLeftTextView);
        promptTextView = (TextView)findViewById(R.id.promptTextView);
        currentScoreTextView = (TextView)findViewById(R.id.currentScoreTextView);
        rightWrongTextView = (TextView)findViewById(R.id.rightWrongTextView);
        goButton = (Button)findViewById(R.id.goButton);
        buttonOne = (Button)findViewById(R.id.buttonOne);
        buttonTwo = (Button)findViewById(R.id.buttonTwo);
        buttonThree = (Button)findViewById(R.id.buttonThree);
        buttonFour = (Button)findViewById(R.id.buttonFour);
        playAgainButton = (Button)findViewById(R.id.playAgainButton);

        // begin the app with the "Go!" button and all values set to 0

        setupGame(playAgainButton);
    }

    public void setupGame (View view) {

        // method used to begin the game on first run, and reset
        // game state when the "Play Again" button is pressed

        score = 0;
        attempted = 0;
        userScore = "0/0";
        rightWrongTextView.setText("");
        timeLeftTextView.setVisibility(View.INVISIBLE);
        promptTextView.setVisibility(View.INVISIBLE);
        currentScoreTextView.setVisibility(View.INVISIBLE);
        rightWrongTextView.setVisibility(View.INVISIBLE);
        buttonOne.setVisibility(View.INVISIBLE);
        buttonTwo.setVisibility(View.INVISIBLE);
        buttonThree.setVisibility(View.INVISIBLE);
        buttonFour.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);

        goButton.setVisibility(View.VISIBLE);

    }

    public void startGame (View view) {

        // method used to begin the game, removing the "Go!" button and
        // setting the game UI elements visible to the user

        gameIsActive = true;

        goButton.setVisibility(View.INVISIBLE);

        timeLeftTextView.setVisibility(View.VISIBLE);
        promptTextView.setVisibility(View.VISIBLE);
        currentScoreTextView.setVisibility(View.VISIBLE);
        rightWrongTextView.setVisibility(View.VISIBLE);
        buttonOne.setVisibility(View.VISIBLE);
        buttonTwo.setVisibility(View.VISIBLE);
        buttonThree.setVisibility(View.VISIBLE);
        buttonFour.setVisibility(View.VISIBLE);

        generateAnswer();

        gameTimer = new CountDownTimer(30000 + 100, 1000) {

            @Override
            public void onTick(long l) {

                int timeLeft = (int) l / 1000;
                timeLeftTextView.setText(Integer.toString(timeLeft) + "s");

            }

            @Override
            public void onFinish() {

                timeLeftTextView.setText("0");
                gameIsActive = false;
                playAgainButton.setVisibility(View.VISIBLE);
                rightWrongTextView.setText("Your score : " + userScore);

            }
        }.start();

    }

    public void generateAnswer () {

        // generates the prompt question, the correct answer, and incorrect answers
        // assigns the answer values to the array and then to the appropriate buttons,
        // and keeps track of the location of the correct answer

        Random rand = new Random();

        int a = rand.nextInt(21);
        int b = rand.nextInt(21);

        promptTextView.setText(Integer.toString(a) + " + " + Integer.toString(b));

        locOfAnswer = rand.nextInt(4);
        int wrongAnswer;

        for (int i = 0; i <4; i++) {
            if (i == locOfAnswer) {
                answers.add(a + b);
            } else {

                wrongAnswer = rand.nextInt(41);

                while (wrongAnswer == a + b) {

                    wrongAnswer = rand.nextInt(41);

                }

                answers.add(wrongAnswer);
            }
        }

        buttonOne.setText(Integer.toString(answers.get(0)));
        buttonTwo.setText(Integer.toString(answers.get(1)));
        buttonThree.setText(Integer.toString(answers.get(2)));
        buttonFour.setText(Integer.toString(answers.get(3)));

    }

    public void checkAnswer(View view) {

        // determine which button has been pressed, and then, if the game is active, check
        // the button pressed against the location of the correct answer within the array.
        // Whether the user iscorrect is passed to the updateScore() method, and "Correct!"
        // or "Incorrect!" is displayed to the user. The array is then cleared, and the
        // method to generate new answers is called.

        int pressed = Integer.parseInt(view.getTag().toString());

        if (gameIsActive) {
            if (pressed == locOfAnswer + 1) {
                rightWrongTextView.setText("Correct!");
                updateScore(true);
            } else {
                rightWrongTextView.setText("Incorrect!");
                updateScore(false);
            }

            answers.clear();
            generateAnswer();

        }



    }

    public void updateScore(boolean correctAnswer) {

        // receives a true or false indicator from the calling function which determines
        // whether the user's score is incremented or left alone, and increments the
        // number of attempts, then displays the current total score to the user

        if (correctAnswer) {
            score++;
        }

        attempted++;

        userScore = score + "/" + attempted;

        currentScoreTextView.setText(userScore);

    }
}
