package controllers;

import models.User;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import play.*;
import play.cache.Cache;
import play.mvc.*;

import views.html.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render());
    }


    public static Result openIdLogIn() {

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
            String returnUrl = "http://mighty-spire.herokuapp.com/openid/validate";

            // perform discovery on the user-supplied identifier
            List discoveries = manager.discover(openId);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // store the discovery information in the cache for later use
            Cache.set(openId + "manager", manager, 60 * 15);
            Cache.set(openId + "discovered", discovered, 60 * 15);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authRequest = manager.authenticate(discovered, returnUrl);

            return redirect(authRequest.getDestinationUrl(true));
        }

        catch (Exception e) {
            e.printStackTrace();
            return ok("Oops! something went wrong");
        }

        //return ok("OpenId: " + openId + " accountId: " + accountId);
    }


    public static Result openIdValidate() {

        Map<String, String[]> queryParams = request().queryString();

        String claimedId = queryParams.get("openid.claimed_id")[0];

        // retrieve the previously stored consumer manager and discovery information
        ConsumerManager manager = (ConsumerManager)Cache.get(claimedId + "manager");
        DiscoveryInformation discovered = (DiscoveryInformation)Cache.get(claimedId + "discovered");

        String receivingUrl = "http://" + request().host() + request().uri();
        ParameterList parameterList = new ParameterList(queryParams);

        // verify the response
        try {
            VerificationResult verification = manager.verify(receivingUrl, parameterList, discovered);
            Identifier verified = verification.getVerifiedId();

            if (verified != null) {
                String userOpenId = queryParams.get("openid.identity")[0];
                User user = User.find().where().eq("openId", userOpenId).findUnique();

                if (user != null) {
                    String userId = user.uuid;
                    session().clear();
                    session("userId", userId);

                    return redirect("/home");
                }

                else {
                    return unauthorized("User does not exist");
                }
            }

            else {
                return unauthorized("Unauthorized");
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            return unauthorized("Unauthorized");
        }

    }


    public static Result home() {
        String userId = session("userId");
        User user = User.find().byId(userId);
        return ok(home.render(user.firstName, user.lastName, user.openId));
    }
  
}
