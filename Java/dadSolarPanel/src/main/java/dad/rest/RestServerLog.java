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
import dad.interfaces.BasicOperation;
import dad.interfaces.LogHandler;
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
public class RestServerLog implements LogHandler, BasicOperation {

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
	static RestServerLog create(Vertx vertx, Router router) {
		return new RestServerLog(vertx, router);
	}

	/**
	 *
	 */
	@Override
	public void handle(RoutingContext event) {
		// TODO Auto-generated method stub
		router.handleContext(event);
	}

	/**
	 * @param vertx
	 * @param router
	 */
	public RestServerLog(Vertx vertx, Router router) {
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
		router.get("/api/log").handler(this::getAll);
		router.get("/api/log/:id").handler(this::getOne);
		router.get("/api/log/board/:id_board").handler(this::getAllForBoard);
		router.get("/api/log/datesFilter/").handler(this::getAllDateFilter);

		/* POST METHOD */
		router.post("/api/log").handler(this::create);

		/* PUT METHOD */
		router.put("/api/log").handler(this::update);

		/* DELETE METHOD */
		router.delete("/api/log/:id").handler(this::delete);
	}

	/**
	 * @param routingContext
	 */
	public void getAll(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "log_ALL");
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
	public void getAllForBoard(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "log_ONE_board").put("id_board",
				Integer.parseInt(routingContext.request().getParam("id_board")));
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
	public void getOne(RoutingContext routingContext) {
		System.out.println("GET ONE ");
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "log_ONE").put("id", Integer.parseInt(routingContext.request().getParam("id")));
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
	public void getAllDateFilter(RoutingContext routingContext) {
		JsonObject obj = routingContext.getBodyAsJson();
		obj.put("CLASS", "log_ALL_dateFilter");
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
	public void create(RoutingContext routingContext) {
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

	/**
	 * @param routingContext
	 */
	public void update(RoutingContext routingContext) {
		System.out.println(routingContext.getBodyAsString());

		final Log log = gson.fromJson(routingContext.getBodyAsString(), Log.class);
		eventBus.request("PUT", JsonObject.mapFrom(log).put("CLASS", "Log"), reply -> {
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
		obj.put("CLASS", "Log").put("id", Integer.parseInt(routingContext.request().getParam("id")));

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
