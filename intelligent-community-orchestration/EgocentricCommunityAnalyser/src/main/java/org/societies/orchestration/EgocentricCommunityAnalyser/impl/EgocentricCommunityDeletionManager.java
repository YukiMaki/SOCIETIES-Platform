/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske držbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOAÇÃO, SA (PTIN), IBM Corp., 
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

package org.societies.orchestration.EgocentricCommunityAnalyser.impl;

import static org.mockito.Mockito.*;

import org.societies.orchestration.api.ISuggestedCommunityAnalyser;

import org.societies.api.internal.css.directory.ICssDirectory;

import org.societies.api.internal.css.discovery.ICssDiscovery;

import org.societies.api.internal.cis.management.ICisActivityFeed;
import org.societies.api.internal.cis.management.ServiceSharingRecord;
import org.societies.api.internal.cis.management.ICisActivity;
import org.societies.api.internal.cis.management.ICisRecord;
import org.societies.api.internal.cis.management.ICisManager;

//import org.societies.api.cis.management.ICisRecord;
//import org.societies.api.cis.management.ICisManager;
//import org.societies.api.cis.management.ICisOwned;
//import org.societies.api.cis.management.ICisSubscribed;
//import org.societies.api.cis.management.ICisEditor;
//import org.societies.api.cis.management.ICisActivity;
//import org.societies.api.cis.management.ICisActivityFeed;
//import org.societies.api.cis.management.ICis;

import org.societies.api.internal.css.management.CSSRecord;
import org.societies.api.internal.css.management.ICssActivity;
import org.societies.api.internal.css.management.ICssActivityFeed;
import org.societies.api.internal.css.management.ICSSLocalManager;
import org.societies.api.internal.css.management.ICSSManagerCallback;
import org.societies.api.internal.css.management.ICSSRemoteManager;

//import org.societies.api.internal.context.user.similarity.IUserCtxSimilarityEvaluator;

//import org.societies.api.internal.context.user.prediction.IUserCtxPredictionMgr;

//import org.societies.api.internal.context.user.db.IUserCtxDBMgr;

//import org.societies.api.internal.context.user.history.IUserCtxHistoryMgr;

import org.societies.api.internal.context.broker.ICtxBroker;
//import org.societies.api.internal.context.broker.IUserCtxBroker;
//import org.societies.api.internal.context.broker.ICommunityCtxBroker;
//import org.societies.api.internal.context.broker.IUserCtxBrokerCallback;

import org.societies.api.context.model.CtxModelType;
import org.societies.api.context.model.CtxIdentifier;

import org.societies.api.identity.IIdentity;
//import org.societies.api.comm.xmpp.datatypes.Identity;
//import org.societies.comm.examples.commsmanager.impl.CommsServer; 
//import org.societies.comm.xmpp.interfaces.ICommCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



//import org.societies.api.internal.useragent.feedback.IUserFeedbackCallback;
import org.societies.api.internal.useragent.feedback.IUserFeedback;
import org.societies.api.internal.useragent.feedback.IUserFeedbackCallback;


import org.societies.api.internal.useragent.model.ExpProposalContent;

/**
 * This is the class for the Egocentric Community Deletion Manager component
 * 
 * @author Fraser Blackmun
 * @version 0
 * 
 * The component is responsible for automating, and triggering the process of 
 * suggesting to one or more relevant CSSs, the deletion of CISs. This 
 * is achieved by perform various forms of analysis on CSSs, CISs, their attributes, and their
 * connections, and using different algorithms. Social network analysis methods and similarity of users
 * -based approaches and algorithms will be used, including an
 * approach that views groups/CISs as either ongoing (non-terminating, with no deadline or 
 * fulfillable purpose for existing) or temporary (not going to last, e.g. because it exists just
 * for a goal that will be completed, or has a clear lifespan, or group breakdown is inevitable). 
 * 
 */

public class EgocentricCommunityDeletionManager //implements ICommCallback
{

	private IIdentity linkedCss;
	
    private ICisRecord linkedCis;
    
    //private Domain linkedDomain;  // No datatype yet representing a domain
	//private IIdentity linkedDomain;
	
	private int longestTimeWithoutActivity; //measured in minutes
	
	private ICtxBroker userContextBroker;
	//private IUserCtxDBMgr userContextDatabaseManager;
	//private IUserCtxBroker userContextBroker;
	//private ICommunityCtxBroker communityContextBroker;
	//private IUserCtxBrokerCallback userContextBrokerCallback;
	private ICisManager cisManager;
	private ICSSLocalManager cssManager;
	private ICssActivityFeed activityFeed;
	
	private IUserFeedback userFeedback;
	//private IUserFeedbackCallback userFeedbackCallback;
	private String userResponse;
	
	private ArrayList<ICisRecord> recentRefusals;

	private IUserFeedbackCallback userFeedbackCallback;
	
	private ISuggestedCommunityAnalyser suggestedCommunityAnalyser;
	
	/*
     * Constructor for EgocentricCommunityConfigurationManager
     * 
	 * Description: The constructor creates the EgocentricCommunityConfigurationManager
	 *              component on a given CSS.
	 * Parameters: 
	 * 				linkedEntity - the non-CIS entity, either a user CSS or a domain deployment,
	 *              that this object will operate on behalf of.
	 */
	
	public EgocentricCommunityDeletionManager(IIdentity linkedEntity, String linkType) {
		if (linkType.equals("CSS"))
			this.linkedCss = linkedEntity;
		//else
		//	this.linkedDomain = linkedEntity;
	}
	
	/*
     * Constructor for EgocentricCommunityDeletionManager
     * 
	 * Description: The constructor creates the EgocentricCommunityDeletionManager
	 *              component on a CIS, either at a domain/cloud level or for an administrating CSS.
	 * Parameters: 
	 * 				linkedCis - the CIS on behalf of which this object is to operate, i.e.
	 *                          continually checking for whether to delete it/suggest deleting it.
	 */
	
	public EgocentricCommunityDeletionManager(ICisRecord linkedCis) {
		this.linkedCis = linkedCis;
	}
	
	/*
	 * Description: The method looks for CISs to delete, using as a base the CIS records relevant
	 *              to this object's 'linked' component (see the fields). If the linked component
	 *              is just a CIS, it will only perform the check on that CIs. If the linked component
	 *              is a CSS, it will check all CISs they administrate. If the linked component is 
	 *              a domain, the check is done on all CISs in that domain.
	 */
	
	public void identifyCissToDelete(HashMap <IIdentity, String> userCissMetadata) {
		
		String[] it = new String[1];
		linkedCss = mock(IIdentity.class);
		cisManager = mock(ICisManager.class);
		cssManager = mock(ICSSLocalManager.class);
		activityFeed = mock(ICssActivityFeed.class);
		
		it[0] = linkedCss.getIdentifier();
		//ICisRecord[] listOfUserJoinedCiss = cisManager.getCisList(new ICisRecord(null, null, null, null, null, it, null, null, null));
		ICisRecord[] listOfUserJoinedCiss = new ICisRecord[0];
		ArrayList<ICisRecord> userJoinedCiss = new ArrayList<ICisRecord>();
		
		
		ICisRecord[] records;
		if (linkedCss != null && listOfUserJoinedCiss != null) {
			for (int i = 0; i < listOfUserJoinedCiss.length; i++) {
			    userJoinedCiss.add(listOfUserJoinedCiss[i]);   
			}
			//records = cisManager.getCisList(new CisRecord(null, linkedCss.toString(), null, null, null, null, null, null)/** CISs administrated by the CSS */);
		}
		if (linkedCis != null) {
			userJoinedCiss.add(linkedCis);
		}
		//if (linkedDomain != null) {
			//records = cisManager.getCisList(new CisRecord(null, linkedDomain.toString(), null, null, null, null, null, null));
			//CisRecord[] records = ICisManager.getCisList(/** CISs in the domain */);
		//}
		
		ArrayList<ICisRecord> cissToDelete = new ArrayList<ICisRecord>();
		
		for (int i = 0; i < userJoinedCiss.size(); i++) {
			ICisRecord thisCis = userJoinedCiss.get(i);
			String deadFeed = null;
			//deadFeed = (thisCis.feed.getActivities(it[0], "between now and 2 hours ago"));
		    if (deadFeed == null) {
		//    if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - (longestTimeWithoutActivity/1440)) {
		        deadFeed = null;
		        //deadFeed = thisCis.feed.getActivities(it[0], "CommunityLifecycleManagement metadata: temporary short-term", "forever")
			    if (deadFeed == null) {
	                cissToDelete.add(thisCis);
		        }
			    //could also check changed location on location-defined CIS and other things
		    }
		    
		  //deadFeed = (thisCis.feed.getActivities(it[0], "between now and 2 weeks ago"));
		    if (deadFeed == null) {
		//    if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - (longestTimeWithoutActivity/1440)) {
		        deadFeed = null;
		        //deadFeed = thisCis.feed.getActivities(it[0], "CommunityLifecycleManagement metadata: temporary medium-term", "forever")
			    if (deadFeed == null) {
	                cissToDelete.add(thisCis);
		        }
		    }
		    
		  //deadFeed = (thisCis.feed.getActivities(it[0], "between now and 6 months ago"));
		    if (deadFeed == null) {
		//    if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - (longestTimeWithoutActivity/1440)) {
		        deadFeed = null;
		        //deadFeed = thisCis.feed.getActivities(it[0], "CommunityLifecycleManagement metadata: temporary long-term", "forever")
			    if (deadFeed == null) {
	                cissToDelete.add(thisCis);
		        }
		    }
		//if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - 1 week) {
				//    if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - (longestTimeWithoutActivity/1440)) {
				//        if (theCisRecord.getActivityFeed().search("CommunityLifecycleManagement metadata: temporary medium-term)) {
			    //            cissToDelete.add(theCisRecord);
				//        }
		//if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - 18 months) {
				//    if (theCisRecord.getActivityFeed().getHistory().latestDate() <= Date.timestamp() - (longestTimeWithoutActivity/1440)) {
				//        if (theCisRecord.getActivityFeed().search("CommunityLifecycleManagement metadata: temporary long-term)) {
			    //            cissToDelete.add(theCisRecord);
				//        }
		//        If the CIS has never gone such a long period without activity before, 
		//        suggest deletion via User Agent to CIS owner/administrators, i.e. whoever
		//        this deployment runs on behalf of.
		//  
		//        Date date= new java.util.Date();
		//        System.out.println(new Timestamp(date.getTime()));
        //        ^Above 2 lines would produce something of this format: 
		//           2010-03-08 14:59:30.252
		//
		//        Future directions here can include - being able to identify CISs to delete very soon,
		//        or at more flexible time than just the 5 days as above,
		//        after lack of activity or other key event e.g. purpose fulfillment or location change.
		
		//    }
		//}
		}
		
		//Can't use GUI in tests
        //cissToDelete = getUserFeedbackOnDeletion(cissToDelete);
		
		//for (int i = 0; i < cissToDelete.size(); i++)
		//	cisManager.deleteCis(linkedCss.getIdentifier(), cissToDelete.get(i).getCisId());
		
		
		
		
	}
	
	public ArrayList<ICisRecord> getUserFeedbackOnDeletion(ArrayList<ICisRecord> cissToDelete) {
		ArrayList<ICisRecord> realCissToDelete = new ArrayList<ICisRecord>();
		List<String> options = new ArrayList<String>();
		options.add("options");
		userResponse = null;
		boolean responded = false;
		//userFeedback.getExplicitFB(0,  new ExpProposalContent("SOCIETIES suspects these CISs may be obsolete. If you would like to delete one or more of these CISs, please check them.", options), userFeedbackCallback);
		for (int i = 0; i < 300; i++) {
		    if (userResponse == null)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
			    responded = true;
		}
		
		if (responded == false) {
		    //User obviously isn't paying attention to CSS, so put the message in the background/list of messages for them to see at their leisure.
		    String background = "This message is in your inbox or something, waiting for you to read it";
		}
		else {
		   	Iterator<ICisRecord> iterator = cissToDelete.iterator();
			while (iterator.hasNext()) {
			    ICisRecord potentiallyDeletableCis = iterator.next();
		        if (userResponse.equals("Yes")) {
				    realCissToDelete.add(potentiallyDeletableCis);
			       // cisManager.deleteCis(linkedCss, potentiallyDeletableCis.getCisId());
		        }
		        else {
		    	    recentRefusals.add(potentiallyDeletableCis);
		    	    //store as context the CIS is marked as one level up on the Ongoing/Temporary chain
		    	    //(short-term temporary becomes medium-term, becomes long-term, becomes ongoing.
		    	    //Purely a mechanic for use by this service.
		        }
		   }
		}
		return realCissToDelete;
	}
	
    public void initialiseEgocentricCommunityDeletionManager() {
    	//getCommManager().register(this);

    	new EgocentricCommunityDeletionManager(linkedCss, "CSS");
    }

    public IIdentity getLinkedCss() {
    	return linkedCss;
    }
    
    public void setLinkedCss(IIdentity linkedCss) {
    	this.linkedCss = linkedCss;
    }
    
   /** public IIdentity getLinkedDomain() {
    	return linkedDomain;
    }
    
    public void setLinkedDomain(IIdentity linkedDomain) {
    	this.linkedDomain = linkedDomain;
    }*/
    
    /**
    public void setUserContextDatabaseManager(IUserCtxDBMgr userContextDatabaseManager) {
    	System.out.println("GOT database" + userContextDatabaseManager);
    	this.userContextDatabaseManager = userContextDatabaseManager;
    }*/
    
    public void setUserContextBroker(ICtxBroker userContextBroker) {
    	System.out.println("GOT user context broker" + userContextBroker);
    	this.userContextBroker = userContextBroker;
    }
    
    /**public void setUserContextBrokerCallback(ICtxBrokerCallback userContextBrokerCallback) {
    	System.out.println("GOT user context broker callback" + userContextBrokerCallback);
    	this.userContextBrokerCallback = userContextBrokerCallback;
    }
    
    public void setCommunityContextBroker(ICommunityCtxBroker communityContextBroker) {
    	System.out.println("GOT community context broker" + communityContextBroker);
    	this.communityContextBroker = communityContextBroker;
    }*/
    
    public void setCisManager(ICisManager cisManager){
		this.cisManager = cisManager;
	}
    
    public ICisManager getCisManager() {
    	return cisManager;
    }
    
    public ISuggestedCommunityAnalyser getSuggestedCommunityAnalyser() {
    	return suggestedCommunityAnalyser;
    }
    
    public void setSuggestedCommunityAnalyser(ISuggestedCommunityAnalyser suggestedCommunityAnalyser) {
    	this.suggestedCommunityAnalyser = suggestedCommunityAnalyser;
    }
    
    public IUserFeedback getUserFeedback() {
    	return userFeedback;
    }
    
    public void setUserFeedback(IUserFeedback userFeedback) {
    	this.userFeedback = userFeedback;
    }
    
    public IUserFeedbackCallback getUserFeedbackCallback() {
    	return userFeedbackCallback;
    }
    
    public void setUserFeedbackCallback(IUserFeedbackCallback userFeedbackCallback) {
    	this.userFeedbackCallback = userFeedbackCallback;
    }
    
    public void getUserResponse(String userResponse) {
    	this.userResponse = userResponse;
    }
    
  //public CommManagerBundle getCommManager() {
    //	return commManager;
    //}
    
    //public void setCommManager(CommManagerBundle commManager) {
    //	this.commManager = commManager;
    //}
    
    /**Returns the list of package names of the message beans you'll be passing*/
    public List<String> getJavaPackages() {
		return null;
    	
    }
    
    /**Returns the list of namespaces for the message beans you'll be passing*/
    public List<String> getXMLNamespaces() {
    	return null;
    }
    
    /** Put your functionality here if there is NO return object, ie, VOID */
    //public void receiveMessage(Stanza stanza, Object messageBean) {
    //	return null;
    //}
    
    /** Put your functionality here if there IS a return object */
    //public Object getQuery(Stanza stanza, Object messageBean) {
    //	return null;
    //}
    
    /** Put your functionality here if there IS a return object and you are updating also */
    //public Object setQuery(Stanza arg0, Object arg1) {
    //	return null;
    //}
    
}