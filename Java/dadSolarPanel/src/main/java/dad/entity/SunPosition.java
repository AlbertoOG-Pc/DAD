package dad.entity;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD Class SunPosition - Entidad que maneja
 *         el modelo de datos la clase SunPosition. Que se usara para tener un
 *         registro de los movimientos de las placas
 *
 */
public class SunPosition {

	/**
	 * Identificador de la tupla con la posicion del Sol
	 */
	private int id;

	/**
	 * Identificador de las coordenadas relacionadas con las posiciones solares
	 */
	private int id_coordinates;

	/**
	 * Fecha de la posicion
	 */
	private LocalDateTime date;

	/**
	 * Elevacion del sol en la hora y posicion indicada
	 */
	private Float elevation;

	/**
	 * 
	 */
	private Float azimut;

	/**
	 * Objeto construido para el formateo de la fecha
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Constructor vacio
	 */
	public SunPosition() {
		super();
	}

	/**
	 * @param id             - Integer - Identificador de la tupla de posicion solar
	 * @param id_coordinates - Integer Identificador de la coordenadas asignada a la
	 *                       tupla de posicion solar
	 * @param date           - LocalDateTime - Fecha de la posicion del sol
	 * @param elevation      - Float - Elevacion del Sol
	 * @param azimut         - Float -
	 */
	public SunPosition(int id, int id_coordinates, LocalDateTime date, Float elevation, Float azimut) {
		super();
		this.id = id;
		this.id_coordinates = id_coordinates;
		this.date = date;
		this.elevation = elevation;
		this.azimut = azimut;
	}

	/**
	 * @return Devuelve el identificador de la tupla
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id - Integer Establece el identificador de la tupla de la posicion
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Devuelve el identificador de las coordenadas
	 */
	public int getId_coordinates() {
		return id_coordinates;
	}

	/**
	 * @param id_coordinates - Integer Establece el identificador de las coordenadas
	 *                       asociadas a las posiciones del sol
	 */
	public void setId_coordinates(int id_coordinates) {
		this.id_coordinates = id_coordinates;
	}

//	public LocalDateTime getDateTime() {
//		return dateTime;
//	}

	// Get format String
	/**
	 * @return devuelve Un String con el formato indicado en el objeto
	 *         DateTimeFormatter de la fecha
	 */
	public String getDate() {
		return date.format(formatter);
	}

	/**
	 * @param date - LocalDateTime Establece la fecha del registro de la posicion
	 *             del Sol
	 */
	public void setDateTime(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return Devuelve la elevacion del Sol
	 */
	public Float getElevation() {
		return elevation;
	}

	/**
	 * @param elevation - Float Establece la elevacion del sol
	 */
	public void setElevation(Float elevation) {
		this.elevation = elevation;
	}

	/**
	 * @return devuelve
	 */
	public Float getAzimut() {
		return azimut;
	}

	/**
	 * @param azimut - Float Establece el valor de
	 */
	public void setAzimut(Float azimut) {
		this.azimut = azimut;
	}

	/**
	 * Metodo hashCode() autogenerado
	 */
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

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "SunPosition [id=" + id + ", id_coordinates=" + id_coordinates + ", date=" + date + ", elevation="
				+ elevation + ", azimut=" + azimut + "]";
	}

}
