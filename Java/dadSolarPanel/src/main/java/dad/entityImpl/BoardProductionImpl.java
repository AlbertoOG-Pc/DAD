package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.BoardProduction;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class BoardProductionImpl {

	private List<BoardProduction> boardProductionList;

	public BoardProductionImpl() {
		super();
	}

	public List<BoardProduction> getBoardProductionList() {
		return boardProductionList;
	}

	public void setBoardProductionList(List<BoardProduction> boardProductionList) {
		this.boardProductionList = boardProductionList;
	}

	public static void getALLBoardProduction(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query(
				"SELECT * FROM dad.board_production",
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						// System.out.println(resultSet.size());
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new BoardProduction(elem.getInteger("id_board"),
									elem.getInteger("id_sun"), elem.getInteger("positionServo"),
									elem.getLocalDateTime("date"), elem.getDouble("production"))));
						}
						// resultado = result.toString();
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardProductionList == null) ? 0 : boardProductionList.hashCode());
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
		BoardProductionImpl other = (BoardProductionImpl) obj;
		if (boardProductionList == null) {
			if (other.boardProductionList != null)
				return false;
		} else if (!boardProductionList.equals(other.boardProductionList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardProductionImpl [boardProductionList=" + boardProductionList + "]";
	}

}
