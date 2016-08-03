package nl.mwinkels.photomanager.dao;


import nl.mwinkels.photomanager.data.Photo;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;


public class PhotoDao extends HibernateDaoSupport {

    private final DetachedCriteria criteria = DetachedCriteria.forClass(Photo.class).setFetchMode("faces", FetchMode.SELECT).addOrder(Order.asc("taken"));

    public PhotoDao(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Transactional
    public Photo persist(Photo photo) {
        super.getHibernateTemplate().save(photo);
        return photo;
    }

    public ScrollableResults scrollAll() {
        return super.getHibernateTemplate().executeWithNativeSession(s -> {
            Criteria executableCriteria = criteria.getExecutableCriteria(s);
            return executableCriteria.scroll(ScrollMode.FORWARD_ONLY);
        });
    }
}
