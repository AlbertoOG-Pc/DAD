package dad.interfaces;

import dad.rest.RestServerCoordinates;
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
public interface CoordinatesHandler extends Handler<RoutingContext> {
	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static CoordinatesHandler create(Vertx vertx, Router router) {
		return new RestServerCoordinates(vertx, router);
	}
}
