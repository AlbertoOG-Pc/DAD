package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.Board;
import dad.entity.Coordinates;
import dad.entity.Log;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class LogImpl {
	private List<Log> logList;

	public LogImpl() {
		super();
	}

	public List<Log> getBoardList() {
		return logList;
	}

	public void setBoardList(List<Log> logList) {
		this.logList = logList;
	}

	public static void getALLLog(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.log;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				// System.out.println(resultSet.size());
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
							elem.getLocalDateTime("date"), elem.getString("issue"))));
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
		result = prime * result + ((logList == null) ? 0 : logList.hashCode());
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
		LogImpl other = (LogImpl) obj;
		if (logList == null) {
			if (other.logList != null)
				return false;
		} else if (!logList.equals(other.logList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardImpl [boardList=" + logList + "]";
	}

}
