package dad.rest;

import java.util.ArrayList;

import dad.dadSolarPanel.App;
import dad.dadSolarPanel.Database;
import dad.dadSolarPanel.Mqtt;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 *         Class RestServer - Entrada principal
 *
 */
public class RestServer extends AbstractVerticle {

	/**
	 *
	 */
	public void start(Promise<Void> startFuture) {
		getVertx().eventBus();

		// Defining the router object
		Router router = Router.router(vertx);
		
		router.route().handler(CorsHandler.create("*")
			    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
			    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
			    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
			    .allowedHeader("Access-Control-Allow-Headers")
			    .allowedHeader("Authorization")
			    .allowedHeader("Access-Control-Allow-Method")
			    .allowedHeader("Access-Control-Allow-Origin")
			    .allowedHeader("Access-Control-Allow-Credentials")
			    .allowedHeader("Content-Type"));

		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8089, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		ArrayList<String> verticles = new ArrayList<>();
		verticles.add(App.class.getName());
		verticles.add(Database.class.getName());
		verticles.add(Mqtt.class.getName());
		
		verticles.stream().forEach(verticle -> {
			getVertx().deployVerticle(verticle, deployResult -> {
				if (deployResult.succeeded()) {
					System.out.println(verticle + " ha sido desplegado correctamente");
				} else {
					deployResult.cause().printStackTrace();
					System.out.println("No se ha podio desplegar");
				}
			});
		});
		
		
		
		/* DEFINING GENERAL ROUTES */

		// Log Router
		router.route("/api/log*").handler(BodyHandler.create());
		router.route("/api/log*").handler(RestServerLog.create(vertx, router));
		
		// Board Router
		router.route("/api/board*").handler(BodyHandler.create());
		router.route("/api/board*").handler(RestServerBoard.create(vertx, router));
		
		// Coordinates Router
		router.route("/api/coordinates*").handler(BodyHandler.create());
		router.route("/api/coordinates*").handler(RestServerCoordinates.create(vertx, router));
		
		// Board Production Router
		router.route("/api/boardProduction*").handler(BodyHandler.create());
		router.route("/api/boardProduction*").handler(RestServerBoardProduction.create(vertx, router));
		
		// Sun Position Router
		router.route("/api/sunPosition*").handler(BodyHandler.create());
		router.route("/api/sunPosition*").handler(RestServerSunPosition.create(vertx, router));

		
		// User Router
		router.route("/api/user*").handler(BodyHandler.create());
		router.route("/api/user*").handler(RestServerUser.create(vertx, router));

	}
}
