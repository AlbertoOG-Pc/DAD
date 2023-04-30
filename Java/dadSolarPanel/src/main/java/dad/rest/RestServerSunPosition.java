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

import dad.entity.SunPosition;
import dad.interfaces.BasicOperation;
import dad.interfaces.SunPositionHandler;
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
public class RestServerSunPosition implements SunPositionHandler, BasicOperation {

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
	 * @return
	 */
	static RestServerSunPosition create(Vertx vertx, Router router) {
		return new RestServerSunPosition(vertx, router);
	}

	/**
	 *
	 */
	public void handle(RoutingContext event) {
		router.handleContext(event);
	}

	/**
	 * @param vertx
	 * @param router
	 */
	public RestServerSunPosition(Vertx vertx, Router router) {
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
		router.get("/api/sunPosition").handler(this::getAll);
		router.get("/api/sunPosition/:id").handler(this::getOne);
		router.get("/api/sunPosition/dateFilter/").handler(this::getSunPositionByDate);
		router.get("/api/sunPosition/dateFilterClient/:id_coordinates").handler(this::getSunPositionByDate);

		/* POST METHOD */
		router.post("/api/sunPosition").handler(this::create);

		/* PUT METHOD */
		router.put("/api/sunPosition").handler(this::update);

		/* DELETE METHOD */
		router.delete("/api/sunPosition/:id").handler(this::delete);

	}

	/**
	 * @param routingContext
	 */
	public void getAll(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "sunPosition_ALL");
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

	/**
	 * @param routingContext
	 */
	public void getOne(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "sunPosition_ONE").put("id", Integer.parseInt(routingContext.request().getParam("id")));
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

	/**
	 * @param routingContext
	 */
	public void getSunPositionByDate(RoutingContext routingContext) {
		System.out.println(routingContext.getBodyAsJson());
		JsonObject obj = routingContext.getBodyAsJson() == null ? new JsonObject() : routingContext.getBodyAsJson();
		obj.put("CLASS", "sunPositionByDate").put("id_coordinates",
				Integer.parseInt(routingContext.request().getParam("id_coordinates") == null ? "0"
						: routingContext.request().getParam("id_coordinates")));
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

	/**
	 * @param routingContext
	 */
	public void create(RoutingContext routingContext) {
		// System.out.println(routingContext.getBodyAsString());

		final SunPosition sunPosition = gson.fromJson(routingContext.getBodyAsString(), SunPosition.class);
		eventBus.request("POST", JsonObject.mapFrom(sunPosition).put("CLASS", "SunPosition"), reply -> {
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
	public void update(RoutingContext routingContext) {
		// System.out.println(routingContext.getBodyAsString());

		final SunPosition sunPosition = gson.fromJson(routingContext.getBodyAsString(), SunPosition.class);
		eventBus.request("PUT", JsonObject.mapFrom(sunPosition).put("CLASS", "SunPosition"), reply -> {
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
	public void delete(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "SunPosition").put("id", Integer.parseInt(routingContext.request().getParam("id")));

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
