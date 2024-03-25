package com.mes.consumer.jpa.repo;

import com.mes.consumer.jpa.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantJpaRepository extends JpaRepository<Merchant,Long> {

	Merchant findByName(String name);
}
