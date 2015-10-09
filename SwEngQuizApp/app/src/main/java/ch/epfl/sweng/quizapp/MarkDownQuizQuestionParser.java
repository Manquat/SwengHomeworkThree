package ch.epfl.sweng.quizapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Parses questions in MarkDown format */
public class MarkDownQuizQuestionParser implements QuizQuestionParser
{

    @Override
    public QuizQuestion parse(String s) throws QuizQuestionParseException
    {
        // TODO: complete this method to parse the quiz question.
        // You may use whatever method you like, and can freely
        // add/delete/change code inside this method. Do not change the method
        // signature, though. The following Java API methods might be helpful:
        // - java.util.regex.Pattern#matcher
        // - java.lang.String#split

        // Example code follows...

        // First, split the string into questions and answers. Throw a
        // QuizQuestionParseException if there are extra paragraphs, or not
        // enough.
        String[] parts = s.split("\\n\\n");
        if (parts.length != 2)
        {
            throw new QuizQuestionParseException ();
        }
        //String bodyAndAttrs = parts[0];
        //String answerList = parts[1];

        List<String> answers = new ArrayList<>();
        answers.add("We don't know");
        answers.add("42");
        answers.add("six times nine (if you compute in base 13)");
        answers.add("The mice know, but they won't tell.");

        return new QuizQuestion(
                17005,                                         // ID
                "sweng",                                  // Owner
                "What is the answer to life, the universe and everything?",                                       // Question text
                answers,  // Answers
                2,                                         // Solution index
                Arrays.asList(new String[] {})             // Tags
                );
    }

}
