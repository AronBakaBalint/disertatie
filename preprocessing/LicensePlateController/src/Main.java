import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Main {

	private static Enumeration<CommPortIdentifier> portList;
	private static CommPortIdentifier portId;
	private static SerialPort serialPort;
	private static OutputStream outputStream;
	
	static {
		portList = CommPortIdentifier.getPortIdentifiers();
		
	    while (portList.hasMoreElements()) {
	        portId = portList.nextElement();
	        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	             if (portId.getName().equals("COM3")) {
	                try {
	                    serialPort = (SerialPort)portId.open("SimpleWriteApp", 2000);
	                    Thread.sleep(1000);
	                    outputStream = serialPort.getOutputStream();
	                    serialPort.setSerialPortParams(9600,
	                        SerialPort.DATABITS_8,
	                        SerialPort.STOPBITS_1,
	                        SerialPort.PARITY_NONE);
	                } catch(Exception e) {
	                	System.out.println("Problem with sending data on serial port");
	                }
	            }
	        }
	    }
	}
	
	public static void main(String[] args) throws IOException {
		
		displayOnLCD("processing");
		
		String imageToOcrPath = args[0];
		int buffLength = 2048;
		byte[] buffer = new byte[2048];
		byte[] data;
		
		FileInputStream stream = new FileInputStream(imageToOcrPath);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int readLength;
		while((readLength = stream.read(buffer, 0, buffLength)) != -1) {
			out.write(buffer, 0, readLength);
		}
		
		data = out.toByteArray();
		String imageString = Base64.getEncoder().withoutPadding().encodeToString(data);
		
		out.close();
		stream.close();
		
		sendMessage(imageString);
		
		System.exit(0);
	}
	
	private static void sendMessage(String message) throws IOException {
		URL obj = new URL("http://192.168.0.159:8080/ocr");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Content-Type", "text/html");
		con.setRequestMethod("POST");
		
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(message.getBytes());
		os.flush();
		os.close();
		
		int responseCode = con.getResponseCode();
		if(responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			displayOnLCD(response.toString().trim());
		}
	}
	
	public static void displayOnLCD(String message) {
		try {
			outputStream.write(message.getBytes());
		} catch (Exception e) {
			System.out.println("Hardware not found!");
		}
	}

}
