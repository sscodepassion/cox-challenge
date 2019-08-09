package com.cox.coredomain.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class VehicleDealerDataWrapper {
	
	private BigInteger dealerId;
	
	private String name;
	
	private List<Vehicle> vehicles;
	
	public BigInteger getDealerId() {
		return dealerId;
	}

	public VehicleDealerDataWrapper setDealerId(BigInteger dealerId) {
		this.dealerId = dealerId;
		return this;
	}

	public String getName() {
		return name;
	}

	public VehicleDealerDataWrapper setName(String name) {
		this.name = name;
		return this;
	}

	public List<Vehicle> getVehicles() {
		
		if (vehicles == null) {
			vehicles = new ArrayList<>();
		}
		
		return vehicles;
	}

	public VehicleDealerDataWrapper setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		return this;
	}
}
