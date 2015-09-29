/*
 * Copyright 2015 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp;

/**
 * Thrown to indicate a problem encountered by a {@link QuizClient} when
 * communicating to the quiz server.
 *
 */
public class QuizClientException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public QuizClientException() {
        super();
    }
    
    public QuizClientException(String message) {
        super(message);
    }
    
    public QuizClientException(Throwable throwable) {
        super(throwable);
    }
}
