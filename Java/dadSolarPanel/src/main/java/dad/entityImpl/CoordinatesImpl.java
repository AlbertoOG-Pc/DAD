package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.Coordinates;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

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
					result.add(JsonObject.mapFrom(new Coordinates(elem.getInteger("id"), elem.getDouble("latitude"),
							elem.getDouble("longitude"))));
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
