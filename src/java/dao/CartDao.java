
package dao;

import domain.Cart;
import domain.Customer;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class CartDao extends GenericDao<Cart>{
    public List<Cart> findByCustomer(Customer iq, String x){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from Cart a where a.customer = :v and a.status = :x GROUP BY a.item.id");
        q.setParameter("v", iq);
        q.setParameter("x", x);
        List<Cart> list = q.list();
        s.close();
        return list;
    }
}
