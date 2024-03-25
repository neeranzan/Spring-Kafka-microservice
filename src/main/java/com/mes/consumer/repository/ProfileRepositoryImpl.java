package com.mes.consumer.repository;

import com.mes.consumer.domain.model.Profile;
import com.mes.consumer.jpa.repo.MerchantJpaRepository;
import com.mes.consumer.mappers.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProfileRepositoryImpl implements ProfileRepository {

	@Autowired
	private MerchantJpaRepository merchantJpaRepository;

	@Autowired
	private MerchantMapper merchantMapper;

	@Override
	public void save(Profile profile) {
		merchantJpaRepository.save(merchantMapper.profileToMerchant(profile));
		log.info("merchant information saved to db.");
	}
}
