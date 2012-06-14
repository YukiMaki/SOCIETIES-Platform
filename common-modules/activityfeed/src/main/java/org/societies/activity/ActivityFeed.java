package org.societies.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import org.apache.shindig.social.core.model.ActivityEntryImpl;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.model.Activity;
import org.societies.activity.model.ActivityString;
import org.societies.api.activity.IActivity;
import org.societies.api.activity.IActivityFeed;
import org.societies.api.comm.xmpp.pubsub.Subscriber;
import org.societies.api.identity.IIdentity;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "org_societies_activity_ActivityFeed")
public class ActivityFeed implements IActivityFeed, Subscriber {
	/**
	 * 
	 */
	
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
	
	//timeperiod: "millisecondssinceepoch millisecondssinceepoch+n" 
	//where n has to be equal to or greater than 0
	@Override
	public List<IActivity> getActivities(String timePeriod) {
		ArrayList<IActivity> ret = new ArrayList<IActivity>();
		String times[] = timePeriod.split(" ",2);
		if(times.length < 2){
			//throw exception ?
			return ret;
		}
		long fromTime = 0;long toTime = 0;
		try{
			fromTime = Long.parseLong(times[0]);
			toTime = Long.parseLong(times[0]);
		}catch(Exception e){
			
		}
		
		for(Activity act : list){
			if(act.getTime()>=fromTime && act.getTime()<=toTime){
				ret.add(act);
			}
		}
		return ret;
	}
	//query can be e.g. 'object,contains,"programming"'
	//TODO: Needs to support specifying that a attribute needs to empty!
	@Override
	public List<IActivity> getActivities(String query, String timePeriod) {
		ArrayList<IActivity> ret = new ArrayList<IActivity>();
		List<IActivity> tmp = this.getActivities(timePeriod);
		if(tmp.size()==0) return ret;
		//start parsing query..
		JSONObject arr = null;
		try {
			arr = new JSONObject(query);
		} catch (JSONException e) {
			e.printStackTrace();
			return ret;
		}
		String methodName; String filterBy; String filterValue;
		try {
			methodName = arr.getString("filterOp");
			filterBy = arr.getString("filterBy");
			filterValue = arr.getString("filterValue");
		} catch (JSONException e1) {
			e1.printStackTrace();
			return ret;
		}

		java.lang.reflect.Method method = null;
		try {
			method = ActivityString.class.getMethod(methodName, String.class);
		} catch (SecurityException e) {
			return ret;
		} catch (NoSuchMethodException e) {
			return ret;
		}
		//filter..
		try {
			for(IActivity act : tmp){
				if((Boolean)method.invoke(((Activity)act).getValue(filterBy),filterValue) ){
					ret.add(act);
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
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
	public int cleanupFeed(String criteria) {
		int ret = 0;
		String forever = "0 "+Long.toString(System.currentTimeMillis());
		List<IActivity> toBeDeleted = getActivities(criteria,forever);
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		try{
			for(IActivity act : toBeDeleted){
				this.list.remove(act);
				session.delete((Activity)act);
			}
		}catch(Exception e){
			t.rollback();
			log.warn("deleting activities failed, rolling back");
		}
		return ret;
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
		log.info("in activityfeed close");
	}
	@Override
	public void pubsubEvent(IIdentity pubsubService, String node,
			String itemId, Object item) {
		if(item.getClass().equals(Activity.class)){
			Activity act = (Activity)item;
			this.addCisActivity(act);
		}
	}
	@Override
	public List<IActivity> getActivities(String CssId, String query,
			String timePeriod) {
		return this.getActivities(query,timePeriod);
	}
	@Override
	public boolean deleteActivity(IActivity activity) {
		// x
		return false;
	}
}
