package com.cox.coredomain.model;

import java.math.BigInteger;
import java.util.Objects;

public class Dealer {
	
	private BigInteger dealerId;
	
	private String name;
	
	public Dealer() {}
	
	public Dealer(BigInteger dealerId) {
		this.dealerId = dealerId;
	}

	public BigInteger getDealerId() {
		return dealerId;
	}

	public void setDealerId(BigInteger dealerId) {
		this.dealerId = dealerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dealerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		
		if (!(obj instanceof Dealer)) {
			return false;
		}
		
		Dealer dealer = (Dealer) obj;
		
		return Objects.equals(dealerId, dealer.dealerId);
	}
}