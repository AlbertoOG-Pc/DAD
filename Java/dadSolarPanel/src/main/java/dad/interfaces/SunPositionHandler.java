package dad.interfaces;

import dad.rest.RestServerSunPosition;
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
public interface SunPositionHandler extends Handler<RoutingContext> {
	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static RestServerSunPosition create(Vertx vertx, Router router) {
		return new RestServerSunPosition(vertx, router);
	}
}
