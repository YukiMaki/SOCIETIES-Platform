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
package org.societies.android.api.internal.privacytrust.model.dataobfuscation;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Temperature
 *
 * @author olivierm
 *
 */
public class Temperature implements Parcelable {
	private double degree;
	private String degreeString;
	private boolean resultIsString;
	public static enum heat {burny, hot, cool, cold, freezy};
	public static String[] heatString = {"burny", "hot", "cool", "cold", "freezy"};
	
	public Temperature() {
		super();
	}
	public Temperature(double degree) {
		super();
		this.degree = degree;
		resultIsString = false;
	}
	public Temperature(String degree) {
		super();
		this.degreeString = degree;
		resultIsString = true;
	}
	
	

	/**
	 * @return the degree
	 */
	public double getDegree() {
		return degree;
	}
	/**
	 * @param degree the degree to set
	 */
	public void setDegree(double degree) {
		this.degree = degree;
		resultIsString = false;
	}

	/**
	 * @return the degreeString
	 */
	public String getDegreeString() {
		return degreeString;
	}
	/**
	 * @param degreeString the degreeString to set
	 */
	public void setDegreeString(String degreeString) {
		this.degreeString = degreeString;
		resultIsString = false;
		if (null != degreeString && !degreeString.equals("")) {
			resultIsString = true;
		}
	}

	/**
	 * @return the resultIsString
	 */
	public boolean isResultIsString() {
		return resultIsString;
	}
	/**
	 * @param resultIsString the resultIsString to set
	 */
	public void setResultIsString(boolean resultIsString) {
		this.resultIsString = resultIsString;
	}


	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// -- Verify reference equality
		if (obj == this) {
			return true;
		}

		// -- Verify obj type
		if (obj instanceof Temperature) {
			Temperature other = (Temperature) obj;
			return(this.getDegree() == other.getDegree()
					&& this.getDegreeString().equals(other.getDegreeString())
					&& this.isResultIsString() == other.isResultIsString());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Temperature [degree=" + degree + ", degreeString="
				+ degreeString + ", resultIsString=" + resultIsString + "]";
	}
	
	/* ************************
	 * Parcelable Management
	 * ************************ */
	
	public Temperature(Parcel in) {
		readFromParcel(in);
	}
	
	/*
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		return 0;
	}

	/*
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel out, int flag) {
		out.writeDouble(degree);
		out.writeString(degreeString);
		out.writeInt(resultIsString ? 1 : 0);
	}
	
	private void readFromParcel(Parcel in) {
		degree = in.readDouble();
		degreeString = in.readString();
		resultIsString = in.readInt() == 1 ? true : false;
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Temperature createFromParcel(Parcel in) {
			return new Temperature(in);
		}

		public Temperature[] newArray(int size) {
			return new Temperature[size];
		}
	};
}
