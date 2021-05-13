package dad.rest;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import dad.entity.BoardProduction;
import dad.interfaces.BoardProductionHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 *
 */
public class RestServerBoardProduction implements BoardProductionHandler {

	/**
	 * 
	 */
	private EventBus eventBus;
	/**
	 * 
	 */
	private Router router;
	/**
	 * 
	 */
	private Gson gson;

	/**
	 * @param vertx
	 * @param router
	 */
	public RestServerBoardProduction(Vertx vertx, Router router) {
		this.router = router;
		eventBus = vertx.eventBus();

		gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {

				return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			}

		}).create();

		/* GET METHOD */
		router.get("/api/boardsProduction").handler(this::getALLBoardProduction);
		router.get("/api/boardProduction/:id").handler(this::getBoardProductionByID);
		router.get("/api/boardProduction/board/:id_board").handler(this::getBoardProductionByBoardID);
		router.get("/api/boardProduction/board/:id_board/:production").handler(this::getBestsBoardProductionsOfBoardID);
		router.get("/api/boardProduction/datesFilter/").handler(this::getBoardProductionByDates);

		/* POST METHOD */
		router.post("/api/boardProduction").handler(this::createBoardProduction);

		/* PUT METHOD */
		router.put("/api/boardProduction").handler(this::updateBoardProduction);

		/* DELETE METHOD */
		router.delete("/api/boardProduction/:id").handler(this::deleteBoardProduction);

	}

	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static RestServerBoardProduction create(Vertx vertx, Router router) {
		return new RestServerBoardProduction(vertx, router);
	}

	/**
	 *
	 */
	@Override
	public void handle(RoutingContext event) {
		router.handleContext(event);
	}

	/**
	 * @param routingContext
	 */
	private void getALLBoardProduction(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "boardProduction_ALL");
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

	private void getBoardProductionByID(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "boardProductionByID").put("id", Integer.parseInt(routingContext.request().getParam("id")));
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

	private void getBoardProductionByBoardID(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "boardProductionByBoardID").put("id_board",
				Integer.parseInt(routingContext.request().getParam("id_board")));
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

	private void getBestsBoardProductionsOfBoardID(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "bestsBoardProductionsOfBoardID")
				.put("id_board", Integer.parseInt(routingContext.request().getParam("id_board")))
				.put("production", Float.parseFloat(routingContext.request().getParam("production")));
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

	private void getBoardProductionByDates(RoutingContext routingContext) {
		JsonObject obj = routingContext.getBodyAsJson();
		obj.put("CLASS", "boardProductionByDates");
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta" + reply.toString());
			}
		});
	}

	/**
	 * @param routingContext
	 */
	private void createBoardProduction(RoutingContext routingContext) {
		System.out.println(routingContext.getBodyAsString());
		final BoardProduction boardProduction = gson.fromJson(routingContext.getBodyAsString(), BoardProduction.class);
		System.out.println("Aqui llego create");
		eventBus.request("POST", JsonObject.mapFrom(boardProduction).put("CLASS", "BoardProduction"), reply -> {
			// LOS DATOS ESTAN AQUI
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

	/**
	 * @param routingContext
	 */
	private void updateBoardProduction(RoutingContext routingContext) {
		System.out.println(routingContext.getBodyAsString());
		final BoardProduction boardProduction = gson.fromJson(routingContext.getBodyAsString(), BoardProduction.class);
		System.out.println("Aqui llego create");
		eventBus.request("PUT", JsonObject.mapFrom(boardProduction).put("CLASS", "BoardProduction"), reply -> {
			// LOS DATOS ESTAN AQUI
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

	/**
	 * @param routingContext
	 */
	private void deleteBoardProduction(RoutingContext routingContext) {
		System.out.println("Hola");
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "BoardProduction").put("id", Integer.parseInt(routingContext.request().getParam("id")));

		eventBus.request("DELETE", obj, reply -> {
			// LOS DATOS ESTAN AQUI
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
	}

}
