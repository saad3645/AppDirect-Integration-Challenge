package models.xml;

import play.db.ebean.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="order")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Order {

    private String editionCode;
    private String pricingDuration;
    private Item[] item;


    public String getEditionCode() {
        return editionCode;
    }

    public void setEditionCode(String editionCode) {
        this.editionCode = editionCode;
    }

    public String getPricingDuration() {
        return pricingDuration;
    }

    public void setPricingDuration(String pricingDuration) {
        this.pricingDuration = pricingDuration;
    }

    public Item[] getItem() {
        return item;
    }

    public void setItem(Item[] item) {
        this.item = item;
    }
}
