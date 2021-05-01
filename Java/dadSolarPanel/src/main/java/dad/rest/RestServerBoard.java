package dad.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dad.entity.Board;
import dad.interfaces.BoardHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class RestServerBoard implements BoardHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;
	private Gson gson;
	
	static RestServerBoard create(Vertx vertx, Router router) {
        return new RestServerBoard(vertx, router);
    }
	@Override
	public void handle(RoutingContext event) {
		// TODO Auto-generated method stub
		router.handleContext(event);
	}
	
	public RestServerBoard(Vertx vertx, Router router) {
		this.router = router;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		eventBus = vertx.eventBus();
		
		router.get("/api/board").handler(this::getAll);
		router.post("/api/board").handler(this::createBoard);
	}

	private void getAll(RoutingContext routingContext) {
		eventBus.request("consulta", "board_ALL", reply -> {
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
	
	
	private void createBoard(RoutingContext routingContext) {
		//System.out.println(routingContext.getBodyAsString());

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
	
}
