package com.java.egrocer.customer.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class GrocerProducts {

	private Map<String,Object> grocerProductMap = new LinkedHashMap<>();

	public Map<String, Object> getGrocerProductMap() {
		return grocerProductMap;
	}

	public void setGrocerProductMap(Map<String, Object> grocerProductMap) {
		this.grocerProductMap = grocerProductMap;
	}
}
