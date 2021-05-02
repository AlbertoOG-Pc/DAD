package dad.entity;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class BoardProduction {
	private int id_board;
	private int id_sun;
	private int positionServo;
	private LocalDateTime date;
	private Double production;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public BoardProduction() {
		super();
	}

	public BoardProduction(int id_board, int id_sun, int positionServo, LocalDateTime date, Double production) {
		super();
		this.id_board = id_board;
		this.id_sun = id_sun;
		this.positionServo = positionServo;
		this.date = date;
		this.production = production;
	}

	public int getId_board() {
		return id_board;
	}

	public void setId_board(int id_board) {
		this.id_board = id_board;
	}

	public int getId_sun() {
		return id_sun;
	}

	public void setId_sun(int id_sun) {
		this.id_sun = id_sun;
	}

	public int getPositionServo() {
		return positionServo;
	}

	public void setPositionServo(int positionServo) {
		this.positionServo = positionServo;
	}

	/*
	 * public LocalDateTime getDate() { return date; }
	 */

	// Get format String
	public String getDate() {
		return date.format(formatter);
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Double getProduction() {
		return production;
	}

	public void setProduction(Double production) {
		this.production = production;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id_board;
		result = prime * result + id_sun;
		result = prime * result + ((production == null) ? 0 : production.hashCode());
		result = prime * result + positionServo;
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
		BoardProduction other = (BoardProduction) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id_board != other.id_board)
			return false;
		if (id_sun != other.id_sun)
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		if (positionServo != other.positionServo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardProduction [id_board=" + id_board + ", id_sun=" + id_sun + ", positionServo=" + positionServo
				+ ", date=" + date + ", production=" + production + "]";
	}

}
