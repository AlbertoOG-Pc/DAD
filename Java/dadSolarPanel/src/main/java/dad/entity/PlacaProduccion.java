package dad.entity;

import java.time.*;

public class PlacaProduccion {
	private int id_placa;
	private int id_sol;
	private int posicionServo;
	private LocalDateTime fecha;
	private Double produccion;
	
	public PlacaProduccion() {
		super();
	}
	
	public PlacaProduccion(int id_placa, int id_sol, int posicionServo, LocalDateTime fecha, Double produccion) {
		super();
		this.id_placa = id_placa;
		this.id_sol = id_sol;
		this.posicionServo = posicionServo;
		this.fecha = fecha;
		this.produccion = produccion;
	}

	public int getId_placa() {
		return id_placa;
	}

	public void setId_placa(int id_placa) {
		this.id_placa = id_placa;
	}

	public int getId_sol() {
		return id_sol;
	}

	public void setId_sol(int id_sol) {
		this.id_sol = id_sol;
	}

	public int getPosicionServo() {
		return posicionServo;
	}

	public void setPosicionServo(int posicionServo) {
		this.posicionServo = posicionServo;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Double getProduccion() {
		return produccion;
	}

	public void setProduccion(Double produccion) {
		this.produccion = produccion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + id_placa;
		result = prime * result + id_sol;
		result = prime * result + posicionServo;
		result = prime * result + ((produccion == null) ? 0 : produccion.hashCode());
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
		PlacaProduccion other = (PlacaProduccion) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (id_placa != other.id_placa)
			return false;
		if (id_sol != other.id_sol)
			return false;
		if (posicionServo != other.posicionServo)
			return false;
		if (produccion == null) {
			if (other.produccion != null)
				return false;
		} else if (!produccion.equals(other.produccion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlacaProduccion [id_placa=" + id_placa + ", id_sol=" + id_sol + ", posicionServo=" + posicionServo
				+ ", fecha=" + fecha + ", produccion=" + produccion + "]";
	}
	
	
}


