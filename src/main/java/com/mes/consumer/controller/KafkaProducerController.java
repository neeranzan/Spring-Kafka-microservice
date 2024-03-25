package com.mes.consumer.controller;

import com.mes.consumer.constant.KafkaConstants;
import com.mes.consumer.util.Producer;
import com.mes.event.dto.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerController {

	@Autowired
	private Producer<ProfileDTO> profileDTOProducer;

	@PostMapping("/publish/profile")
	public ResponseEntity<String> publish(@RequestBody ProfileDTO profileDTO){
		String messageKey="profile_key";
		profileDTOProducer.sendMessage(KafkaConstants.PROFILE_TOPIC,profileDTO, messageKey);
		return ResponseEntity.ok("Message sent to kafka topic");
	}
}