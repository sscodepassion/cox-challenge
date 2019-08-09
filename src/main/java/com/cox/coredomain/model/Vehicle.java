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

	public void setVehicleId(BigInteger vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public BigInteger getDealerId() {
		return dealerId;
	}

	public void setDealerId(BigInteger dealerId) {
		this.dealerId = dealerId;
	}
}
