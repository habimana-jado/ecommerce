package Controller;

import dao.CartDao;
import dao.CustomerDao;
import dao.CustomerOrderDao;
import dao.ItemDao;
import dao.ItemImageDao;
import dao.UserDao;
import domain.Cart;
import domain.Customer;
import domain.CustomerOrder;
import domain.Item;
import domain.ItemImage;
import domain.UserX;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CustomerController {

    private UserX loggedInUser = new UserX();

    private int orderQuantity = 0;

    private ItemImage chosenItemImage = new ItemImage();

    private List<Cart> customerCart = new ArrayList<>();

    private List<ItemImage> itemImages = new ArrayList<>();

    private List<CustomerOrder> ownCustomerOrders = new ArrayList<>();

    private List<CustomerOrder> customerOrders = new ArrayList<>();

    private String msg = new String();

    private double totalPrice = 0.0;

    private double totalPriceInUsd = 0.0;

    private Customer loggedInCustomer = new Customer();

    private CustomerOrder chosenCustomerOrder = new CustomerOrder();
    
    @PostConstruct
    public void init() {
        customerCartInit();
    }

    public void updateAccount() {
        new UserDao().Update(loggedInUser);
    }

    public void updateProfile() {
        new CustomerDao().Update(loggedInCustomer);
    }
    
    public String populateOrder(CustomerOrder order){
        chosenCustomerOrder = order;
        return "claim.xhtml?faces-redirect=true";
    }

    public void registerRefund() {
        chosenCustomerOrder.setStatus("RefundClaimed");
        new CustomerOrderDao().Update(chosenCustomerOrder);
        ownCustomerOrders = new CustomerOrderDao().findByCustomer(loggedInUser.getCustomer());

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Claim Submitted"));
    }

    public void updateCart(Cart cart) {
        new CartDao().Update(cart);
        calculateTotal();
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Cart Updated"));
    }

    public void customerCartInit() {
        userInit();
        customerCart = new CartDao().findByCustomer(loggedInUser.getCustomer(), "Pending");
        ownCustomerOrders = new CustomerOrderDao().findByCustomer(loggedInUser.getCustomer());
        calculateTotal();
    }

    public void calculateTotal() {
        totalPrice = 0.0;
        for (Cart c : customerCart) {
            totalPrice = totalPrice + (c.getQuantity() * c.getItem().getUnitPrice());

        }
        totalPriceInUsd = totalPrice / 940;
    }

    public String exploreOrder(String orderNo) {
        customerOrders = new CustomerOrderDao().findByOrderNo(orderNo);
        return "explore.xhtml?faces-redirect=true";
    }

    public void userInit() {
        loggedInUser = (UserX) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        loggedInCustomer = loggedInUser.getCustomer();
    }

    public String navigateItem(ItemImage itemImage) {
        chosenItemImage = itemImage;
        itemImages = new ItemImageDao().findByItem(itemImage.getItem());
        return "single.xhtml?faces-redirect=true";
    }

    public String addToCart() {
        userInit();
        Cart cart = new Cart();
        cart.setQuantity(orderQuantity);
        cart.setItem(chosenItemImage.getItem());
        cart.setCustomer(loggedInUser.getCustomer());
        cart.setDueDate(new Date());
        new CartDao().register(cart);
        cart = new Cart();
        customerCartInit();

        return "mycart.xhtml?faces-redirect=true";
    }

    public String addToCart(ItemImage image) {
        if (orderQuantity > image.getItem().getQuantity()) {
            msg = "Insufficient Quantity Max Available = " + image.getItem().getQuantity();
            return msg;
        } else if (orderQuantity < 1) {
            msg = "Invalid quantity";
            return msg;
        } else {
            chosenItemImage = image;
            userInit();
            Cart cart = new Cart();
            cart.setQuantity(orderQuantity);
            cart.setItem(chosenItemImage.getItem());
            cart.setCustomer(loggedInUser.getCustomer());
            cart.setDueDate(new Date());
            cart.setStatus("Pending");
            new CartDao().register(cart);
            cart = new Cart();
            customerCartInit();

            return "mycart.xhtml?faces-redirect=true";
        }
    }

    public void removeProduct(Cart c) {
        new CartDao().Delete(c);
        customerCartInit();

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Product Removed from Cart"));
    }

    public String buyNow() {
        String orderNo = "order_" + UUID.randomUUID().toString().substring(1, 7);
        Item x = new Item();
        for (Cart c : customerCart) {
            CustomerOrder order = new CustomerOrder();
            order.setCart(c);
            order.setCustomer(c.getCustomer());
            order.setQuantity(c.getQuantity());
            order.setStatus("Pending");
            order.setOrderNo(orderNo);
            new CustomerOrderDao().register(order);
            c.setStatus("Completed");
            new CartDao().Update(c);
            //Updating remaining Item Quantity
            x = new ItemDao().FindOne(Item.class, c.getItem().getId());
            x.setQuantity(x.getQuantity() - c.getQuantity());
            new ItemDao().Update(x);
        }

        customerCartInit();
//        FacesContext ct = FacesContext.getCurrentInstance();
//        ct.addMessage(null, new FacesMessage("Order Placed, You will be contacted on delivery"));
        return "myorders.xhtml?faces-redirect=true";
    }

    public void completeCustomerOrder(CustomerOrder cust) {
        cust.setStatus("Completed");
        new CustomerOrderDao().Update(cust);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Customer Order Completed"));
    }

    public UserX getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserX loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public ItemImage getChosenItemImage() {
        return chosenItemImage;
    }

    public void setChosenItemImage(ItemImage chosenItemImage) {
        this.chosenItemImage = chosenItemImage;
    }

    public List<Cart> getCustomerCart() {
        return customerCart;
    }

    public void setCustomerCart(List<Cart> customerCart) {
        this.customerCart = customerCart;
    }

    public List<ItemImage> getItemImages() {
        return itemImages;
    }

    public void setItemImages(List<ItemImage> itemImages) {
        this.itemImages = itemImages;
    }

    public List<CustomerOrder> getOwnCustomerOrders() {
        return ownCustomerOrders;
    }

    public void setOwnCustomerOrders(List<CustomerOrder> ownCustomerOrders) {
        this.ownCustomerOrders = ownCustomerOrders;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPriceInUsd() {
        return totalPriceInUsd;
    }

    public void setTotalPriceInUsd(double totalPriceInUsd) {
        this.totalPriceInUsd = totalPriceInUsd;
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Customer loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public CustomerOrder getChosenCustomerOrder() {
        return chosenCustomerOrder;
    }

    public void setChosenCustomerOrder(CustomerOrder chosenCustomerOrder) {
        this.chosenCustomerOrder = chosenCustomerOrder;
    }

}
