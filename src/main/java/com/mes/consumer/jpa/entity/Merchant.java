package com.mes.consumer.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="poc_consumer_merchant")
@Getter
@Setter
@NoArgsConstructor
public class Merchant {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "active")
	private boolean active;

	@Column(name = "last_updated")
	private Timestamp lastUpdated;

}
