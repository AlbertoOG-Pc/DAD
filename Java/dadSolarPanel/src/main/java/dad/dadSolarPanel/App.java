package dad.dadSolarPanel;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class App extends AbstractVerticle {

	public void start(Future<Void> startFuture) {
		vertx.createHttpServer().requestHandler(r -> {
			r.response().end("<h1>Bienvenido a mi primera aplicacion Vert.x 3</h1>"
					+ "Esto es un ejemplo de una Verticle sencillo para probar el despliegue");
		}).listen(8089, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
}}