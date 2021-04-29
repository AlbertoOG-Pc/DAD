package dad.dadSolarPanel;

import dad.data.Board;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class App extends AbstractVerticle {

	MySQLPool mySqlClient;

	@Override
	public void start(Promise<Void> startFuture) {
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("dad").setUser("dad").setPassword("Solarpanel1");

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);
		
		getVertx().eventBus().consumer("consulta", message -> {
			//JsonArray result = getAll();
			getAll(message);
			//message.reply(result.toString());
		});
		
	}

	private void getAll(Message<?> message) {
		JsonArray result = new JsonArray();
		
		mySqlClient.query("SELECT * FROM dad.placa;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				// System.out.println(resultSet.size());
				for (Row elem : resultSet) {
					System.out.println("Elementos " + elem);
					result.add(JsonObject.mapFrom(new Board(elem.getInteger("id"), elem.getDouble("latitude"),
							elem.getDouble("longitude"), elem.getDouble("energiaMaxima"))));
				}
				//resultado = result.toString();
			} else {
				result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
				//resultado = "Error: " + res.cause().getLocalizedMessage();
			}
			message.reply(result.toString());
		});
		//return result;
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}

