package org.societies.platform.socialdata.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.shindig.social.opensocial.model.Group;
import org.json.JSONObject;

public class GroupConverterFromSN implements GroupConverter {

	@Override
	public List<Group> load(String data) {
		
		return new ArrayList<Group>();
	}

}
