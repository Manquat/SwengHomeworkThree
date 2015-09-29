/*
 * Copyright 2015 EPFL. All rights reserved.
 */

package ch.epfl.sweng.quizapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * A {@link QuizClient} implementation that uses a {@link NetworkProvider} to
 * communicate with a SwEng quiz server.
 *
 */
public class NetworkQuizClient implements QuizClient {
    private final String mServerUrl;
    private final NetworkProvider mNetworkProvider;
    
    private final static int HTTP_SUCCESS_START = 200;
    private final static int HTTP_SUCCESS_END = 299;

    /**
     * Creates a new NetworkQuizClient instance that communicates with a SwEng
     * server at a given location, through a {@link NetworkProvider} object.
     * @param serverUrl the base URL of the SwEng server
     * (e.g., "https://sweng-quiz.appspot.com").
     * @param networkProvider a NetworkProvider object through which to channel
     * the server communication.
     */
    public NetworkQuizClient(String serverUrl, NetworkProvider networkProvider) {
        mServerUrl = serverUrl;
        mNetworkProvider = networkProvider;
    }
    
    public QuizQuestion fetchRandomQuestion() throws QuizClientException {
        try {
            URL url = new URL(mServerUrl + "/quizquestions/random");
            HttpURLConnection conn = mNetworkProvider.getConnection(url);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            
            int response = conn.getResponseCode();
            if (response < HTTP_SUCCESS_START || response > HTTP_SUCCESS_END) {
                throw new QuizClientException("Invalid HTTP response code");
            }
            JSONObject jsonObject = new JSONObject(fetchContent(conn));
            return QuizQuestion.parseFromJSON(jsonObject);
        } catch (IOException e) {
            throw new QuizClientException(e);
        } catch (JSONException e) {
            throw new QuizClientException(e);
        }
    }
    
    private String fetchContent(HttpURLConnection conn) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            
            String result = out.toString();
            Log.d("HTTPFetchContent", "Fetched string of length "
                    + result.length());
            return result;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
