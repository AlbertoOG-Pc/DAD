package dad.dadSolarPanel;

import com.google.gson.JsonSyntaxException;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class Mqtt extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startFuture) {
		MqttClient mqttClient = MqttClient.create(getVertx(),
				new MqttClientOptions().setAutoKeepAlive(true).setUsername("admin").setPassword("admin"));

		// .setUsername("admin").setPassword("admin"));

		mqttClient.connect(1883, "192.168.1.81", connection -> {
			if (connection.succeeded()) {
				System.out.println("Client name: " + connection.result().code().name());
				mqttClient.subscribe("topic_1", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to topic");
					}
				});
				mqttClient.subscribe("topic_2", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to topic");
					}
				});

				mqttClient.publishHandler(message -> {
					System.out.println("Message published on topic: " + message.topicName());
					System.out.println(message.payload().toString());
					if (message.topicName().equals("topic_2")) {
						try {
							/*
							 * SensorEntity sensor = gson.fromJson(message.payload().toString(),
							 * SensorEntity.class); System.out.println(sensor.toString());
							 */
						} catch (JsonSyntaxException e) {
							System.out.println("Message content is wrong");
						}
					}
				});
				mqttClient.publish("topic_2", Buffer.buffer("Lo que sea"), MqttQoS.AT_LEAST_ONCE, false, false);
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
