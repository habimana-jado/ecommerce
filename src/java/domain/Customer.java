
package domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Customer implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String email;
    private String phone;
    private String location;
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Cart> cart;
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<CustomerOrder> customerOrder;
    
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private UserX user;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public UserX getUser() {
        return user;
    }

    public void setUser(UserX user) {
        this.user = user;
    }

    public List<CustomerOrder> getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(List<CustomerOrder> customerOrder) {
        this.customerOrder = customerOrder;
    }   
    
}
