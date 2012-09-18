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
package org.societies.android.api.cis.directory;

import org.societies.android.api.cis.management.AMembershipCrit;
import org.societies.api.schema.cis.directory.CisAdvertisementRecord;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describe your class here...
 *
 * @author aleckey
 *
 */
public class ACisAdvertisementRecord extends CisAdvertisementRecord implements Parcelable {

	private static final long serialVersionUID = -7971431315356401469L;

	public AMembershipCrit getMembershipCrit() {
		return (AMembershipCrit)this.getMembershipCrit();
	}
	
	public void setMembershipCrit(AMembershipCrit amembershipCrit) {
		super.setMembershipCrit(amembershipCrit);
	}
	
	public ACisAdvertisementRecord() {
		super();
	}
	
	/* @see android.os.Parcelable#describeContents()*/
	public int describeContents() {
		return 0;
	}

	/* @see android.os.Parcelable#writeToParcel(android.os.Parcel, int) */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getId() );
		dest.writeString(this.getCssownerid());
		dest.writeString(this.getName());
		dest.writeString(this.getPassword());
		dest.writeString(this.getType());
		dest.writeParcelable(this.getMembershipCrit(), flags);
	}
	
	private ACisAdvertisementRecord(Parcel in) {
		this.setId(in.readString());
		this.setCssownerid(in.readString());
		this.setName(in.readString());
		this.setPassword(in.readString());
		this.setType(in.readString());
		this.setMembershipCrit((AMembershipCrit) in.readParcelable(this.getClass().getClassLoader()));
	}

	public static final Parcelable.Creator<ACisAdvertisementRecord> CREATOR = new Parcelable.Creator<ACisAdvertisementRecord>() {
		public ACisAdvertisementRecord createFromParcel(Parcel in) {
			return new ACisAdvertisementRecord(in);
		}

		public ACisAdvertisementRecord[] newArray(int size) {
			return new ACisAdvertisementRecord[size];
		}
	};
	
	public static ACisAdvertisementRecord convertCisAdvertRecord(CisAdvertisementRecord record) {
		ACisAdvertisementRecord arecord = new ACisAdvertisementRecord();
		arecord.setCssownerid(record.getCssownerid());
		arecord.setId(record.getId());
		arecord.setName(record.getName());
		arecord.setPassword(record.getPassword());
		arecord.setType(record.getType());
		arecord.setMembershipCrit(AMembershipCrit.convertMembershipCrit(record.getMembershipCrit()));
		
		return arecord;
	}
}