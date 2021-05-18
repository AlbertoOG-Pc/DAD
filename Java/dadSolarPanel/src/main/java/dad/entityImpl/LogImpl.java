package dad.entityImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.Log;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD Class CoordinatesImpl - Entidad que
 *         implementa el modelo de datos la clase Coordinates. Que se usara para
 *         realizar las consultas a la base de datos
 * 
 */
public class LogImpl {
	/**
	 * Lista donde almacenaremos los datos extraidos de la base de datos
	 */
	private List<Log> logList;

	/**
	 * Constructor vacio
	 */
	public LogImpl() {
		super();
	}

	/**
	 * @return Devuelve el listado de Log
	 */
	public List<Log> getBoardList() {
		return logList;
	}

	/**
	 * @param logList List<Log> Establece o modifica el listado de Log recibido
	 */
	public void setBoardList(List<Log> logList) {
		this.logList = logList;
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getALLLog(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.log;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
							elem.getLocalDateTime("date"), elem.getString("issue"))));
				}
			} else {
				result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
			}
			message.reply(result.toString());
		});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getOneLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.log WHERE id = ?", Tuple.of(data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
									elem.getLocalDateTime("date"), elem.getString("issue"))));
						}
					} else {
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getALLLogBoard(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.log WHERE id_board = ?",
				Tuple.of(data.getInteger("id_board")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
									elem.getLocalDateTime("date"), elem.getString("issue"))));
						}
					} else {
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getALLLogDateFilter(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.log WHERE date BETWEEN ? AND ?", Tuple.of(
				LocalDateTime.parse(data.getString("fechaIni"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse(data.getString("fechaFin"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
									elem.getLocalDateTime("date"), elem.getString("issue"))));
						}
					} else {
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void createLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
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
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void updateLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		System.out.println(data);
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("UPDATE dad.log SET id_board = ?, date = ?, issue = ? WHERE id = ?", Tuple
				.of(data.getInteger("id_board"), data.getValue("date"), data.getString("issue"), data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						getOneLog(message);
						/*RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Log(elem.getInteger("id"), elem.getInteger("id_board"),
									elem.getLocalDateTime("date"), elem.getString("issue"))));
						}*/
					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					//message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void deleteLog(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		getOneLog(message);
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("DELETE FROM dad.log WHERE id = ?", Tuple.of(data.getInteger("id")), res -> {
			if (!res.succeeded()) {
				System.out.println("Failure: " + res.cause().getMessage());
				result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
			}
			//message.reply(result.toString());
		});
	}

	/**
	 * Metodo hashCode() autogenerado
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logList == null) ? 0 : logList.hashCode());
		return result;
	}

	/**
	 * Metodo equals() autogenerado
	 */
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

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "BoardImpl [boardList=" + logList + "]";
	}

}
