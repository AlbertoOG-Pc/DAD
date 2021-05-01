package dad.interfaces;

import dad.rest.RestServerCoordinates;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface CoordinatesHandler extends Handler<RoutingContext> {
	static CoordinatesHandler create(Vertx vertx, Router router) {
        return new RestServerCoordinates(vertx, router);
    }
}
