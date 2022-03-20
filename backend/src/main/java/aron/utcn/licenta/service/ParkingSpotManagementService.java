package aron.utcn.licenta.service;

import java.util.List;

import aron.utcn.licenta.model.ParkingSpot;

public interface ParkingSpotManagementService {

	public List<ParkingSpot> getAll();
	
	public void save(ParkingSpot parkingPlace);
	
	public ParkingSpot findById(int id);
	
	public String handleScannedCode(String qrCode);
}
