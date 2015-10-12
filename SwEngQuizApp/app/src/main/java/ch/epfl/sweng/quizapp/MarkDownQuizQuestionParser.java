package ch.epfl.sweng.quizapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses questions in MarkDown format */
public class MarkDownQuizQuestionParser implements QuizQuestionParser
{
    static final String CORRECT_ANSWER_MARK = "[]{.icon-ok}";
    static final String OWNER_MARK = "owner=";

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
        String bodyAndAttrs = parts[0];
        String answerList = parts[1];

        int index;
        String owner;
        List<String> answers;
        int indexOfCorrectAnswer;


        //------------------------------------------------------------------------------------------
        // treatment of the question's part

        String[] bodyAndAttrsPart = bodyAndAttrs.split("\\{");

        if (bodyAndAttrsPart.length != 2)
        {
            throw new QuizQuestionParseException();
        }

        String questionBody = normalize(bodyAndAttrsPart[0]);

        int endBracket = bodyAndAttrsPart[1].indexOf('}');

        // if the bracket is never close
        if (endBracket == -1)
        {
            throw new QuizQuestionParseException();
        }

        String stringAttributes = bodyAndAttrsPart[1].substring(0, endBracket);

        Pattern pId = Pattern.compile("#.* ");
        Matcher mId = pId.matcher(stringAttributes);

        // if there is no ID
        if (!mId.find())
        {
            throw new QuizQuestionParseException();
        }


        // avoid the # and space character
        String stringIndex = mId.group();
        stringIndex = stringIndex.substring(1, stringIndex.length()-1);
        if (stringIndex.length() == 0)
        {
            throw new QuizQuestionParseException();
        }

        try
        {
            index = Integer.parseInt(stringIndex);
        }
        catch (NumberFormatException e)
        {
            throw new QuizQuestionParseException();
        }

        int indexOfOwner = stringAttributes.indexOf(OWNER_MARK);
        if (indexOfOwner == -1)
        {
            throw new QuizQuestionParseException();
        }

        owner = stringAttributes.substring(indexOfOwner + OWNER_MARK.length(),
                stringAttributes.length());

        //------------------------------------------------------------------------------------------
        // treatment of the answer's part

        String[] answerListString = answerList.split("\\* ");
        if (answerListString[0].equals(""))
        {
            String[] tempString = new String[answerListString.length-1];
            for (int i=0; i < answerListString.length -1; i++)
            {
                tempString[i] = answerListString[i+1];
            }
            answerListString = tempString;
        }

        indexOfCorrectAnswer = -1;
        for (int i = 0; i < answerListString.length; i++)
        {
            answerListString[i] = normalize(answerListString[i]);

            // if this is the correct answer
            int tempIndex = answerListString[i].indexOf(CORRECT_ANSWER_MARK);
            if (tempIndex != -1)
            {
                if (indexOfCorrectAnswer != -1)
                {
                    throw new QuizQuestionParseException();
                }
                indexOfCorrectAnswer = i;
                answerListString[i] = answerListString[i].substring(CORRECT_ANSWER_MARK.length());
                answerListString[i] = normalize(answerListString[i]);
            }
        }
        // if there is no correct answer
        if (indexOfCorrectAnswer == -1)
        {
            throw new QuizQuestionParseException();
        }

        answers = Arrays.asList(answerListString);




        return new QuizQuestion(
                index,                                      // ID
                owner,                                      // Owner
                questionBody,                               // Question text
                answers,                                    // Answers
                indexOfCorrectAnswer,                       // Solution index
                Arrays.asList(new String[] {})              // Tags
                );
    }

    private String normalize(String string)
    {
        String normalized = string;
        Boolean findSomething;

        Pattern pAnswerNewLine = Pattern.compile("\\n");
        Pattern pAnswerDoubleSpace = Pattern.compile("  ");

        do
        {
            findSomething = false;

            // find and replace all the \n by a space
            Matcher m = pAnswerNewLine.matcher(normalized);
            if (m.find())
            {
                normalized = m.replaceAll(" ");
                findSomething = true;
            }

            // find and replace all the double space by a space
            m = pAnswerDoubleSpace.matcher(normalized);
            if (m.find())
            {
                normalized = m.replaceAll(" ");
                findSomething = true;
            }

            // if there is a space at the end of the answer pop it
            if (normalized.startsWith(" "))
            {
                normalized = normalized.substring(1);
                findSomething = true;
            }

            // if there is a space at the end of the answer pop it
            if (normalized.endsWith(" "))
            {
                normalized = normalized.substring(0, normalized.length() - 1);
                findSomething = true;
            }
        }
        while (findSomething);

        return normalized;
    }

}
