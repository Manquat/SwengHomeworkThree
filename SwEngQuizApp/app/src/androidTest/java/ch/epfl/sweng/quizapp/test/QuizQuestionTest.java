/*
 * Copyright 2014 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp.test;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.quizapp.QuizQuestion;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

/** Tests the QuizQuestion class. */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizQuestionTest {
    private static final int SAMPLE_QUESTION_ID = 12;
    private List<String> answerList;
    private List<String> tagList;

    @Before
    public void setUp() throws Exception {
        answerList = new ArrayList<String>();
        answerList.add("answer1");
        answerList.add("answer2");
        answerList.add("answer3");

        tagList = new ArrayList<String>();
        tagList.add("tag1");
        tagList.add("tag2");
    }

    @Test
    public void testIDAccess() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody",
                answerList, 1, tagList);

        assertEquals("Mismatched question ID", SAMPLE_QUESTION_ID, question.getID());
    }

    @Test
    public void testOwnerAccess() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody",
                answerList, 1, tagList);

        assertEquals("Mismatched owner", "test", question.getOwner());
    }

    @Test
    public void testAnswerListAccess() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody",
                answerList, 1, tagList);
        assertEquals("Mismatched answer list", answerList, question.getAnswers());
    }

    @Test
    public void testSolutionIndexAccess() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody",
                answerList, 1, tagList);
        assertEquals("Mismatched solution index", 1, question.getSolutionIndex());
    }

    @Test
    public void testTagListAccess() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody",
                answerList, 1, tagList);
        assertEquals("Mismatched tag list", tagList, question.getTags());
    }

    @Test
    public void testSolutionIndexOutOfRange() {
        try {
            final int outOfRangeSolutionIndex = 100;
            new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody", answerList, outOfRangeSolutionIndex, tagList);
            fail("Solution index larger than answer list size");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testSolutionIndexNegative() {
        try {
            new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody", answerList, -1, tagList);
            fail("Negative solution index");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testSolutionIndexArraySize() {
        try {
            final int solutionIndex = 3;
            new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody", answerList, solutionIndex, tagList);
            fail("Solution index should be less than answer list size");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testArgumentImmutable() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody", answerList, 1, tagList);
        answerList.add("bogus");
        tagList.add("bogus");

        assertFalse("Answers should be copied", answerList.equals(question.getAnswers()));
        assertFalse("Tags should be copied", tagList.equals(question.getTags()));
    }

    @Test
    public void testMemberImmutable() {
        QuizQuestion question = new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody", answerList, 1, tagList);
        try {
            question.getAnswers().add("bogus");
        } catch (UnsupportedOperationException e) {
            // good
        }

        try {
            question.getTags().add("bogus");
        } catch (UnsupportedOperationException e) {
            // good
        }

        assertEquals("Answers should be copied", answerList, question.getAnswers());
        assertEquals("Tags should be copied", tagList, question.getTags());
    }

    @Test
    public void testTooFewAnswers() {
        List<String> shortAnswerList = new ArrayList<String>();
        shortAnswerList.add("answer");

        try {
            new QuizQuestion(SAMPLE_QUESTION_ID, "test", "qbody", shortAnswerList, 0, tagList);
            fail("Question should have at least two answers");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testNullBody() {
        try {
            new QuizQuestion(SAMPLE_QUESTION_ID, "test", null, answerList, 1, tagList);
            fail("Question must have non-null body");
        } catch (IllegalArgumentException e) {
            // success
        } catch (NullPointerException e) {
            // success
        }
    }

    @Test
    public void testNullOwner() {
        try {
            new QuizQuestion(SAMPLE_QUESTION_ID, null, "qbody", answerList, 1, tagList);
            fail("Question must have non-null owner");
        } catch (IllegalArgumentException e) {
            // success
        } catch (NullPointerException e) {
            // success
        }
    }
}
