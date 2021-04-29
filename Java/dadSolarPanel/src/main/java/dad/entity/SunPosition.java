package dad.entity;

public class SunPosition {

	private int id;
	private int id_coordinates;
	
	public SunPosition() {
		super();
	}
	
	public SunPosition(int id, int id_coordinates) {
		super();
		this.id = id;
		this.id_coordinates = id_coordinates;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_coordinates() {
		return id_coordinates;
	}

	public void setId_coordinates(int id_coordinates) {
		this.id_coordinates = id_coordinates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + id_coordinates;
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
		SunPosition other = (SunPosition) obj;
		if (id != other.id)
			return false;
		if (id_coordinates != other.id_coordinates)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PosicionSol [id=" + id + ", id_coordinates=" + id_coordinates + "]";
	}

	
}
