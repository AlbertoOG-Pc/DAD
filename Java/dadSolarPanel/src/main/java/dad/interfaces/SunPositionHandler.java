package dad.interfaces;

import dad.rest.RestServerSunPosition;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface SunPositionHandler extends Handler<RoutingContext> {
	static RestServerSunPosition create(Vertx vertx, Router router) {
		return new RestServerSunPosition(vertx, router);
	}
}
