package com.cox.coredomain.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class VehicleIDsWrapper {

	private List<BigInteger> vehicleIds;

	public List<BigInteger> getVehicleIds() {
		
		if (vehicleIds == null) {
			vehicleIds = new ArrayList<>();
		}
		
		return vehicleIds;
	}

	public void setVehicleIds(List<BigInteger> vehicleIds) {
		this.vehicleIds = vehicleIds;
	}
}
