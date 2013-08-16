package controllers;


import models.Company;
import models.Subscription;
import models.User;
import models.xml.Account;
import models.xml.Event;
import play.libs.F;
import play.libs.Json;
import play.libs.WS;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Saad Ahmed
 */
public class Users extends Controller {


    @BodyParser.Of(BodyParser.Xml.class)
    public static Result assign() {

        Map<String, String[]> queryParams = request().queryString();

        if (!queryParams.containsKey("url")) {
            return badRequest();
        }

        String encodedUrl = queryParams.get("url")[0];
        String url = "";
        String authorization = "";

        try {
            url = URLDecoder.decode(encodedUrl, "UTF-8");
            authorization = Security.signAuthorization(url);
        }

        catch (Exception e) {
            return unauthorized();
        }

        return async(
                WS.url(url).setHeader("Authorization", authorization).get().map(
                        new F.Function<WS.Response, Result>() {
                            @Override
                            public Result apply(WS.Response response) throws Throwable {

                                String s = response.getBody();
                                StringReader reader = new StringReader(s);

                                JAXBContext context = JAXBContext.newInstance(Event.class);
                                Unmarshaller unmarshaller = context.createUnmarshaller();

                                Event event = (Event) unmarshaller.unmarshal(reader);
                                Account account = event.getPayload().getAccount();
                                User user = event.getPayload().getUser();


                                Subscription subscription = Subscription.find().byId(account.getAccountIdentifier());

                                if (subscription == null) {
                                    String errorResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                            "<result>\n" +
                                            "    <success>false</success>\n" +
                                            "    <errorCode>ACCOUNT_NOT_FOUND</errorCode>\n" +
                                            "    <message>The account " + account.getAccountIdentifier() + " could not be found.</message>\n" +
                                            "</result>";

                                    return ok(errorResponse).as("text/xml");
                                }

                                Subscription.addUser(subscription.id, user);

                                String successResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                        "<result>\n" +
                                        "    <success>true</success>\n" +
                                        "    <message>User assigned successfully</message>" +
                                        "</result>";

                                return ok(successResponse).as("text/xml");
                            }
                        }
                )
        );
    }


    @BodyParser.Of(BodyParser.Xml.class)
    public static Result unassign() {
        Map<String, String[]> queryParams = request().queryString();

        if (!queryParams.containsKey("url")) {
            return badRequest();
        }

        String encodedUrl = queryParams.get("url")[0];
        String url = "";
        String authorization = "";

        try {
            url = URLDecoder.decode(encodedUrl, "UTF-8");
            authorization = Security.signAuthorization(url);
        }

        catch (Exception e) {
            return unauthorized();
        }

        return async(
                WS.url(url).setHeader("Authorization", authorization).get().map(
                        new F.Function<WS.Response, Result>() {
                            @Override
                            public Result apply(WS.Response response) throws Throwable {

                                String s = response.getBody();
                                StringReader reader = new StringReader(s);

                                JAXBContext context = JAXBContext.newInstance(Event.class);
                                Unmarshaller unmarshaller = context.createUnmarshaller();

                                Event event = (Event) unmarshaller.unmarshal(reader);
                                Account account = event.getPayload().getAccount();
                                User user = event.getPayload().getUser();


                                Subscription subscription = Subscription.find().byId(account.getAccountIdentifier());

                                if (subscription == null) {
                                    String errorResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                            "<result>\n" +
                                            "    <success>false</success>\n" +
                                            "    <errorCode>ACCOUNT_NOT_FOUND</errorCode>\n" +
                                            "    <message>The account " + account.getAccountIdentifier() + " could not be found.</message>\n" +
                                            "</result>";

                                    return ok(errorResponse).as("text/xml");
                                }


                                List<User> users = subscription.users;

                                if (!users.contains(user)) {
                                    String errorResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                            "<result>\n" +
                                            "    <success>false</success>\n" +
                                            "    <errorCode>USER_NOT_FOUND</errorCode>\n" +
                                            "    <message>User " + user.firstName + " " + user.lastName + " has not been assigned</message>\n" +
                                            "</result>";

                                    return ok(errorResponse).as("text/xml");
                                }

                                Subscription.removeUser(subscription.id, user);

                                String successResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                        "<result>\n" +
                                        "    <success>true</success>\n" +
                                        "    <message>User unassigned successfully</message>" +
                                        "</result>";

                                return ok(successResponse).as("text/xml");
                            }
                        }
                )
        );
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result all() {
        List<User> users = User.find().all();
        return ok(Json.toJson(users));
    }


    public static Result deleteAllUsers() {
        List<User> users = User.find().all();
        for (User u : users) {
            u.delete();
        }

        return ok("All users deleted");
    }

    public static Result deleteAllCompanies() {
        List<Company> companies = Company.find().all();
        for (Company c: companies) {
            c.delete();
        }

        return ok("All companies deleted");
    }
}
