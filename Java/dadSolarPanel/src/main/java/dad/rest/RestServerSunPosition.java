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

import dad.entity.Board;
import dad.entity.SunPosition;
import dad.interfaces.SunPositionHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServerSunPosition implements SunPositionHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;
	private Gson gson;

	static RestServerSunPosition create(Vertx vertx, Router router) {
		return new RestServerSunPosition(vertx, router);
	}

	public void handle(RoutingContext event) {
		router.handleContext(event);
	}

	public RestServerSunPosition(Vertx vertx, Router router) {
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
		router.get("/api/sunPosition").handler(this::getAll);
		router.post("/api/sunPosition").handler(this::createSunPosition);

	}

	private void getAll(RoutingContext routingContext) {
		eventBus.request("consulta", "sunPosition_ALL", reply -> {
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

	private void createSunPosition(RoutingContext routingContext) {
		// System.out.println(routingContext.getBodyAsString());

		final SunPosition sunPosition = gson.fromJson(routingContext.getBodyAsString(), SunPosition.class);
		eventBus.request("POST", JsonObject.mapFrom(sunPosition).put("CLASS", "sunPosition"), reply -> {
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
