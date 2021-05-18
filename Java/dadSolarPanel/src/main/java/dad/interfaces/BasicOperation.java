package dad.interfaces;

import io.vertx.ext.web.RoutingContext;

public interface BasicOperation {
	void getAll(RoutingContext routingContext);
	void getOne(RoutingContext routingContext);
	void create(RoutingContext routingContext);
	void update(RoutingContext routingContext);
	void delete(RoutingContext routingContext);
}
