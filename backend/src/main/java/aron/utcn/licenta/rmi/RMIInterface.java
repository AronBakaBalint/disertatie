package aron.utcn.licenta.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {

	String sendOcrisedLicensePlate(String ocrResult) throws RemoteException;
}
