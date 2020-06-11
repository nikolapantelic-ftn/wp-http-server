package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private ServerSocket serverSocket;
	private int port;
	private ArrayList<Patient> patients;
	
	public Server(int port) throws IOException {
		this.port = port;
		this.serverSocket = new ServerSocket(this.port);
		this.patients = new ArrayList<Patient>();
	}
	
	public void run() {
		Socket socket;
		while(true) {
			try {
				socket = serverSocket.accept();
				String resource = getResource(socket.getInputStream());
				if(resource == null) {
					continue;
				}
				sendResponse(resource, socket.getOutputStream());
				socket.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getResource(InputStream inputStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String s = br.readLine();
		if(s == null) {
			return null;
		}
		System.out.println(s);
		
		String[] parts = s.split(" ");
		if(!parts[0].contentEquals("GET")) {
			return null;
		}
		return parts[1].substring(1);
	}
	
	public void sendResponse(String resource, OutputStream outputStream) throws IOException {
		PrintStream ps = new PrintStream(outputStream);
		
		resource.replace('/', File.separatorChar);
		if(resource.equals("")) {
			resource = "static/index.html";
		}
		if(resource.startsWith("register")) {
			String[] parts = resource.split("\\?");
			String[] tokens = parts[1].split("&");
			String osiguranje = tokens[0].split("=")[1];
			String ime = tokens[1].split("=")[1];
			String prezime = tokens[2].split("=")[1];
			String datumRodjenja = tokens[3].split("=")[1];
			String pol = tokens[4].split("=")[1];
			String status = tokens[5].split("=")[1];
			patients.add(new Patient(osiguranje, ime, prezime, datumRodjenja, pol, status));
			printPatients(ps);
			ps.flush();
			return;
		}
		if(resource.startsWith("update")) {
			String[] parts = resource.split("\\?");
			String[] tokens = parts[1].split("&");
			String osiguranje = tokens[0].split("=")[1];
			for(Patient p: patients) {
				if(p.getOsiguranje().equals(osiguranje)) {
					p.setStatus("ZARAZEN");
				}
			}
			printPatients(ps);
			ps.flush();
			return;
		}
		File file = new File(resource);
		if(!file.exists()) {
			ps.print("HTTP/1.1 404 File not found\n\n");
			return;
		}
		ps.print("HTTP/1.1 200 OK\n\n");
		
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[8192];
		int len;
		while((len = fis.read(data)) != -1) {
			ps.write(data, 0, len);
		}
		ps.flush();
		fis.close();
	}

	private void printPatients(PrintStream ps) {
		ps.print("HTTP/1.1 200 OK\n\n");
		ps.print("<html>\n" + 
				"    <head>\n" + 
				"        <title>Pregled pacijenata</title>\n" + 
				"        <style>.zarazen { background-color: red; }</style>\n" + 
				"    </head>\n" + 
				"    <body>\n" + 
				"        <h1>Http. Pregled pacijenata [COVID-19]</h1>\n" + 
				"        <table>");
		for(Patient p: this.patients) {
			if(p.getStatus().equals("ZARAZEN"))
				ps.print("<tr class=\"zarazen\">");
			else
				ps.print("<tr>");
			ps.print("<td>" + p.getOsiguranje()+"</td><td>" + p.getIme()+"</td><td>" + p.getPrezime()
					+ "</td><td>" + p.getDatumRodjenja() + "</td><td>" + p.getPol() + "</td><td>" + p.getStatus() + "</td>");
			if(!p.getStatus().equals("ZARAZEN")) {
				ps.print("<td><a href=\"http://localhost:8080/update?osiguranje=" + p.getOsiguranje() + "\">Test je pozitivan!</a></td></tr>");
			} else {
				ps.print("<td></td></tr>");
			}
		}
		ps.print("</table>\n" + 
				"    </body>\n" + 
				"</html>");
		
	}
}
