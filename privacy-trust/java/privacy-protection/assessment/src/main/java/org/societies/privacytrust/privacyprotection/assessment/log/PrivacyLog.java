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
package org.societies.privacytrust.privacyprotection.assessment.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacyassessment.IPrivacyLog;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacyassessment.LogEntry;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacyassessment.PrivacyLogFilter;
import org.societies.privacytrust.privacyprotection.assessment.logger.CommsFwTestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 *
 * @author Mitja Vardjan
 *
 */
public class PrivacyLog implements IPrivacyLog {

	private static Logger LOG = LoggerFactory.getLogger(PrivacyLog.class);

	private CommsFwTestBean testBean;
	private ICommManager commMgr;

	public PrivacyLog() {
		LOG.info("constructor");
	}
	
	public void init() {
		LOG.info("init()");
		
		//ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "META-INF/spring/bundle-context.xml" });
		//LOG.debug("init(): 1");
		//CommsFwTestBean aopBean = (CommsFwTestBean) appContext.getBean("aopBeanProxy");
		
		
		LOG.debug("init(): 2");
		String aspectName = testBean.getAspectName();
		LOG.debug("init(): 3 aspectName = " + aspectName);
		testBean.setAspectName("ahoj");
		aspectName = testBean.getAspectName();
		LOG.debug("init(): 4 aspectName = " + aspectName);
		
//		try {
//			LOG.debug("init(): 1");
//			commMgr.sendIQGet(null, null, null);
//			LOG.debug("init(): 2");
//		} catch (CommunicationException e) {
//			LOG.warn("init()", e);
//		}
	}
	

	// Getters and setters for beans
	public ICommManager getCommMgr() {
		LOG.debug("getCommMgr()");
		return commMgr;
	}
	public void setCommMgr(ICommManager commMgr) {
		LOG.debug("setCommMgr()");
		this.commMgr = commMgr;
	}
	public CommsFwTestBean getTestBean() {
		LOG.debug("getTestBean()");
		return testBean;
	}
	public void setTestBean(CommsFwTestBean testBean) {
		LOG.debug("setTestBean()");
		this.testBean = testBean;
	}
	
	@Override
	public LogEntry[] search(PrivacyLogFilter filter) {
		
		LOG.debug("search({})", filter);
		
		return null;
	}
	
	@Override
	public LogEntry[] getAll() {
		
		LOG.debug("getAll()");
		
		return null;
	}
}