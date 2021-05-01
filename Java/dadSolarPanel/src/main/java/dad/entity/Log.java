package dad.entity;

import java.time.*;

public class Log {

	private int id;
	private int id_board;
	private LocalDateTime date;
	private String issue;
	
	public Log()  {
		super();
	}
	
	public Log(int id, int id_board, LocalDateTime date, String issue) {
		super();
		this.id = id;
		this.id_board = id_board;
		this.date = date;
		this.issue = issue;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_placa() {
		return id_board;
	}

	public void setId_placa(int id_board) {
		this.id_board = id_board;
	}

	public LocalDateTime getFecha() {
		return date;
	}

	public void setFecha(LocalDateTime date) {
		this.date = date;
	}

	public String getAsunto() {
		return issue;
	}

	public void setAsunto(String issue) {
		this.issue = issue;
	}

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

	@Override
	public String toString() {
		return "Log [id=" + id + ", id_placa=" + id_board + ", fecha=" + date + ", asunto=" + issue + "]";
	}
	
	
	
	
}
