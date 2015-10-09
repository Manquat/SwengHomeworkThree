package ch.epfl.sweng.quizapp;

import java.io.IOException;

/**
 * This factory class knows about supported question formats.
 */
public class QuizQuestionParserFactory
{
    /**
     * Obtains a parser for the given MIME type.
     *
     * @param contentType
     *            The MIME type that the parser should understand, e.g.,
     *            "application/json"
     * @return A parser for the given contentType
     * @throws NoSuchQuestionFormatException
     *             If no known parser supports this content type
     */
    public static QuizQuestionParser parserForContentType(String contentType)
            throws NoSuchQuestionFormatException
    {
        // TODO: Implement formats
        QuizQuestionParser quizQuestionParser;

        // the given type is void
        if (contentType == null)
        {
            throw new NoSuchQuestionFormatException();
        }

        int index = contentType.indexOf(';');

        // there is no indication of the character encoding
        if (index == -1)
        {
            index = contentType.length();
        }
        switch (contentType.substring(0, index))
        {
            case "application/json":
                quizQuestionParser = new JsonQuizQuestionParser();
                break;
            case "text/markdown":
                quizQuestionParser = new MarkDownQuizQuestionParser();
                break;
            default:
                throw new NoSuchQuestionFormatException();
        }
        return quizQuestionParser;
    }
}
