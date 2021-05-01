package dad.rest;

import dad.interfaces.SunPositionHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServerSunPosition implements SunPositionHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;

	public RestServerSunPosition(Vertx vertx, Router router) {
		this.router = router;
		eventBus = vertx.eventBus();
		router.get("/api/sunPosition").handler(this::getAll);

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

	static RestServerSunPosition create(Vertx vertx, Router router) {
		return new RestServerSunPosition(vertx, router);
	}

	public void handle(RoutingContext event) {
		router.handleContext(event);
	}

}
