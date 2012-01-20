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

package org.societies.personalisation.UserPreferenceLearning.impl;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.context.model.CtxHistoryAttribute;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.mock.EntityIdentifier;
import org.societies.api.personalisation.model.IAction;
import org.societies.personalisation.preference.api.model.IPreferenceCondition;
import org.societies.personalisation.preference.api.model.ContextPreferenceCondition;
import org.societies.personalisation.preference.api.model.OperatorConstants;

public class CtxIdentifierCache {

    Hashtable<EntityIdentifier, Hashtable<String, CtxIdentifier>> dataOwner_cache;

    public CtxIdentifierCache(){
        dataOwner_cache = new Hashtable<EntityIdentifier, Hashtable<String, CtxIdentifier>>();
    }
    
    public void cacheCtxIdentifiers(EntityIdentifier dataOwner, Map<CtxHistoryAttribute, List<CtxHistoryAttribute>> history){
        Iterator<CtxHistoryAttribute> history_it = history.keySet().iterator();
        while(history_it.hasNext()){
            CtxHistoryAttribute nextPrimary = (CtxHistoryAttribute)history_it.next();
            List<CtxHistoryAttribute> context = (List<CtxHistoryAttribute>)history.get(nextPrimary);
            Iterator<CtxHistoryAttribute> context_it = context.iterator();
            while(context_it.hasNext()){
                CtxHistoryAttribute nextAttr = (CtxHistoryAttribute)context_it.next();
                storeCtxIdentifier(dataOwner, nextAttr.getType(), nextAttr.getId());
            }
        }
    }
    
    public IPreferenceCondition getPreferenceCondition(EntityIdentifier dpi, IAction action){
        String parameter = action.getparameterName();
        CtxAttributeIdentifier id = retrieveCtxIdentifier(dpi, parameter);
        IPreferenceCondition translated = new ContextPreferenceCondition(
                id, 
                OperatorConstants.EQUALS, 
                action.getvalue(), 
                parameter);
        
        return translated;
    }
    
    /*public IPreferenceTreeModel translateToCtxIdentifiers(IDigitalPersonalIdentifier dpi, IPreferenceTreeModel tree){
        
        IPreference root = (IPreference)tree.getRootPreference();
        Enumeration<IPreference> preOrder = root.depthFirstEnumeration();
        preOrder.nextElement();  //ignore root
        String parameterName = null;
        while(preOrder.hasMoreElements()){
            IPreference nextNode = preOrder.nextElement();
            if(!nextNode.isLeaf()){
                IAction action = (IAction)nextNode.getUserObject();
                String parameter = action.getparameterName();
                ICtxAttributeIdentifier id = retrieveCtxIdentifier(dpi, parameter);
                IPreferenceCondition translated = new ContextPreferenceCondition(
                        id, 
                        OperatorConstants.EQUALS, 
                        action.getvalue(), 
                        parameter);
                nextNode.setUserObject(translated);
            }else{
                if(parameterName == null){
                    IOutcome action = (IOutcome)nextNode.getUserObject();
                    parameterName = action.getparameterName();
                }
            }
        }
        IPreferenceTreeModel treeModel = new PreferenceTreeModel(root);
        treeModel.setPreferenceName(parameterName);
        return treeModel;
    }*/

    /*
     * Helper methods
     */
    private void storeCtxIdentifier(EntityIdentifier dataOwner, String type, CtxIdentifier id){    
        Hashtable<String, CtxIdentifier> type_cache = null;
        if(dataOwner_cache.containsKey(dataOwner)){
            //System.out.println("dpi_cache already contains dpi: "+dpi.toString());
            type_cache = dataOwner_cache.get(dataOwner);
            if(!type_cache.containsKey(type)){
                //System.out.println("Storing "+type+" with CtxIdentifier: "+id.toString());
                type_cache.put(type, id);
            }
        }else{
            //System.out.println("DPI: "+dpi.toString()+" not found in dpi_cache, creating");
            type_cache = new Hashtable<String, CtxIdentifier>();
            //System.out.println("Storing "+type+" with CtxIdentifier: "+id.toString());
            type_cache.put(type, id);
            dataOwner_cache.put(dataOwner, type_cache);
        }
    }

    private CtxAttributeIdentifier retrieveCtxIdentifier(EntityIdentifier dataOwner, String type){
        CtxAttributeIdentifier id = null;
        Hashtable<String, CtxIdentifier> type_cache = dataOwner_cache.get(dataOwner);
        if(type_cache != null){
            id = (CtxAttributeIdentifier)type_cache.get(type);
            if(id == null){
                System.out.println("Could not find an CtxIdentifier for this type: "+type);
            }
        }else{
            System.out.println("Could not find a type_cache for this data owner: "+dataOwner.toString());
        }
        
        return id;
    }
}