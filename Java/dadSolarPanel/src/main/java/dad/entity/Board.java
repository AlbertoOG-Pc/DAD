package dad.entity;

public class Board {
	private int id;
	private int id_coordinates;
	private double maxPower;
	
	public Board() {
		super();
	}

	public Board(int id, int id_coordinates, double maxPower) {
		super();
		this.id = id;
		this.id_coordinates = id_coordinates;
		this.maxPower = maxPower;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public double getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(double maxPower) {
		this.maxPower = maxPower;
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
		long temp;
		temp = Double.doubleToLongBits(maxPower);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Board other = (Board) obj;
		if (Double.doubleToLongBits(maxPower) != Double.doubleToLongBits(other.maxPower))
			return false;
		if (id != other.id)
			return false;
		if (id_coordinates != other.id_coordinates)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Board [id=" + id + ", id_coordinates=" + id_coordinates + ", energy=" + maxPower + "]";
	}
	


	
}