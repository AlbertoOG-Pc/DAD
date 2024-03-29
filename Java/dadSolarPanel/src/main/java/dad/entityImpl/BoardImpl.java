package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.dadSolarPanel.Mqtt;
import dad.entity.Board;
import dad.entity.CompleteSystem;
import dad.entity.Coordinates;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
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
 *         implementa el modelo de datos la clase Board. Que se usara para
 *         realizar las consultas a la base de datos
 *
 */
public class BoardImpl {
	/**
	 * Lista donde almacenaremos los datos extraidos de la base de datos
	 */
	private List<Board> boardList;

	/**
	 * Constructor vacio
	 */
	public BoardImpl() {
		super();
	}

	/**
	 * @return Devuelve el listado de Board
	 */
	public List<Board> getBoardList() {
		return boardList;
	}

	/**
	 * @param boardList List<Board> Establece o modifica el listado de board
	 *                  recibido
	 */
	public void setBoardList(List<Board> boardList) {
		this.boardList = boardList;
	}

	/**
	 * @param Message<?> Recibe el cuerpo de la comunicacion con el verticle que
	 *                   maneja el APIREST E imprime por pantalla el resultado
	 *                   obtenido
	 */
	public static void getALLBoard(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT board.*, coordinates.latitude, coordinates.longitude FROM board INNER "
				+ "JOIN coordinates ON board.id_coordinates = coordinates.id", res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Board(elem.getInteger("id"),
									new Coordinates(elem.getInteger("id_coordinates"), elem.getFloat("latitude"),
											elem.getFloat("longitude"), elem.getString("description")),
									elem.getDouble("maxPower"), elem.getString("board_code"))));
						}
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}
	
	/**
	 * @param Message<?> Recibe el cuerpo de la comunicacion con el verticle que
	 *                   maneja el APIREST E imprime por pantalla el resultado
	 *                   obtenido
	 */
	public static void getInformation(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT count(board.id) as num_placas, sum(board.maxPower) as systemPower FROM board;", res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new CompleteSystem(elem.getInteger("num_placas"),elem.getFloat("systemPower"))));
						}
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}
	

	/**
	 * @param Message<?> Recibe el cuerpo de la comunicacion con el verticle que
	 *                   maneja el APIREST E imprime por pantalla el resultado
	 *                   obtenido
	 */
	public static void getOneBoard(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		System.out.println(data);
		String query = "";

		if (data.containsKey("id")) {
			query = "SELECT board.*, coordinates.latitude, coordinates.longitude FROM board INNER "
					+ "JOIN coordinates ON board.id_coordinates = coordinates.id WHERE board.id = ?";
		} else if (data.containsKey("code")) {
			query = "SELECT board.*, coordinates.latitude, coordinates.longitude FROM board INNER "
					+ "JOIN coordinates ON board.id_coordinates = coordinates.id WHERE board.board_code = ?";
		}
		Database.mySqlClient.preparedQuery(query,
				Tuple.of(data.containsKey("id") ? data.getInteger("id") : data.getString("code")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Board(elem.getInteger("id"),
									new Coordinates(elem.getInteger("id_coordinates"), elem.getFloat("latitude"),
											elem.getFloat("longitude"),elem.getString("description")),
									elem.getDouble("maxPower"), elem.getString("board_code"))));
						}
					} else {
						System.out.println("Error: " + res.cause().getLocalizedMessage());
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param Message<?> Recibe el cuerpo de la comunicacion con el verticle que
	 *                   maneja el APIREST E imprime por pantalla el resultado
	 *                   obtenido
	 */
	public static void getALLforCordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.board where id_coordinates = ?",
				Tuple.of(data.getInteger("id_coordinates")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new Board(elem.getInteger("id"), elem.getInteger("id_coordinates"),
											elem.getDouble("maxPower"), elem.getString("board_code"))));
						}
					} else {
						System.out.println("Error: " + res.cause().getLocalizedMessage());
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Message<?> Recibe el cuerpo de la comunicacion con el verticle
	 *                que maneja el APIREST E imprime por pantalla el resultado
	 *                obtenido
	 */
	public static void createBoard(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery(
				"INSERT INTO dad.board (id_coordinates, maxPower, board_code) VALUES (?, ?, ?)",
				Tuple.of(data.getJsonObject("coordinate").getInteger("id"), data.getDouble("maxPower"), data.getString("board_code")),
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
	 * @param message Message<?> Recibe el cuerpo de la comunicacion con el verticle
	 *                que maneja el APIREST E imprime por pantalla el resultado
	 *                obtenido
	 */
	public static void moveServo(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Mqtt.mqttClient.publish("/servo/manualPosition", Buffer.buffer(data.toString()), MqttQoS.AT_LEAST_ONCE, false,
				false);

		message.reply(result.toString());
	}

	/**
	 * @param message @param Message<?> Recibe el cuerpo de la comunicacion con el
	 *                verticle que maneja el APIREST E imprime por pantalla el
	 *                resultado obtenido
	 */
	public static void updateBoard(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery(
				"UPDATE dad.board SET id_coordinates = ?, maxPower = ?, board_code = ? WHERE id = ?",
				Tuple.of(data.getJsonObject("coordinate").getInteger("id"), data.getDouble("maxPower"),
						data.getString("board_code"), data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new Board(elem.getInteger("id"), elem.getInteger("id_coordinates"),
											elem.getDouble("maxPower"), elem.getString("board_code"))));
						}

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					getOneBoard(message);
					// message.reply(result.toString());
				});
	}

	/**
	 * @param message @param Message<?> Recibe el cuerpo de la comunicacion con el
	 *                verticle que maneja el APIREST E imprime por pantalla el
	 *                resultado obtenido
	 */
	public static void updateBoardCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("UPDATE dad.board SET id_coordinates = ? WHERE id = ?",
				Tuple.of(data.getInteger("id_coordinates"), data.getInteger("id")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new Board(elem.getInteger("id"), elem.getInteger("id_coordinates"),
											elem.getDouble("maxPower"), elem.getString("board_code"))));
						}

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					getOneBoard(message);
					// message.reply(result.toString());
				});
	}

	/**
	 * @param message @param Message<?> Recibe el cuerpo de la comunicacion con el
	 *                verticle que maneja el APIREST E imprime por pantalla el
	 *                resultado obtenido
	 */
	public static void deleteBoard(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		getOneBoard(message);
		Database.mySqlClient.preparedQuery("DELETE FROM dad.board WHERE id = ?", Tuple.of(data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
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
		result = prime * result + ((boardList == null) ? 0 : boardList.hashCode());
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
		BoardImpl other = (BoardImpl) obj;
		if (boardList == null) {
			if (other.boardList != null)
				return false;
		} else if (!boardList.equals(other.boardList))
			return false;
		return true;
	}

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "BoardImpl [boardList=" + boardList + "]";
	}

}
