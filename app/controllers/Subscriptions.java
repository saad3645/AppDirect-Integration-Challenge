package controllers;

import models.Company;
import models.Subscription;
import models.SubscriptionItem;
import models.User;
import models.xml.Event;
import models.xml.Item;
import models.xml.Order;
import models.xml.Payload;
import play.libs.F.Function;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;


/**
 *
 * @author Saad Ahmed
 */
public class Subscriptions extends Controller {


    public static Result order() {
        Map<String, String[]> queryParams = request().queryString();

        if (!queryParams.containsKey("url")) {
            return badRequest();
        }

        String encodedUrl = queryParams.get("url")[0];
        String url = "";

        try {
            url = URLDecoder.decode(encodedUrl, "UTF-8");
        }

        catch (UnsupportedEncodingException e) {
            return badRequest();
        }

        return async(
            WS.url(url).get().map(
                    new Function<WS.Response, Result>() {
                        @Override
                        public Result apply(WS.Response response) throws Throwable {

                            JAXBContext context = JAXBContext.newInstance(Event.class);
                            Unmarshaller unmarshaller = context.createUnmarshaller();

                            Event event = (Event)unmarshaller.unmarshal(response.getBodyAsStream());
                            User creator = event.getCreator();
                            Payload payload = event.getPayload();
                            Company company = payload.getCompany();
                            Order order = payload.getOrder();
                            String edition = order.getEditionCode();
                            Item[] items = order.getItem();

                            if (User.find().ref(creator.uuid) == null) {
                                creator.save();
                            }

                            if (Company.find().ref(company.uuid) == null) {
                                company.save();
                            }

                            UUID accountId = Subscription.create(company, creator, edition);

                            for (int i = 0; i < items.length; i++) {
                                Subscription.addItem(accountId, new SubscriptionItem(items[i].getUnit(), items[i].getQuantity()));
                            }

                            return ok(accountId.toString());
                        }
                    }
            )
        );

    }

    public static Result change() {
        return ok();
    }

    public static Result cancel() {
        return ok();
    }

    public static Result notice() {
        return ok();
    }



}
