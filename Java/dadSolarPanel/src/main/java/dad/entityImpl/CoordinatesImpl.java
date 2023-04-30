package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.Coordinates;
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
 */
public class CoordinatesImpl {
	/**
	 * Lista donde almacenaremos los datos extraidos de la base de datos
	 */
	private List<Coordinates> coordinatesList;

	/**
	 * Constructor vacio
	 */
	public CoordinatesImpl() {
		super();
	}

	/**
	 * @return Devuelve el listado de Coordinates
	 */
	public List<Coordinates> getBoardList() {
		return coordinatesList;
	}

	/**
	 * @param coordinatesList List<Coordinates> Establece o modifica el listado de
	 *                        Coordenadas recibido
	 */
	public void setBoardList(List<Coordinates> coordinatesList) {
		this.coordinatesList = coordinatesList;
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getALLCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.coordinates;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(JsonObject.mapFrom(new Coordinates(elem.getInteger("id"), elem.getFloat("latitude"),
							elem.getFloat("latitude"),elem.getString("description"))));
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
	public static void getCoordinatesByID(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.coordinates WHERE id = ?;",
				Tuple.of(data.getInteger("id")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						// System.out.println(resultSet.size());
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Coordinates(elem.getInteger("id"),
									elem.getFloat("latitude"), elem.getFloat("longitude"),elem.getString("description"))));
						}
						// resultado = result.toString();
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void createCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery("INSERT INTO dad.coordinates (latitude, longitude, description) VALUES (?, ?, ?)",
				Tuple.of(data.getFloat("latitude"), data.getFloat("longitude"), data.getString("description")), res -> {
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
	public static void updateCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery("UPDATE dad.coordinates SET latitude = ?, longitude = ?, description = ? WHERE id = ?",
				Tuple.of(data.getFloat("latitude"), data.getFloat("longitude"), data.getString("description"), data.getInteger("id")), res -> {
					if (res.succeeded()) {
						// Get the result set
						getCoordinatesByID(message);
						/*RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Coordinates(elem.getInteger("id"),
									elem.getFloat("latitude"), elem.getFloat("longitude"))));
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
	public static void deleteCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		getCoordinatesByID(message);
		Database.mySqlClient.preparedQuery("DELETE FROM dad.coordinates WHERE id = ?", Tuple.of(data.getInteger("id")),
				res -> {
					if (res.succeeded()) {

						//result.add(data);

					} else {
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
		result = prime * result + ((coordinatesList == null) ? 0 : coordinatesList.hashCode());
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
		CoordinatesImpl other = (CoordinatesImpl) obj;
		if (coordinatesList == null) {
			if (other.coordinatesList != null)
				return false;
		} else if (!coordinatesList.equals(other.coordinatesList))
			return false;
		return true;
	}

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "BoardImpl [boardList=" + coordinatesList + "]";
	}

}
