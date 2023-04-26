package dad.interfaces;

import dad.rest.RestServerUser;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface UserHandler extends Handler<RoutingContext>{
	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static UserHandler create(Vertx vertx, Router router) {
		return new RestServerUser(vertx, router);
	}
}
