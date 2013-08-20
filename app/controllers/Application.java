package controllers;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
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


        try {
            ConsumerManager manager = new ConsumerManager();
            String returnUrl = "http://mighty-spire.herokuapp.com/openId";

            // perform discovery on the user-supplied identifier
            List discoveries = manager.discover(openId);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authRequest = manager.authenticate(discovered, returnUrl);

            return redirect(authRequest.getDestinationUrl(true));
        }

        catch (DiscoveryException e) {
            e.printStackTrace();
            return ok(e.getMessage());
        } catch (MessageException e) {
            e.printStackTrace();
            return ok(e.getMessage());
        } catch (ConsumerException e) {
            e.printStackTrace();
            return ok(e.getMessage());
        }


        //return ok("OpenId: " + openId + " accountId: " + accountId);
    }


    public static Result openId() {
        return ok();
    }
  
}
