
package dao;

import Common.PassCode;
import domain.User;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserDao extends GenericDao<User>{
    
    public List<User> login(String u,String password){
        Session s=HibernateUtil.getSessionFactory().openSession();
        Query q=s.createQuery("select a from User a where a.username=:v and a.password=:p");
        q.setParameter("v", u);
        q.setParameter("p", password);
        List<User> l=q.list();
        return l;
    } 
    
    public List<User> loginencrypt(String u, String pass) throws Exception {

        Session s = HibernateUtil.getSessionFactory().openSession();
        List<User> list = new ArrayList<>();

        List<User> users = new UserDao().FindAll(User.class);
        String z = "";
        for (User us : users) {
            if (us.getUsername().matches(u)) {
                if ((new PassCode().decrypt(us.getPassword())).matches(pass)) {
                    list.add(us);
                }
            }

        }

        s.close();
        return list;

    }
    
}
