/*
 * Copyright 2015 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main activity of the app.
 *
 */
public class QuizActivity extends Activity {
    private QuizClient mQuizClient;
    
    private TextView mQuestionBody;
    private RadioGroup mAnswerGroup;
    private Button mNextQuestionButton;
    private ProgressBar mProgressBar;
    
    private final static String SWENG_SERVER_URL = "https://sweng-quiz.appspot.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        mQuestionBody = (TextView) findViewById(R.id.questionBodyView);
        mAnswerGroup = (RadioGroup) findViewById(R.id.answerGroup);
        mNextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        
        setQuizClient(new NetworkQuizClient(SWENG_SERVER_URL,
                new DefaultNetworkProvider()));
        
        new DownloadQuestionTask().execute(mQuizClient);
    }
    
    /**
     * Retrieve the current {@link QuizClient} object used by the activity to
     * communicate with the SwEng quiz server.
     */
    public QuizClient getQuizClient() {
        return mQuizClient;
    }
    
    /**
     * Set the current {@link QuizClient} object used by the activity to
     * communicate with the SwEng quiz server.
     * 
     * <p> This update does not affect any pending communication with the
     * server, which is still performed on the old client.
     * @param quizClient the new QuizClient object.
     */
    public void setQuizClient(QuizClient quizClient) {
        this.mQuizClient = quizClient;
    }
    
    public void onNextQuestionClick(View v) {
        new DownloadQuestionTask().execute(mQuizClient);
    }
    
    /**
     * Async task for retrieving a random quiz question.
     *
     */
    private class DownloadQuestionTask extends AsyncTask<QuizClient, Void, QuizQuestion> {

        @Override
        protected QuizQuestion doInBackground(QuizClient... quizClient) {
            try {
                return quizClient[0].fetchRandomQuestion();
            } catch (QuizClientException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        @Override
        protected void onPreExecute() {
            // Disable all user input while fetching the question
            mNextQuestionButton.setEnabled(false);
            for (int i = 0; i < mAnswerGroup.getChildCount(); ++i) {
                mAnswerGroup.getChildAt(i).setEnabled(false);
            }
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
        
        @Override
        protected void onPostExecute(QuizQuestion result) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            
            if (result == null) {
                setErrorState();
                return;
            }
            
            displayQuestion(result);
        }
        
        private void setErrorState() {
            Context context = getApplicationContext();
            mNextQuestionButton.setEnabled(true);
            for (int i = 0; i < mAnswerGroup.getChildCount(); ++i) {
                mAnswerGroup.getChildAt(i).setEnabled(true);
            }
            Toast.makeText(context, getString(R.string.question_fetch_error),
                    Toast.LENGTH_SHORT).show();
        }
        
        private void displayQuestion(final QuizQuestion result) {
            mQuestionBody.setText(result.getBody());
            mQuestionBody.setVisibility(View.VISIBLE);
            
            List<String> answers = result.getAnswers();
            mAnswerGroup.removeAllViews();
            for (int i = 0; i < answers.size(); ++i) {
                RadioButton answerButton = new RadioButton(mAnswerGroup.getContext());
                answerButton.setText(answers.get(i));
                answerButton.setTag(i);
                answerButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioButton button = (RadioButton) v;
                        if ((Integer) button.getTag() != result.getSolutionIndex()) {
                            button.setTextColor(Color.RED);
                            button.setPaintFlags(button.getPaintFlags()
                                    | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            button.setTextColor(Color.GREEN);
                            mNextQuestionButton.setEnabled(true);
                        }
                    }
                });
                mAnswerGroup.addView(answerButton);
            }
        }
    }
}
