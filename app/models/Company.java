package models;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;


/**
 *
 * @author Saad Ahmed
 */

@Entity
@XmlAccessorType(XmlAccessType.PROPERTY)
@Table(name="companies")
public class Company extends Model {

    @Id
    public String uuid;

    @NotNull @NotEmpty
    public String email;

    @NotNull @NotEmpty
    public String name;

    public String phoneNumber;
    public String website;
    public String country;



    public Company(String uuid, String email, String name, String phoneNumber, String website, String country) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.country = country;
    }


    public static Finder<String, Company> find() {
        return new Finder<String, Company>(String.class, Company.class);
    }


    public static String create(String uuid, String email, String name, String phoneNumber, String website, String country) {
        Company company = new Company(uuid, email, name, phoneNumber, website, country);
        company.save();
        return uuid;
    }
}
