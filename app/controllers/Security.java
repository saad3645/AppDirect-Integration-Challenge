package controllers;


import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.client.methods.HttpGet;

public class Security {


    public static String signAuthorization(String url) throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer("Dummy", "secret");
        HttpGet request = new HttpGet(url);
        consumer.sign(request);
        return request.getHeaders("Authorization")[0].getValue();
    }
}
