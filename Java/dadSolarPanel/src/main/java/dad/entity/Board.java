package dad.entity;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 * 
 *         Class Board - Entidad que maneja el modelo de datos de las placas.
 */
public class Board {
	/**
	 * Identificador de la placa
	 */
	private int id;

	/**
	 * Identificar de las coordenadas
	 */
	private int id_coordinates;

	/**
	 * Produccion maxima de la placa
	 */
	private double maxPower;

	/**
	 * Atributo que guarda las coordendas de la placa
	 */
	private Coordinates coordinate;
	
	/**
	 * Atributo que guarda un codigo identificador de la placa
	 */
	private String board_code;
	
	/**
	 * Contrucctor vacio de la clase board
	 */
	
	public Board() {
		super();
	}

	/**
	 * Construtor con todos los datos de la placa
	 * 
	 * @param id             Tipo Entero que guarda el identificar de la placa
	 * @param id_coordinates Tipo Entero que guarda el identificar de las
	 *                       coordenadas
	 * @param maxPower       Tipo Doble con la produccion maxima que es capaz de
	 *                       generar la placa
	 */
	public Board(int id, int id_coordinates, double maxPower, String board_code) {
		super();
		this.id = id;
		this.id_coordinates = id_coordinates;
		this.coordinate = null;
		this.maxPower = maxPower;
		this.board_code = board_code;
	}

	/**
	 * @param id         Tipo Entero que guarda el identificar de la placa
	 * @param coordinate Tipo Coordinates que guarda el objeto de coordendas
	 * @param maxPower   Tipo Doble con la produccion maxima que es capaz de generar
	 *                   la placa
	 */
	public Board(int id, Coordinates coordinate, double maxPower, String board_code) {
		super();
		this.id = id;
		this.coordinate = coordinate;
		this.id_coordinates = coordinate.getId();
		this.maxPower = maxPower;
		this.board_code = board_code;
	}

	/**
	 * @return devuelve una Coordinate
	 */
	public Coordinates getCoordinate() {
		if(coordinate == null) {
			coordinate = new Coordinates();
			coordinate.setId(this.getId_coordinates());
		}
		return coordinate;
	}

	/**
	 * @param coordinate Recibe un Coordinate y modifica o establece la actual
	 */
	public void setCoordinate(Coordinates coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * @return Devuelve el identificador de la placa
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param int Identificador Modifica o establece el identificador de la placa
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Devuelve la cantidad de produccion maxima de la placa
	 */
	public double getMaxPower() {
		return maxPower;
	}

	/**
	 * @param maxPower - Double Establece la cantidad de produccion maxima de la
	 *                 placa
	 */
	public void setMaxPower(double maxPower) {
		this.maxPower = maxPower;
	}

	/**
	 * @return Devuelve la ID de las coordenadas de la placa
	 */
	private int getId_coordinates() {
		return id_coordinates;
	}

	/**
	 * @param id_coordinates - Int Establece la ID de las coordenadas de la placa
	 */
	public void setId_coordinates(int id_coordinates) {
		this.id_coordinates = id_coordinates;
	}
	
	/**
	 * @return Devuelve el codigo identificador de la placa
	 */
	public String getBoard_code() {
		return board_code;
	}
	
	/**
	 * @param board_code - String Establece el codigo identificativo de la placa
	 */
	public void setBoard_code(String board_code) {
		this.board_code = board_code;
	}

	
	/**
	 * Metodo hashCode() autogenerado
	 */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board_code == null) ? 0 : board_code.hashCode());
		result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result + id;
		result = prime * result + id_coordinates;
		long temp;
		temp = Double.doubleToLongBits(maxPower);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Metedo equals() autogenerado
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (board_code == null) {
			if (other.board_code != null)
				return false;
		} else if (!board_code.equals(other.board_code))
			return false;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (id != other.id)
			return false;
		if (id_coordinates != other.id_coordinates)
			return false;
		if (Double.doubleToLongBits(maxPower) != Double.doubleToLongBits(other.maxPower))
			return false;
		return true;
	}

	
	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "Board [id=" + id + ", id_coordinates=" + id_coordinates + ", maxPower=" + maxPower + ", coordinate="
				+ coordinate + ", board_code=" + board_code + "]";
	}

	
}