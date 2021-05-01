package dad.rest;

import dad.interfaces.LogHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServerLog implements LogHandler {

	// private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private EventBus eventBus;
	private Router router;

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

		// Defining the router object
		// router = Router.router(vertx);

		// Handling any server startup result
		/*
		 * vertx.createHttpServer().requestHandler(router::handle).listen(8089, result
		 * -> { if (result.succeeded()) { startFuture.complete(); } else {
		 * startFuture.fail(result.cause()); } });
		 */

		// Defining URI paths for each method in RESTful interface, including body
		// handling by /api/users* or /api/users/*
		// router.route("/api/log*").handler(BodyHandler.create());
		router.get("/api/log").handler(this::getAll);
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
}
