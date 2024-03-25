package com.mes.consumer.mappers;

import com.mes.consumer.domain.model.Profile;
import com.mes.consumer.jpa.entity.Merchant;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.FIELD)
public interface MerchantMapper {

	@Mapping(target="name", source="merchantName")
	@Mapping(target="lastUpdated", ignore = true)
	@Mapping(target="id", ignore=true)
	Merchant profileToMerchant(Profile profile);
}
