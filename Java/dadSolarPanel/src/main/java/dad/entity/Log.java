package dad.entity;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD Class Log - Entidad que maneja el
 *         modelo de datos la clase Log. Que se usara para tener un registro de
 *         lo que ocurre con las placas
 */
public class Log {

	/**
	 * Identificador de la tupla de la clase LOG
	 */
	private int id;

	/**
	 * Identificador de la placa asiaciada a la linea de LOG
	 */
	private int id_board;

	/**
	 * Fecha del log
	 */
	private LocalDateTime date;

	/**
	 * Texto del log
	 */
	private String issue;

	/**
	 * Objeto construido para el formateo de la fecha
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Constructor vacio
	 */
	public Log() {
		super();
	}

	/**
	 * @param id       - Integer - Identificador de la tupla del registro
	 * @param id_board - Integer - Identificador de la placa asociada al registro
	 * @param date     - LocalDateTime - Fecha asociada de la tupla del registro
	 * @param issue    - String - Informacion del registro
	 */
	public Log(int id, int id_board, LocalDateTime date, String issue) {
		super();
		this.id = id;
		this.id_board = id_board;
		this.date = date;
		this.issue = issue;
	}

	/**
	 * @return Identificador de la tupla LOG
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id Integer Establece identificador de la tupla LOG
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Integer - Identificador de la placa asociada a la tupla
	 */
	public int getId_board() {
		return id_board;
	}

	/**
	 * @param id_board Integer Establece el identificador de la placa asociada
	 */
	public void setId_board(int id_board) {
		this.id_board = id_board;
	}

//	public LocalDateTime getFecha() {
//		return date;
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
	 * @param date - LocalDateTime Establece la fecha del registro
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return devuelve un String con el asunto del Log
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * @param issue - String Establece el asunto del Log
	 */
	public void setIssue(String issue) {
		this.issue = issue;
	}

	/**
	 * Metodo hashCode() autogenerado
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((issue == null) ? 0 : issue.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + id_board;
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
		Log other = (Log) obj;
		if (issue == null) {
			if (other.issue != null)
				return false;
		} else if (!issue.equals(other.issue))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (id_board != other.id_board)
			return false;
		return true;
	}

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "Log [id=" + id + ", id_board=" + id_board + ", date=" + date + ", issue=" + issue + "]";
	}

}
