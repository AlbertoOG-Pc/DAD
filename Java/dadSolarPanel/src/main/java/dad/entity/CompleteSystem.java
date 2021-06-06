package dad.entity;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD Class Coordinates - Entidad que maneja
 *         la informacion relativa al sistema completo.
 *         
 *         Esta informacion no esta guardada en base de datos se genera sobre los datos almacenados.
 */

public class CompleteSystem {
	/**
	 * Identificador de la tupla de coordenadas
	 */
	private int num_placas;

	/**
	 * Longitud de las coordenadas
	 */
	private float systemPower;
	
	/**
	 * Contructor vacio de la clase system
	 */
	public CompleteSystem() {
		super();
	}

	/**
	 * @param num_placas  - Int - Numero de placas del sistema completo.
	 * @param systemPower - Float - Produccion maxima del sistema completo
	 * 
	 * Constructor parametrizado con todos los datos de la entidad
	 */
	public CompleteSystem(int num_placas, Float systemPower) {
		super();
		this.num_placas = num_placas;
		this.systemPower = systemPower;
	}

	public int getNum_placas() {
		return num_placas;
	}

	public void setNum_placas(int num_placas) {
		this.num_placas = num_placas;
	}

	public float getSystemPower() {
		return systemPower;
	}

	public void setSystemPower(float systemPower) {
		this.systemPower = systemPower;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num_placas;
		result = prime * result + Float.floatToIntBits(systemPower);
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
		CompleteSystem other = (CompleteSystem) obj;
		if (num_placas != other.num_placas)
			return false;
		if (Float.floatToIntBits(systemPower) != Float.floatToIntBits(other.systemPower))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "System [num_placas=" + num_placas + ", systemPower=" + systemPower + "]";
	}
	
	
}
