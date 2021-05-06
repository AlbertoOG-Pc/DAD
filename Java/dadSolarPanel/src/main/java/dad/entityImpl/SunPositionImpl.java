package dad.entityImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class SunPositionImpl {

	private List<SunPosition> sunPositionList;

	public SunPositionImpl() {
		super();
	}

	public SunPositionImpl(List<SunPosition> sunPositionList) {
		this.sunPositionList = sunPositionList;
	}

	public List<SunPosition> getSunPositionList() {
		return sunPositionList;
	}

	public void setSunPositionList(List<SunPosition> sunPositionList) {
		this.sunPositionList = sunPositionList;
	}

	public static void getALLSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.sunposition;", res -> {
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

	public static void getSunPositionByDate(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
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
	}

	public static void createSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		// result.add(message.body().toString());
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
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	public static void updateSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		// result.add(message.body().toString());
		System.out.println(message.body().toString());
		Database.mySqlClient.preparedQuery(
				"UPDATE dad.sunposition SET id_coordinates = ?, date = ?, elevation = ?, azimut = ? WHERE id = ?",
				Tuple.of(data.getInteger("id_coordinates"), data.getValue("date"), data.getFloat("elevation"),
						data.getFloat("azimut"), data.getInteger("id")),
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
						// result.add(data);

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	public static void deleteSunPosition(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("DELETE FROM dad.sunposition WHERE id = ?", Tuple.of(data.getInteger("id")),
				res -> {
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
		result = prime * result + ((sunPositionList == null) ? 0 : sunPositionList.hashCode());
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
		SunPositionImpl other = (SunPositionImpl) obj;
		if (sunPositionList == null) {
			if (other.sunPositionList != null)
				return false;
		} else if (!sunPositionList.equals(other.sunPositionList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SunPositionImpl [sunPositionList=" + sunPositionList + "]";
	}
}
