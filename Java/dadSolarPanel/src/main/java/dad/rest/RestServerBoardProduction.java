package dad.rest;

import dad.interfaces.BoardProductionHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServerBoardProduction implements BoardProductionHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;

	public RestServerBoardProduction(Vertx vertx, Router router) {
		this.router = router;
		eventBus = vertx.eventBus();
		router.get("/api/boardProduction").handler(this::getAll);

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
}
