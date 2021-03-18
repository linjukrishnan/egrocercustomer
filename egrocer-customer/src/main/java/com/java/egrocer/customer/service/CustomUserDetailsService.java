package com.java.egrocer.customer.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.egrocer.customer.model.CustomerEntity;
import com.java.egrocer.customer.model.Customer;
import com.java.egrocer.customer.repository.CustomerDao;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = null;
		
		CustomerEntity customer = customerDao.findByUsername(username);
		if (customer != null) {
			roles = Arrays.asList(new SimpleGrantedAuthority(customer.getRole()));
			return new User(customer.getUsername(), customer.getPassword(), roles);
		}
		throw new UsernameNotFoundException("User not found with the name " + username);	}
	
	public CustomerEntity save(Customer customerDTO) {
		CustomerEntity customer = new CustomerEntity();
		customer.setUsername(customerDTO.getUsername());
		customer.setPassword(bcryptEncoder.encode(customerDTO.getPassword()));
		customer.setRole(customerDTO.getRole());
		return customerDao.save(customer);
	}

}
