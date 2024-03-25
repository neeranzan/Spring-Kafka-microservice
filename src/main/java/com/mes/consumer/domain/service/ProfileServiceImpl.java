package com.mes.consumer.domain.service;

import com.mes.consumer.domain.model.Profile;
import com.mes.consumer.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements  ProfileService{

	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public void save(Profile profile) {
		profileRepository.save(profile);
	}
}
