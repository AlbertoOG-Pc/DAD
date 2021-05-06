package dad.interfaces;

import dad.rest.RestServerLog;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 *
 */
public interface LogHandler extends Handler<RoutingContext> {
	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static LogHandler create(Vertx vertx, Router router) {
		return new RestServerLog(vertx, router);
	}
}
