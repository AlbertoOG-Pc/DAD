package dad.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dad.dadSolarPanel.App;
import dad.data.Board;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

	//private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private Gson gson;
	private EventBus eventBus;
	
	public void start(Promise<Void> startFuture) {
		eventBus = getVertx().eventBus();
		// Instantiating a Gson serialize object using specific date format
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		// Defining the router object
		Router router = Router.router(vertx);

		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8089, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
		getVertx().deployVerticle(App.class.getName(), deployResult -> {
			if (deployResult.succeeded()) {
				System.out.println("App ha sido desplegado correctamente");
			} else {
				deployResult.cause().printStackTrace();
				System.out.println("No se ha podio desplegar");
			}
		});


		// Defining URI paths for each method in RESTful interface, including body
		// handling by /api/users* or /api/users/*
		router.route("/api/board*").handler(BodyHandler.create());
		router.get("/api/board").handler(this::getAllWithParams);
	}


	private void getAllWithParams(RoutingContext routingContext) {
		eventBus.send("consulta","Datos de placas",reply-> {			
				if (reply.succeeded()) {
					String replyMessage = (String) reply.result().body();
					System.out.println("Respuesta recibida: " + replyMessage);
					routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(gson.toJson(replyMessage));
				} else {
					System.out.println("No ha habido respuesta");
				}
			});
	}
}
