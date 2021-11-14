
package Controller;

import dao.CustomerOrderDao;
import dao.DelivererDao;
import dao.ItemDao;
import dao.ItemImageDao;
import domain.CustomerOrder;
import domain.Deliverer;
import domain.Item;
import domain.ItemImage;
import domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

    private User user = new User();

    private String password = new String();

    private User loggedInUser = new User();

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
        loggedInUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
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
    
    

}
