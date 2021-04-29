package dad.rest;

import com.google.gson.GsonBuilder;

import dad.dadSolarPanel.App;
import dad.dadSolarPanel.Database;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class RestServer extends AbstractVerticle {

	public void start(Promise<Void> startFuture) {
		getVertx().eventBus();
		new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

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
		//Desplegar bbdd
		getVertx().deployVerticle(App.class.getName(), deployResult -> {
			if (deployResult.succeeded()) {
				System.out.println("App ha sido desplegado correctamente");
			} else {
				deployResult.cause().printStackTrace();
				System.out.println("No se ha podio desplegar");
			}
		});
		getVertx().deployVerticle(Database.class.getName(), deployResult -> {
					if (deployResult.succeeded()) {
						System.out.println("App ha sido desplegado correctamente");
					} else {
						deployResult.cause().printStackTrace();
						System.out.println("No se ha podio desplegar");
					}
				});
		//Desplegar rutas Board
		getVertx().deployVerticle(RestServerBoard.class.getName(), deployResult -> {
			if (deployResult.succeeded()) {
				System.out.println("RestServerBoard ha sido desplegado correctamente");
			} else {
				deployResult.cause().printStackTrace();
				System.out.println("No se ha podio desplegar");
			}
		});
	}
}
