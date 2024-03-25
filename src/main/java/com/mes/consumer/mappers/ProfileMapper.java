package com.mes.consumer.mappers;

import com.mes.consumer.domain.model.Profile;
import com.mes.event.dto.ProfileDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(injectionStrategy = InjectionStrategy.FIELD)
public interface ProfileMapper {

	//ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

	@Mapping(target="merchantName", source="merchantName", qualifiedByName = "charSequenceToString")
	@Mapping(target = "active", source = "status")
	Profile  profileDtoToProfile(ProfileDTO profileDTO);

	@Named("charSequenceToString")
   default String charSequenceToString(CharSequence source){
	   return source.toString();
   }

}
