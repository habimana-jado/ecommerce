
package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Company implements Serializable{
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String domain;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date regDate = new Date();
    private String location;
    private String country_code;
    private String phone;
    private String email;
    private String image;
    private String tinNumber;
    
    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Item> item;
    
    @OneToOne(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserX user;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Deliverer> deliverer;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public UserX getUser() {
        return user;
    }

    public void setUser(UserX user) {
        this.user = user;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }  

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public List<Deliverer> getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(List<Deliverer> deliverer) {
        this.deliverer = deliverer;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
       
}
