package org.pazu.messager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pazu.messager.database.DatabaseClass;
import org.pazu.messager.model.Profile;

public class ProfileService {

	private Map<String, Profile> profiles = DatabaseClass.getProfiles();

	public ProfileService() {
		profiles.put("profile A", new Profile(1, "profile A", "first A",
				"last A"));
		profiles.put("profile B", new Profile(1, "profile B", "first B",
				"last B"));
		profiles.put("profile C", new Profile(1, "profile C", "first C",
				"last C"));
	}

	public List<Profile> getAllProfiles() {
		return new ArrayList<Profile>(profiles.values());
	}

	public Profile getProfile(String profileName) {
		return profiles.get(profileName);
	}

	public Profile addProfile(Profile profile) {
		profile.setId(profiles.size() + 1);
		return profiles.put(profile.getProfileName(), profile);
	}

	public Profile updateProfile(Profile profile) {

		if (profile.getProfileName().isEmpty()) {
			return null;
		}
		return profiles.put(profile.getProfileName(), profile);
	}
	
	public Profile removeProfile(String profileName){
		return profiles.remove(profileName);
	}

}
