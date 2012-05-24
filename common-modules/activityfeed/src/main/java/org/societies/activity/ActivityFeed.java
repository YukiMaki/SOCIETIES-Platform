package org.societies.activity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.model.Activity;
import org.societies.api.activity.IActivity;
import org.societies.api.activity.IActivityFeed;
import org.societies.api.comm.xmpp.pubsub.Subscriber;
import org.societies.api.identity.IIdentity;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "org_societies_activity_ActivityFeed")
public class ActivityFeed implements IActivityFeed, Subscriber {
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
		Session session = sessionFactory.openSession();
		try{
			List l = session.createCriteria(ActivityFeed.class).add(Property.forName("id").eq(id)).list();
			if(l.size() == 0)
				return new ActivityFeed(id);
			if(l.size() > 1){
				log.error("activityfeed startup with id: "+id+" gave more than one activityfeed!! ");
				return null;
			}
			ret = (ActivityFeed) l.get(0);
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
	@Override
	public void pubsubEvent(IIdentity pubsubService, String node,
			String itemId, Object item) {
		if(item.getClass().equals(Activity.class)){
			Activity act = (Activity)item;
			this.addCisActivity(act);
		}
	}
}
