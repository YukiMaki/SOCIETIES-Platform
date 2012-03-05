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

package org.societies.orchestration.CommunityLifecycleManagement.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static org.mockito.Mockito.*;

import org.societies.api.internal.css.directory.ICssDirectory;

import org.societies.api.internal.css.discovery.ICssDiscovery;

import org.societies.api.internal.cis.management.CisActivityFeed;
import org.societies.api.internal.cis.management.ServiceSharingRecord;
import org.societies.api.internal.cis.management.CisActivity;
import org.societies.api.internal.cis.management.CisRecord;
import org.societies.api.internal.cis.management.ICisManager;

//import org.societies.api.internal.context.user.similarity.IUserCtxSimilarityEvaluator;

//import org.societies.api.internal.context.user.prediction.IUserCtxPredictionMgr;

//import org.societies.api.internal.context.user.db.IUserCtxDBMgr;

//import org.societies.api.internal.context.user.history.IUserCtxHistoryMgr;

//import org.societies.api.internal.context.broker.IUserCtxBroker;
import org.societies.api.internal.context.broker.ICtxBroker;
//import org.societies.api.internal.context.broker.ICommunityCtxBroker;
//import org.societies.api.internal.context.broker.IUserCtxBrokerCallback;
import org.societies.api.internal.useragent.feedback.IUserFeedback;
import org.societies.api.internal.useragent.feedback.IUserFeedbackCallback;
import org.societies.api.internal.useragent.model.ExpProposalContent;

import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.context.model.CtxIdentifier;

import org.societies.api.identity.IIdentity;
//import org.societies.api.comm.xmpp.datatypes.Identity;
//import org.societies.comm.examples.commsmanager.impl.CommsServer; 
//import org.societies.comm.xmpp.interfaces.ICommCallback;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This is the class for the Automatic Community Creation Manager component.
 * 
 * The component is responsible for automating, and triggering the process of 
 * suggesting to one or more relevant CSSs, the creation of CISs and sub-CISs. This 
 * is achieved by perform various forms of analysis on CSSs, CISs, their attributes, and their
 * connections, and using different algorithms. Social network analysis methods and similarity of users
 * -based approaches and algorithms will be used, including an
 * approach that views groups/CISs as either ongoing (non-terminating, with no deadline or 
 * fulfillable purpose for existing) or temporary (not going to last, e.g. because it exists just
 * for a goal that will be completed, or has a clear lifespan, or group breakdown is inevitable). 
 * 
 * 
 * @author Fraser Blackmun
 * @version 0
 * 
 */

public class AutomaticCommunityCreationManager //implements ICommCallback
{
	
	private IIdentity linkedCss;
	
    private CisRecord linkedSuperCis;
    
	private IIdentity linkedDomain;
	
	private ICtxBroker userContextBroker;
	//private IUserCtxDBMgr userContextDatabaseManager;
	//private IUserCtxBroker userContextBroker;
	//private ICommunityCtxBroker communityContextBroker;
	//private IUserCtxBrokerCallback userContextBrokerCallback;
	private ArrayList<CisRecord> recentRefusals;
	private IUserFeedback userFeedback;
	private IUserFeedbackCallback userFeedbackCallback;
	
	private ICisManager cisManager;
    
	private ArrayList<CtxEntity> availableContextData;
	
	private ICssDirectory userCssDirectory;
    
	/*
     * Constructor for AutomaticCommunityConfigurationManager
     * 
	 * Description: The constructor creates the AutomaticCommunityConfigurationManager
	 *              component on a given CSS.
	 * Parameters: 
	 * 				linkedEntity - the non-CIS entity, either a user CSS or a domain deployment,
	 *              that this object will operate on behalf of.
	 */
	
	public AutomaticCommunityCreationManager(IIdentity linkedEntity, String linkType) {
		if (linkType.equals("CSS"))
			this.linkedCss = linkedEntity;
		else
			this.linkedDomain = linkedEntity;
	}
	
	/*
     * Constructor for AutomaticCommunityCreationManager
     * 
	 * Description: The constructor creates the AutomaticCommunityCreationManager
	 *              component abstractly at a CIS level.
	 * Parameters: 
	 * 				linkedSuperCis - the CIS on behalf of which this object is to operate, by
	 *                               suggesting sub-CISs on it.
	 */
	
	public AutomaticCommunityCreationManager(CisRecord linkedSuperCis) {
		this.linkedSuperCis = linkedSuperCis;
	}
	
	public ArrayList<IIdentity> getIDsOfInteractingCsss(String startingDate, String endingDate) {
		//What CSSs is this one currently interacting with?
		//Found by: For each service, shared service, and resource the user is using (in the last ~5 minutes), is there an end-CSS they're interacting with?
		//Is there a CSS they're indirectly interacting with over the service?
		
		
		//Needs a framework for capturing this in the platform.
		//It needs a timestamp for this, so either the context is stored with timestamps or 
		//we get it from the CSS activity feed (which isn't implemented yet)
		ArrayList<IIdentity> interactingCsss = null;
		
		try {
			userContextBroker.lookup(CtxModelType.ATTRIBUTE, "used services");
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
		//for (int i = 0; i < userContextBrokerCallback.size(); i++) {
		//    userContextBroker.lookup(CtxModelType.ATTRIBUTE, "CSSs sharing service " + thisService, userContextBrokerCallback);
		//    userContextBroker.lookup(CtxModelType.ATTRIBUTE, "CSSs interacted with over service " + thisService, userContextBrokerCallback);
        //
		//    Get the lists from the callbacks
		//
		//    filter (sharingAndInteractingCsssList).split("timestamp: ")[1] >= Date.getDate() - 300000;
		//}
//	    userContextBroker.lookup(CtxModelType.ATTRIBUTE, "CSSs shared resources with", userContextBrokerCallback);

		
		
		return interactingCsss;
	}
	
	/*
	 * Description: The method looks for CISs to create, using as a base the information related to
	 *              this object's 'linked' component (see the fields). If the linked component
	 *              is just a CIS, it will only try to create sub-CISs on it. If the linked component
	 *              is a CSS, it will check all information relevant to that CSS to create
	 *              CISs that would be relevant to them. If the linked component is 
	 *              a domain (or something else like a local area?), the checks are not 'selfish'
	 *              but try to objectively identify appropriate CISs for groups of people, based
	 *              on collective aspects like context attributes.
	 */
	
	public void identifyCissToCreate(String evaluationType) {
		
		
		
		ArrayList<IIdentity> interactedCssIDs = null;
		ArrayList<IIdentity> friendCssIDs = null;
		ArrayList<IIdentity> localCsss = null;
		// ...
		
		ArrayList<CisRecord> cissToCreate = new ArrayList<CisRecord>();
		ArrayList<CisRecord> cissToAutomaticallyCreate = null;
		//v1.0 algorithms
		
		linkedCss = mock(IIdentity.class);
		cisManager = mock(ICisManager.class);
		userContextBroker = mock(ICtxBroker.class);
		userCssDirectory = mock(ICssDirectory.class);
		
		String[] it = new String[1];
		it[0] = linkedCss.getIdentifier();
		
		CisRecord[] listOfUserJoinedCiss = cisManager.getCisList(new CisRecord(null, null, null, null, null, it, null, null, null));
		ArrayList<CisRecord> userJoinedCiss = new ArrayList<CisRecord>();
		if (listOfUserJoinedCiss != null)
		    for (int i = 0; i < listOfUserJoinedCiss.length; i++) {
		        userJoinedCiss.add(listOfUserJoinedCiss[i]);   
		    }
		
		if (evaluationType.equals("extensive")) { //every day or so
			if (linkedCss != null) {
				//interactedCssIDs = getIDsOfInteractingCsss();
				
				/**
				 * public CisRecord(CisActivityFeed feed, String ownerCss,
			String membershipCriteria, String cisId, String permaLink,
			String[] membersCss, String password, String host,
			Set<ServiceSharingRecord> sharedServices) {
				 * 
				 */
				
				//first step: look for more obvious CISs on high-priority kinds of context,
				//e.g. friends in contact list, family in contact list (from SNS extractor or SOCIETIES)
				
				//If CISs are appropriate for friends' lists in Google+ circle fashion, then that counts
				
				
				
				//CSS directory
				Collection<Object/**CssAdvertisementRecord*/> cssDirectoryMembers = userCssDirectory.findForAllCss();
				boolean cisExistsAlready = false;
				if (cssDirectoryMembers.size() >= 2)
				for (int i = 0; i < userJoinedCiss.size(); i++) {
					//if (userJoinedCiss.get(i).getOrchestrationMetdata.contains("Personal CSS directory")) cisExistsAlready = true;
					Collection<Object> membersOfCis = null;
					String[] membersOfCisStringArray = userJoinedCiss.get(i).membersCss;
					for (int m = 0; m < membersOfCisStringArray.length; m++) {
					    membersOfCis.add(membersOfCisStringArray[m]);
					}
				    if ((membersOfCis == cssDirectoryMembers) && (userJoinedCiss.get(i).membershipCriteria.equals("null")))
				         cisExistsAlready = true;
				     else if ((cssDirectoryMembers.contains(userJoinedCiss.get(i).membersCss) && (userJoinedCiss.get(i).membershipCriteria.equals("")))) {
				         /**ArrayList<CisActivity> thisCisActivityHistory = */userJoinedCiss.get(i).feed.getActivities(linkedCss.toString(), "time");
				         ///**ArrayList<CssActivity> thisCssActivityHistory = */userCssManager.feed.getActivities("time");
				         //Date cisCreationDate = thisCisActivityHistory.getEarliestDate();
				         //thisCssActivityHistory = thisCssActivityHistory.split(cisCreationDate)[0];
				         //if (!thisCssActivityHistory.contains(userJoinedCiss[i].getMembers())
				         //    cisExistsAlready = true;
				     }
				     //else if ((userJoinedCiss.get(i).getActivityHistory().contains("personal css directory cis")))
				     //    cisExistsAlready = true;
				     //else if ((userJoinedCiss.get(i).getOrchestrationMetadata().contains("personal css directory cis")))
				     //    cisExistsAlready = true;
				/**    if (!cisManager.getCiss().get(i).getMembers() == people)*/
				}
				if (cisExistsAlready == false)
				    cissToCreate.add(new CisRecord(null, linkedCss.toString(), "PERSONAL CIS for your CSS directory members", null, null, null, null, null, null));
				
				
				//friends?
				//userContextBroker.lookup(CtxModelType.ENTITY, "SNGroup", userContextBrokerCallback);
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				
				//fanpage informs interests-based CISs
				//userContextBroker.lookup(CtxModelType.ENTITY, "FanPage", userContextBrokerCallback);
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				
				Future<List<CtxIdentifier>> friendsFuture = null;
				try {
				    friendsFuture = userContextBroker.lookup(CtxModelType.ATTRIBUTE, "close friends");
				    //Filter to friends who all consider each-other friends, within the group of user's friends
				    //Need API in place to obtain this data
			    } catch (CtxException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
			    }
				
				//while (friendsFuture == null) {
				//	continue;
				//}
				
				List<CtxIdentifier> theFriends = null;
				try {
					if (friendsFuture != null)
					    theFriends = friendsFuture.get();
				} catch (InterruptedException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (ExecutionException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				boolean similarCis = false;
				if (listOfUserJoinedCiss != null) {
				    for (int i = 0; i < listOfUserJoinedCiss.length; i++) {
					    String[] members = ((CisRecord)listOfUserJoinedCiss[i]).membersCss;
					    int number = 0;
					    for (int m = 0; m < members.length; m++) {
						    if (theFriends.contains(members[i]))
							    number++;
				    	}
					    if (((number/theFriends.size()) >= 0.8) && (((number/members.length) >= 0.8))) {
						    similarCis = true;
						//if no CIS exists with 100% friend members, suggest sub-CIS of that one
						
						//number = 0;
						//for (int m = 0; m < theFriends.size(); m++) {
						//	if (members[i]theFriends.contains(members[i]))
						//		number++;
						//}
						
			                cissToCreate.add(new CisRecord(null, linkedCss.toString(), "family relation to all members", null, null, null, null, null, null));
						
					    }
				    }
				}
				if (similarCis == false) {
		            cissToCreate.add(new CisRecord(null, linkedCss.toString(), "Mutual friends", null, null, null, null, null, null));
				    
				}
				
				
				
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people) //SUB-CIS OF PERSONAL CSS DIRECTORY CIS
				//            cissToCreate.add(new CisRecord(null, linkedCss, "family relation to all members", null, null, null, null, null));
				
				List<CtxIdentifier> contextList; //the list retrieved from above callback
				
				//look up user joined CISs. Does a CIS already exist with very similar membership
				//structure to friends list, e.g. of total CIS members, at least 80% are from friends list,
				//or at least 80% of CIS members are from friends list. Although such a CIS could evolve,
				//eventually failing to meet that, it's a first approximation for now at least.
				
				//if no pre-existing CIS for this purpose, add to list of CISs that will be suggested
				//to create or automatically create
				
				
				
				//second step: some obvious CISs that might benefit a user.
				Future<List<CtxIdentifier>> familyFuture = null;
				
				try {
					familyFuture = userContextBroker.lookup(CtxModelType.ATTRIBUTE, "family relations");
				} catch (CtxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people)
				//            cissToCreate.add(new CisRecord(null, linkedCss, "family relation to all members", null, null, null, null, null));
				
				Future<List<CtxIdentifier>> nationalityFuture = null;
				try {
					nationalityFuture = userContextBroker.lookup(CtxModelType.ATTRIBUTE, "nationality");
				} catch (CtxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people)
				//            cissToCreate.add(new CisRecord(null, linkedCss, "Nationals", null, null, null, null, null));
				
				Future<List<CtxIdentifier>> languageFuture = null;
				
				try {
					languageFuture = userContextBroker.lookup(CtxModelType.ATTRIBUTE, "first language");
				} catch (CtxException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people)
				//            cissToCreate.add(new CisRecord(null, linkedCss, "Native language speakers", null, null, null, null, null));
				
				Future<List<CtxIdentifier>> interestFuture = null;
				try {
					interestFuture = userContextBroker.lookup(CtxModelType.ATTRIBUTE, "interests");
				} catch (CtxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people)
				//            cissToCreate.add(new CisRecord(null, linkedCss, "Interests", null, null, null, null, null));
				
				try {
					userContextBroker.lookup(CtxModelType.ATTRIBUTE, "local CSSs");
				} catch (CtxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//historyOfLocalCsss.add(thisResult);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people)
				//            cissToCreate.add(new CisRecord(null, linkedCss, "Local proximity", null, null, null, null, null));
				
				//final step: retrieve as much context data on CSS user and inter-CSS connections 
				//amongst their immediate connection neighbourhood as possible.
				String yesterday = new Timestamp(new Date().getTime()).toString();
				String[] yesterdayHyphens = yesterday.split("-");
				String lastPartOfYesterdayHyphens = yesterdayHyphens[2].split(" ")[1];
				yesterday = yesterdayHyphens[0] + "-" + yesterdayHyphens[1] + "-" + (Integer.valueOf(yesterdayHyphens[2].split(" ")[0]) - 1) + " " + lastPartOfYesterdayHyphens; 
				String lastWeek = "";
				String lastMonth = "";
				interactedCssIDs = getIDsOfInteractingCsss(yesterday, new Timestamp(new Date().getTime()).toString());
				interactedCssIDs = getIDsOfInteractingCsss(lastWeek, new Timestamp(new Date().getTime()).toString());
				interactedCssIDs = getIDsOfInteractingCsss(lastMonth, new Timestamp(new Date().getTime()).toString());
				
				//CssActivityFeed theFeed = cssManager.getCssActivityFeed();
				////CssActivityFeed todayFeed = theFeed.searchQuery("contains: " + yesterdayHyphens[2].split(" ")[0]);
				//ArrayList<String> interactionRecordsLastDay = todayFeed.searchQuery("interaction").toString();
				
				
				//CssActivityFeed theFeed = cssManager.getCssActivityFeed();
				////CssActivityFeed todayFeed = theFeed.searchQuery("contains: " + lastWeekHyphens[2].split(" ")[0]);
				//ArrayList<String> interactionRecordsLastWeek = todayFeed.searchQuery("interaction").toString();
				
				//CssActivityFeed theFeed = cssManager.getCssActivityFeed();
				////CssActivityFeed todayFeed = theFeed.searchQuery("contains: " + lastMonthHyphens[2].split(" ")[0]);
				//ArrayList<String> interactionRecordsLastMonth = todayFeed.searchQuery("interaction").toString();
				
				//ArrayList<String> potentialCisMembers = new ArrayList<String>;
				
				//for (int i = 0; i < interactedCssIDs.size(); i++) {
				//    if (interactionRecordsLastWeek.contains(interactedCssIDs.get(i)) && interactionRecordsLastMonth.contains(i))
				//        potentialCisMembers.add(interactedCssIDs.get(i);
				//}
				//if (potentialCisMembers.size >= 2)
				//    if (!joinedCiss.getMemberList().contains(thisone))
				//        cissToCreate.add(new CisRecord(null, linkedCss, "Interactors on Service " + "serviceName", null, null, null, null, null);
			}
		}
			
		else { //non-extensive check, every few minutes or so.
			if (linkedCss != null) {
				interactedCssIDs = getIDsOfInteractingCsss(new Timestamp(new Date().getTime() - 200000).toString(), new Timestamp(new Date().getTime()).toString());
				//retrieve recent history of certain kinds of context data on CSS user and inter-CSS connections 
				//amongst their immediate connection neighbourhood as possible.
				
				Future<List<CtxIdentifier>> localCssFuture = null;
				try {
					localCssFuture = userContextBroker.lookup(CtxModelType.ATTRIBUTE, "local CSSs");
				} catch (CtxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				List<CtxIdentifier> temporaryLocalCsss = null;
				
				try {
					if (localCssFuture != null)
					    temporaryLocalCsss = localCssFuture.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (temporaryLocalCsss != null) {
				    for (int i = 0; i < temporaryLocalCsss.size(); i++) {
					    if (userCssDirectory.findForAllCss().contains(temporaryLocalCsss.get(i))) {
						    if (userJoinedCiss.contains(new CisRecord(null, null, "friends", null, null, null, null, null, null))) {
							
							    if (userJoinedCiss.get(i).membershipCriteria.equals("friends") /**&& userJoinedCiss.getSubCiss("proximity") != null*/ /**it has a sub-CIS defined on this location*/) {
								//not create
							    }
							    else {
								    cissToCreate.add(new CisRecord(null, linkedCss.toString(), "Local proximity", null, null, null, null, null, null));
							    }
						    }
					    }
					//do for all other attributes: family, workers, interest?, CSS directory (personal and mutual if there is one)
					//then do this for service interaction, same process
				    }
				}
				
				//userContextBrokerCallback.ctxModelObjectsLookedUp(List<CtxIdentifier> list);
				//historyOfLocalCsss.add(thisResult);
				//ArrayList<Identity> people = userCssDirectory.getContextMatchingCsss(list);
				//if (people.size() >= 2)
				//    for (int i = 0; i < cisManager.getCiss(); i++)
				//        if (!cisManager.getCiss().get(i).getMembers() == people)
				//            cissToCreate.add(new CisRecord(null, linkedCss, "Local proximity", null, null, null, null, null));
				
				int sharingCis = 0;
				// processing - here or delegated to local method
				if (localCsss != null) {
					//for (int m = 0; m < cisManager.getCisList(linkedCss); m++)
				    for (int n = 0; n < localCsss.size(); n++) {
					    //if part of shared super-CIS
				    	//if (cisManager.getCisList(linkedCss).get(m).getMembersList().contains(localCsss.get(n)))
				    	    //sharingCis + 1;
				    	//if (sharingCis/(localCsss-1) >= 0.8)
				    	    //if activity isn't restricted to just before 5 mins ago, or looking back 1 hour shows spikes of activity
					            //potential to suggest location-based sub-CIS
				    	    //then if shared context - context_local_sharedCIS counter+1
					               //potentially suggest sub-CIS if none exist or ignore completely as CIS already exists for it
					    //if shared context - context_local counter+1
					
				    }
				}
				ArrayList<IIdentity> recentlyInteractedCsss = new ArrayList<IIdentity>(); //interaction timestamps are last 24 hours(?)
				ArrayList<IIdentity> recentlyReferencingCsss = new ArrayList<IIdentity>();
				
				
				for (int i = 0; i < recentlyInteractedCsss.size(); i++) {
					
					
				}
				
				for (int i = 0; i < recentlyReferencingCsss.size(); i++) {
					
					
				}
				
				//boolean flag doneLocalVicinityCheckRecently = false;
				
				//now we compare all the suggested CISs to see if they should sub-CIS etc.
				//each-other, using layering principles:
				
				//If location was suggested and same people interact with service there also suggested,
				//then if one has existed before the other, make that the CIS and the other the super-CIS,
				//else make one CIS on both relations if always tied together, else keep them separate
				
				//APIs not yet available to achieve ...
				//Need APIs: get CISs that match *only* certain members of member criteria,
				//get CISs that contain at least the specified query, etc.
				
			}
		}
		
		
		
		
		
		//invoke UserAgent suggestion GUI for creation of CISs
		//OR
		//automatically call CIS management functions to create CISs
		
		//Can't use GUI in tests
		//cissToCreate = getUserFeedbackOnCreation(cissToCreate);
		
		if (cissToCreate != null) 
		    for (int i = 0; i < cissToCreate.size(); i++)
			    cisManager.createCis(linkedCss.getIdentifier(), cissToCreate.get(i).getCisId());
	}
	
	public ArrayList<CisRecord> getUserFeedbackOnCreation(ArrayList<CisRecord> cissToCreate) {
		ArrayList<CisRecord> finalisedCiss = null;
		String[] options = new String[1];
		options[0] = "options";
		String userResponse = null;
		boolean responded = false;
		userFeedback.getExplicitFB(0,  new ExpProposalContent("SOCIETIES suspects the follwing CISs may benefit you. If you would like to create one or more of these CISs, please check them.", options), userFeedbackCallback);
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
		   	Iterator<CisRecord> iterator = cissToCreate.iterator();
			while (iterator.hasNext()) {
			    CisRecord potentiallyCreatableCis = iterator.next();
		        if (userResponse.equals("Yes")) {
				    finalisedCiss.add(potentiallyCreatableCis);
			       // cisManager.createCis(linkedCss, potentiallyCreatableCis.getCisId());
		        }
		        else {
		    	    recentRefusals.add(potentiallyCreatableCis);
		        }
		   }
		}
		return finalisedCiss;
	}
	
	
	public boolean isSituationSuggestiveOfTemporaryCISCreation() {
		boolean tempCisPossibility = true;
		return tempCisPossibility;
	}
	
    public void initialiseAutomaticCommunityCreationManager() {
    	//getCommManager().register(this);
    	
    	new AutomaticCommunityCreationManager(linkedCss, "CSS");
    }
    
    public IIdentity getLinkedCss() {
    	return linkedCss;
    }
    
    public void setLinkedCss(IIdentity linkedCss) {
    	this.linkedCss = linkedCss;
    }
    
    public CisRecord getLinkedSuperCis() {
    	return linkedSuperCis;
    }
    
    public void setLinkedCis(CisRecord linkedSuperCis) {
    	this.linkedSuperCis = linkedSuperCis;
    }
    
    public IIdentity getLinkedDomain() {
    	return linkedDomain;
    }
    
    public void setLinkedDomain(IIdentity linkedDomain) {
    	this.linkedDomain = linkedDomain;
    }
    
    /**public IUserCtxDBMgr getUserContextDatabaseManager() {
    	return userContextDatabaseManager;
    }
    
    public void setUserContextDatabaseManager(IUserCtxDBMgr userContextDatabaseManager) {
    	System.out.println("GOT database" + userContextDatabaseManager);
    	this.userContextDatabaseManager = userContextDatabaseManager;
    }
    */
    public void setUserContextBroker(ICtxBroker userContextBroker) {
    	System.out.println("GOT user context broker" + userContextBroker);
    	this.userContextBroker = userContextBroker;
    }
    
    /**public void setUserContextBrokerCallback(ICtxBrokerCallback userContextBrokerCallback) {
    	System.out.println("GOT user context broker callback" + userContextBrokerCallback);
    	this.userContextBrokerCallback = userContextBrokerCallback;
    }*/
    
    public void retrieveUserContextBrokerCallback(CtxEntity theContext) {
    	availableContextData.add(theContext);
    }
    
    public ICisManager getCisManager() {
    	return cisManager;
    }
    
    public void setCisManager(ICisManager cisManager) {
    	this.cisManager = cisManager;
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