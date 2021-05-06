package dad.entity;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD Class Coordinates - Entidad que maneja
 *         el modelo de datos de las coordenadas
 */
public class Coordinates {
	/**
	 * Identificador de la tupla de coordenadas
	 */
	private int id;

	/**
	 * Longitud de las coordenadas
	 */
	private Float longitude;

	/**
	 * Latitud de las coordenadas
	 */
	private Float latitude;

	/**
	 * Contructor vacio de la clase coordinates
	 */
	public Coordinates() {
		super();
	}

	/**
	 * @param id        - Int - identificador de la
	 * @param longitude - Float - Longitud de la coordendas
	 * @param latitude  - Float - Latitud de la coordendas
	 * 
	 *                  Constructor parametrizado con todos los datos de la entidad
	 */
	public Coordinates(int id, Float longitude, Float latitude) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * @return Devuelve un int con el identificador de la coordenada
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id - Integer Establece el identificador de la coordenada
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return float con la longitud de la coordenada
	 */
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitud - Float Establece la longitud de la coordenada
	 */
	public void setLongitude(Float longitud) {
		this.longitude = longitud;
	}

	/**
	 * @return float con la latitude de la coordenada
	 */
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitud - Float Establece la longitud de la coordenada
	 */
	public void setLatitude(Float latitud) {
		this.latitude = latitud;
	}

	/**
	 * Metodo hashCode() autogenerado
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		return result;
	}

	/**
	 * Metodo equals() autogenerado
	 */
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

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "Coordenadas [id=" + id + ", longitud=" + longitude + ", latitud=" + latitude + "]";
	}

}
