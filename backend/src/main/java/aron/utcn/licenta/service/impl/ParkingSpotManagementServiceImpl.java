package aron.utcn.licenta.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.model.ParkingSpot;
import aron.utcn.licenta.model.Reservation;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.repository.ReservationRepository;
import aron.utcn.licenta.service.ParkingSpotManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingSpotManagementServiceImpl implements ParkingSpotManagementService {

	private final ParkingPlaceRepository parkingPlaceRepository;

	private final ReservationRepository reservationRepository;

	@Override
	public List<ParkingSpot> getAll() {
		return parkingPlaceRepository.getAllParkingPlaces();
	}

	@Override
	@Transactional
	public void save(ParkingSpot parkingPlace) {
		parkingPlaceRepository.save(parkingPlace);
	}

	@Override
	public ParkingSpot findById(int id) {
		return parkingPlaceRepository.findById(id);
	}

	@Override
	@Transactional
	public String handleScannedCode(String licensePlate) {
		Optional<Reservation> optreservation = reservationRepository.findByDetectedLicensePlate(licensePlate);
		if (optreservation.isPresent()) {
			Reservation reservation = optreservation.get();
			if (reservation.isExpired()) {
				return "reservation expired";
			} else {
				ParkingSpot parkingSpot = parkingPlaceRepository.findById(reservation.getParkingSpotId());
				if (parkingSpot.isOccupied()) {
					reservation.setFinished();
					parkingSpot.setFree();
					return "goodbye";
				} else {
					reservation.setOccupied();
					parkingSpot.setOccupied();
					parkingSpot.setArrivalTime(new Date());
					return "welcome";
				}
			}
		} else {
			return "no reservation found";
		}
	}
}
