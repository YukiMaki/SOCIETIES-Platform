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
package org.societies.privacytrust.privacyprotection.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxIdentifierFactory;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.Requestor;
import org.societies.api.internal.privacytrust.privacyprotection.IPrivacyDataManager;
import org.societies.api.internal.privacytrust.privacyprotection.IPrivacyPolicyManager;
import org.societies.api.internal.privacytrust.privacyprotection.model.listener.IPrivacyDataManagerListener;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.Action;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.ResponseItem;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.constants.ActionConstants;
import org.societies.api.internal.privacytrust.privacyprotection.remote.IPrivacyDataManagerRemote;

/**
 * @author Olivier Maridat (Trialog)
 *
 */
public class Test implements IPrivacyDataManagerListener {
	private static Logger LOG = LoggerFactory.getLogger(Test.class.getSimpleName());
	
	private IPrivacyDataManager privacyDataManager;
	private IPrivacyDataManagerRemote privacyDataManagerRemote;
	private IPrivacyPolicyManager privacyPolicyManager;
	private ICommManager commManager;
	
	public void start() {
		try {
			if (null == privacyPolicyManager) {
				throw new Exception("privacyPolicyManager NULL");
			}
			if (null == commManager) {
				throw new Exception("CommManager NULL");
			}
			if (null == commManager.getIdManager()) {
				throw new Exception("IdManager NULL");
			}
			IIdentity cisId = commManager.getIdManager().fromJid("red@societies.local");
			boolean result = false;
			result = privacyPolicyManager.deletePrivacyPolicy(cisId);
			LOG.info("************* [Test Resullt] Privacy policy deleted? "+result);
		} catch (Exception e) {
			LOG.error("************* [Tests PrivacyPolicyManager] Error Exception: "+e.getMessage()+"\n", e);
		}
		
		
		try {
			if (null == privacyDataManager) {
				throw new Exception("privacyDataManager NULL");
			}
			if (null == commManager) {
				throw new Exception("CommManager NULL");
			}
			if (null == commManager.getIdManager()) {
				throw new Exception("IdManager NULL");
			}
			IIdentity requestorId = commManager.getIdManager().fromJid("orange@societies.local");
			IIdentity ownerId = commManager.getIdManager().fromJid("red@societies.local");
			Requestor requestor = new Requestor(requestorId);
			CtxIdentifier dataId = CtxIdentifierFactory.getInstance().fromString("red@societies.local/ENTITY/person/1/ATTRIBUTE/name/13");
			Action action = new Action(ActionConstants.READ);
			ResponseItem permission = privacyDataManager.checkPermission(requestor, ownerId, dataId, action);
			LOG.info("************* [Test Result] Permission checked? "+(null != permission));
			if (null != permission) {
				LOG.info(permission.toString());
			}
		} catch (Exception e) {
			LOG.error("************* [Tests PrivacyDataManager] Error Exception: "+e.getMessage()+"\n", e);
		}
		
		try {
			if (null == privacyDataManagerRemote) {
				throw new Exception("privacyDataManagerRemote NULL");
			}
			if (null == commManager) {
				throw new Exception("CommManager NULL");
			}
			if (null == commManager.getIdManager()) {
				throw new Exception("IdManager NULL");
			}
			IIdentity requestorId = commManager.getIdManager().fromJid("orange@societies.local");
			IIdentity ownerId = commManager.getIdManager().fromJid("red@societies.local");
			Requestor requestor = new Requestor(requestorId);
			CtxIdentifier dataId = CtxIdentifierFactory.getInstance().fromString("red@societies.local/ENTITY/person/1/ATTRIBUTE/name/13");
			Action action = new Action(ActionConstants.READ);
			privacyDataManagerRemote.checkPermission(requestor, ownerId, dataId, action, this);
			LOG.info("************* Permission check remote: launched");
		} catch (Exception e) {
			LOG.error("************* [Tests PrivacyDataManagerRemote] Error Exception: "+e.getMessage()+"\n", e);
		}
	}

	public void setCommManager(ICommManager commManager) {
		this.commManager = commManager;
		LOG.info("************* commManager injected");
	}
	public void setPrivacyDataManager(IPrivacyDataManager privacyDataManager) {
		this.privacyDataManager = privacyDataManager;
		LOG.info("************* privacyDataManager injected");
	}
	public void setPrivacyDataManagerRemote(
			IPrivacyDataManagerRemote privacyDataManagerRemote) {
		this.privacyDataManagerRemote = privacyDataManagerRemote;
		LOG.info("************* privacyDataManagerREMOTE injected");
	}
	public void setPrivacyPolicyManager(IPrivacyPolicyManager privacyPolicyManager) {
		this.privacyPolicyManager = privacyPolicyManager;
		LOG.info("************* privacyPolicyManager injected");
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.societies.api.internal.privacytrust.privacyprotection.model.listener.IPrivacyDataManagerListener#onAccessControlChecked(org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.ResponseItem)
	 */
	@Override
	public void onAccessControlChecked(ResponseItem permission) {
		LOG.info("************* onAccessControlChecked "+permission.toXMLString());
	}
	/* (non-Javadoc)
	 * @see org.societies.api.internal.privacytrust.privacyprotection.model.listener.IPrivacyDataManagerListener#onAccessControlCancelled(java.lang.String)
	 */
	@Override
	public void onAccessControlCancelled(String msg) {
		LOG.info("************* onAccessControlCancelled "+msg);
	}
	/* (non-Javadoc)
	 * @see org.societies.api.internal.privacytrust.privacyprotection.model.listener.IPrivacyDataManagerListener#onAccessControlAborted(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void onAccessControlAborted(String msg, Exception e) {
		LOG.info("************* onAccessControlAborted "+msg, e);
	}

}
