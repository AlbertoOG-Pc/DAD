package dad.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dad.entity.Coordinates;
import dad.interfaces.BasicOperation;
import dad.interfaces.CoordinatesHandler;
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
public class RestServerCoordinates implements CoordinatesHandler, BasicOperation {

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
	static RestServerCoordinates create(Vertx vertx, Router router) {
		return new RestServerCoordinates(vertx, router);
	}

	/**
	 * @param vertx
	 * @param router
	 */
	public RestServerCoordinates(Vertx vertx, Router router) {
		this.router = router;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		eventBus = vertx.eventBus();

		/* GET METHOD */
		router.get("/api/coordinates").handler(this::getAll);
		router.get("/api/coordinates/:id").handler(this::getOne);

		/* POST METHOD */
		router.post("/api/coordinates").handler(this::create);

		/* PUT METHOD */
		router.put("/api/coordinates").handler(this::update);

		/* DELETE METHOD */
		router.delete("/api/coordinates/:id").handler(this::delete);

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
	 * @param routingContext
	 */
	public void getAll(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "coordinates_ALL");
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
		obj.put("CLASS", "coordinates_ONE").put("id", Integer.parseInt(routingContext.request().getParam("id")));
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
		final Coordinates coordinates = gson.fromJson(routingContext.getBodyAsString(), Coordinates.class);
		eventBus.request("POST", JsonObject.mapFrom(coordinates).put("CLASS", "Coordinates"), reply -> {
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

		final Coordinates coordinates = gson.fromJson(routingContext.getBodyAsString(), Coordinates.class);
		eventBus.request("PUT", JsonObject.mapFrom(coordinates).put("CLASS", "Coordinates"), reply -> {
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
		obj.put("CLASS", "Coordinates").put("id", Integer.parseInt(routingContext.request().getParam("id")));

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
