package societies.integration.test.bit.user_preference_learning;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.context.model.CtxEntityTypes;
import org.societies.api.context.model.CtxAttributeTypes;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.IdentityType;
import org.societies.api.internal.context.broker.ICtxBroker;
import org.societies.api.internal.personalisation.IPersonalisationManager;
import org.societies.api.personalisation.model.Action;
import org.societies.api.personalisation.model.IAction;
import org.societies.api.schema.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.api.useragent.monitoring.IUserActionMonitor;
import org.junit.*;

import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
public class Tester {
	private IUserActionMonitor uam;
	private IIdentity userId;
	private ICtxBroker ctxBroker;
	private CtxEntity person;
	private CtxAttribute symLocAttribute;
	private CtxAttribute statusAttribute;
	private CtxAttribute activitiesAttribute;
	private IPersonalisationManager personMan;
//	private Logger logging = LoggerFactory.getLogger(this.getClass());
	private IAction action$1;
	private IAction action$2;
	private IAction action$3;
	private ServiceResourceIdentifier id;
	public Tester(){

	}
	
	@Before
	public void setUp(){
		try{
		this.uam = Test748.getUam();
		this.ctxBroker = Test748.getCtxBroker();
		this.userId=new MockIdentity(IdentityType.CSS, "user", "societies.org");
		 id = new ServiceResourceIdentifier();
		this.action$1=new Action(id,"serviceintest","volume","0");
		this.action$2=new Action(id,"serviceintest","volume","10");
		this.action$3=new Action(id,"serviceintest","volume","90");
		this.personMan=Test748.getPersonMan();
		setupContext();
		changeContext("home", "free", "sleep");
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	
	@org.junit.Test
	public void Test(){
		for (int i=1; i<21; i++){
			if(i%3==0){
				uam.monitor(userId, action$1);
			}else if(i%3==1){
				uam.monitor(userId, action$2);
			}else if(i%3==2){
				uam.monitor(userId, action$3);
			}
			if(i%2==0){
				this.changeContext("home", "busy", "working");
			}else if(i%2==1){
				this.changeContext("office", "free", "cafe");
			}
		}
		PreferenceCallBack callback=new PreferenceCallBack();
		this.personMan.getPreference(this.userId,"serviceintest", this.id, "volume", callback);
		Assert.assertTrue(callback.get());
	}
	

	private void changeContext(String symLocValue, String statusValue, String activityValue){
		try {
			this.symLocAttribute.setStringValue(symLocValue);
			this.symLocAttribute = (CtxAttribute) this.ctxBroker.update(symLocAttribute).get();
			
			this.statusAttribute.setStringValue(statusValue);
			this.statusAttribute = (CtxAttribute) this.ctxBroker.update(statusAttribute).get();
			this.activitiesAttribute.setStringValue(activityValue);
			this.activitiesAttribute = (CtxAttribute) this.ctxBroker.update(activitiesAttribute).get();
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * PreTest setup:
	 */
	
	
	
	
	private void setupContext() {
		this.getPersonEntity();
		this.getSymLocAttribute();
		this.getStatusAttribute();
	}
//
	private void getPersonEntity(){
		try {
 		Future<List<CtxIdentifier>> futurePersons = this.ctxBroker.lookup(CtxModelType.ENTITY, CtxEntityTypes.PERSON);
			List<CtxIdentifier> persons = futurePersons.get();
			if (persons.size() == 0){
				person = this.ctxBroker.createEntity(CtxEntityTypes.PERSON).get();
				
			}else{
				person = (CtxEntity) this.ctxBroker.retrieve(persons.get(0)).get();
			}
			
 		
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getSymLocAttribute(){
		try {
			Future<List<CtxIdentifier>> futureAttrs = this.ctxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.LOCATION_SYMBOLIC);
			List<CtxIdentifier> attrs = futureAttrs.get();
			if (attrs.size() == 0){
				symLocAttribute = this.ctxBroker.createAttribute(person.getId(), CtxAttributeTypes.LOCATION_SYMBOLIC).get();
			}else{
				symLocAttribute = (CtxAttribute) this.ctxBroker.retrieve(attrs.get(0)).get();
			}
			
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
			
	}
	private void getStatusAttribute(){
		try {
			Future<List<CtxIdentifier>> futureAttrs = this.ctxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.STATUS);
			List<CtxIdentifier> attrs = futureAttrs.get();
			if (attrs.size() == 0){
				statusAttribute = this.ctxBroker.createAttribute(person.getId(), CtxAttributeTypes.STATUS).get();
			}else{
				statusAttribute = (CtxAttribute) this.ctxBroker.retrieve(attrs.get(0)).get();
			}
			
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getActivitiesAttribute(){
		try {
			Future<List<CtxIdentifier>> futureAttrs = this.ctxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.ACTION);
			List<CtxIdentifier> attrs = futureAttrs.get();
			if (attrs.size() == 0){
				activitiesAttribute = this.ctxBroker.createAttribute(person.getId(), CtxAttributeTypes.ACTION).get();
			}else{
				activitiesAttribute = (CtxAttribute) this.ctxBroker.retrieve(attrs.get(0)).get();
			}
			
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
