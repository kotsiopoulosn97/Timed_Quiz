package com.example.timedquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int sec;  //δευτερόλεπτα χρονομέτρου
    private int score;   //το σκορ του χρήστη
    private boolean counting;   //true αν τρέχει το χρονόμετρο
    private boolean addBonus;  //true αν δικαιούται μπόνους
    // ο πίνακας των ερωτήσεων και των απαντήσεων
    private String[][] qa = {
            {"Ποια είναι η πρωτεύουσα της Γαλλίας;","Παρίσι", "Λιόν", "Γκρενόμπλ", "Νίκαια"},
            {"Ποια ΔΕΝ είναι Γαλλική ομάδα ποδοσφαίρου;", "Παρί Σεν Ζερμέν", "Τουλούζ", "Μπαρτσελόνα", "Μονακό"},
            {"Ποιος είναι ο πρόεδρος της Γαλλίας;", "Σαρκοζί", "Ρουαγιάλ", "Λαγκάρντ", "Ολάντ"} };
    //ο πίνακας με τον δείκτη της ορθής απάντησης για κάθε ερώτηση
    private int[] correctAnswers = {1,3,4};
    //ο δείκτης της τρέχουσας ερώτησης
    private int currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null) {
            sec = savedInstanceState.getInt("sec");
            score = savedInstanceState.getInt("score");
            counting = savedInstanceState.getBoolean("counting");
            addBonus = savedInstanceState.getBoolean("addBonus");
            currentQuestion = savedInstanceState.getInt("currentQuestion");
        }
        else {
            sec = 0; score = 0; counting = true;
            addBonus = true;
            currentQuestion = 0;
        }
        TextView scoreView = (TextView) findViewById(R.id.textViewScoreValue);
        scoreView.setText("" + score);
        nextQuestion();
        showTime();
    }
    private void nextQuestion() {
        if (currentQuestion<qa.length) {
            TextView question = (TextView) findViewById(R.id.textViewQuestionText);
            question.setText(qa[currentQuestion][0]);
            RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
            rb1.setText(qa[currentQuestion][1]);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
            rb2.setText(qa[currentQuestion][2]);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
            rb3.setText(qa[currentQuestion][3]);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);
            rb4.setText(qa[currentQuestion][4]);
        }
    }
    private void showTime() {
        final TextView timerView = (TextView) findViewById(R.id.TextViewTimeRemaining);
        final Handler handler = new Handler();
        handler.post(
                new Runnable() {
            @Override
            public void run() {
                if (sec <= 120 && counting) {
                    int minutes = sec / 60;
                    int seconds = sec % 60;
                    String time = String.format("%02d:%02d", minutes, seconds);
                    timerView.setText(time); sec++;
                    handler.postDelayed(this, 1000);
                }
                else {
                    String noBonusMessage = getString(R.string.noBonus);
                    timerView.setText(noBonusMessage); addBonus = false;
                }
            }
        });
    }
    public void onClicAnswer(View view) {
        //ανάκτησε τις απαντήσεις και έλεγξέ τις
        // για να δεις αν ο χρήστης απάντησε σωστά.
        int correctAnswer = correctAnswers[currentQuestion];
        int selectedAnswer = getSelectedAnswer();
        //Άλλαξε το σκορ αν ο χρήστης βρήκε την σωστή απάντηση
        if (selectedAnswer==correctAnswer) {
            if (addBonus)
                score += 2 * 5;
            else score += 5;
        }
     //ενημέρωσε το score στην οθόνη της εφαρμογής
      TextView scoreText = (TextView) findViewById(R.id.textViewScoreValue);
      scoreText.setText(""+score);
      //Αύξησε τον μετρητή της τρέχουσας ερώτησης
      currentQuestion++;
      //Αν υπερβήκαμε το όριο των ερωτήσεων
      if (currentQuestion==3) {
        //σταμάτησε να μετράας το χρόνο γιατί τελείωσε το παιχνίδι
        counting=false;
        //Κατεύθυνε τον χρήστη σε μία άλλη δραστηριότητα ενημέρωσης τέλους παιχνιδιού
        Intent intent = new Intent(this, EndGameActivity.class);
        //πέρασε εκεί το score για να εμφανισθεί
        intent.putExtra("score",score);
        startActivity(intent);
      }
      else {
        //πέρασε στην επόμενη ερώτηση αν δεν ήταν η τελευταία
        nextQuestion();
        // ξεκίνησε το χρόνο από το μηδέν
        sec = 0;
        counting = true;
        addBonus = true;
        showTime();
      }
    }
    private int getSelectedAnswer() {
        int selectedAnswer=0;
        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
        if (rb1.isChecked()) selectedAnswer=1;
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
        if (rb2.isChecked()) selectedAnswer=2;
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
        if (rb3.isChecked()) selectedAnswer=3;
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);
        if (rb4.isChecked()) selectedAnswer=4;
        return selectedAnswer;
        }
}




