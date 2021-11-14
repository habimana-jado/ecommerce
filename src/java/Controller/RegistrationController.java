package Controller;

import Common.FileUpload;
import dao.CompanyDao;
import dao.CustomerDao;
import dao.UserDao;
import domain.Company;
import domain.Customer;
import domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@SessionScoped
@ManagedBean
public class RegistrationController {

    private Company company = new Company();

    private Customer customer = new Customer();

    private String custPassword = new String();

    private String compPassword = new String();

    private User user = new User();

    private List<String> choosenImage = new ArrayList<>();

    private String compName = new String();

    private String compTin = new String();

    private String compEmail = new String();

    public void registerCompany() {
        try {
            if (choosenImage.isEmpty()) {

                FacesContext ct = FacesContext.getCurrentInstance();
                ct.addMessage(null, new FacesMessage("Upload Company's Image"));
            } else {
                for (String x : choosenImage) {
                    company.setImage(x);
                }
                choosenImage.clear();
                company.setTinNumber(compTin);
                company.setName(compName);
                company.setEmail(compEmail);
                new CompanyDao().register(company);
                user.setAccess("Company");
                user.setStatus("Active");
                user.setCompany(company);
                user.setPassword((compPassword));
                new UserDao().register(user);

                user = new User();
                company = new Company();

                FacesContext ct = FacesContext.getCurrentInstance();
                ct.addMessage(null, new FacesMessage("Company Registered Now"));
            }
        } catch (Exception ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registerCustomer() {
        try {
            new CustomerDao().register(customer);
            user.setAccess("Customer");
            user.setStatus("Active");
            user.setCustomer(customer);
            user.setPassword((custPassword));
            new UserDao().register(user);

            user = new User();
            customer = new Customer();

            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Customer Registered Now"));

        } catch (Exception ex) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Upload(FileUploadEvent event) {
        choosenImage.add(new FileUpload().Upload(event, "C:\\Users\\nizey\\OneDrive\\Documents\\NetBeansProjects\\Thesis\\madeinrw\\web\\uploads\\company\\"));
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompPassword() {
        return compPassword;
    }

    public void setCompPassword(String compPassword) {
        this.compPassword = compPassword;
    }

    public List<String> getChoosenImage() {
        return choosenImage;
    }

    public void setChoosenImage(List<String> choosenImage) {
        this.choosenImage = choosenImage;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustPassword() {
        return custPassword;
    }

    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompTin() {
        return compTin;
    }

    public void setCompTin(String compTin) {
        this.compTin = compTin;
    }

    public String getCompEmail() {
        return compEmail;
    }

    public void setCompEmail(String compEmail) {
        this.compEmail = compEmail;
    }

}
