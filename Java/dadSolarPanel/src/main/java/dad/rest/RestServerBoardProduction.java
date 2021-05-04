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

public class RestServerBoardProduction implements BoardProductionHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;
	private Gson gson;

	public RestServerBoardProduction(Vertx vertx, Router router) {
		this.router = router;
		eventBus = vertx.eventBus();

		// Fumadita by stackoverflow
		gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {

				return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			}

		}).create();

		router.get("/api/boardProduction").handler(this::getAll);
		router.post("/api/boardProduction").handler(this::createBoardProduction);
		router.put("/api/boardProduction").handler(this::updateBoardProduction);
		router.delete("/api/boardProduction/:id").handler(this::deleteBoardProduction);

	}

	private void getAll(RoutingContext routingContext) {
		eventBus.request("consulta", "boardProduction_ALL", reply -> {
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

	static RestServerBoardProduction create(Vertx vertx, Router router) {
		return new RestServerBoardProduction(vertx, router);
	}

	@Override
	public void handle(RoutingContext event) {
		router.handleContext(event);
	}

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
