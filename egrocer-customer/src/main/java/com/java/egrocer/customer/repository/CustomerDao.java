package com.java.egrocer.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.java.egrocer.customer.model.CustomerEntity;

@Repository
public interface CustomerDao extends JpaRepository<CustomerEntity, Long> {
	CustomerEntity findByUsername(String username);

}