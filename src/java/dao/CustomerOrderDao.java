
package dao;

import domain.Company;
import domain.Customer;
import domain.CustomerOrder;
import domain.Deliverer;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class CustomerOrderDao extends GenericDao<CustomerOrder>{
    public List<CustomerOrder> findByCompany(Company c, String y){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM CustomerOrder a WHERE a.cart.item.company = :x and a.status = :y GROUP BY a.orderNo");
        q.setParameter("x", c);
        q.setParameter("y", y);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
    
    public List<CustomerOrder> findByDeliverer(Deliverer c){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM CustomerOrder a WHERE a.deliverer = :x");
        q.setParameter("x", c);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
    
    
    public List<CustomerOrder> findByCustomer(Customer c){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM CustomerOrder a WHERE a.customer = :x GROUP BY a.orderNo");
        q.setParameter("x", c);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
    
    
    public List<CustomerOrder> findByOrderNo(String c){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM CustomerOrder a WHERE a.orderNo = :x");
        q.setParameter("x", c);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
}
