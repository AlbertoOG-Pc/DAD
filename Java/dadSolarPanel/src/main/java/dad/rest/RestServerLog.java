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

import dad.entity.Log;
import dad.interfaces.LogHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServerLog implements LogHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;
	private Gson gson;

	static RestServerLog create(Vertx vertx, Router router) {
		return new RestServerLog(vertx, router);
	}

	@Override
	public void handle(RoutingContext event) {
		// TODO Auto-generated method stub
		router.handleContext(event);
	}

	public RestServerLog(Vertx vertx, Router router) {
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

		router.get("/api/log").handler(this::getAll);
		router.post("/api/log").handler(this::createLog);
	}

	public void getAll(RoutingContext routingContext) {
		System.out.println("RestServerLog getAll()");
		eventBus.request("consulta", "log_ALL", reply -> {
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

	private void createLog(RoutingContext routingContext) {
		System.out.println(routingContext.getBodyAsString());
		final Log log = gson.fromJson(routingContext.getBodyAsString(), Log.class);
		// System.out.println("Aqui llego create");
		eventBus.request("POST", JsonObject.mapFrom(log).put("CLASS", "Log"), reply -> {
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
