package dad.interfaces;

import dad.rest.RestServerBoard;
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
public interface BoardHandler extends Handler<RoutingContext> {
	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static BoardHandler create(Vertx vertx, Router router) {
		return new RestServerBoard(vertx, router);
	}
}
