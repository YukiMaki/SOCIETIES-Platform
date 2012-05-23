package org.societies.cis.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.cis.management.ICisManagerCallback;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.internal.css.management.ICSSManagerCallback;
import org.societies.api.schema.cis.community.Community;
import org.springframework.beans.factory.annotation.Autowired;


public class CisManagerClientCallback implements ICommCallback {


	private final static List<String> NAMESPACES = Collections
			.unmodifiableList( Arrays.asList("http://societies.org/api/schema/cis/manager",
						  		"http://societies.org/api/schema/cis/community"));
			//.singletonList("http://societies.org/api/schema/cis/manager");
	private final static List<String> PACKAGES = Collections
			//.singletonList("org.societies.api.schema.cis.manager");
			.unmodifiableList( Arrays.asList("org.societies.api.schema.cis.manager",
					"org.societies.api.schema.cis.community"));
	
	private static Logger LOG = LoggerFactory.getLogger(CisManagerClientCallback.class);
	
	private CisManager cisManag;
	private ICisManagerCallback sourceCallback = null;
	
	public CisManagerClientCallback (String clientId, ICisManagerCallback sourceCallback,CisManager cisManag) {
		this.sourceCallback = sourceCallback;
		this.cisManag = cisManag;
	}
	
	@Override
	public List<String> getXMLNamespaces() {	
		return this.NAMESPACES;
	}

	@Override
	public List<String> getJavaPackages() {
		return this.PACKAGES;
	}

	@Override
	public void receiveResult(Stanza stanza, Object payload) {
		
		LOG.info("receive result received");
		// community namespace
		if (payload instanceof Community) {
			LOG.info("Callback with result");
			Community c = (Community) payload ;
			
			
			// join response
			if(c.getJoinResponse() != null){
				LOG.info("Join response received");
				if(c.getJoinResponse().isResult()){
					// updates the list of CIS where I belong
					cisManag.subscribeToCis(new CisRecord(c.getMembershipMode(),c.getCommunityName(),c.getCommunityJid()));
					LOG.info("subscription worked");
					// return callback
					this.sourceCallback.receiveResult(c);
					
					
					
				}
				else{ // there is no result field
					LOG.warn("join response had no result tag");
					this.sourceCallback.receiveResult( (Community)null);
				}
			}
			// end of join response
			
			
			// leave response
			if(c.getLeaveResponse() != null){
				LOG.info("Leave response received");
				if(c.getLeaveResponse().isResult() && (c.getCommunityJid() !=null) ){
					// updates the list of CIS where I belong
					if (!cisManag.unsubscribeToCis(c.getCommunityJid()))
						LOG.info("unsubscription did not worked");
					LOG.info("unsubscription worked");
					// return callback
					this.sourceCallback.receiveResult(c);
					
					
					
				}
				else{ // there is no result field
					LOG.warn("unsubscription response was mallformed");
					this.sourceCallback.receiveResult( (Community)null);
				}
			}
			// end of join response

			// get info response
			if(c.getGetInfoResponse() != null){
				LOG.info("Get info response received");
				if(c.getGetInfoResponse().isResult()  ){
					LOG.info("get info arrived fine");
					// return callback
					this.sourceCallback.receiveResult(c);
					
					
					
				}
				else{ // there is no result field
					LOG.warn("get info failed");
					this.sourceCallback.receiveResult( (Community)null);
				}
			}
			// end of get info response
			
			
		}
	}

	@Override
	public void receiveError(Stanza stanza, XMPPError error) {
		// TODO Auto-generated method stub
		LOG.info("receive error on CisManagerClient" +error.getMessage());

	}

	@Override
	public void receiveInfo(Stanza stanza, String node, XMPPInfo info) {
		// TODO Auto-generated method stub
		LOG.info("receive info on CisManagerClient" + info.getIdentityName());

	}

	@Override
	public void receiveItems(Stanza stanza, String node, List<String> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(Stanza stanza, Object payload) {
		// TODO Auto-generated method stub

	}

}
