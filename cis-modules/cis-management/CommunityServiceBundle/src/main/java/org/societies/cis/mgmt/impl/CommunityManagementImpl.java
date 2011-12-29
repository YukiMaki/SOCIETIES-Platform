/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÃ‡ÃƒO, SA (PTIN), IBM ISRAEL
 * SCIENCE AND TECHNOLOGY LTD (IBM), INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA
 * PERIORISMENIS EFTHINIS (AMITEC), TELECOM ITALIA S.p.a.(TI),  TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD
 * (NEC))
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

package org.societies.cis.mgmt.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.societies.cis.mgmt.CommunityManagement;
import org.societies.comm.xmpp.datatypes.Stanza;
import org.societies.comm.xmpp.exceptions.CommunicationException;
import org.societies.comm.xmpp.interfaces.CommManager;
import org.societies.comm.xmpp.interfaces.FeatureServer;
import org.societies.community.Community;
import org.societies.community.Participant;
import org.societies.community.Who;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Joao M. Goncalves (PTIN)
 * 
 *         This is the implementation of both the {@link CommunityManagement}
 *         service and of the {@link FeatureServer} interface. It handles
 *         XEP-SOC1 related logic. Registers on XCCommunicationMgr to receive
 *         stanza elements of namespace http://societies.org/community, and
 *         handles those requests.
 * 
 *         TODO no distinction between get and set... join and leave should be
 *         set and who should be get log exceptions
 * 
 */

@Component
public class CommunityManagementImpl implements CommunityManagement,
		FeatureServer {

	private final static List<String> NAMESPACES = Collections
			.singletonList("http://societies.org/community");
	private final static List<String> PACKAGES = Collections
			.singletonList("org.societies.community");

	private CommManager endpoint;
	private Set<String> participants;
	private Set<String> leaders;

	@Autowired
	public CommunityManagementImpl(CommManager endpoint) {
		participants = new HashSet<String>();
		leaders = new HashSet<String>();
		this.endpoint = endpoint;
		try {
			endpoint.register(this); // TODO unregister??
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getXMLNamespaces() {
		return NAMESPACES;
	}

	@Override
	public List<String> getJavaPackages() {
		return PACKAGES;
	}

	@Override
	public void receiveMessage(Stanza stanza, Object payload) {
		// do nothing
		// no use-case so far for community-received messages
	}

	@Override
	public Object receiveQuery(Stanza stanza, Object payload) {
		// all received IQs contain a community element
		if (payload.getClass().equals(Community.class)) {
			Community c = (Community) payload;
			if (c.getJoin() != null) {
				String jid = stanza.getFrom().getIdentity().toString();
				if (!participants.contains(jid)) {
					participants.add(jid);
				}
				// TODO add error cases to schema
				Community result = new Community();
				result.setJoin(""); // null means no element and empty string
									// means empty element
				return result;
			}
			if (c.getLeave() != null) {
				String jid = stanza.getFrom().getIdentity().toString();
				if (participants.contains(jid)) {
					participants.remove(jid);
				}
				// TODO add error cases to schema
				Community result = new Community();
				result.setLeave(""); // null means no element and empty string
										// means empty element
				return result;
			}
			if (c.getWho() != null) {
				// TODO add error cases to schema
				Community result = new Community();
				Who who = new Who();
				for (String jid : participants) {
					Participant p = new Participant();
					p.setJid(jid);
					if (leaders.contains(jid))
						p.setRole("leader");
					else
						p.setRole("participant");
					who.getParticipant().add(p);
				}
				result.setWho(who);
				return result;
			}
		}
		return null;
	}

	@Override
	public Set<String> getParticipants() {
		return new HashSet<String>(participants);
	}

	@Override
	public Set<String> getLeaders() {
		return new HashSet<String>(leaders);
	}

	@Override
	public String getOwner() {
		// TODO Auto-generated method stub
		return null;
	}
}
