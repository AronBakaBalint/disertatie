package aron.utcn.licenta.rmi;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.service.ParkingSpotManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RMIInterfaceImpl implements RMIInterface {
	
	private final ParkingSpotManagementService parkingSpotService;
	
	@Override
	public String sendOcrisedLicensePlate(String ocrResult) {
		return parkingSpotService.handleScannedCode(ocrResult);
	}	

}
