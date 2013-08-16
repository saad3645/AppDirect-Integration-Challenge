package models;


import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="user_attributes")
public class UserAttribute extends Model {

    @Id
    public Long id;

    @ManyToOne
    @NotNull
    public  User user;

    @NotNull @NotEmpty
    public String key;

    @NotNull @NotEmpty
    public String value;


    public UserAttribute(User user, String key, String value) {
        this.user = user;
        this.key = key;
        this.value = value;
    }


    public static Finder<Long, UserAttribute> find() {
        return new Finder<Long, UserAttribute>(Long.class, UserAttribute.class);
    }

    public static void create(User user, String key, String value) {
        UserAttribute attribute = new UserAttribute(user, key, value);
        attribute.save();
    }


}
