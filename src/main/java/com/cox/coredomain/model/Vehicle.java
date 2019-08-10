package com.cox.coredomain.model;

import java.math.BigInteger;

public class Vehicle {
	
	private BigInteger vehicleId;
	
	private Integer year;
	
	private String make;
	
	private String model;
	
	private BigInteger dealerId;

	public BigInteger getVehicleId() {
		return vehicleId;
	}

	public Vehicle setVehicleId(BigInteger vehicleId) {
		this.vehicleId = vehicleId;
		return this;
	}

	public Integer getYear() {
		return year;
	}

	public Vehicle setYear(Integer year) {
		this.year = year;
		return this;
	}

	public String getMake() {
		return make;
	}

	public Vehicle setMake(String make) {
		this.make = make;
		return this; 
	}

	public String getModel() {
		return model;
	}

	public Vehicle setModel(String model) {
		this.model = model;
		return this;
	}

	public BigInteger getDealerId() {
		return dealerId;
	}

	public Vehicle setDealerId(BigInteger dealerId) {
		this.dealerId = dealerId;
		return this;
	}
}
