package com.mes.consumer.listener;

import com.mes.consumer.domain.service.ProfileService;
import com.mes.consumer.mappers.ProfileMapper;
import com.mes.event.dto.ProfileDTO;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.mes.consumer.constant.KafkaConstants.PROFILE_TOPIC;

@Component
public class ProfileListener {

	@Autowired
	private ProfileService profileService;

	@Autowired ProfileMapper profileMapper;


	@KafkaListener(topics = PROFILE_TOPIC)
	public void consume(ProfileDTO profileDTO, Acknowledgment ack){
		profileService.save(profileMapper.profileDtoToProfile(profileDTO));
		ack.acknowledge();
	}

}
