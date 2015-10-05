package ch.epfl.sweng.quizapp;

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

        switch (contentType.substring(0, contentType.indexOf(';')))
        {
            case "application/json":
                quizQuestionParser = new JsonQuizQuestionParser();
                break;
            default:
                throw new NoSuchQuestionFormatException();
        }
        return quizQuestionParser;
    }
}
