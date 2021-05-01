package dad.interfaces;

import dad.rest.RestServerBoardProduction;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface BoardProductionHandler extends Handler<RoutingContext> {
	static BoardProductionHandler create(Vertx vertx, Router router) {
		return new RestServerBoardProduction(vertx, router);
	}

}
