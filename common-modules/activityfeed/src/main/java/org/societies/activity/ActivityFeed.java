package org.societies.activity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.model.Activity;
import org.societies.api.activity.IActivity;
import org.societies.api.activity.IActivityFeed;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "org_societies_activity_ActivityFeed")
public class ActivityFeed implements IActivityFeed {
	@Id
	private String id;
	@OneToMany(cascade=CascadeType.ALL)
	private
	Set<Activity> list;
	public ActivityFeed(){}
	public ActivityFeed(String id){
		this.id = id;
	}
	@Autowired 
	private static SessionFactory sessionFactory;
	private static Logger log = LoggerFactory.getLogger(ActivityFeed.class);
	
	
	@Override
	public void getActivities(String CssId, String timePeriod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getActivities(String CssId, String query, String timePeriod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCisActivity(IActivity activity) {

		//persist.
		Session session = sessionFactory.openSession();//getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		Activity newact = new Activity(activity);
		try{
			session.save(newact);
			t.commit();
		}catch(Exception e){
			t.rollback();
			log.warn("Saving activity failed, rolling back");
		}finally{
			if(session!=null)
				session.close();
		}		
	}

	@Override
	public void cleanupFeed(String criteria) {
		// TODO Auto-generated method stub
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public static SessionFactory getStaticSessionFactory() {
		return sessionFactory;
	}
	public static void setStaticSessionFactory(SessionFactory isessionFactory) {
		sessionFactory = isessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public static ActivityFeed startUp(String id){
		ActivityFeed ret = null;
		Session session = sessionFactory.openSession();//sessionFactory.getCurrentSession();
		log.info("using session: "+session.hashCode());
		try{
			Query q = session.createQuery("select a from ActivityFeed a");
			long l = q.list().size();
			System.out.println("l: "+l);
			if(l== 0)
				return new ActivityFeed(id);
			q = session.createQuery("select a from ActivityFeed a where a.id = ?");
			q.setString(0, id);
			ret = (ActivityFeed) q.uniqueResult();
		}catch(Exception e){
			log.warn("Query for actitvies failed..");

		}finally{
			if(session!=null)
				session.close();
		}
		return ret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Activity> getList() {
		return list;
	}

	public void setList(Set<Activity> list) {
		this.list = list;
	}
	public void init()
	{
		log.info("in activityfeed init");
	}
	
	public void close()
	{
		
	}
}
