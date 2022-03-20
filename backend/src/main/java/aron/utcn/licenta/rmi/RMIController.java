package aron.utcn.licenta.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class RMIController implements CommandLineRunner {

	private final RMIInterface obj;
	
	@Override
	public void run(String... args) throws Exception {
		try { 
	         RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject( obj, 0);  
	         // Binding the remote object (stub) in the registry 
	         Registry registry = LocateRegistry.createRegistry(1099);
	         
	         registry.rebind("RMIInterface", stub);  
	         System.err.println("Server ready"); 
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
		
	}

}
