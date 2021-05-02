package dad.entity;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class BoardProduction {
	private int id;
	private int id_board;
	private int positionServo;
	private LocalDateTime date;
	private Float production;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public BoardProduction() {
		super();
	}

	public BoardProduction(int id, int id_board, int positionServo, LocalDateTime date, Float production) {
		super();
		this.id = id;
		this.id_board = id_board;
		this.positionServo = positionServo;
		this.date = date;
		this.production = production;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_board() {
		return id_board;
	}

	public void setId_board(int id_board) {
		this.id_board = id_board;
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

	public Float getProduction() {
		return production;
	}

	public void setProduction(Float production) {
		this.production = production;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + id_board;
		result = prime * result + positionServo;
		result = prime * result + ((production == null) ? 0 : production.hashCode());
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
		if (id != other.id)
			return false;
		if (id_board != other.id_board)
			return false;
		if (positionServo != other.positionServo)
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardProduction [id=" + id + ", id_board=" + id_board + ", positionServo=" + positionServo + ", date="
				+ date + ", production=" + production + "]";
	}

}
