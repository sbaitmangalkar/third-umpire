package com.voter.info.service;

import com.voter.info.model.UserRequest;
import com.voter.info.model.Voter;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class VoterServiceImpl implements VoterService {

	@Override
	public Voter findVoter(UserRequest request) {
		Voter voter = null;
		String name = request.getName();
		String districtName = request.getDistrict();
		String assemblyConstituencyName = request.getAssemblyConstituencyName();
		if(districtName == null || districtName.equals("")) {
			throw new IllegalArgumentException("Please provide a valid district name for a faster search!!");
		}
		
		
		if(assemblyConstituencyName != null && !assemblyConstituencyName.equals("")) {
			voter = PersonFinder.findPerson(name, districtName, assemblyConstituencyName);
		} else {
			voter = PersonFinder.findPerson(name, districtName);
		}
		return voter;
	}
	
}
