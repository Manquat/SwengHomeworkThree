/*
 * Copyright 2015 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp;


/**
 * A client object to a SwEng quiz server that abstracts the underlying
 * communication protocol and data formats.
 *
 */
public interface QuizClient
{
    /**
     * 
     * @return A {@link QuizQuestion} object encapsulating a new random
     * question retrieved from the server.
     * @throws QuizClientException in case the random question could not be
     * retrieved for any reason external to the application (network failure,
     * no random questions available, etc.)
     */
    QuizQuestion fetchRandomQuestion() throws QuizClientException;
}
