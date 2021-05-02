package dad.entity;

public class Coordinates {
	private int id;
	private Float longitude;
	private Float latitude;

	public Coordinates() {
		super();
	}

	public Coordinates(int id, Float longitude, Float latitude) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitud) {
		this.longitude = longitud;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitud) {
		this.latitude = latitud;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
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
		Coordinates other = (Coordinates) obj;
		if (id != other.id)
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coordenadas [id=" + id + ", longitud=" + longitude + ", latitud=" + latitude + "]";
	}

}
