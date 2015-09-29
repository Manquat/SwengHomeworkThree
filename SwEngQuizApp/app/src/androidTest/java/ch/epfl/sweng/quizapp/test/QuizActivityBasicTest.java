/*
 * Copyright 2014 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sweng.quizapp.QuizActivity;
import ch.epfl.sweng.quizapp.QuizClient;
import ch.epfl.sweng.quizapp.QuizClientException;
import ch.epfl.sweng.quizapp.QuizQuestion;
import ch.epfl.sweng.quizapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertSame;
import static org.hamcrest.Matchers.not;

/** Tests basic functionality of the QuizActivity */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityBasicTest {

    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule = new ActivityTestRule<>(
            QuizActivity.class);

    @Test
    public void testQuizClientGetterSetter() throws QuizClientException {
        QuizActivity activity = mActivityRule.getActivity();
        QuizClient quizClient = new QuizClient() {
            @Override
            public QuizQuestion fetchRandomQuestion() throws QuizClientException {
                return null;
            }
        };

        activity.setQuizClient(quizClient);
        assertSame(quizClient, activity.getQuizClient());
    }

    @Test
    public void testNextQuestionButtonName() {
        onView(withId(R.id.nextQuestionButton))
                .check(matches(withText("Next question")));
    }

    @Test
    public void testFirstQuestionBodyVisible() {
        onView(withId(R.id.questionBodyView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNextQuestionDisabled() {
        onView(withId(R.id.nextQuestionButton)).check(matches(not(isEnabled())));
    }
}
