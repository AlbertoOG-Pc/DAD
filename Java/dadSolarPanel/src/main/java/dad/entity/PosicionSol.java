package dad.entity;

public class PosicionSol {

	private int id;
	private int id_coordenadas;
	
	public PosicionSol() {
		super();
	}
	
	public PosicionSol(int id, int id_coordenadas) {
		super();
		this.id = id;
		this.id_coordenadas = id_coordenadas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_coordenadas() {
		return id_coordenadas;
	}

	public void setId_coordenadas(int id_coordenadas) {
		this.id_coordenadas = id_coordenadas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + id_coordenadas;
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
		PosicionSol other = (PosicionSol) obj;
		if (id != other.id)
			return false;
		if (id_coordenadas != other.id_coordenadas)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PosicionSol [id=" + id + ", id_coordenadas=" + id_coordenadas + "]";
	}

	
}
