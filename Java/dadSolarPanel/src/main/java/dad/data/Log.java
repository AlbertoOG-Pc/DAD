package dad.data;

import java.time.*;

public class Log {

	private int id;
	private int id_placa;
	private LocalDateTime fecha;
	private String asunto;
	
	public Log()  {
		super();
	}
	
	public Log(int id, int id_placa, LocalDateTime fecha, String asunto) {
		super();
		this.id = id;
		this.id_placa = id_placa;
		this.fecha = fecha;
		this.asunto = asunto;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_placa() {
		return id_placa;
	}

	public void setId_placa(int id_placa) {
		this.id_placa = id_placa;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asunto == null) ? 0 : asunto.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + id;
		result = prime * result + id_placa;
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
		Log other = (Log) obj;
		if (asunto == null) {
			if (other.asunto != null)
				return false;
		} else if (!asunto.equals(other.asunto))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (id != other.id)
			return false;
		if (id_placa != other.id_placa)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", id_placa=" + id_placa + ", fecha=" + fecha + ", asunto=" + asunto + "]";
	}
	
	
	
	
}
