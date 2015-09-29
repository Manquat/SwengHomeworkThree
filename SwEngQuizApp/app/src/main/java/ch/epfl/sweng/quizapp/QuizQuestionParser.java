package ch.epfl.sweng.quizapp;

/**
 * A QuizQuestionParser knows how to translate text into quiz questions. Parsers
 * may exist for a variety of formats, but they all share this common interface.
 */
public interface QuizQuestionParser {
    /**
     * Parses some text, and returns the created QuizQuestion.
     *
     * @param s    The text to parse
     * @return     A QuizQuestion instance created from parsing s
     * @throws QuizQuestionParseException
     *             if s does not represent a valid quiz question (according to
     *             the format that the parser supports)
     */
    QuizQuestion parse(String s) throws QuizQuestionParseException;
}
