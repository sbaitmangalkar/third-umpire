package com.voter.info.service;

import java.util.List;

import com.voter.info.model.UserRequest;
import com.voter.info.model.Voter;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public interface VoterService {
	public abstract List<Voter> findVoter(UserRequest request);
	public abstract List<String> getAllDistricts();
	public abstract List<String> getAllAssemblyConstituencies(String districtName);
}
