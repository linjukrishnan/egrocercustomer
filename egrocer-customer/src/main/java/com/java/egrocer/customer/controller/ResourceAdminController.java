package com.java.egrocer.customer.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.egrocer.customer.model.GrocerProducts;
import com.java.egrocer.customer.model.Product;

@RestController
@RequestMapping("/admin")
public class ResourceAdminController {
	
	private String catalogUri;

	@Value("${catalog.uri}")
	public void setCatalogUri(String catalogUri) {
		this.catalogUri = catalogUri;
	}
	
	@Autowired
    private RestTemplate restTemplate;

	@RequestMapping(method = RequestMethod.GET, value = "/adminHome")
	public <T>T getAdminLandingPage()
	{
		GrocerProducts grocerProducts = restTemplate.getForObject(catalogUri+"/getAllProducts",GrocerProducts.class);
		List<Product> productList = (List<Product>) grocerProducts.getGrocerProductMap().get("productList");
		if(!productList.isEmpty()) {
			return (T) grocerProducts;
		}
		return (T) "No products available.Kindly add new product.";
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/addProduct")
	public ResponseEntity<String> addProduct(@RequestBody Map<String,Object> productDetails) {
		
		ObjectMapper mapper = new ObjectMapper(); 
		Product product = mapper.convertValue(productDetails, Product.class);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Product> request = new HttpEntity<Product>(product, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(catalogUri+"/addProduct",request,String.class);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/updateProduct")
	public ResponseEntity<String> updateProduct(@RequestBody Map<String,Object> productDetails) {
		
		ObjectMapper mapper = new ObjectMapper(); 
		Product product = mapper.convertValue(productDetails, Product.class);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Product> request = new HttpEntity<Product>(product, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(catalogUri+"/updateProduct",request,String.class);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/deleteProduct")
	public ResponseEntity<String> deleteProduct(@RequestBody Map<String,Object> productDetails) {
		
		ObjectMapper mapper = new ObjectMapper(); 
		Product product = mapper.convertValue(productDetails, Product.class);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Product> request = new HttpEntity<Product>(product, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(catalogUri+"/deleteProduct",request,String.class);
		return response;
	}

}
