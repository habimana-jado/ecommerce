
package dao;

import domain.Company;
import domain.Deliverer;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class DelivererDao extends GenericDao<Deliverer>{
    public List<Deliverer> findByCompany(Company iq){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from Deliverer a where a.company = :v");
        q.setParameter("v", iq);
        List<Deliverer> list = q.list();
        s.close();
        return list;
     }
}
