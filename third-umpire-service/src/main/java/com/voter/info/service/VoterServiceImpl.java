package com.voter.info.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.voter.info.model.UserRequest;
import com.voter.info.model.Voter;

/**
 * Service layer implementation
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class VoterServiceImpl implements VoterService {

	@Override
	public List<Voter> findVoter(UserRequest request) {
		List<Voter> voters = null;
		String firstName = request.getFirstName();
		String middleName = request.getMiddleName();
		String lastName = request.getLastName();
		
		String districtName = request.getDistrict();
		String assemblyConstituencyName = request.getAssemblyConstituencyName();
		if(districtName == null || districtName.equals("")) {
			throw new IllegalArgumentException("Please provide a valid district name for a faster search!!");
		}
		
		//TODO: Change this condition check to support "All" district search
		if(assemblyConstituencyName != null && !assemblyConstituencyName.equals("") && !assemblyConstituencyName.equals("All")) {
			voters = PersonFinder.findPerson(firstName, middleName, lastName, districtName, assemblyConstituencyName);
		} else {
			voters = PersonFinder.findPerson(firstName, middleName, lastName, districtName);
		}
		return voters;
	}

	@Override
	public List<String> getAllAssemblyConstituencies(String districtName) {
		return AssemblyConstituencyFinder.findAllAssemblyConstituencies(districtName)
		                                 .entrySet()
		                                 .stream()
		                                 .map(e -> e.getKey())
		                                 .collect(Collectors.toList());
	}

	@Override
	public List<String> getAllDistricts() {
		return DistrictFinder.getAllDistrictDetails()
		                     .entrySet()
		                     .stream()
		                     .map(e -> e.getKey())
		                     .collect(Collectors.toList());
	}

	@Override
	public Map<String, List<String>> getDistrictsWithAssemblyConstituencies() {
		
		return DistrictFinder.getAllDistrictDetails()
		                     .entrySet()
		                     .stream()
		                     .collect(Collectors.toMap(entry -> entry.getKey(), entry -> getAllAssemblyConstituencies(entry.getKey())));
		              
	}
	
}
