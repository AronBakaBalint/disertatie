package aron.utcn.licenta.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;

import org.springframework.stereotype.Service;

import aron.utcn.licenta.rmi.RMIInterface;

@Service
public class OcrServiceImpl implements OcrService {

	@Override
	public String doOcrOnImage(String base64EncodedImage) {
		try {
			byte[] decodedImg = Base64.getDecoder().decode(base64EncodedImage.getBytes(StandardCharsets.UTF_8));
			Path destinationFile = Paths.get("C:\\Users\\aronb\\Desktop\\licenta\\ocr\\licplate\\img", "licplate.jpg");
			Files.write(destinationFile, decodedImg);
			Process process = Runtime.getRuntime()
					.exec("py C:\\Users\\aronb\\Desktop\\licenta\\ocr\\licplate\\main.py");
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			String text = "";
			while ((line = reader.readLine()) != null) {
				if (!line.isBlank()) {
					text = line;
				}
			}

			System.out.println(text);
			return handleOcrisedLicensePlate(text);
		} catch (IOException | InterruptedException e) {
			return "Error";
		}
	}

	private String handleOcrisedLicensePlate(String result) {
		try {
			Registry registry = LocateRegistry.getRegistry(1099);
			RMIInterface stub = (RMIInterface) registry.lookup("RMIInterface");
			return stub.sendOcrisedLicensePlate(result);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

}
