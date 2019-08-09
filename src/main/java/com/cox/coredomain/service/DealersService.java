package com.cox.coredomain.service;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import com.cox.coredomain.model.Dealer;

public interface DealersService {

	public CompletableFuture<Dealer> findDealerForDataSetByDealerId(String datasetId, BigInteger dealerId);
	
}
