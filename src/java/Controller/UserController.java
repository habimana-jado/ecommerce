package Controller;

import dao.UserDao;
import domain.UserX;
import java.io.IOException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UserController {

    private UserX user = new UserX();

    private UserX userDetails = new UserX();

    private UserDao userDao = new UserDao();

    private List<UserX> users;

    private String username = new String();

    private String password = new String();

    private String userdetails = new String();

    private String sid = new String();

    private String sectid = new String();

    private UserX u = new UserX();

    public String login2() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
        ec.redirect(ec.getRequestContextPath() + "/faces/pages/admin/products.xhtml");
        return "faces/pages/admin/products.xhtml?faces-redirect=true";
    }

    public String login() throws IOException, Exception {
        findUser();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (user != null) {

            switch (user.getAccess()) {
                case "Admin":
                    System.out.println("Foudn1");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/admin/products.xhtml");
                    return "faces/pages/admin/products.xhtml?faces-redirect=true";
                case "SalesManager":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/deliverer/pendingorder.xhtml");
                    return "faces/pages/deliverer/pendingorder.xhtml?faces-redirect=true";
                case "CompanyAdmin1":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/customer/single.xhtml");
                    return "faces/pages/customer/single.xhtml?faces-redirect=true";
                case "Block":
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your Account is not activated"));
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/index.xhtml");
                    return "faces/pages/index.xhtml";
                default:
                    user = null;

                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/index.xhtml");

                    return "/madeinrw/faces/pages/index.xhtml";
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Wrong Password Or Username"));
            ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
            return "faces/index.xhtml";
        }

    }

    public void findUser() throws Exception {
        List<UserX> usersLogin = new UserDao().loginencrypt(username, password);

        if (!usersLogin.isEmpty()) {
            for (UserX u : usersLogin) {
                user = u;
                System.out.println("Found");
            }
        } else {
            user = null;
        }
    }

    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        user = null;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
    }

    public UserX getUser() {
        return user;
    }

    public void setUser(UserX user) {
        this.user = user;
    }

    public UserX getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserX userDetails) {
        this.userDetails = userDetails;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserX> getUsers() {
        return users;
    }

    public void setUsers(List<UserX> users) {
        this.users = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(String userdetails) {
        this.userdetails = userdetails;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSectid() {
        return sectid;
    }

    public void setSectid(String sectid) {
        this.sectid = sectid;
    }

    public UserX getU() {
        return u;
    }

    public void setU(UserX u) {
        this.u = u;
    }

}
