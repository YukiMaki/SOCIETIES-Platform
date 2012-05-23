/**
 * Copyright (c) 2011, SOCIETIES Consortium
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

package org.societies.personalisation.CRISTUserIntentTaskManager.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.identity.IIdentity;
import org.societies.api.internal.context.broker.ICtxBroker;
import org.societies.api.schema.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.personalisation.CRIST.api.CRISTUserIntentDiscovery.ICRISTUserIntentDiscovery;
import org.societies.personalisation.CRIST.api.CRISTUserIntentPrediction.ICRISTUserIntentPrediction;
import org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.ICRISTUserIntentTaskManager;
import org.societies.personalisation.CRIST.api.model.CRISTUserAction;
import org.societies.personalisation.CRIST.api.model.CRISTUserSituation;
import org.societies.personalisation.CRIST.api.model.CRISTUserTask;
import org.societies.personalisation.CRIST.api.model.CRISTUserTaskModelData;
import org.societies.personalisation.CRISTUserIntentDiscovery.impl.CRISTHistoryData;
import org.societies.personalisation.CRISTUserIntentDiscovery.impl.CRISTUserIntentDiscovery;

public class CRISTUserIntentTaskManager implements ICRISTUserIntentTaskManager {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private LinkedHashMap<String, Integer> intentModel = null;
	//private LinkedHashMap<String, Integer> situationModel = null;
	private ArrayList<String> registeredContext = CRISTHistoryData.getRegisteredContext();

	private HashMap<IIdentity, CRISTUserAction> currentUserActionMap = new HashMap<IIdentity, CRISTUserAction>();
	private HashMap<IIdentity, CRISTUserSituation> currentUserSituationMap = new HashMap<IIdentity, CRISTUserSituation>();
	private HashMap<IIdentity, ArrayList<String>> currentUserContextMap = new HashMap<IIdentity, ArrayList<String>>();

	private ICRISTUserIntentPrediction cristPrediction;
	private ICRISTUserIntentDiscovery cristDiscovery;
	private ICtxBroker ctxBroker;
	private CRISTCtxBrokerContact ctxBrokerContact;
	
	private CtxAttributeIdentifier cristIntentModelAttrId;

	private CtxAttribute myCtx;
	private IIdentity myID;

	private int maxStep = 3;

	private ArrayList<CRISTHistoryData> historyList = new ArrayList<CRISTHistoryData>();

	public CRISTUserIntentTaskManager() {
		LOG.info("Hello! I'm the CRIST User Intent Manager!");
	}

	public ICRISTUserIntentPrediction getCristPrediction() {
		return cristPrediction;
	}

	public void setCristPrediction(ICRISTUserIntentPrediction cristPrediction) {
		this.cristPrediction = cristPrediction;
	}

	public ICRISTUserIntentDiscovery getCristDiscovery() {
		return cristDiscovery;
	}

	public void setCristDiscovery(ICRISTUserIntentDiscovery cristDiscovery) {
		this.cristDiscovery = cristDiscovery;
	}

	public ICtxBroker getCtxBroker() {
		return ctxBroker;
	}

	public void setCtxBroker(ICtxBroker ctxBroker) {
		this.ctxBroker = ctxBroker;
	}

	public void initialiseCRISTUserIntentManager() {

		if (ctxBroker == null) {
			LOG.error(this.getClass().getName() + "CtxBroker is null");
		} 

		LOG.info("Yo!! I'm a brand new service and my interface is: "
				+ this.getClass().getName());
		
		ctxBrokerContact = new CRISTCtxBrokerContact(ctxBroker);
		
		CRISTCtxBrokerContact.initializeHistory(registeredContext, ctxBrokerContact);
		
		historyList = CRISTCtxBrokerContact.retrieveHistoryData(ctxBrokerContact);

		for (CRISTHistoryData historyData : historyList)
		{
			LOG.info("retrieveHistoryData ----- " + historyData.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #addSituationsAndActionsToTask(org.societies.personalisation
	 * .CRIST.api.model.CRISTUserTask, java.util.HashMap, java.util.HashMap)
	 */
	@Override
	public CRISTUserTask addSituationsAndActionsToTask(CRISTUserTask arg0,
			HashMap<CRISTUserAction, Double> arg1,
			HashMap<CRISTUserSituation, Double> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#getAction(java.lang.String)
	 */
	@Override
	public CRISTUserAction getAction(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#getActionsByType(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ArrayList<CRISTUserAction> getActionsByType(String arg0, String arg1) {
		// TODO Auto-generated method stub

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #getCurrentIntentAction(org.societies.api.comm.xmpp.datatypes.IIdentity,
	 * org.societies.api.comm.xmpp.datatypes.IIdentity,
	 * org.societies.api.servicelifecycle.model.ServiceResourceIdentifier)
	 */
	@Override
	public CRISTUserAction getCurrentIntentAction(IIdentity arg0,
			IIdentity arg1, ServiceResourceIdentifier arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#getCurrentUserAction()
	 */
	@Override
	public CRISTUserAction getCurrentUserAction(IIdentity entityID) {
		// TODO Auto-generated method stub
		return this.currentUserActionMap.get(entityID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#getCurrentUserSituation()
	 */
	@Override
	public CRISTUserSituation getCurrentUserSituation(IIdentity entityID) {
		// TODO Auto-generated method stub
		return this.currentUserSituationMap.get(entityID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #getCurrentUserContext(org.societies.api.identity.IIdentity)
	 */
	@Override
	public ArrayList<String> getCurrentUserContext(IIdentity entityID) {
		// TODO Auto-generated method stub
		return this.currentUserContextMap.get(entityID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #updateUserSituation(org.societies.api.identity.IIdentity,
	 * org.societies.api.context.model.CtxAttribute)
	 */
	@Override
	public void updateUserSituation(IIdentity entityID,
			CtxAttribute ctxAttribute) {

		// TODO: Identify the user's current situation based on her real-time
		// context
		// Retrieve the user's real-time context: How to retrieve a given user's
		// context
		// this.ctxBroker.retrieve();
		int currentPos = this.historyList.size() - 1;
		ArrayList<String> lastContextClique = this.historyList.get(currentPos)
				.getContext();
		ArrayList<String> currentContextClique = new ArrayList<String>();

		// Generate a new context clique based on the newly received
		// ctxAttribute
		String contextID = ctxAttribute.getType(); // how to handle when ctxAttribute is null?
		for (int i = 0; i < lastContextClique.size(); i++) {
			String oneContextID = this.registeredContext.get(i);
			if (oneContextID.equals(contextID)) {
				currentContextClique.add(ctxAttribute.getStringValue());
			} else {
				currentContextClique.add(lastContextClique.get(i));
			}
		}

		CRISTUserSituation userSituation = inferUserSituation(currentContextClique);

		// CRISTUserSituation userSituation = new CRISTUserSituation();
		// MOCK
		// userSituation.setSituationID("Outdoor");
		if (this.currentUserSituationMap.containsKey(entityID)) {
			this.currentUserSituationMap.remove(entityID);
		}
		this.currentUserSituationMap.put(entityID, userSituation);
		
		if (this.currentUserContextMap.containsKey(entityID)) {
			this.currentUserContextMap.remove(entityID);
		} 
		this.currentUserContextMap.put(entityID, currentContextClique);
		
	}

	//only for mocked data? it's OK
	private CRISTUserSituation inferUserSituation(
			ArrayList<String> contextClique) {
		double currentLight = Double.parseDouble(contextClique.get(1));
		double currentSound = Double.parseDouble(contextClique.get(2));
		double currentTemp = Double.parseDouble(contextClique.get(3));
		String currentGPS = contextClique.get(4);

		// Currently we simply use predefined rules to infer the situation, a
		// more valid way should
		// be constructing a situation-model based on the history context based
		// supervised learning mechanism
		CRISTUserSituation situation = new CRISTUserSituation();
		if (currentLight > 80 && currentLight < 120 && currentSound < 50
				&& currentTemp > 16 && currentTemp < 24
				&& currentGPS.equals("N/A")) {
			situation.setSituationID("Study Hall");
		} else if (!currentGPS.equals("N/A")) {
			situation.setSituationID("Outdoor");
		} else if (currentLight > 100 && currentSound > 60 && currentTemp > 22
				&& currentGPS.equals("N/A")) {
			situation.setSituationID("Shopping Mall");
		} else if (currentLight > 80 && currentSound < 50 && currentTemp > 20
				&& currentTemp < 28 && currentGPS.equals("N/A")) {
			situation.setSituationID("Office");
		} else {
			situation.setSituationID("Home");
		}

		return situation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #predictUserIntent(org.societies.api.identity.IIdentity,
	 * org.societies.api.context.model.CtxAttribute)
	 */
	@Override
	public ArrayList<CRISTUserAction> predictUserIntent(IIdentity entityID,
			CtxAttribute ctxAttribute) {

		updateUserSituation(entityID, ctxAttribute);
		CRISTUserAction currentUserAction = getCurrentUserAction(entityID);
		CRISTUserSituation currentUserSituation = getCurrentUserSituation(entityID);
		ArrayList<String> currentContextClique = getCurrentUserContext(entityID);

		if (currentUserAction != null && currentUserSituation != null && currentContextClique != null)
		{
			CRISTHistoryData oneHisData = new CRISTHistoryData(
					currentUserAction, currentUserSituation.getSituationID(), currentContextClique);

			ctxBrokerContact.storeActionHistory(oneHisData);
			this.historyList.add(oneHisData); //synchronize mechanism?
		}
		
		// Predict user intent based on one's current action
		ArrayList<CRISTUserAction> results = getNextActions(entityID,
				currentUserAction, currentUserSituation);
		return results;
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #predictUserIntent(org.societies.api.identity.IIdentity,
	 * org.societies.personalisation.CRIST.api.model.CRISTUserAction)
	 */
	@Override
	public ArrayList<CRISTUserAction> predictUserIntent(IIdentity entityID,
			CRISTUserAction userAction) {
		
		CRISTUserAction action = userAction;
		
		if (entityID == null) {
			LOG.info("entityID is null, predictUserIntent can not run.");
			return null;
		}
		
		if (action == null) {
			LOG.info("userAction is null, use current action instead.");
			action = getCurrentUserAction(entityID);
		}

		// Update the given user's current action
		if (this.currentUserActionMap.containsKey(entityID)) {
			this.currentUserActionMap.remove(entityID);
		}
		this.currentUserActionMap.put(entityID, userAction);
		
		CRISTUserSituation currentUserSituation = getCurrentUserSituation(entityID);
		

		String situationID;
		if (currentUserSituation == null) {
			LOG.info("getCurrentUserSituation(entityID) is null.");
			situationID = null;
		}
		else {
			situationID = currentUserSituation.getSituationID();
		}
		
		ArrayList<String> currentContextClique = getCurrentUserContext(entityID);
		//if there is null para, don't create a new record
		if (userAction != null && situationID != null && currentContextClique != null)
		{
			CRISTHistoryData oneHisData = new CRISTHistoryData(
					userAction, situationID, currentContextClique);

			ctxBrokerContact.storeActionHistory(oneHisData);
			this.historyList.add(oneHisData); //synchronize mechanism?
		}
		// Predict user intent based on one's current action
		ArrayList<CRISTUserAction> results = getNextActions(entityID,
				userAction, currentUserSituation);

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #getCurrentUserIntent(org.societies.api.identity.IIdentity,
	 * org.societies.
	 * api.schema.servicelifecycle.model.ServiceResourceIdentifier,
	 * java.lang.String)
	 */
	@Override
	public CRISTUserAction getCurrentUserIntent(IIdentity entityID,
			ServiceResourceIdentifier serviceID, String parameterName) {

		CRISTUserAction currentUserAction = null;
		CRISTUserSituation currentUserSituation = null;
		CRISTUserAction predictedUserAction = null;

		if (this.currentUserActionMap.containsKey(entityID)) {
			currentUserAction = this.currentUserActionMap.get(entityID);
		}
		if (this.currentUserSituationMap.containsKey(entityID)) {
			currentUserSituation = this.currentUserSituationMap.get(entityID);
		}

		ArrayList<CRISTUserAction> results = new ArrayList<CRISTUserAction>();
		if (currentUserAction != null && currentUserSituation != null) {
			results = getNextActions(entityID, currentUserAction,
					currentUserSituation);
			
			ServiceResourceIdentifier currentServiceID = null;
			String currentParameterName = null;					
			for(int i = 0;i<results.size();i++){
				currentServiceID = results.get(i).getServiceID();
				currentParameterName = results.get(i).getparameterName();
				if (currentServiceID.equals(serviceID)&&currentParameterName.equalsIgnoreCase(parameterName)){
					predictedUserAction = results.get(i);
					break;
				}
			}
		}
		
		return predictedUserAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #getNextActions(org.societies.personalisation.CRIST
	 * .api.model.CRISTUserAction)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public ArrayList<CRISTUserAction> getNextActions(IIdentity entityID, // this para isn't used
			CRISTUserAction currentAction, CRISTUserSituation currentSituation) {

		String actionValue = currentAction.getActionID();
		if (actionValue == null) {
			LOG.info("actionValue is null, set to \"\".");
			actionValue = "";
		}
		ArrayList<CRISTUserAction> predictedAction = new ArrayList<CRISTUserAction>();
		String currentPrediction = "";
		HashMap<String, Integer> predictionResult = new HashMap<String, Integer>();
		Integer totalScore = 0;

		if (cristDiscovery == null) {
			cristDiscovery = new CRISTUserIntentDiscovery();
			((CRISTUserIntentDiscovery) cristDiscovery).initialiseCRISTDiscovery();
		}
		this.cristDiscovery.enableCRISTUIDiscovery(true);
		this.intentModel = this.cristDiscovery.generateNewCRISTUIModel(this.historyList);
		
		
/*		
 * update intent model every time, and do not store in DB.
		if (this.intentModel == null) {
			// TODO: Retrieve intent model from CtxBroker
			LOG.info("Trying to retrieve the user's intent model from CtxBroker...");
			// this.ctxBroker.retrieveCRISTUIModel(entityID);

			if (this.intentModel == null) {
				// In case there is no intent model on the CtxBroker, generate a
				// new model
				// TODO: Retrieve the user's history data from CtxBroker
				// this.historyList = this.ctxBroker.retrieveHistory(entiryID);
				ArrayList<CRISTHistoryData> historyData = this.historyList;

				this.cristDiscovery.enableCRISTUIDiscovery(true);
				this.intentModel = this.cristDiscovery
						.generateNewCRISTUIModel(historyData);
				// TODO: Upload the new intentModel to the CtxBroker
			}
		}
*/
		if (this.intentModel != null) {
			// Get the next actions
			LinkedHashMap<String, Integer> candidateAction = new LinkedHashMap<String, Integer>();
			if (currentSituation != null && currentSituation.toString().length() > 0) {
				// In case the user's current situation is available
				String situationValue = currentSituation.getSituationID();
				String currentBehavior = actionValue + "@" + situationValue;
				Set<String> modelKeys = this.intentModel.keySet();
				Object[] keyArray = modelKeys.toArray();
				for (int i = 0; i < keyArray.length; i++) {
					if (keyArray[i].toString().startsWith(currentBehavior)) {
						String oneCandidate = keyArray[i].toString().replace(
								currentBehavior, "");
						Integer oneScore = this.intentModel.get(keyArray[i]);
						candidateAction.put(oneCandidate, oneScore);
					}
				}
			} else {
				// In case the user's current situation is not available, consider all situations
				String currentBehavior = actionValue;
				Set<String> modelKeys = this.intentModel.keySet();
				Object[] keyArray = modelKeys.toArray();
				for (int i = 0; i < keyArray.length; i++) {
					if (keyArray[i].toString().startsWith(currentBehavior)) {
						String oneCandidate = keyArray[i].toString().replace(
								currentBehavior, "");
						oneCandidate = oneCandidate.substring(
								oneCandidate.indexOf('#'),
								oneCandidate.length());
						Integer oneScore = this.intentModel.get(keyArray[i]);
						candidateAction.put(oneCandidate, oneScore);
					}
				}
			}

			// In case the predicted user intent action is not null
			if (candidateAction.size() > 0) {
				Set<String> candidateKeys = candidateAction.keySet();
				Object[] candidateArray = candidateKeys.toArray();

				for (int i = 0; i < this.maxStep; i++) {
					LinkedHashMap<String, Integer> currentCandidate = new LinkedHashMap<String, Integer>();
					for (int j = 0; j < candidateArray.length; j++) {
						if (candidateArray[j].toString().startsWith(
								currentPrediction)) {
							String[] candidates = candidateArray[j].toString()
									.substring(1).split("#");
							Integer historyScore = candidateAction
									.get(candidateArray[j]);
							if (candidates.length > i) {
								if (currentCandidate.containsKey(candidates[i])) {
									Integer currentScore = currentCandidate
											.get(candidates[i]);
									currentCandidate.put(candidates[i],
											currentScore + historyScore);
								} else {
									currentCandidate.put(candidates[i],
											historyScore);
								}
							}
						}
					}

					if (currentCandidate.size() > 0) {
						Set<String> currentCandidateKeys = currentCandidate
								.keySet();
						Object[] currentCandidateArray = currentCandidateKeys
								.toArray();
						Integer[] currentCandidateScore = new Integer[currentCandidate
								.size()];
						for (int j = 0; j < currentCandidate.size(); j++) {
							currentCandidateScore[j] = currentCandidate
									.get(currentCandidateArray[j]);
						}
						int maxScore = 0;
						int maxIndex = 0;
						for (int j = 0; j < currentCandidateScore.length; j++) {
							if (currentCandidateScore[j] > maxScore) {
								maxScore = currentCandidateScore[j];
								maxIndex = j;
							}
						}

						predictionResult.put(
								currentCandidateArray[maxIndex].toString(),
								maxScore);
						totalScore += maxScore;
						currentPrediction = currentPrediction + "#"
								+ currentCandidateArray[maxIndex].toString();
					}
				}
			}
		}

		if (predictionResult.size() > 0) {
			String[] predictionResultArray = currentPrediction.substring(1)
					.split("#");
			for (int i = 0; i < predictionResult.size(); i++) {
				CRISTUserAction oneAction = new CRISTUserAction();
				oneAction.setActionID(predictionResultArray[i].toString());
				// Double oneScore =
				// ((double)predictionResult.get(predictionResultArray[i]))/((double)totalScore);
				int confidenceLevel = predictionResult
						.get(predictionResultArray[i]);
				oneAction.setConfidenceLevel(confidenceLevel);
				predictedAction.add(oneAction);
			}
		}

		return predictedAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #getNextTasks(org.societies.personalisation.CRIST
	 * .api.model.CRISTUserTask)
	 */
	@Override
	public ArrayList<CRISTUserTask> getNextTasks(CRISTUserTask arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#getTask(java.lang.String)
	 */
	@Override
	public CRISTUserTask getTask(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#getTaskModelData()
	 */
	@Override
	public CRISTUserTaskModelData getTaskModelData() {
		// TODO Auto-generated method stub

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #getTasks(org.societies.personalisation.CRIST.api.model.CRISTUserAction,
	 * org.societies.personalisation.CRIST.api.model.CRISTUserSituation)
	 */
	@Override
	public ArrayList<CRISTUserTask> getTasks(CRISTUserAction arg0,
			CRISTUserSituation arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#identifyActionTaskInModel(java.lang.String,
	 * java.lang.String, java.util.HashMap)
	 */
	@Override
	public HashMap<CRISTUserAction, CRISTUserTask> identifyActionTaskInModel(
			String arg0, String arg1, HashMap<String, Serializable> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#identifyActions()
	 */
	@Override
	public ArrayList<CRISTUserAction> identifyActions() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#identifySituations()
	 */
	@Override
	public ArrayList<CRISTUserSituation> identifySituations() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#identifyTasks()
	 */
	@Override
	public ArrayList<CRISTUserTask> identifyTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager#resetTaskModelData()
	 */
	@Override
	public void resetTaskModelData() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #setNextActionLink(org.societies.personalisation
	 * .CRIST.api.model.CRISTUserAction,
	 * org.societies.personalisation.CRIST.api.model.CRISTUserAction,
	 * java.lang.Double)
	 */
	@Override
	public void setNextActionLink(CRISTUserAction arg0, CRISTUserAction arg1,
			Double arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #setNextSituationLink(org.societies.personalisation
	 * .CRIST.api.model.CRISTUserSituation,
	 * org.societies.personalisation.CRIST.api.model.CRISTUserSituation,
	 * java.lang.Double)
	 */
	@Override
	public void setNextSituationLink(CRISTUserSituation arg0,
			CRISTUserSituation arg1, Double arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.personalisation.CRIST.api.CRISTUserIntentTaskManager.
	 * ICRISTUserIntentTaskManager
	 * #setNextTaskLink(org.societies.personalisation.
	 * CRIST.api.model.CRISTUserTask,
	 * org.societies.personalisation.CRIST.api.model.CRISTUserTask,
	 * java.lang.Double)
	 */
	@Override
	public void setNextTaskLink(CRISTUserTask arg0, CRISTUserTask arg1,
			Double arg2) {
		// TODO Auto-generated method stub

	}
	
	
}
