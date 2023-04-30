package dad.entity;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 * 
 *         Class BoardProduction - Entidad que maneja el modelo de datos de la
 *         produccion de las placas.
 */
public class BoardProduction {
	/**
	 * Identificador de la tupla de produccion de la placa
	 */
	private int id;

	/**
	 * Identificador de la placa
	 */
	private int id_board;

	/**
	 * Posicion del servo en la tucla identificada
	 */
	private int positionServoE;
	
	/**
	 * Posicion del servo en la tucla identificada
	 */
	private int positionServoA;

	/**
	 * Fecha del registro de la tupla
	 */
	private LocalDateTime date;

	/**
	 * Produccion de la placa
	 */
	private Float production;

	/**
	 * Objeto construido para el formateo de la fecha
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Objeto construido para el formateo de la fecha sin hora
	 */
	private DateTimeFormatter formatterExcludeHour = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	/**
	 * Contructor vacio de la clase BoardProduction
	 */
	public BoardProduction() {
		super();
	}

	/**
	 * @param id            - Integer - Identificador de la tupla
	 * @param id_board      - Integer - Identificador de la placa
	 * @param positionServo - Integer - Angulo de posicion del servo
	 * @param date          - LocalDateTime - Fecha de registro de los datos
	 * @param production    - Float - Produccion de la placa en la fecha indicada
	 */
	public BoardProduction(int id, int id_board, int positionServoE, int positionServoA, LocalDateTime date, Float production) {
		super();
		this.id = id;
		this.id_board = id_board;
		this.positionServoE = positionServoE;
		this.positionServoA = positionServoA;
		this.date = date;
		this.production = production;
	}

	/**
	 * @return devuelve el Id de la tupla
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id - Integer Establece el ID de la tupla
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Devuelve el ID de la placa a la que hace referencia la tupla
	 */
	public int getId_board() {
		return id_board;
	}

	/**
	 * @param id_board - Integer Establece el ID de la placa a la que hace
	 *                 referencia la tupla
	 */
	public void setId_board(int id_board) {
		this.id_board = id_board;
	}

	/**
	 * @return Devuelve un int con la posicion del servo
	 */
	public int getPositionServoE() {
		return positionServoE;
	}

	/**
	 * @param positionServo - Int Establece la posicion del servo
	 */
	public void setPositionServoE(int positionServo) {
		this.positionServoE = positionServo;
	}
	
	/**
	 * @return Devuelve un int con la posicion del servo
	 */
	public int getPositionServoA() {
		return positionServoA;
	}

	/**
	 * @param positionServo - Int Establece la posicion del servo
	 */
	public void setPositionServoA(int positionServo) {
		this.positionServoA = positionServo;
	}
	

	/*
	 * public LocalDateTime getDate() { return date; }
	 */

	/**
	 * @return devuelve Un String con el formato indicado en el objeto
	 *         DateTimeFormatter de la fecha
	 */
	public String getDate() {
		return date.format(formatter);
	}
	
	/**
	 * @return devuelve Un String con el formato indicado en el objeto
	 *         DateTimeFormatter de la fecha
	 */
	public String getDateExcludeHour() {
		return date.format(formatterExcludeHour);
	}

	/**
	 * @param date - LocalDateTime Establece la fecha del registro de la tupla
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	/**
	 * @param date - String Establece la fecha del registro de la tupla
	 */
	public void setDate(String date) {
		this.date = LocalDateTime.parse(date, formatter);;
	}

	/**
	 * @return devuelve un float con la produccion registrada
	 */
	public Float getProduction() {
		return production;
	}

	/**
	 * @param production - Float Establece la produccion de la tupla
	 */
	public void setProduction(Float production) {
		this.production = production;
	}

	/**
	 * Metodo hashCode() autogenerado
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((formatter == null) ? 0 : formatter.hashCode());
		result = prime * result + id;
		result = prime * result + id_board;
		result = prime * result + positionServoA;
		result = prime * result + positionServoE;
		result = prime * result + ((production == null) ? 0 : production.hashCode());
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
		BoardProduction other = (BoardProduction) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (id_board != other.id_board)
			return false;
		if (positionServoA != other.positionServoA)
			return false;
		if (positionServoE != other.positionServoE)
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "BoardProduction [id=" + id + ", id_board=" + id_board + ", positionServoE=" + positionServoE
				+ ", positionServoA=" + positionServoA + ", date=" + getDate() + ", production=" + production + "]";
	}

	
	

}
