package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.Coordinates;
import dad.entity.Log;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

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

	public static void createLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		// System.out.println("Fecha : " + data.getString("date"));
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("INSERT INTO dad.log (id_board, date, issue) VALUES (?,?,?);",
				Tuple.of(data.getInteger("id_board"), data.getValue("date"), data.getString("issue")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						data.remove("CLASS");
						data.put("id", resultSet.property(MySQLClient.LAST_INSERTED_ID));
						result.add(data);

					} else {
						System.out.println("Failure: " + res.cause());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	public static void updateLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("UPDATE dad.log SET id_board = ?, date = ?, issue = ?, WHERE id = ?", Tuple
				.of(data.getInteger("id_board"), data.getValue("date"), data.getString("issue"), data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
									elem.getLocalDateTime("date"), elem.getString("issue"))));
						}
						// result.add(data);

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	public static void deleteLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("DELETE FROM dad.log WHERE id = ?", Tuple.of(data.getInteger("id")), res -> {
			if (res.succeeded()) {

				result.add(data);

			} else {
				System.out.println("Failure: " + res.cause().getMessage());
				result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
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
