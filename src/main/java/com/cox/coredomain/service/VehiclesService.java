package com.cox.coredomain.service;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.cox.coredomain.model.Vehicle;

public interface VehiclesService {
	
	public List<BigInteger> findAllVehiclesForDataSet(String datasetId);
	
	public CompletableFuture<Vehicle> findVehicleForDataSetByVehicleId(String datasetId, BigInteger vehicleId);
}
