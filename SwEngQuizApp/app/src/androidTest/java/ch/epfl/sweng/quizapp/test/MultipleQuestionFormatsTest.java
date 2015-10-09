package ch.epfl.sweng.quizapp.test;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.quizapp.NetworkProvider;
import ch.epfl.sweng.quizapp.NetworkQuizClient;
import ch.epfl.sweng.quizapp.QuizClientException;
import ch.epfl.sweng.quizapp.QuizQuestion;
import ch.epfl.sweng.quizapp.QuizQuestionParseException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/** Tests whether the app correctly handles multiple question formats. */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MultipleQuestionFormatsTest
{
    private static final int ASCII_SPACE = 0x20;
    private static final int SAMPLE_QUESTION_ID = 17005;
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
    private static final String MARDOWN_RESPONSE = "What is the answer to life, the universe "
            + "and everything? {#17005 owner=sweng}\n"
            + "\n"
            + "* We don't know\n"
            + "* 42\n"
            + "* []{.icon-ok} six times nine\n"
            + "  (if you compute in base 13)\n"
            + "* The mice know,\n"
            + "  but they won't tell.";

    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String TEXT_CONTENT_TYPE = "text/plain; charset=utf-8";
    private static final String PDF_CONTENT_TYPE = "text/pdf";
    private static final String MARKDOWN_CONTENT_TYPE = "text/markdown";


    private NetworkQuizClient quizClient;
    private HttpURLConnection connection;
    private NetworkProvider networkProvider;

    @Before
    public void setUp() throws Exception
    {
        connection = Mockito.mock(HttpURLConnection.class);
        networkProvider = Mockito.mock(NetworkProvider.class);
        Mockito.doReturn(connection).when(networkProvider).getConnection(Mockito.any(URL.class));

        quizClient = new NetworkQuizClient("http://example.com", networkProvider);
    }

    private void configureResponse(int status, String content, String contentType)
            throws IOException
    {
        InputStream dataStream = new ByteArrayInputStream(content.getBytes());
        Mockito.doReturn(status).when(connection).getResponseCode();
        Mockito.doReturn(dataStream).when(connection).getInputStream();
        Mockito.doReturn(contentType).when(connection).getContentType();
    }

    private void configureCrash(int status) throws IOException
    {
        InputStream dataStream = Mockito.mock(InputStream.class);
        Mockito.when(dataStream.read())
                .thenReturn(ASCII_SPACE, ASCII_SPACE, ASCII_SPACE, ASCII_SPACE)
                .thenThrow(new IOException());

        Mockito.doReturn(status).when(connection).getResponseCode();
        Mockito.doReturn(dataStream).when(connection).getInputStream();
    }


    /**
     * Test that the quiz client is correctly create with a JSON file
     * @throws IOException
     * @throws QuizClientException
     */
    @Test
    public void testQuestionParsingJSON() throws IOException, QuizClientException
    {
        configureResponse(HttpURLConnection.HTTP_OK, JSON_RESPONSE, JSON_CONTENT_TYPE);
        QuizQuestion question = quizClient.fetchRandomQuestion();
        assertNotNull(question);
    }

    @Test
    public void testQuestionParsingPDF() throws IOException
    {
        configureResponse(HttpURLConnection.HTTP_OK, JSON_RESPONSE, PDF_CONTENT_TYPE);
        try
        {
            quizClient.fetchRandomQuestion();
            fail("Did not raise QuizClientException");
        }
        catch (QuizClientException e)
        {
            //good
        }
    }

    /**
     * Test that the constructed quiz question is correct when a Markdown file is given.
     * @throws QuizClientException
     * @throws IOException
     */
    @Test
    public void testQuestionParsingMarkDown() throws QuizClientException, IOException
    {
        configureResponse(HttpURLConnection.HTTP_OK, MARDOWN_RESPONSE, MARKDOWN_CONTENT_TYPE);
        QuizQuestion question = quizClient.fetchRandomQuestion();

        List<String> answers = new ArrayList<>();
        answers.add("We don't know");
        answers.add("42");
        answers.add("six times nine (if you compute in base 13)");
        answers.add("The mice know, but they won't tell.");

        List<String> tags = new ArrayList<>();

        assertEquals(SAMPLE_QUESTION_ID, question.getID());
        assertEquals("What is the answer to life, the universe and everything?", question.getBody());
        assertEquals(answers, question.getAnswers());
        assertEquals(3, question.getSolutionIndex());
        assertEquals(tags, question.getTags());
        assertEquals("sweng", question.getOwner());
    }
}
