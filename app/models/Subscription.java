package models;

import com.avaje.ebean.validation.NotNull;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author Saad Ahmed
 */

@Entity
@Table(name="subscriptions")
public class Subscription extends Model {

    @Id
    public String id;

    @NotNull
    @ManyToOne (cascade = CascadeType.ALL)
    public Company company;

    @NotNull
    @ManyToOne (cascade = CascadeType.ALL)
    public User creator;

    @ManyToMany (cascade = CascadeType.ALL)
    List<User> users;

    @NotNull
    public String edition;

    @OneToMany (cascade = CascadeType.ALL)
    public List<SubscriptionItem> items;

    @NotNull
    public String status;


    public Subscription(Company company, User creator, String edition) {
        this.id = UUID.randomUUID().toString();
        this.company = company;
        this.creator = creator;
        this.edition = edition;
        this.status = Status.FREE_TRIAL.toString().toUpperCase();
    }


    public static Finder<String, Subscription> find() {
        return new Finder<String, Subscription>(String.class, Subscription.class);
    }

    public static String create(Company company, User creator, String edition) {
        Subscription subscription = new Subscription(company, creator, edition);
        subscription.save();
        return subscription.id;
    }

    public static void addItem(String id, SubscriptionItem item) {
        Subscription subscription = Subscription.find().ref(id);
        subscription.items.add(item);
        subscription.update();
    }

    public static void changeEdition(String id, String edition) {
        Subscription subscription = Subscription.find().ref(id);
        subscription.edition = edition;
        subscription.update();
    }

    public static void changeStatus(String id, String status) {
        Subscription subscription = Subscription.find().ref(id);
        subscription.status = status.toUpperCase();
        subscription.update();
    }


    public enum Status {FREE_TRIAL, ACTIVE, FREE_TRIAL_EXPIRED, SUSPENDED, CANCELLED}
}
