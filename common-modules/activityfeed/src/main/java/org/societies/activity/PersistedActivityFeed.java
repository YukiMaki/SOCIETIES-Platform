/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp., 
 * INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
 * ITALIA S.p.a.(TI),  TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.societies.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.ActivityObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.model.Activity;
import org.societies.activity.model.ActivityString;
import org.societies.api.activity.IActivity;
import org.societies.api.activity.IActivityFeed;
import org.societies.api.activity.IActivityFeedCallback;
import org.societies.api.comm.xmpp.pubsub.Subscriber;
import org.societies.api.identity.IIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PersistedActivityFeed extends ActivityFeed implements IActivityFeed, Subscriber {
	/**
	 * 
	 */
	

	//timeperiod: "millisecondssinceepoch millisecondssinceepoch+n" 
	//where n has to be equal to or greater than 0
	@Override
	public List<IActivity> getActivities(String timePeriod) {
        LOG.info("in persisted activityfeed getActivities, gettin data from DB.");
		ArrayList<IActivity> ret = new ArrayList<IActivity>();
		String times[] = timePeriod.split(" ",2);
		if(times.length < 2){
			LOG.error("timeperiod string was malformed: "+timePeriod);
			return ret;
		}
		long fromTime = 0;long toTime = 0;
		try{
			fromTime = Long.parseLong(times[0]);
			toTime = Long.parseLong(times[1]);
		}catch(Exception e){
			LOG.error("time period string was malformed, could not parse long");
			return ret;
		}
		List<Activity> l = session.createCriteria(Activity.class)
			.add(
					Restrictions.between("time", new Long(fromTime-1), new Long(toTime+1))
					)
					.list();
		LOG.info("time period: "+fromTime+" - " + toTime);
		if(list != null){
			LOG.info(" list size: "+list.size());
			for(Activity act : list){
				if(Long.parseLong(act.getPublished())>=fromTime && Long.parseLong(act.getPublished())<=toTime){
					ret.add(act);
				}
			}
				
		}
		return ret;
	}

	@Override
	public void addActivity(IActivity activity) {

		Transaction t = session.beginTransaction();
		Activity newAct = new Activity(activity);
        newAct.setOwnerId(this.id);
		try{
			session.save(newAct);
			t.commit();
		}catch(Exception e){
			e.printStackTrace();
			t.rollback();
			LOG.warn("Saving activity failed, rolling back");
			e.printStackTrace();
		}finally{

		}		
	}


    @Override
	synchronized public int cleanupFeed(String criteria) {
		int ret = 0;
		String forever = "0 "+Long.toString(System.currentTimeMillis());
		List<IActivity> toBeDeleted = getActivities(criteria,forever);
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		try{
			for(IActivity act : toBeDeleted){
				session.delete((Activity)act);
			}
		}catch(Exception e){
			t.rollback();
			LOG.warn("deleting activities failed, rolling back");
		}
		return ret;
	}

	public void init()
	{
		LOG.info("in activityfeed init");
	}
	
	public void close()
	{
		LOG.info("in activityfeed close");
	}
	@Override
	synchronized public void pubsubEvent(IIdentity pubsubService, String node,
			String itemId, Object item) {
		if(item.getClass().equals(Activity.class)){
			Activity act = (Activity)item;
			this.addActivity(act);
		}
	}
	@Override
	synchronized public List<IActivity> getActivities(String CssId, String query,
			String timePeriod) {
		return this.getActivities(query,timePeriod);
	}
	@Override
	public boolean deleteActivity(IActivity activity) {
		if(!list.contains(activity))
			return false;
		boolean ret = list.remove(activity);
		Transaction t = session.beginTransaction();
		session.delete(activity);
		return ret;
	}
	@Override
	synchronized public long importActivityEntries(List<?> activityEntries) {
		long ret = 0;
		if(activityEntries.size() == 0){
			LOG.error("list is empty, exiting");
			return ret;
		}
		if(!ActivityEntry.class.isInstance(activityEntries.get(0))){ //just checking the first entry.
			LOG.error("first instance in the given list is not of type ActivityEntry, exiting");
			return ret;
		}
		List<ActivityEntry> castedList = (List<ActivityEntry>) activityEntries;
		Activity newAct = null;
		Transaction t = session.beginTransaction();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		ParsePosition pp = new ParsePosition(0);
		try{
			for(ActivityEntry act : castedList){
				pp.setIndex(0);
				newAct = new Activity();
				newAct.setActor(getContentIfNotNull(act.getActor()));
				newAct.setOwnerId(this.id);
				newAct.setObject(getContentIfNotNull(act.getObject()));
				newAct.setPublished(Long.toString(df.parse(act.getPublished(),pp).getTime()));
				newAct.setTarget(getContentIfNotNull(act.getTarget()));
				newAct.setVerb(act.getVerb());
				ret++;
				session.save(newAct);
			}
			t.commit();
		}catch(Exception e){
			t.rollback();
			LOG.warn("Importing of activities from social data failed..");
			e.printStackTrace();

		}finally{
		}

		return ret;
	}
	public String getContentIfNotNull(ActivityObject a){
		if(a == null) return null;
		if(a.getObjectType().contains("person"))
			return a.getDisplayName();
		if(a.getObjectType().contains("note"))
			return "note";
		if(a.getObjectType().contains("bookmark"))
			return a.getUrl();
		return a.getContent();
	}
	public void clear(){
		for(Activity act : list){
			Transaction t = session.beginTransaction();
			session.delete(act);
			t.commit();
		}
		list.clear();
	}
}
