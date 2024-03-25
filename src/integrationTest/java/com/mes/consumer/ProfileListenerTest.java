package com.mes.consumer;

import com.mes.consumer.constant.KafkaConstants;
import com.mes.consumer.configuration.ConsumerApplicationIntegrationTestConfig;
import com.mes.consumer.domain.service.ProfileServiceImpl;
import com.mes.consumer.jpa.entity.Merchant;
import com.mes.consumer.jpa.repo.MerchantJpaRepository;
import com.mes.consumer.listener.ProfileListener;
import com.mes.consumer.mappers.MerchantMapperImpl;
import com.mes.consumer.mappers.ProfileMapperImpl;
import com.mes.consumer.repository.ProfileRepositoryImpl;
import com.mes.event.dto.ProfileDTO;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import java.io.IOException;

@Slf4j
@EmbeddedKafka(partitions = 1, topics = {KafkaConstants.PROFILE_TOPIC}, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(locations = {"classpath:application-integrationTesting.properties"})
@ActiveProfiles("test")
@SpringBootTest(classes = {ConsumerApplicationIntegrationTestConfig.class})
@ContextConfiguration(
		classes = {ProfileListener.class, ProfileServiceImpl.class, ProfileRepositoryImpl.class, MerchantMapperImpl.class, ProfileMapperImpl.class})
@EnableJpaRepositories(basePackageClasses = {MerchantJpaRepository.class})
@EntityScan(basePackageClasses = {Merchant.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileListenerTest {

	@Autowired
	private MockSchemaRegistryClient mockSchemRegistryClient;

	@Autowired
	private ProducerFactory<String, ProfileDTO> producerFactory;

	@Autowired
	private MerchantJpaRepository merchantJpaRepository;

	private static final String merchantTopic = KafkaConstants.PROFILE_TOPIC;
	private static final String subject = KafkaConstants.PROFILE_TOPIC + "-value";

	private static final String TEST_SCHEMA_JSON =
			"{\"type\":\"record\",\"name\":\"ProfileDTO\",\"namespace\":\"com.mes.event.dto\",\"fields\":[{\"name\":\"merchantName\",\"type\":\"string\",\"default\":\"null\"},{\"name\":\"status\",\"type\":\"boolean\",\"default\":false}]}";

	@BeforeAll
	public void setUp() throws RestClientException, IOException {
		mockSchemRegistryClient.register(subject, new Schema.Parser().parse(TEST_SCHEMA_JSON));
		dbSetup();
	}

	private void dbSetup() {
		merchantJpaRepository.deleteAll();
	}

	@Test
	@DisplayName(("Test profile consumer receives and persists data in db"))
	public void testConsumerProcessTheValidMessageCorrectly() throws InterruptedException {

		ProfileDTO profileDTO = generateKafkaEvent();
		whenProfileMessage_SentToKafka(profileDTO);
		thenWaitForConsumer();
		thenConsumerReceives_AndDataIsPersistedInDB(profileDTO);
	}

	private void whenProfileMessage_SentToKafka(ProfileDTO event){
		Producer<String, ProfileDTO> producer = producerFactory.createProducer();
		producer.send(new ProducerRecord<>(merchantTopic, "test_key", event));
		producer.close();
	}

	private void thenWaitForConsumer() throws InterruptedException {
		Thread.sleep(2000);
	}

	private void thenConsumerReceives_AndDataIsPersistedInDB(ProfileDTO sourceEvent) {
		assertNotNull(merchantJpaRepository.findByName(sourceEvent.getMerchantName().toString()));
	}

	private ProfileDTO generateKafkaEvent() {
		ProfileDTO profileDTO = new ProfileDTO();
		profileDTO.setMerchantName("test_merchant");
		profileDTO.setStatus(true);
		return profileDTO;
	}

	@AfterAll
	public void tearDown() throws RestClientException, IOException {
		mockSchemRegistryClient.deleteSubject(subject);
	}

}
