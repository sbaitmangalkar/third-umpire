package com.voter.info.service;

import com.voter.info.model.Voter;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public interface VoterService {
	public abstract Voter findVoter(String name);
	public abstract Voter findVoter(String name, String polingBooth);
}
