package dad.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dad.entity.User;
import dad.interfaces.BasicOperation;
import dad.interfaces.UserHandler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServerUser implements UserHandler, BasicOperation{
	/**
	 * 
	 */
	private EventBus eventBus;
	/**
	 * 
	 */
	private Router router;
	/**
	 * 
	 */
	private Gson gson;

	/**
	 * @param vertx
	 * @param router
	 * @return
	 */
	static RestServerUser create(Vertx vertx, Router router) {
		return new RestServerUser(vertx, router);
	}
	
	public RestServerUser(Vertx vertx, Router router) {
		this.router = router;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		eventBus = vertx.eventBus();
		/* GET METHOD */
		router.get("/api/users").handler(this::getAll);
		router.get("/api/users/:id").handler(this::getOne);

		/* POST METHOD */
		router.post("/api/user").handler(this::create);

		/* PUT METHOD */
		router.put("/api/user").handler(this::update);

		/* DELETE METHOD */
		router.delete("/api/user/:id").handler(this::delete);
	}

	@Override
	public void handle(RoutingContext event) {
		// TODO Auto-generated method stub
		router.handleContext(event);
	}

	@Override
	public void getAll(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "user_ALL");
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
		
	}

	@Override
	public void getOne(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();

		obj.put("CLASS", "user_ONE");
		try {
			obj.put("id", Integer.parseInt(routingContext.request().getParam("id")));
		} catch (NumberFormatException e) {
			obj.put("code", routingContext.request().getParam("id"));
		}
		eventBus.request("consulta", obj, reply -> {
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
		
	}

	@Override
	public void create(RoutingContext routingContext) {
		final User user = gson.fromJson(routingContext.getBodyAsString(), User.class);
		eventBus.request("POST", JsonObject.mapFrom(user).put("CLASS", "User"), reply -> {
			// LOS DATOS ESTAN AQUI
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
		
	}

	@Override
	public void update(RoutingContext routingContext) {
		final User user = gson.fromJson(routingContext.getBodyAsString(), User.class);
		eventBus.request("PUT", JsonObject.mapFrom(user).put("CLASS", "User"), reply -> {
			// LOS DATOS ESTAN AQUI
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
		
	}

	@Override
	public void delete(RoutingContext routingContext) {
		JsonObject obj = new JsonObject();
		obj.put("CLASS", "User").put("id", Integer.parseInt(routingContext.request().getParam("id")));

		eventBus.request("DELETE", obj, reply -> {
			// LOS DATOS ESTAN AQUI
			if (reply.succeeded()) {
				String replyMessage = (String) reply.result().body();
				System.out.println("Respuesta recibida: " + replyMessage);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(replyMessage);
			} else {
				System.out.println("No ha habido respuesta");
			}
		});
		
	}

}
