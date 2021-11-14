
package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Cart implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();
    private int quantity;
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    private String status;
    
    @ManyToOne
    private Item item;

    @ManyToOne
    private Customer customer;
    
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Fetch(FetchMode.SUBSELECT)
    private List<CustomerOrder> customerOrder;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerOrder> getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(List<CustomerOrder> customerOrder) {
        this.customerOrder = customerOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
