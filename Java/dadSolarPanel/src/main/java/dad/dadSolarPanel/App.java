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

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 * 
 *         Class APP - Clase que comunica Verticles. Recibe la comunicacion
 *         a traves del eventBus de Vertx y ejecutara las funciones que
 *         correspondan
 *
 */
public class App extends AbstractVerticle {

	/**
	 * Metodo Start o Main del Verticle punto de entrada de la comunicacion mediante
	 * el eventBus.
	 * 
	 */
	@Override
	public void start(Promise<Void> startFuture) {
		/* Comunicacion con METODOS GET */
		getVertx().eventBus().consumer("consulta", message -> {
			System.out.println("APP consulta");
			getQuery(message);
		});

		/* Comunicacion con METODOS POST */
		getVertx().eventBus().consumer("POST", message -> {
			System.out.println("APP POST");
			getQueryPost(message);
		});

		/* Comunicacion con METODOS PUT */
		getVertx().eventBus().consumer("PUT", message -> {
			System.out.println("APP PUT");
			getQueryPut(message);
		});

		/* Comunicacion con METODOS PATCH */
		getVertx().eventBus().consumer("PATCH", message -> {
			System.out.println("APP PATCH");
			getQueryPatch(message);
		});

		/* Comunicacion con METODOS DELETE */
		getVertx().eventBus().consumer("DELETE", message -> {
			System.out.println("APP DELETE");
			getQueryDelete(message);
		});
	}

	/**
	 * @param message -- Recibe un Message en el cual el cuerpo debe tener un
	 *                String que contiene la clase accedida en funcion de la
	 *                consulta pedida
	 */
	private void getQuery(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "board_ALL":
			BoardImpl.getALLBoard(message);
			break;
		case "board_ONE":
			BoardImpl.getOneBoard(message);
			break;
		case "board_ALL_coordinate":
			BoardImpl.getALLforCordinates(message);
			break;
		case "log_ALL":
			LogImpl.getALLLog(message);
			break;
		case "log_ONE":
			LogImpl.getOneLog(message);
			break;
		case "log_ONE_board":
			LogImpl.getALLLogBoard(message);
			break;
		case "log_ALL_dateFilter":
			LogImpl.getALLLogDateFilter(message);
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
			result.add("{'Error': 'Method GET Comunicacion fallida entre verticle'}");
			message.reply(result.toString());
		}
	}

	/**
	 * @param message -- Recibe un objeto message el cual en el cuerpo contiene un
	 *                Json con los datos que se van han recibido en las peticiones
	 *                POST ademas debe incluir la clase a la que se accede.
	 */
	private void getQueryPost(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "Board":
			BoardImpl.createBoard(message);
			break;
		case "Log":
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
			result.add("{'Error': 'Method POST Comunicacion fallida entre verticle'}");
			message.reply(result.toString());
		}
	}

	/**
	 * @param message -- Recibe un objeto message el cual en el cuerpo contiene un
	 *                Json con los datos que se van han recibido en las peticiones
	 *                POST ademas debe incluir la clase a la que se accede.
	 */
	private void getQueryPut(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "Board":
			BoardImpl.updateBoard(message);
			break;
		case "Log":
			LogImpl.updateLog(message);
			break;
		case "Coordinates":
			CoordinatesImpl.updateCoordinates(message);
			break;
		case "BoardProduction":
			BoardProductionImpl.updateBoardProduction(message);
			break;
		case "SunPosition":
			SunPositionImpl.updateSunPosition(message);
			break;
		default:
			result.add("{'Error': 'Method PUT Comunicacion fallida entre verticle'}");
			message.reply(result.toString());
		}
	}

	/**
	 * @param message -- Recibe un objeto message el cual en el cuerpo contiene un
	 *                Json con los datos que se van han recibido en las peticiones
	 *                POST ademas debe incluir la clase a la que se accede.
	 */
	private void getQueryPatch(Message<?> message) {
		JsonArray result = new JsonArray();
		switch (JsonObject.mapFrom(message.body()).getString("CLASS")) {
		case "board_ALL_coordinate":
			BoardImpl.updateBoardCoordinates(message);
			break;
		default:
			result.add("{'Error': 'Method PATCH Comunicacion fallida entre verticle'}");
			message.reply(result.toString());
		}
	}

	/**
	 * @param message -- Recibe un objeto message el cual en el cuerpo contiene un
	 *                Json con los datos que se van han recibido en las peticiones
	 *                POST ademas debe incluir la clase a la que se accede.
	 */
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
			result.add("{'Error': 'Method DELETE Comunicacion fallida entre verticle'}");
			message.reply(result.toString());
		}
	}

	/**
	 * Metodo para parar el verticle.
	 */
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}
