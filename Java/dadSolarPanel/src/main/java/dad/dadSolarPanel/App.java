package dad.dadSolarPanel;

import dad.entityImpl.BoardImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class App extends AbstractVerticle {
	

	@Override
	public void start(Promise<Void> startFuture) {		
		getVertx().eventBus().consumer("consulta", message -> {
			getQuery(message);
		});
		
	}

	private void getQuery(Message<?> message) {
		
		JsonArray result = new JsonArray();
		switch(message.body().toString()) {
		case "board_ALL": BoardImpl.getALLBoard(message); break;
			default: 
				result.add(JsonObject.mapFrom(new String("Error: Invalid Param")));
				message.reply(result.toString());
		}
		
		//return result;
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}

