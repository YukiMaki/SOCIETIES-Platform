package org.societies.context.user.db.test.mock;

import org.societies.api.context.CtxException;
import org.societies.api.context.event.CtxChangeEventListener;
import org.societies.api.context.event.CtxEvent;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.context.api.event.CtxEventScope;
import org.societies.context.api.event.ICtxEventMgr;
import org.springframework.stereotype.Service;

@Service
public class MockCtxEventMgr implements ICtxEventMgr {

	@Override
	public void post(CtxEvent arg0, String[] arg1, CtxEventScope arg2)
			throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerChangeListener(CtxChangeEventListener arg0,
			String[] arg1, CtxIdentifier arg2) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerChangeListener(CtxChangeEventListener arg0,
			String[] arg1, CtxEntityIdentifier arg2, String arg3)
			throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterChangeListener(CtxChangeEventListener arg0,
			String[] arg1, CtxIdentifier arg2) throws CtxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterChangeListener(CtxChangeEventListener arg0,
			String[] arg1, CtxEntityIdentifier arg2, String arg3)
			throws CtxException {
		// TODO Auto-generated method stub

	}

}