package com.mes.consumer.util;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.UUID;

@Service
@Slf4j
public class Producer<V extends  Serializable> {

	@Autowired
	private KafkaTemplate<String, V> kafkaTemplate;

	public void sendMessage(String topic, V event, String key){
	//	log.info(String.format("Message sent -> %s", event.toString()));

		Message<V> message = MessageBuilder
				.withPayload(event)
				.setHeader(KafkaHeaders.MESSAGE_KEY, key)
				.setHeader(KafkaHeaders.TOPIC, topic)
				.build();

		kafkaTemplate.send(message);
		System.out.println(("success: message sent to kafka topic. "));
	}
}
