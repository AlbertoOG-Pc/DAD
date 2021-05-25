package dad.entityImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.SunPosition;
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
public class SunPositionImpl {

	/**
	 * Lista donde almacenaremos los datos extraidos de la base de datos
	 */
	private static List<SunPosition> sunPositionList = new ArrayList<SunPosition>();

	/**
	 * Constructor vacio
	 */
	public SunPositionImpl() {
		super();
	}

	/**
	 * @param sunPositionList List<SunPosition> Constructor apartir de una lista de
	 *                        SunPosition
	 */
	public SunPositionImpl(List<SunPosition> sunPositionList) {
		SunPositionImpl.sunPositionList = sunPositionList;
	}

	/**
	 * @return Devuelve el listado de SunPosition
	 */
	public List<SunPosition> getSunPositionList() {
		return sunPositionList;
	}

	/**
	 * @param sunPositionList List<SunPosition> Establece o modifica el listado de
	 *                        SunPosition recibido
	 */
	public void setSunPositionList(List<SunPosition> sunPositionList) {
		SunPositionImpl.sunPositionList = sunPositionList;
	}

	/**
	 * @param message Recibe el cuerpo de la comunicacion con el verticle que maneja
	 *                el APIREST E imprime por pantalla el resultado obtenido
	 */
	public static void getALLSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.sunposition;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(JsonObject.mapFrom(new SunPosition(elem.getInteger("id"),
							elem.getInteger("id_coordinates"), elem.getLocalDateTime("date"),
							elem.getFloat("elevation"), elem.getFloat("azimut"))));
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
	public static void getSunPositionByID(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.sunposition WHERE id = ?;",
				Tuple.of(data.getInteger("id")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						// System.out.println(resultSet.size());
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new SunPosition(elem.getInteger("id"),
									elem.getInteger("id_coordinates"), elem.getLocalDateTime("date"),
									elem.getFloat("elevation"), elem.getFloat("azimut"))));
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
	public static void getSunPositionByDate(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		if (data.getString("date") != null) {
			Database.mySqlClient.preparedQuery("SELECT * FROM dad.sunPosition WHERE date BETWEEN ? AND ?",
					Tuple.of(
							LocalDateTime
									.parse(data.getString("date") + " 00:00:00",
											DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
									.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
							LocalDateTime
									.parse(data.getString("date") + " 23:59:59",
											DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
									.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
					res -> {
						if (res.succeeded()) {
							// Get the result set
							RowSet<Row> resultSet = res.result();
							for (Row elem : resultSet) {
								System.out.println("Elementos " + elem);
								result.add(JsonObject.mapFrom(new SunPosition(elem.getInteger("id"),
										elem.getInteger("id_coordinates"), elem.getLocalDateTime("date"),
										elem.getFloat("elevation"), elem.getFloat("azimut"))));
							}
							// resultado = result.toString();
						} else {
							result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
						}
						message.reply(result.toString());
					});
		} else {
			getSunPositionByDateWithOutData(message);
		}

	}

	public static void getSunPositionByDateWithOutData(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.preparedQuery("SELECT * FROM dad.sunPosition WHERE date BETWEEN ? AND ?",
				Tuple.of(
						LocalDateTime.now().minusMinutes(20)
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
						LocalDateTime.now().plusMinutes(20)
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							sunPositionList.add(new SunPosition(elem.getInteger("id"),
									elem.getInteger("id_coordinates"), elem.getLocalDateTime("date"),
									elem.getFloat("elevation"), elem.getFloat("azimut")));		
						}
						
						result.add(JsonObject.mapFrom(sunPositionList.stream().min((sp1, sp2) -> Math.abs((Duration.between(LocalDateTime.now(), sp1.date).toMinutes())) >
						Math.abs((Duration.between(LocalDateTime.now(), sp2.date).toMinutes())) ? 1 : -1).get()));
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
	public static void createSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery(
				"INSERT INTO dad.sunposition (id_coordinates, date, elevation, azimut) VALUES (?,?,?,?);",
				Tuple.of(data.getInteger("id_coordinates"), data.getValue("date"), data.getFloat("elevation"),
						data.getFloat("azimut")),
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
	public static void updateSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		System.out.println(message.body().toString());
		Database.mySqlClient.preparedQuery(
				"UPDATE dad.sunposition SET id_coordinates = ?, date = ?, elevation = ?, azimut = ? WHERE id = ?",
				Tuple.of(data.getInteger("id_coordinates"), data.getValue("date"), data.getFloat("elevation"),
						data.getFloat("azimut"), data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						getSunPositionByID(message);
						/*
						 * RowSet<Row> resultSet = res.result(); for (Row elem : resultSet) {
						 * System.out.println("Elementos " + elem); result.add(JsonObject.mapFrom(new
						 * SunPosition(elem.getInteger("id"), elem.getInteger("id_coordinates"),
						 * elem.getLocalDateTime("date"), elem.getFloat("elevation"),
						 * elem.getFloat("azimut")))); }
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
	public static void deleteSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		getSunPositionByID(message);
		Database.mySqlClient.preparedQuery("DELETE FROM dad.sunposition WHERE id = ?", Tuple.of(data.getInteger("id")),
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
		result = prime * result + ((sunPositionList == null) ? 0 : sunPositionList.hashCode());
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
		if (sunPositionList == null) {
			if (SunPositionImpl.sunPositionList != null)
				return false;
		} else if (!sunPositionList.equals(SunPositionImpl.sunPositionList))
			return false;
		return true;
	}

	/**
	 * Metodo toString() autogenerado
	 */
	@Override
	public String toString() {
		return "SunPositionImpl [sunPositionList=" + sunPositionList + "]";
	}
}
