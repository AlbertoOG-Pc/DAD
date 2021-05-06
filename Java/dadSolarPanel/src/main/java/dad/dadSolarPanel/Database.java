package dad.dadSolarPanel;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.Promise;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

/**
 * @author Alberto, Pablo
 * 
 *         Proyecto Placas solares - DAD
 * 
 *         Class Database - Clase que crea la conexion a la base de datos
 *
 */
public class Database extends AbstractVerticle {

	/**
	 * Atributo tipo MySQLPool donde se almacenara la conexion de la base de datos.
	 */
	public static MySQLPool mySqlClient;

	/**
	 * Metodo de entrada del verticle que mantendra la conexion de la base de datos
	 * 
	 * En este metodo start inicia la comunicacion con la base de datos y deja una
	 * conexion abierta ejecutar las diferentes consultas.
	 */
	public void start(Promise<Void> startFuture) {
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("dad").setUser("dad").setPassword("Solarpanel1");

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);

		System.out.println("Conexion a bbdd realizada");
	}

	/**
	 * Construcctor vacio de la clase.
	 */
	public Database() {
		super();
	}
}
