package org.societies.api.identity;

import java.util.Set;

import org.societies.utilities.annotations.SocietiesExternalInterface;
import org.societies.utilities.annotations.SocietiesExternalInterface.SocietiesInterfaceType;

/**
 * Provides methods for Identity Management
 */
@SocietiesExternalInterface(type = SocietiesInterfaceType.PROVIDED)
public interface IIdentityManager {
	/**
	 * Parses a Jabber ID to an IIdentity object, eg, john@societies.local
	 * @param jid
	 * @return
	 * @throws InvalidFormatException
	 */
	IIdentity fromJid(String jid)  throws InvalidFormatException;
	
	/**
	 * Parses a full Jabber ID including the resource to an IIdentity object
	 * eg, john@societies.local/laptop
	 * @param jid
	 * @return
	 * @throws InvalidFormatException
	 */
	INetworkNode fromFullJid(String jid) throws InvalidFormatException;
	
	// TODO these methods should be Internal
	/**
	 * Returns the identity of this node
	 * @return
	 */
	INetworkNode getThisNetworkNode();
	
	/** Ctx Requirement
	 * @return
	 */
	IIdentityContextMapper getIdentityContextMapper();
	
	/** Pseudonym check methods
	 * 
	 * @return
	 */
	Set<IIdentity> getPublicIdentities();
	
	/**
	 * Checks if an Identity is the current identity
	 * @param identity
	 * @return
	 */
	boolean isMine(IIdentity identity);
	
	/** Pseudonym mgmt methods
	 * 
	 * @param memorableIdentifier
	 * @return
	 */
	IIdentity newMemorableIdentity(String memorableIdentifier);
	
	boolean releaseMemorableIdentity(IIdentity memorableIdentity);
	
	IIdentity newTransientIdentity();
	
	// TODO this should be the External method (available to 3rd party services)
	// 3rd parties should run in an identity sandbox
	//IIdentity getIdentity();
	
	
	/** Returns the Domain Authority Node Identity
	 * @return
	 */
	INetworkNode getDomainAuthorityNode();
}
