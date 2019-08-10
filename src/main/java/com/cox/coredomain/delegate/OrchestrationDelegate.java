package com.cox.coredomain.delegate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cox.coredomain.model.AnswerRequest;
import com.cox.coredomain.model.AnswerResponse;
import com.cox.coredomain.model.Dealer;
import com.cox.coredomain.model.Vehicle;
import com.cox.coredomain.model.VehicleDealerDataWrapper;
import com.cox.coredomain.service.DataSetService;
import com.cox.coredomain.service.DealersService;
import com.cox.coredomain.service.VehiclesService;

@Component
public class OrchestrationDelegate {

	@Autowired
	private DataSetService datasetService;
	
	@Autowired
	private DealersService dealersService;
	
	@Autowired
	private VehiclesService vehiclesService;
	
	public Optional<AnswerResponse> processAndSaveAnswer() throws InterruptedException, ExecutionException, IOException {
		
		Map<Dealer, List<Vehicle>> dealerVehiclesMap = null;
		
		/*
		 * Retrieve a datasetID
		 */
		Optional<String> datasetID = datasetService.retrieveDataSet();

		if (datasetID.isPresent()) {
			
			/*
			 * Retrieve all Vehicles for the datasetID
			 */
			List<BigInteger> vehicles = vehiclesService.findAllVehiclesForDataSet(datasetID.get());
			
			if (vehicles != null && vehicles.size() > 0) {
				
				List<CompletableFuture<Vehicle>> vehicleCFList = new ArrayList<>(); 
				
				/*
				 * Retrieve Vehicle Details for each Vehicle in the datasetID by iterating through all the Unique Vehicle IDs
				 * This call will be made asynchronously to speed up the process of retrieving Vehicle details and reduce response times
				 */
				for (BigInteger vehicleId : vehicles) {
					
					CompletableFuture<Vehicle> vehicleCF = vehiclesService.findVehicleForDataSetByVehicleId(datasetID.get(), vehicleId);
					vehicleCFList.add(vehicleCF);
				}
				
				if (!vehicleCFList.isEmpty()) {
					
					Map<BigInteger, CompletableFuture<Dealer>> dealerCFMap = new HashMap<>();
					
					for (CompletableFuture<Vehicle> vehicleCF : vehicleCFList) {
						dealerCFMap = retrieveUniqueVehicleDealers(dealerCFMap, vehicleCF, datasetID.get());
					}
					
					dealerVehiclesMap = processDealerAndVehicleData(vehicleCFList, dealerCFMap);
				}
			}
		}
		
		if (!MapUtils.isEmpty(dealerVehiclesMap)) {
			
			/*
			 * Submit the data to the Answer end point and return the response back to the Controller layer
			 */
			return datasetService.saveAnswer(datasetID.get(), 
					createAnswerRequestListForPostingToAnswerResource(dealerVehiclesMap));
		}
		
		return Optional.empty();
	}
	
	private Map<BigInteger, CompletableFuture<Dealer>> retrieveUniqueVehicleDealers( final Map<BigInteger, CompletableFuture<Dealer>> dealerCFMap, 
		CompletableFuture<Vehicle> vehicleCF, String datasetID ) throws InterruptedException, ExecutionException {
		if (vehicleCF.get() != null) {
			Vehicle vehicle = vehicleCF.get();
			
			/*
			 * Retrieve Dealer Details for each Vehicle in the datasetID by iterating through all the Vehicles.
			 * A check should be first done against the Dealer key in the dealerVehiclesMap to avoid multiple calls to Dealers resource on the API
			 * The Vehicles list for the Dealer should also be updated in the process 
			 */
			
			if (!dealerCFMap.containsKey(vehicle.getDealerId())) {
				CompletableFuture<Dealer> dealerCF = dealersService.findDealerForDataSetByDealerId(datasetID, vehicle.getDealerId());
				dealerCFMap.put(vehicle.getDealerId(), dealerCF);
			}
		}
		return dealerCFMap;
	}
	
	private Map<Dealer, List<Vehicle>> processDealerAndVehicleData(	final List<CompletableFuture<Vehicle>> vehicleCFList, 
			final Map<BigInteger, CompletableFuture<Dealer>> dealerCFMap) throws InterruptedException, ExecutionException {
		Map<Dealer, List<Vehicle>> dealerVehiclesMap = new HashMap<>();
		
		if (!vehicleCFList.isEmpty() && !dealerCFMap.isEmpty()) {
			for (CompletableFuture<Vehicle> vehicleCF : vehicleCFList) {
				if (!dealerVehiclesMap.containsKey(new Dealer(vehicleCF.get().getDealerId()))) {
					List<Vehicle> vehiclesList = new ArrayList<>();
					vehiclesList.add(vehicleCF.get());
					dealerVehiclesMap.put(dealerCFMap.get(vehicleCF.get().getDealerId()).get(), vehiclesList);
				} else {
					List<Vehicle> vehiclesList = dealerVehiclesMap.get(new Dealer(vehicleCF.get().getDealerId()));
					vehiclesList.add(vehicleCF.get());
				}
			}
		}
		return dealerVehiclesMap; 
	}
	
	private AnswerRequest createAnswerRequestListForPostingToAnswerResource(final Map<Dealer, List<Vehicle>> dealerVehiclesMap) {
		AnswerRequest answerRequest = new AnswerRequest();
		List<VehicleDealerDataWrapper> vehicleDealerDataList = new ArrayList<>();
		VehicleDealerDataWrapper vehicleDealerData = null;
		// Remove Null keys if any from the Map
		dealerVehiclesMap.keySet().removeIf(Objects::isNull);
		for (Map.Entry<Dealer, List<Vehicle>> entry: dealerVehiclesMap.entrySet()) {
			vehicleDealerData = new VehicleDealerDataWrapper()
					.setDealerId(entry.getKey().getDealerId())
					.setName(entry.getKey().getName())
					.setVehicles(entry.getValue());
			vehicleDealerDataList.add(vehicleDealerData);
		}
		answerRequest.setDealers(vehicleDealerDataList);
		return answerRequest;
	}
}