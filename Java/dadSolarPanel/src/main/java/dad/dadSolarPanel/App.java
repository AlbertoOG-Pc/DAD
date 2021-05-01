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

	}

	private void getQuery(Message<?> message) {
		JsonArray result = new JsonArray();
		
		switch (message.body().toString()) {
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
		default:
			result.add(JsonObject.mapFrom(new String("Error: Invalid Param")));
			message.reply(result.toString());
		}

		// return result;
	}
	
	private void getQueryPost(Message<?> message) {
		System.out.println("Aqui llego");
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "Board":
			BoardImpl.createBoard(message);
			break;
		case "log_ONE":
			LogImpl.getALLLog(message);
			break;
		case "coordinates_ONE":
			CoordinatesImpl.getALLCoordinates(message);
			break;
		case "boardProduction_ONE":
			BoardProductionImpl.getALLBoardProduction(message);
			break;
		case "sunPosition_ONE":
			SunPositionImpl.createSunPosition(message);
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
