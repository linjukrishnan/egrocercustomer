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
import com.java.egrocer.customer.model.Cart;
import com.java.egrocer.customer.model.CartProduct;
import com.java.egrocer.customer.model.GrocerProducts;
import com.java.egrocer.customer.model.Product;

@RestController
@RequestMapping("/user")
public class ResourceUserController {

	private String catalogUri;
	
	private String cartUri;
	
	private String orderUri;

	@Value("${catalog.uri}")
	public void setCatalogUri(String catalogUri) {
		this.catalogUri = catalogUri;
	}
	
	@Value("${cart.uri}")
	public void setCartUri(String cartUri) {
		this.cartUri = cartUri;
	}
	
	@Value("${order.uri}")
	public void setOrderUri(String orderUri) {
		this.orderUri = orderUri;
	}

	@Autowired
    private RestTemplate restTemplate;
	
	@RequestMapping(method = RequestMethod.GET, value = "/userHome")
	public <T>T getUserLandingPage()
	{
		GrocerProducts grocerProducts = restTemplate.getForObject(catalogUri+"/getAllDetails",GrocerProducts.class);
		List<Product> productList = (List<Product>) grocerProducts.getGrocerProductMap().get("productList");
		if(!productList.isEmpty()) {
			return (T) grocerProducts;
		}
		return (T) "No products available";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getProductByCategory")
	public <T>T getProductByCategory(@RequestBody Map<String,Object> request)
	{
		String category = (String)request.get("category");
		GrocerProducts grocerProducts = restTemplate.getForObject(catalogUri+"/getProductByCategory/"+category,GrocerProducts.class);
		List<Product> productCategoryList = (List<Product>) grocerProducts.getGrocerProductMap().get("productCategoryList");
		if(!productCategoryList.isEmpty()) {
			return (T) grocerProducts;
		}
		return (T) "No products available";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody Map<String,Object> productDetails)
	{
		ObjectMapper mapper = new ObjectMapper(); 
		Cart cart = mapper.convertValue(productDetails, Cart.class);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Cart> request = new HttpEntity<Cart>(cart, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(cartUri+"/addToCart",request,String.class);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAllCartDetails")
	public <T>T getAllCartDetails(@RequestBody Map<String,Object> request)
	{
		try {
			long customerId = Long.valueOf(request.get("customerId").toString());
			GrocerProducts grocerProducts = restTemplate.getForObject(cartUri+"/getAllCartDetails/"+customerId,GrocerProducts.class);
			List<CartProduct> cartProductList = (List<CartProduct>) grocerProducts.getGrocerProductMap().get("cartProductList");
			if(!cartProductList.isEmpty()) {
				return (T) grocerProducts;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return (T) "Cart is empty";
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/deleteFromCart")
	public ResponseEntity<String> deleteFromCart(@RequestBody Map<String,Object> productDetails) {
		
		ObjectMapper mapper = new ObjectMapper(); 
		Cart cart = mapper.convertValue(productDetails, Cart.class);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Cart> request = new HttpEntity<Cart>(cart, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(cartUri+"/deleteFromCart",request,String.class );
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/orderProduct")
	public ResponseEntity<String> orderProduct(@RequestBody Map<String,Object> productDetails)
	{
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Map<String,Object>> request = new HttpEntity<Map<String,Object>>(productDetails, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(orderUri+"/orderProduct",request,String.class );
		return response;
	}
}
