
package dao;

import Common.PassCode;
import domain.UserX;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserDao extends GenericDao<UserX>{
    
    public List<UserX> login(String u,String password){
        Session s=HibernateUtil.getSessionFactory().openSession();
        Query q=s.createQuery("select a from UserX a where a.username=:v and a.password=:p");
        q.setParameter("v", u);
        q.setParameter("p", password);
        List<UserX> l=q.list();
        return l;
    } 
    
    public List<UserX> loginencrypt(String u, String pass) throws Exception {

        Session s = HibernateUtil.getSessionFactory().openSession();
        List<UserX> list = new ArrayList<>();

        List<UserX> users = new UserDao().FindAll(UserX.class);
        String z = "";
        for (UserX us : users) {
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
