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
package org.societies.platform.socialdata.testcase;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.Person;
import org.junit.Test;
import org.societies.platform.socialdata.converters.ActivityConverter;
import org.societies.platform.socialdata.converters.ActivityConverterFactory;
import org.societies.platform.socialdata.converters.ActivityConverterFromGooglePlus;
import org.societies.platform.socialdata.converters.PersonConverter;
import org.societies.platform.socialdata.converters.PersonConverterFactory;
import org.societies.platform.socialdata.converters.PersonConverterFromGooglePlus;
import org.societies.platform.sns.connector.googleplus.GooglePlusConnector;

/**
 * Describe your class here...
 *
 * @author Edgar Domingues (PTIN)
 *
 */
public class GooglePlusConvertersTest {

	@Test
	public void testPersonFactoryCorrectConverter() {		
		assertTrue(PersonConverterFactory.getPersonConverter(new GooglePlusConnector()) instanceof PersonConverterFromGooglePlus);
	}
	
	@Test
	public void testActivityFactoryCorrectConverter() {		
		assertTrue(ActivityConverterFactory.getActivityConverter(new GooglePlusConnector()) instanceof ActivityConverterFromGooglePlus);
	}
	
	@Test
	public void testPersonConverter() throws Exception {
		final String JSON = readStringFromFile("person.json");
		PersonConverter converter = new PersonConverterFromGooglePlus();
		
		Person person = converter.load(JSON);
		
		assertEquals("Edgar Domingues", person.getDisplayName());
	}
	
	@Test
	public void testActivitiesConverter() throws Exception {
		final String JSON = readStringFromFile("activities.json");
		ActivityConverter converter = new ActivityConverterFromGooglePlus();
		
		List<ActivityEntry> activities = converter.load(JSON);
		
		assertEquals(2, activities.size());
		assertEquals("Edgar Domingues", activities.get(0).getActor().getDisplayName());
		assertEquals("Edgar Domingues", activities.get(1).getActor().getDisplayName());
	}
	
	private String readStringFromFile(String fileName) throws IOException {	
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/"+fileName)));
		StringBuilder content = new StringBuilder();
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            content.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return content.toString();
	}
}
