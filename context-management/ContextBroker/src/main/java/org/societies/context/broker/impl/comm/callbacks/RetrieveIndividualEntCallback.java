package org.societies.context.broker.impl.comm.callbacks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelObject;
import org.societies.context.broker.impl.comm.ICtxCallback;

public class RetrieveIndividualEntCallback implements ICtxCallback{

	
	/** The logging facility. */
	private static final Logger LOG = LoggerFactory.getLogger(RetrieveCtxCallback.class);
	
	CtxEntityIdentifier entityId;
	
	
	
	@Override
	public void onCreatedEntity(CtxEntity retObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreatedAttribute(CtxAttribute retObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLookupCallback(List<CtxIdentifier> ctxIdsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRetrieveCtx(CtxModelObject ctxObj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateCtx(CtxModelObject ctxObj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveCtxResult(Object retObject, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRetrieveIndiEnt(CtxEntityIdentifier ctxId) {
		
		LOG.info("onRetrieveIndiEnt ctxId " +ctxId);
		this.entityId = ctxId;
		synchronized (this) {	            
			notifyAll();	        
		}
		LOG.info("onRetrieveIndiEnt, notify all done");
		
	}

	
	public CtxEntityIdentifier getResult() {
		return this.entityId;
	}
	
}
