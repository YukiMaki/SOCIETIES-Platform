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
package org.societies.privacytrust.privacyprotection.dataobfuscation.wrapper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.societies.privacytrust.privacyprotection.dataobfuscation.DataWrapper;
import org.societies.privacytrust.privacyprotection.dataobfuscation.obfuscator.SampleObfuscator;

/**
 * This is a sample wrapper, it doing nothing
 * @state skeleton 
 * @author olivierm
 */
public class SampleWrapper extends DataWrapper {
	private SampleObfuscator obfuscator;
	
	// -- CONSTRUCTOR
	public SampleWrapper(int param1) {
		super();
		obfuscator = new SampleObfuscator(param1);
		setObfuscator(obfuscator);
		setAsReadyForObfuscation();
	}

	
	// -- GET/SET
	/**
	 * @return the param1
	 */
	public double getParam1() {
		return obfuscator.getParam1();
	}
	/**
	 * @param param1 the param1 to set
	 */
	public void setParam1(int param1) {
		obfuscator.setParam1(param1);
		if (-1 != obfuscator.getParam1()) {
			setAsReadyForObfuscation();
		}
	}


	/*
	 * @see org.societies.privacytrust.privacyprotection.dataobfuscation.DataWrapper#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// -- Verify reference equality
        if (obj == this) {
            return true;
        }
        
        // -- Verify obj type
        if (obj instanceof SampleWrapper) {
        	SampleWrapper other = (SampleWrapper) obj;
        	return (new EqualsBuilder()
	            .append(this.getParam1(), other.getParam1())
	            .isEquals()) && (super.equals(obj));
        }
        
        return false;
	}
}