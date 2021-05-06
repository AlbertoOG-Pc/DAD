package dad.dadSolarPanel;

import dad.entityImpl.BoardImpl;
import dad.entityImpl.BoardProductionImpl;
import dad.entityImpl.CoordinatesImpl;
import dad.entityImpl.LogImpl;
import dad.entityImpl.SunPositionImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class App extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startFuture) {
		getVertx().eventBus().consumer("consulta", message -> {
			System.out.println("APP consulta");
			getQuery(message);
		});
		getVertx().eventBus().consumer("POST", message -> {
			System.out.println("APP POST");
			getQueryPost(message);
		});
		getVertx().eventBus().consumer("PUT", message -> {
			System.out.println("APP PUT");
			getQueryPut(message);
		});
		getVertx().eventBus().consumer("DELETE", message -> {
			System.out.println("APP DELETE");
			getQueryDelete(message);
		});

	}

	private void getQuery(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "board_ALL":
			BoardImpl.getALLBoard(message);
			break;
		case "log_ALL":
			LogImpl.getALLLog(message);
			break;
		case "coordinates_ALL":
			CoordinatesImpl.getALLCoordinates(message);
			break;
		case "boardProduction_ALL":
			BoardProductionImpl.getALLBoardProduction(message);
			break;
		case "sunPosition_ALL":
			SunPositionImpl.getALLSunPosition(message);
			break;
		// Querys adicionales Board Production
		case "boardProductionByID":
			BoardProductionImpl.getBoardProductionByID(message);
			break;
		case "boardProductionByBoardID":
			BoardProductionImpl.getBoardProductionByBoardID(message);
			break;
		case "bestsBoardProductionsOfBoardID":
			BoardProductionImpl.getBestsBoardProductionsOfBoardID(message);
			break;
		case "boardProductionByDates":
			BoardProductionImpl.getBoardProductionByDates(message);
			break;
		// Querys adicionales Coordinates
		case "coordinates_ONE":
			CoordinatesImpl.getCoordinatesByID(message);
			break;
		// Querys adicionales Sun Position
		case "sunPosition_ONE":
			SunPositionImpl.getSunPositionByID(message);
			break;
		case "sunPositionByDate":
			SunPositionImpl.getSunPositionByDate(message);
			break;
		default:
			result.add(JsonObject.mapFrom(new String("Error: Invalid Param")));
			message.reply(result.toString());
		}

		// return result;
	}

	private void getQueryPost(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "Board":
			// System.out.println("Aqui llego");
			BoardImpl.createBoard(message);
			break;
		case "Log":
			// System.out.println("Aqui llego");
			LogImpl.createLog(message);
			break;
		case "Coordinates":
			CoordinatesImpl.createCoordinates(message);
			break;
		case "BoardProduction":
			BoardProductionImpl.createBoardProduction(message);
			break;
		case "SunPosition":
			SunPositionImpl.createSunPosition(message);
			break;
		default:
			result.add(JsonObject.mapFrom(new String("Error: Invalid Param")));
			message.reply(result.toString());
		}

		// return result;
	}

	private void getQueryPut(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "Board":
			// System.out.println("Aqui llego");
			BoardImpl.updateBoard(message);
			break;
		case "Log":
			// System.out.println("Aqui llego");
			LogImpl.updateLog(message);
			break;
		case "Coordinates":
			CoordinatesImpl.updateCoordinates(message);
			break;
		case "BoardProduction":
			System.out.println("Hola");
			BoardProductionImpl.updateBoardProduction(message);
			break;
		case "SunPosition":
			SunPositionImpl.updateSunPosition(message);
			break;
		default:
			result.add(JsonObject.mapFrom(new String("Error: Invalid Param")));
			message.reply(result.toString());
		}

		// return result;
	}

	private void getQueryDelete(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "Board":
			BoardImpl.deleteBoard(message);
			break;
		case "Log":
			LogImpl.deleteLog(message);
			break;
		case "Coordinates":
			CoordinatesImpl.deleteCoordinates(message);
			break;
		case "BoardProduction":
			System.out.println("Hola");
			BoardProductionImpl.deleteBoardProduction(message);
			break;
		case "SunPosition":
			SunPositionImpl.deleteSunPosition(message);
			break;
		default:
			result.add(JsonObject.mapFrom(new String("Error: Invalid Param")));
			message.reply(result.toString());
		}

		// return result;
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}
