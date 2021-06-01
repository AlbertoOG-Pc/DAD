package dad.dadSolarPanel;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class Mqtt extends AbstractVerticle {

	public static MqttClient mqttClient;
	@Override
	public void start(Promise<Void> startFuture) {
		mqttClient = MqttClient.create(getVertx(),
				new MqttClientOptions().setAutoKeepAlive(true).setUsername("admin").setPassword("admin"));

		// .setUsername("admin").setPassword("admin"));

		mqttClient.connect(1883, "localhost", connection -> {
			if (connection.succeeded()) {
				System.out.println("Client name: " + connection.result().code().name());
			} else {
				System.out.println("Se ha producido un error en la conexión al broker");
			}
		});
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
}
