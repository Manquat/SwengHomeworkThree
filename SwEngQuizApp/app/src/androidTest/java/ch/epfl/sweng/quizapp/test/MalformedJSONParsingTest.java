package ch.epfl.sweng.quizapp.test;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sweng.quizapp.QuizQuestion;

import static junit.framework.Assert.fail;

/** Tests whether the app correctly handles malformed JSON */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MalformedJSONParsingTest {

    private static final String JSON_RESPONSE = "{\n"
            + "  \"id\": 17005,\n"
            + "  \"question\": \"What is the capital of Antigua and Barbuda?\",\n"
            + "  \"answers\": [\n"
            + "    \"Chisinau\",\n"
            + "    \"Saipan\",\n"
            + "    \"St. John's\",\n"
            + "    \"Plymouth\"\n"
            + "  ],\n"
            + "  \"solutionIndex\": 2,\n"
            + "  \"tags\": [\n"
            + "    \"capitals\",\n"
            + "    \"geography\",\n"
            + "    \"countries\"\n"
            + "  ],\n"
            + "  \"owner\": \"sweng\"\n"
            + "}\n";

    private List<String> jsonFields;

    @Before
    public void setUp() throws Exception {
        jsonFields = new ArrayList<String>();
        jsonFields.add("id");
        jsonFields.add("question");
        jsonFields.add("answers");
        jsonFields.add("solutionIndex");
        jsonFields.add("tags");
        jsonFields.add("owner");
    }

    @Test
    public void testEmptyJSON() {
        try {
            QuizQuestion.parseFromJSON(new JSONObject());
            fail("Parsed empty JSON");
        } catch (JSONException e) {
            // success
        }
    }

    @Test
    public void testMissingFields() throws JSONException {
        for (String field: jsonFields) {
            JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
            jsonObject.remove(field);

            try {
                QuizQuestion.parseFromJSON(jsonObject);
                fail("Parsed missing field: " + field);
            } catch (JSONException e) {
                // success
            }
        }
    }

    @Test
    public void ignoredTestNullFields() throws JSONException {
        for (String field: jsonFields) {
            JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
            jsonObject.put(field, JSONObject.NULL);

            try {
                QuizQuestion.parseFromJSON(jsonObject);
                fail("Parsed null field: " + field);
            } catch (JSONException e) {
                // success
            }
        }
    }

    @Test
    public void ignoredTestWrongType() throws JSONException {
        for (String field: jsonFields) {
            JSONObject dummyValue = new JSONObject();
            dummyValue.put("wrong", "type");
            JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
            jsonObject.put(field, dummyValue);

            try {
                QuizQuestion.parseFromJSON(jsonObject);
                fail("Parsed wrong type for field: " + field);
            } catch (JSONException e) {
                // success
            }
        }
    }

    @Test
    public void ignoredTestWrongAnswerType() throws JSONException {
        JSONObject dummyValue = new JSONObject();
        dummyValue.put("wrong", "type");

        JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
        JSONArray answers = jsonObject.getJSONArray("answers");
        answers.put(0, dummyValue);

        try {
            QuizQuestion.parseFromJSON(jsonObject);
            fail("Parsed wrong answer type");
        } catch (JSONException e) {
            // success
        }
    }

    @Test
    public void ignoredTestWrongTagType() throws JSONException {
        JSONObject dummyValue = new JSONObject();
        dummyValue.put("wrong", "type");

        JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
        JSONArray tags = jsonObject.getJSONArray("tags");
        tags.put(0, dummyValue);

        try {
            QuizQuestion.parseFromJSON(jsonObject);
            fail("Parsed wrong tag type");
        } catch (JSONException e) {
            // success
        }
    }

    @Test
    public void testTooFewAnswers() throws JSONException {
        JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
        JSONArray answers = new JSONArray();
        answers.put("answer");
        jsonObject.put("answers", answers);

        try {
            QuizQuestion.parseFromJSON(jsonObject);
            fail("Parsed malformed JSON");
        } catch (JSONException e) {
            // success
        }
    }

    @Test
    public void testWrongSolutionIndex() throws JSONException {
        JSONObject jsonObject = new JSONObject(JSON_RESPONSE);
        final int wrongSolutionIndex = 42;
        jsonObject.put("solutionIndex", wrongSolutionIndex);

        try {
            QuizQuestion.parseFromJSON(jsonObject);
            fail("Parsed malformed JSON");
        } catch (JSONException e) {
            // success
        }
    }
}
