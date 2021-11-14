
package dao;

import domain.Company;
import domain.Item;
import domain.ItemImage;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class ItemImageDao extends GenericDao<ItemImage>{
    public List<ItemImage> findByItem(Item iq){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from ItemImage a where a.item=:v");
        q.setParameter("v", iq);
        List<ItemImage> list = q.list();
        s.close();
        return list;
     }
    
    public List<ItemImage> findByCompany(Company iq){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from ItemImage a where a.item.company = :v");
        q.setParameter("v", iq);
        List<ItemImage> list = q.list();
        s.close();
        return list;
     }
    
    public List<ItemImage> findByItemType(String iq){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from ItemImage a where a.item.type = :v and a.item.quantity != :d");
        q.setParameter("v", iq);
        q.setParameter("d", 0.0);
        List<ItemImage> list = q.list();
        s.close();
        return list;
     }
    
    public List<ItemImage> findByItemName(Company iq, String x){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from ItemImage a where a.item.company = :v and a.item.name = :t");
        q.setParameter("v", iq);
        q.setParameter("t", x);
        List<ItemImage> list = q.list();
        s.close();
        return list;
     }
}
