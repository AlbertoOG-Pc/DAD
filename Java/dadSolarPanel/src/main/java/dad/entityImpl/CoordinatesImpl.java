package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.Board;
import dad.entity.Coordinates;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class CoordinatesImpl {
	private List<Coordinates> coordinatesList;

	public CoordinatesImpl() {
		super();
	}

	public List<Coordinates> getBoardList() {
		return coordinatesList;
	}

	public void setBoardList(List<Coordinates> coordinatesList) {
		this.coordinatesList = coordinatesList;
	}

	public static void getALLCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * FROM dad.coordinates;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				// System.out.println(resultSet.size());
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(JsonObject.mapFrom(new Coordinates(elem.getInteger("id"), elem.getFloat("latitude"),
							elem.getFloat("longitude"))));
				}
				// resultado = result.toString();
			} else {
				result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
				// resultado = "Error: " + res.cause().getLocalizedMessage();
			}
			message.reply(result.toString());
		});
	}

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
									elem.getFloat("latitude"), elem.getFloat("longitude"))));
						}
						// resultado = result.toString();
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					message.reply(result.toString());
				});
	}

	public static void createCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("INSERT INTO dad.coordinates (latitude, longitude) VALUES (?, ?)",
				Tuple.of(data.getFloat("latitude"), data.getFloat("longitude")), res -> {
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

	public static void updateCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("UPDATE dad.coordinates SET latitude = ?, longitude = ? WHERE id = ?",
				Tuple.of(data.getFloat("latitude"), data.getFloat("longitude"), data.getInteger("id")), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new Coordinates(elem.getInteger("id"),
									elem.getFloat("latitude"), elem.getFloat("longitude"))));
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

	public static void deleteCoordinates(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		// result.add(message.body().toString());
		Database.mySqlClient.preparedQuery("DELETE FROM dad.coordinates WHERE id = ?", Tuple.of(data.getInteger("id")),
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
		result = prime * result + ((coordinatesList == null) ? 0 : coordinatesList.hashCode());
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
		CoordinatesImpl other = (CoordinatesImpl) obj;
		if (coordinatesList == null) {
			if (other.coordinatesList != null)
				return false;
		} else if (!coordinatesList.equals(other.coordinatesList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardImpl [boardList=" + coordinatesList + "]";
	}

}
