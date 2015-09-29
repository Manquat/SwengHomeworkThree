/*
 * Copyright 2014 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp.test;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.quizapp.DefaultNetworkProvider;
import ch.epfl.sweng.quizapp.NetworkProvider;
import ch.epfl.sweng.quizapp.NetworkQuizClient;
import ch.epfl.sweng.quizapp.QuizClient;
import ch.epfl.sweng.quizapp.QuizClientException;
import ch.epfl.sweng.quizapp.QuizQuestion;

import static junit.framework.Assert.assertTrue;

/** Tests whether we can interact with the real quiz server. */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NetworkEndToEndTest {

    @Test
    public void testGetRandomQuestion() throws QuizClientException {
        NetworkProvider networkProvider = new DefaultNetworkProvider();
        QuizClient quizClient = new NetworkQuizClient("https://sweng-quiz.appspot.com", networkProvider);
        QuizQuestion quizQuestion = quizClient.fetchRandomQuestion();

        assertTrue("Unexpected ID", quizQuestion.getID() > 0);
        assertTrue("Unexpected owner", quizQuestion.getOwner().length() > 0);
        assertTrue("Unexpected body", quizQuestion.getBody().length() > 0);
        assertTrue("Unexpected answer length", quizQuestion.getAnswers().size() >= 2);
        assertTrue("Unexpected solution index",
                quizQuestion.getSolutionIndex() >= 0);
        assertTrue("Unexpected solution index",
                quizQuestion.getSolutionIndex() < quizQuestion.getAnswers().size());
    }
}
