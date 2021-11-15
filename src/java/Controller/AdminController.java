
package Controller;

import Common.FileUpload;
import dao.CustomerOrderDao;
import dao.DelivererDao;
import dao.ItemDao;
import dao.ItemImageDao;
import domain.CustomerOrder;
import domain.Deliverer;
import domain.Item;
import domain.ItemImage;
import domain.UserX;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class AdminController {
    
    private Item item = new Item();

    private List<String> choosenImage = new ArrayList<>();

    private List<Item> items = new ArrayList<>();

    private List<ItemImage> itemImages = new ArrayList<>();

    private String phone = new String();

    private Deliverer deliverer = new Deliverer();

    private List<Deliverer> deliverers = new ArrayList<>();

    private UserX user = new UserX();

    private String password = new String();

    private UserX loggedInUser = new UserX();

    private ItemImage chosenItemImage = new ItemImage();

    private List<CustomerOrder> customerOrders = new ArrayList<>();

    private List<CustomerOrder> detailedOrders = new ArrayList<>(); 
    
    private String searchName = new String();
    
    private CustomerOrder chosenCustomerOrder = new CustomerOrder();
    
    
    @PostConstruct
    public void init() {
        userInit();
        companyProductImageInit();
    }

    public void companyProductImageInit() {
        itemImages = new ItemImageDao().FindAll(ItemImage.class);
        deliverers = new DelivererDao().FindAll(Deliverer.class);
        customerOrders = new CustomerOrderDao().FindAll(CustomerOrder.class);        
    }
    
    public void userInit() {
        loggedInUser = (UserX) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
    }

    public void registerItem() {
        if (choosenImage.isEmpty()) {
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Upload Product Images"));
        } else {
//            userInit();
//            item.setCompany(loggedInUser.getCompany());
            new ItemDao().register(item);
            for (String x : choosenImage) {
                ItemImage itemImage = new ItemImage();
                itemImage.setImage(x);
                itemImage.setItem(item);
                new ItemImageDao().register(itemImage);
            }
            choosenImage.clear();
            itemImages = new ItemImageDao().FindAll(ItemImage.class);
            items = new ItemDao().FindAll(Item.class);

            item = new Item();

            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Product Registered"));
        }
    }

    public void updateItem(ItemImage im) {
        new ItemDao().Update(im.getItem());

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Product Updated"));
    }

    public void Upload(FileUploadEvent event) {
        choosenImage.add(new FileUpload().Upload(event, "C:\\Users\\nizey\\OneDrive\\Documents\\NetBeansProjects\\Market\\com.bestabc.ecommerce\\web\\uploads\\item\\"));
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<String> getChoosenImage() {
        return choosenImage;
    }

    public void setChoosenImage(List<String> choosenImage) {
        this.choosenImage = choosenImage;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<ItemImage> getItemImages() {
        return itemImages;
    }

    public void setItemImages(List<ItemImage> itemImages) {
        this.itemImages = itemImages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }

    public List<Deliverer> getDeliverers() {
        return deliverers;
    }

    public void setDeliverers(List<Deliverer> deliverers) {
        this.deliverers = deliverers;
    }

    public UserX getUser() {
        return user;
    }

    public void setUser(UserX user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserX getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserX loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ItemImage getChosenItemImage() {
        return chosenItemImage;
    }

    public void setChosenItemImage(ItemImage chosenItemImage) {
        this.chosenItemImage = chosenItemImage;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<CustomerOrder> getDetailedOrders() {
        return detailedOrders;
    }

    public void setDetailedOrders(List<CustomerOrder> detailedOrders) {
        this.detailedOrders = detailedOrders;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public CustomerOrder getChosenCustomerOrder() {
        return chosenCustomerOrder;
    }

    public void setChosenCustomerOrder(CustomerOrder chosenCustomerOrder) {
        this.chosenCustomerOrder = chosenCustomerOrder;
    }
    
    

}
