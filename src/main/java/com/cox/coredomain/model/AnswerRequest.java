package com.cox.coredomain.model;

import java.util.ArrayList;
import java.util.List;

public class AnswerRequest {

	private List<VehicleDealerDataWrapper> dealers;

	public List<VehicleDealerDataWrapper> getDealers() {
		if (dealers == null) {
			dealers = new ArrayList<>();
		}
		return dealers;
	}

	public void setDealers(List<VehicleDealerDataWrapper> dealers) {
		this.dealers = dealers;
	}
}
