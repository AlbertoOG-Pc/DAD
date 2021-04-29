package dad.data;

public class Coordenadas {
	private int id;
	private Double longitud;
	private Double latitud;
	
	
	public Coordenadas() {
		super();
	}
	public Coordenadas(int id, Double longitud, Double latitud) {
		super();
		this.id = id;
		this.longitud = longitud;
		this.latitud = latitud;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((latitud == null) ? 0 : latitud.hashCode());
		result = prime * result + ((longitud == null) ? 0 : longitud.hashCode());
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
		Coordenadas other = (Coordenadas) obj;
		if (id != other.id)
			return false;
		if (latitud == null) {
			if (other.latitud != null)
				return false;
		} else if (!latitud.equals(other.latitud))
			return false;
		if (longitud == null) {
			if (other.longitud != null)
				return false;
		} else if (!longitud.equals(other.longitud))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Coordenadas [id=" + id + ", longitud=" + longitud + ", latitud=" + latitud + "]";
	}
	
	
}
