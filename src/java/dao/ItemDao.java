
package dao;

import domain.Company;
import domain.CustomerOrder;
import domain.Item;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class ItemDao extends GenericDao<Item>{
    public List<CustomerOrder> findByCompany(Company c){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM CustomerOrder a WHERE a.cart.item.company = :x GROUP BY a.orderNo");
        q.setParameter("x", c);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
    
}
