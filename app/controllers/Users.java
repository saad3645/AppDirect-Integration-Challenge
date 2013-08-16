package controllers;


import models.Company;
import models.User;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 *
 * @author Saad Ahmed
 */
public class Users extends Controller {


    // TODO
    public static Result assign() {
        return TODO;
    }

    // TODO
    public static Result unassign() {
        return TODO;
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
