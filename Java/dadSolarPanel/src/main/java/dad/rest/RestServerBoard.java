package dad.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dad.entity.Board;
import dad.interfaces.BasicOperation;
import dad.interfaces.BoardHandler;
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
public class RestServerBoard implements BoardHandler,BasicOperation {

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
	static RestServerBoard create(Vertx vertx, Router router) {
		return new RestServerBoard(vertx, router);
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
	public RestServerBoard(Vertx vertx, Router router) {
		this.router = router;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		eventBus = vertx.eventBus();
		/* GET METHOD */
		router.get("/api/boards").handler(this::getAll);
		router.get("/api/board/:id").handler(this::getOne);
		router.get("/api/board/filtercoordinates/:id_coordinates").handler(this::getALLforCordinates);
		
		/* POST METHOD */
		router.post("/api/board").handler(this::create);

		/* PUT METHOD */
		router.put("/api/board").handler(this::update);

		/* PATCH METHOD */
		router.patch("/api/board/coordinates/:id").handler(this::updateBoardCoordinates);

		/* DELETE METHOD */
		router.delete("/api/board/:id").handler(this::delete);
	}

	/**
	 * @param routingContext
	 */
	public void getAll(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "board_ALL");
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

		obj.put("CLASS", "board_ONE").put("id", Integer.parseInt(routingContext.request().getParam("id")));
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
	public void getALLforCordinates(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();

		obj.put("CLASS", "board_ALL_coordinate").put("id_coordinates",
				Integer.parseInt(routingContext.request().getParam("id_coordinates")));
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

		final Board board = gson.fromJson(routingContext.getBodyAsString(), Board.class);
		eventBus.request("POST", JsonObject.mapFrom(board).put("CLASS", "Board"), reply -> {
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
		final Board board = gson.fromJson(routingContext.getBodyAsString(), Board.class);
		eventBus.request("PUT", JsonObject.mapFrom(board).put("CLASS", "Board"), reply -> {
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

	private void updateBoardCoordinates(RoutingContext routingContext) {
		JsonObject obj = routingContext.getBodyAsJson();
		obj.put("CLASS", "board_ALL_coordinate").put("id",
				Integer.parseInt(routingContext.request().getParam("id")));
		eventBus.request("PATCH", obj, reply -> {
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
		obj.put("CLASS", "Board").put("id", Integer.parseInt(routingContext.request().getParam("id")));

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
