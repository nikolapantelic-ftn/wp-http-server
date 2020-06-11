package server;

public class Patient {
	private String osiguranje;
	private String ime;
	private String prezime;
	private String datumRodjenja;
	private String pol;
	private String status;
	
	public Patient(String osiguranje, String ime, String prezime, String datumRodjenja, String pol, String status) {
		this.osiguranje = osiguranje;
		this.ime = ime;
		this.prezime = prezime;
		this.datumRodjenja = datumRodjenja;
		this.pol = pol;
		this.status = status;
	}
	
	public String getOsiguranje() {
		return osiguranje;
	}
	public void setOsiguranje(String osiguranje) {
		this.osiguranje = osiguranje;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getDatumRodjenja() {
		return datumRodjenja;
	}
	public void setDatumRodjenja(String datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
	public String getPol() {
		return pol;
	}
	public void setPol(String pol) {
		this.pol = pol;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
