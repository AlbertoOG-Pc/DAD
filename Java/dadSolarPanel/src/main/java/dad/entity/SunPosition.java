package dad.entity;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class SunPosition {

	private int id;
	private int id_coordinates;
	private LocalDateTime date;
	private double elevation;
	private double azimut;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public SunPosition() {
		super();
	}

	public SunPosition(int id, int id_coordinates, LocalDateTime date, double elevation, double azimut) {
		super();
		this.id = id;
		this.id_coordinates = id_coordinates;
		this.date = date;
		this.elevation = elevation;
		this.azimut = azimut;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_coordinates() {
		return id_coordinates;
	}

	public void setId_coordinates(int id_coordinates) {
		this.id_coordinates = id_coordinates;
	}

//	public LocalDateTime getDateTime() {
//		return dateTime;
//	}
	
	// Get format String
    public String getDate() {
        return date.format(formatter);
    }

	public void setDateTime(LocalDateTime date) {
		this.date = date;
	}

	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	public double getAzimut() {
		return azimut;
	}

	public void setAzimut(double azimut) {
		this.azimut = azimut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(azimut);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		temp = Double.doubleToLongBits(elevation);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		result = prime * result + id_coordinates;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SunPosition other = (SunPosition) obj;
		if (Double.doubleToLongBits(azimut) != Double.doubleToLongBits(other.azimut))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(elevation) != Double.doubleToLongBits(other.elevation))
			return false;
		if (id != other.id)
			return false;
		if (id_coordinates != other.id_coordinates)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SunPosition [id=" + id + ", id_coordinates=" + id_coordinates + ", date=" + date
				+ ", elevation=" + elevation + ", azimut=" + azimut + "]";
	}

}
