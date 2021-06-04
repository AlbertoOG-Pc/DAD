package dad.entityImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.BoardProduction;
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
 *         Proyecto Placas solares - DAD Class BoardImpl - Entidad que
 *         implementa el modelo de datos la clase BoardProduccion. Que se usara
 *         para realizar las consultas a la base de datos
 *
 */
public class BoardProductionImpl {

	/**
	 * Lista donde almacenaremos los datos extraidos de la base de datos
	 */
	private List<BoardProduction> boardProductionList;

	/**
	 * Constructor vacio
	 */
	public BoardProductionImpl() {
		super();
	}

	/**
	 * @return Devuelve el listado de BoardProduccion
	 */
	public List<BoardProduction> getBoardProductionList() {
		return boardProductionList;
	}

	/**
	 * @param boardProductionList List<BoardProduction> Establece o modifica el
	 *                            listado de boardProduction recibido
	 */
	public void setBoardProductionList(List<BoardProduction> boardProductionList) {
		this.boardProductionList = boardProductionList;
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getALLBoardProduction(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.board_production", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(
							JsonObject.mapFrom(new BoardProduction(elem.getInteger("id"), elem.getInteger("id_board"),
									elem.getInteger("positionServoE"), elem.getInteger("positionServoA"),
									elem.getLocalDateTime("date"), elem.getFloat("production"))));
				}
			} else {
				result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
			}
			message.reply(result.toString());
		});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getBoardProductionByID(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.board_production WHERE id = ?",
				Tuple.of(data.getInteger("id")), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new BoardProduction(elem.getInteger("id"), elem.getInteger("id_board"),
											elem.getInteger("positionServoE"), elem.getInteger("positionServoA"),
											elem.getLocalDateTime("date"), elem.getFloat("production"))));
						}
					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});

	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getBoardProductionByBoardID(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.board_production WHERE id_board = ?",
				Tuple.of(data.getInteger("id_board")), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new BoardProduction(elem.getInteger("id"), elem.getInteger("id_board"),
											elem.getInteger("positionServoE"), elem.getInteger("positionServoA"),
											elem.getLocalDateTime("date"), elem.getFloat("production"))));
						}
					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});

	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getBestsBoardProductionsOfBoardID(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.board_production WHERE id_board = ? AND production > ?",
				Tuple.of(data.getInteger("id_board"), data.getFloat("production")), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new BoardProduction(elem.getInteger("id"), elem.getInteger("id_board"),
											elem.getInteger("positionServoE"), elem.getInteger("positionServoA"),
											elem.getLocalDateTime("date"), elem.getFloat("production"))));
						}
					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});

	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getBoardProductionByDates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.board_production WHERE date BETWEEN ? AND ?", Tuple.of(
				LocalDateTime.parse(data.getString("dateIni"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse(data.getString("dateFin"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new BoardProduction(elem.getInteger("id"), elem.getInteger("id_board"),
											elem.getInteger("positionServoE"), elem.getInteger("positionServoA"),
											elem.getLocalDateTime("date"), elem.getFloat("production"))));
						}
						// resultado = result.toString();
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */

	public static void createBoardProduction(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery(
				"INSERT INTO dad.board_production (id_board, positionServoE, positionServoA, date, production) VALUES (?,?,?,?,?);",
				Tuple.of(data.getInteger("id_board"), data.getInteger("positionServoE"),
						data.getInteger("positionServoA"), data.getValue("date"), data.getFloat("production")),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						data.remove("CLASS");
						data.put("id", resultSet.property(MySQLClient.LAST_INSERTED_ID));
						result.add(data);

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void updateBoardProduction(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		System.out.println(data);
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery(
				"UPDATE dad.board_production SET id_board = ?, positionServo = ?, date = ?, production = ? WHERE id = ?",
				Tuple.of(data.getInteger("id_board"), data.getInteger("positionServo"), data.getValue("date"),
						data.getFloat("production"), data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						getBoardProductionByID(message);
						// Get the result set
						/*
						 * RowSet<Row> resultSet = res.result(); for (Row elem : resultSet) {
						 * System.out.println("Elementos " + elem); result.add(JsonObject.mapFrom(new
						 * BoardProduction(elem.getInteger("id"), elem.getInteger("id_board"),
						 * elem.getInteger("positionServo"), elem.getLocalDateTime("date"),
						 * data.getFloat("production")))); }
						 */

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					// message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void deleteBoardProduction(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		getBoardProductionByID(message);
		Database.mySqlClient.preparedQuery("DELETE FROM dad.board_production WHERE id = ?",
				Tuple.of(data.getInteger("id")), res -> {
					if (res.succeeded()) {

						// result.add(data);

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					// message.reply(result.toString());
				});
	}

	/**
	 * Metodo hashCode() autogenerado
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardProductionList == null) ? 0 : boardProductionList.hashCode());
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
		BoardProductionImpl other = (BoardProductionImpl) obj;
		if (boardProductionList == null) {
			if (other.boardProductionList != null)
				return false;
		} else if (!boardProductionList.equals(other.boardProductionList))
			return false;
		return true;
	}

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "BoardProductionImpl [boardProductionList=" + boardProductionList + "]";
	}

}
