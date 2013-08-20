package controllers;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import play.*;
import play.mvc.*;

import views.html.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Welcome to Mighty Spire"));
    }


    public static Result authenticate() {

        Map<String, String[]> queryParams = request().queryString();

        if (!queryParams.containsKey("openId") || !queryParams.containsKey("accountId")) {
            return badRequest("Invalid url");
        }

        String encodedOpenId = queryParams.get("openId")[0];
        String encodedAccountId = queryParams.get("accountId")[0];

        String openId = "";
        String accountId = "";
        String authorization;

        try {
            openId = URLDecoder.decode(encodedOpenId, "UTF-8");
            accountId = URLDecoder.decode(encodedAccountId, "UTF-8");
        }

        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //try {
        //    ConsumerManager consumerManager = new ConsumerManager();
        //    List discoveries = consumerManager.discover(openId);
        //}

        //catch (DiscoveryException e) {
        //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        //}


        return ok("OpenId: " + openId + " accountId: " + accountId);
    }
  
}
