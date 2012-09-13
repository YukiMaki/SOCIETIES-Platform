/**
Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 

(SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp (IBM),
INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
ITALIA S.p.a.(TI), TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.societies.android.api.internal.cismanager;

import java.util.List;

import org.societies.api.schema.cis.community.Community;


import android.os.Parcel;
import android.os.Parcelable;
/**
 * Android version of {@link CSSProfile}. Implements Parcelable interface for
 * Android IPC.
 *
 */

public class AndroidCISRecord extends Community implements Parcelable {
	
	/**
	 * Default constructor
	 */
	public AndroidCISRecord() {
		super();
	}							   
	public static AndroidCISRecord convertCisRecord(Community cis) {
		AndroidCISRecord arecord = new AndroidCISRecord();

	
		arecord.setCommunityJid(cis.getCommunityJid());
		arecord.setCommunityName(cis.getCommunityName());
		arecord.setCommunityType(cis.getCommunityType());
		arecord.setDescription(cis.getDescription());
		arecord.setOwnerJid(cis.getOwnerJid());
		// TODO: add criteria and what is remaining
		
		return arecord;
	}

	/**
	 * Private constructor 
	 * Must read from Parcel in exact same sequence and reverse method of writeToParcel
	 * 
	 * @param in parcel
	 */
	private AndroidCISRecord(Parcel in) {
		setCommunityJid(in.readString());
		setCommunityName(in.readString());
		setCommunityType(in.readString());
		setDescription(in.readString());
		setOwnerJid(in.readString());
	
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	/**
	 * properties must be written in exact sequence as private constructor
	 */
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(getCommunityJid());
		out.writeString(getCommunityName());
		out.writeString(getCommunityType());
		out.writeString(getDescription());
		out.writeString(getOwnerJid());

	}
	public static final Parcelable.Creator<AndroidCISRecord> CREATOR = new Parcelable.Creator<AndroidCISRecord>() {

		@Override
		public AndroidCISRecord createFromParcel(Parcel in) {
			return new AndroidCISRecord(in);
		}

		@Override
		public AndroidCISRecord[] newArray(int size) {
			return new AndroidCISRecord [size];
		}
		
	};
	
}
