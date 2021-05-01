package dad.interfaces;

import dad.rest.RestServerLog;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface LogHandler extends Handler<RoutingContext> {
	static LogHandler create(Vertx vertx, Router router) {
        return new RestServerLog(vertx, router);
    }
}
