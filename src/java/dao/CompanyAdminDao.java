
package dao;

import domain.Company;
import domain.CompanyAdmin;
import domain.ItemImage;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class CompanyAdminDao extends GenericDao<CompanyAdmin>{
    
    public List<CompanyAdmin> findByCompany(Company iq){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from CompanyAdmin a where a.company = :v");
        q.setParameter("v", iq);
        List<CompanyAdmin> list = q.list();
        s.close();
        return list;
     }
    
}
