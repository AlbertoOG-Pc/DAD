package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.SunPosition;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

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
		Database.mySqlClient.query(
				"SELECT * FROM dad.sunposition;",
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						// System.out.println(resultSet.size());
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new SunPosition(elem.getInteger("id"),
									elem.getInteger("id_coordinates"), elem.getLocalDateTime("date"),
									elem.getDouble("elevation"), elem.getDouble("azimut"))));
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
