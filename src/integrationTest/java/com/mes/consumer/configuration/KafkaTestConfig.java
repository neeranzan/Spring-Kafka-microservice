package com.mes.consumer.configuration;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaTestConfig {

	@Autowired
	private KafkaProperties properties;

	@Bean
	MockSchemaRegistryClient schemaRegistryClient() {
		return new MockSchemaRegistryClient();
	}


	@Bean
	KafkaAvroSerializer kafkaAvroSerializer() {
		return new KafkaAvroSerializer(schemaRegistryClient());
	}

	@Bean
	KafkaAvroDeserializer kafkaAvroDeserializer() {
		return new KafkaAvroDeserializer(schemaRegistryClient(),properties.buildConsumerProperties());
	}

	@Bean
	DefaultKafkaProducerFactory producerFactory() {
		return new DefaultKafkaProducerFactory(
				properties.buildProducerProperties(),
				new StringSerializer(),
				kafkaAvroSerializer()
		);
	}

	@Bean
	DefaultKafkaConsumerFactory consumerFactory() {
	return 	new DefaultKafkaConsumerFactory(
				properties.buildConsumerProperties(),
				new StringDeserializer(),
				kafkaAvroDeserializer()
		);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		return factory;
	}
}
