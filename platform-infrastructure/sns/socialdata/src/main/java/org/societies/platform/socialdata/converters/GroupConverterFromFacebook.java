package org.societies.platform.socialdata.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.shindig.social.core.model.GroupImpl;
import org.apache.shindig.social.opensocial.spi.GroupId.Type;
import org.apache.shindig.social.opensocial.model.Group;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GroupConverterFromFacebook implements GroupConverter {

	private static String DATA = "data";
	@Override
	public List<Group> load(String data) {
		
		List<Group> groups = new ArrayList<Group>();
		try {
			JSONObject jsonData = new JSONObject(data);
			if (jsonData.has(DATA)){
				JSONArray jgroups = jsonData.getJSONArray(DATA);
				for(int i=0; i<jgroups.length(); i++){
					Group g = new GroupImpl();
					JSONObject jGroup = jgroups.getJSONObject(i);
					g.setDescription(jGroup.getString("name"));
					g.setTitle("facebook");
					g.setId(new GroupId(Type.groupId, "facebook:"+jGroup.getString("id")));
					groups.add(g);
					
				}
				
				
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return groups;
	}

}
