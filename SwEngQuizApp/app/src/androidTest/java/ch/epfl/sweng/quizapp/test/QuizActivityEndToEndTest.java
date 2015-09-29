/*
 * Copyright 2014 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp.test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.testing.ViewMatchers.withPaintFlag;
import static ch.epfl.sweng.testing.ViewMatchers.withTextColor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.quizapp.QuizActivity;
import ch.epfl.sweng.quizapp.R;


/** Tests the GUI against the real server */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityEndToEndTest {

    private static final int N_QUESTIONS_TO_TEST = 5;
    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule = new ActivityTestRule<>(
            QuizActivity.class);

    @Test
    public void testFlipThroughQuestions() {

        for (int count = 0; count < N_QUESTIONS_TO_TEST; ++count) {
            onView(withId(R.id.questionBodyView))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.nextQuestionButton))
                    .check(matches(allOf(withText("Next question"), not(isEnabled()))));

            for (int i = 0; true; ++i) {
                try {
                    onView(withTagValue(is((Object) i)))
                            .perform(click())
                            .check(matches(anyOf(
                                    allOf(withTextColor(Color.RED), withPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)),
                                    withTextColor(Color.GREEN))));
                } catch (NoMatchingViewException e) {
                    // Stop clicking, no more radio buttons to check
                    break;
                }
            }

            onView(withId(R.id.nextQuestionButton))
                    .check(matches(isEnabled()))
                    .perform(click())
                    .check(matches(not(isEnabled())));
        }
    }

}
