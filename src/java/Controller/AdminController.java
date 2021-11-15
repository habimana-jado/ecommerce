package Controller;

import Common.FileUpload;
import Common.PassCode;
import dao.CompanyAdminDao;
import dao.CompanyDao;
import dao.ItemDao;
import dao.ItemImageDao;
import dao.UserDao;
import domain.Company;
import domain.CompanyAdmin;
import domain.Item;
import domain.ItemImage;
import domain.UserX;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private UserX user = new UserX();

    private String password = new String();

    private UserX loggedInUser = new UserX();

    private ItemImage chosenItemImage = new ItemImage();

    private Company company = new Company();

    private CompanyAdmin companyAdmin = new CompanyAdmin();
    
    private List<Company> allCompanies = new CompanyDao().FindAll(Company.class);

    @PostConstruct
    public void init() {
//        userInit();
        itemImages = new ItemImageDao().FindAll(ItemImage.class);
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

    public void registerCompany() {
        try {
            for (String x : choosenImage) {
                company.setImage(x);
            }
            choosenImage.clear();
            new CompanyDao().register(company);
            companyAdmin.setCompany(company);
            new CompanyAdminDao().register(companyAdmin);
            user.setAccess("CompanyAdmin");
            user.setStatus("Active");
            user.setCompanyAdmin(companyAdmin);
            user.setPassword(new PassCode().encrypt(password));
            new UserDao().register(user);

            user = new UserX();
            company = new Company();

            allCompanies = new CompanyDao().FindAll(Company.class);
            
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Company Registered"));

        } catch (Exception ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyAdmin getCompanyAdmin() {
        return companyAdmin;
    }

    public void setCompanyAdmin(CompanyAdmin companyAdmin) {
        this.companyAdmin = companyAdmin;
    }

    public List<Company> getAllCompanies() {
        return allCompanies;
    }

    public void setAllCompanies(List<Company> allCompanies) {
        this.allCompanies = allCompanies;
    }

}
