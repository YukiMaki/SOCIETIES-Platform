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
package org.societies.privacytrust.trust.impl.event;

import java.io.Serializable;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.internal.privacytrust.trust.event.ITrustEventListener;
import org.societies.api.internal.privacytrust.trust.event.TrustEvent;
import org.societies.api.internal.privacytrust.trust.event.TrustUpdateEvent;
import org.societies.api.internal.privacytrust.trust.event.ITrustUpdateEventListener;
import org.societies.api.internal.privacytrust.trust.model.MalformedTrustedEntityIdException;
import org.societies.api.internal.privacytrust.trust.model.TrustedEntityId;
import org.societies.api.osgi.event.CSSEvent;
import org.societies.api.osgi.event.CSSEventConstants;
import org.societies.api.osgi.event.EMSException;
import org.societies.api.osgi.event.EventListener;
import org.societies.api.osgi.event.IEventMgr;
import org.societies.api.osgi.event.InternalEvent;
import org.societies.privacytrust.trust.api.event.ITrustEventMgr;
import org.societies.privacytrust.trust.api.event.TrustEventMgrException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link ITrustEventMgr} service.
 *
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.5
 */
@Service
@Lazy(value = false)
public class TrustEventMgr implements ITrustEventMgr {

	private static final Logger LOG = LoggerFactory.getLogger(TrustEventMgr.class); 
	
	@Autowired(required=true)
	private IEventMgr eventMgr;
	
	TrustEventMgr() {
		
		LOG.info(this.getClass() + " instantiated");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.societies.privacytrust.trust.api.event.ITrustEventMgr#postEvent(org.societies.api.internal.privacytrust.trust.event.TrustEvent, java.lang.String[], java.lang.String)
	 */
	@Override
	public void postEvent(final TrustEvent event, final String[] topics,
			final String source) throws TrustEventMgrException {
		
		if (event == null)
			throw new NullPointerException("event can't be null");
		if (topics == null)
			throw new NullPointerException("topics can't be null");
		if (source == null)
			throw new NullPointerException("source can't be null");
		
		if (event instanceof TrustUpdateEvent)
			this.postUpdateEvent((TrustUpdateEvent) event, topics, source);
		else
			throw new TrustEventMgrException("Could not post event "
					+ event + ": Unsupported TrustEvent implementation");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.societies.privacytrust.trust.api.event.ITrustEventMgr#registerListener(org.societies.api.internal.privacytrust.trust.event.ITrustEventListener, java.lang.String[], org.societies.api.internal.privacytrust.trust.model.TrustedEntityId)
	 */
	@Override
	public void registerListener(final ITrustEventListener listener, 
			final String[] topics, final TrustedEntityId teid) throws TrustEventMgrException {
		
		if (listener == null)
			throw new NullPointerException("listener can't be null");
		if (topics == null)
			throw new NullPointerException("topics can't be null");
		
		if (listener instanceof ITrustUpdateEventListener)
			this.registerUpdateListener((ITrustUpdateEventListener) listener, topics, teid);
		else
			throw new TrustEventMgrException("Could not register trust event listener "
					+ listener + ": Unsupported ITrustEventListener extension");
	}
	
	private void postUpdateEvent(final TrustUpdateEvent event, final String[] topics,
			final String source) throws TrustEventMgrException {
		
		final TrustUpdateEventInfo eventInfo = new TrustUpdateEventInfo(
				event.getOldValue(), event.getOldValue()); 
		
		for (int i = 0; i < topics.length; ++i) {
			
			final InternalEvent internalEvent = new InternalEvent(
					topics[i], event.getId().toString(), source, eventInfo);

			if (this.eventMgr == null)
				throw new TrustEventMgrException("Could not send TrustUpdateEvent '"
						+ event + "' to topic " + topics[i]
						+ ": IEventMgr service is not available");
			try {
				if (LOG.isDebugEnabled())
					LOG.debug("Posting internal event"
							+ ": type=" + internalEvent.geteventType()
							+ ", name=" + internalEvent.geteventName()
							+ ", source=" + internalEvent.geteventSource()
							+ ", info=" + internalEvent.geteventInfo()
							+ " to topic " + topics[i]);
				this.eventMgr.publishInternalEvent(internalEvent);
			} catch (EMSException emse) {

				throw new TrustEventMgrException("Could not post internal event"
						+ ": type=" + internalEvent.geteventType()
						+ ", name=" + internalEvent.geteventName()
						+ ", source=" + internalEvent.geteventSource()
						+ ", info=" + internalEvent.geteventInfo()
						+ " to topic " + topics[i]
						+ ": " + emse.getLocalizedMessage(), emse);
			}
		}
	}
	
	private void registerUpdateListener(final ITrustUpdateEventListener listener,
			final String[] topics, final TrustedEntityId teid) throws TrustEventMgrException {
		
		String filter = null;
		if (teid != null)
			filter = "("
				+ CSSEventConstants.EVENT_NAME + "=" + teid.toString()
				+ ")";
			
		if (this.eventMgr == null)
			throw new TrustEventMgrException("Could not register TrustUpdateEvent listener '"
					+ listener + "' to topics " + Arrays.toString(topics)
					+ ": IEventMgr service is not available");
		if (LOG.isInfoEnabled()) 
			LOG.info("Registering TrustUpdateEvent listener to topics "
					+ Arrays.toString(topics));
		this.eventMgr.subscribeInternalEvent(new TrustUpdateEventHandler(listener),
				topics,	filter); // TODO Should we specify filter for event source
	}
	
	private class TrustUpdateEventHandler extends EventListener {

		/** The listener to forward TrustUpdateEvents. */
		private final ITrustUpdateEventListener listener;
		
		private TrustUpdateEventHandler(final ITrustUpdateEventListener listener) {
			
			this.listener = listener;
		}

		/* (non-Javadoc)
		 * @see org.societies.api.osgi.event.EventListener#handleInternalEvent(org.societies.api.osgi.event.InternalEvent)
		 */
		@Override
		public void handleInternalEvent(InternalEvent internalEvent) {
			
			if (LOG.isDebugEnabled())
				LOG.debug("Received internal event"
					+ ": type=" + internalEvent.geteventType()
					+ ", name=" + internalEvent.geteventName()
					+ ", source=" + internalEvent.geteventSource());
			
			if (!(internalEvent.geteventInfo() instanceof TrustUpdateEventInfo))
				LOG.error("Cannot handle internal event"
						+ ": type=" + internalEvent.geteventType()
						+ ", name=" + internalEvent.geteventName()
						+ ", source=" + internalEvent.geteventSource()
						+ ": Unexpected eventInfo: " + internalEvent.geteventInfo());
			final TrustUpdateEventInfo eventInfo = (TrustUpdateEventInfo) internalEvent.geteventInfo();
			
			TrustedEntityId teid;
			try {
				teid = new TrustedEntityId(internalEvent.geteventName());
				final TrustUpdateEvent event = new TrustUpdateEvent(teid,
						eventInfo.getOldValue(), eventInfo.getNewValue());
				if (LOG.isDebugEnabled())
					LOG.debug("Forwarding TrustUpdateEvent " + event + " to listener");
				this.listener.onUpdate(event);
			} catch (MalformedTrustedEntityIdException mteide) {
				
				LOG.error("Cannot forward TrustUpdateEvent to listener:"
						+ " Failed to create TrustedEntityId from internal event name:"
						+ internalEvent.geteventName(), mteide);
			}	
			
		}

		/* (non-Javadoc)
		 * @see org.societies.api.osgi.event.EventListener#handleExternalEvent(org.societies.api.osgi.event.CSSEvent)
		 */
		@Override
		public void handleExternalEvent(CSSEvent cssEvent) {
			
			LOG.warn("Received unexpected external CSS event"
					+ ": type=" + cssEvent.geteventType()
					+ ", name=" + cssEvent.geteventName()
					+ ", source=" + cssEvent.geteventSource());
		}
	}
	
	private class TrustUpdateEventInfo implements Serializable {
		
		private static final long serialVersionUID = -8227968800295980580L;

		private final Double oldValue;
		
		private final Double newValue;
		
		private TrustUpdateEventInfo(final Double oldValue, final Double newValue) {
			
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
		private Double getOldValue() {
			
			return this.oldValue;
		}
		
		private Double getNewValue() {
			
			return this.newValue;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			
			final StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("oldValue=" + this.getOldValue());
			sb.append(",");
			sb.append("newValue=" + this.getNewValue());
			sb.append("}");
			
			return sb.toString();
		}
	}
}