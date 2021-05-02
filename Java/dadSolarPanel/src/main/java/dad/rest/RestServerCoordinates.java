package dad.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dad.entity.Coordinates;
import dad.interfaces.CoordinatesHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServerCoordinates implements CoordinatesHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;
	private Gson gson;
	

	static RestServerCoordinates create(Vertx vertx, Router router) {
        return new RestServerCoordinates(vertx, router);
    }
	
	public RestServerCoordinates(Vertx vertx, Router router) {
		this.router = router;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		eventBus = vertx.eventBus();
		router.get("/api/coordinates").handler(this::getAll);
		router.post("/api/coordinates").handler(this::createCoordinate);
	}
	
	@Override
	public void handle(RoutingContext event) {
		// TODO Auto-generated method stub
		router.handleContext(event);
	}

	private void getAll(RoutingContext routingContext) {
		eventBus.request("consulta", "coordinates_ALL", reply -> {
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
	private void createCoordinate(RoutingContext routingContext) {
		//System.out.println(routingContext.getBodyAsString());
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
	
}
