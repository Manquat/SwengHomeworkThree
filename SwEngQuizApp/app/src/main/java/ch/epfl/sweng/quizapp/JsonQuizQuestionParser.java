package ch.epfl.sweng.quizapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that implement the parser of the parser of JSON into a QuizQuestion
 */
public class JsonQuizQuestionParser implements QuizQuestionParser
{

    /**
     * Parse the given string as a JSON file in order to create a QuizQuestion
     * @param s    The text to parse
     * @return     A QuizQuestion containing the info extract from the string given in parameter
     * @throws QuizQuestionParseException Throw this exception in case of trouble during
     *  the process of the given string as a JSON file
     */
    @Override
    public QuizQuestion parse(String s) throws QuizQuestionParseException
    {
        try
        {
            JSONObject jsonObject = new JSONObject(s);

            return QuizQuestion.parseFromJSON(jsonObject);
        }
        catch (JSONException exception)
        {
            throw new QuizQuestionParseException();
        }
    }
}
