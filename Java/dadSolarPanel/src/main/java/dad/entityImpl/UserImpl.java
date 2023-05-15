package dad.entityImpl;

import java.util.List;

import dad.dadSolarPanel.Database;
import dad.entity.User;
import dad.entity.enums.UserType;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class UserImpl {
	/**
	 * Lista donde almacenaremos los datos extraidos de la base de datos
	 */
	private List<User> userList;

	/**
	 * Constructor vacio
	 */
	public UserImpl() {
		super();
	}

	/**
	 * @return Devuelve el listado de user
	 */
	public List<User> getUserList() {
		return userList;
	}

	/**
	 * @param userList List<User> Establece o modifica el listado de user
	 *                  recibido
	 */
	public void setBoardList(List<User> userList) {
		this.userList = userList;
	}
	
	/**
	 * @param Message<?> Recibe el cuerpo de la comunicacion con el verticle que
	 *                   maneja el APIREST E imprime por pantalla el resultado
	 *                   obtenido
	 */
	public static void getALLUser(Message<?> message) {
		JsonArray result = new JsonArray();
		Database.mySqlClient.query("SELECT * from user", res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						System.out.println("Elementos " + resultSet);
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new User(elem.getInteger("id"),
									elem.getString("username"),
											elem.getString("password"), elem.getString("name"),
									UserType.valueOf(elem.getString("userType")))));
						}
					} else {
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}
	
	/**
	 * @param Message<?> Recibe el cuerpo de la comunicacion con el verticle que
	 *                   maneja el APIREST E imprime por pantalla el resultado
	 *                   obtenido
	 */
	public static void getOneUser(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		System.out.println(data);
		String query = "";

		if (data.containsKey("id")) {
			query = "SELECT * FROM user WHERE user.id = ?";
		} 
		Database.mySqlClient.preparedQuery(query,
				Tuple.of(data.containsKey("id") ? data.getInteger("id") : -1), res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject.mapFrom(new User(elem.getInteger("id"),
									elem.getString("username"),
									elem.getString("password"), elem.getString("name"),
							UserType.valueOf(elem.getString("userType")))));
						}
					} else {
						System.out.println("Error: " + res.cause().getLocalizedMessage());
						result.add(JsonObject.mapFrom(new String("Error: " + res.cause().getLocalizedMessage())));
					}
					message.reply(result.toString());
				});
	}
	
	
	/**
	 * @param message Message<?> Recibe el cuerpo de la comunicacion con el verticle
	 *                que maneja el APIREST E imprime por pantalla el resultado
	 *                obtenido
	 */
	public static void createUser(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		Database.mySqlClient.preparedQuery(
				"INSERT INTO dad.user (username, password, name, userType) VALUES (?, ?, ?, ?)",
				Tuple.of(data.getString("username"), data.getString("password"), data.getString("name"), data.getString("userType")),
				res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						data.remove("CLASS");
						data.put("id", resultSet.property(MySQLClient.LAST_INSERTED_ID));
						result.add(data);

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					message.reply(result.toString());
				});
	}
	
	/**
	 * @param message @param Message<?> Recibe el cuerpo de la comunicacion con el
	 *                verticle que maneja el APIREST E imprime por pantalla el
	 *                resultado obtenido
	 */
	public static void updateUser(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		Database.mySqlClient.preparedQuery(
				"UPDATE dad.board SET name = ?, username = ?, password = ?, userType = ? WHERE id = ?",
				Tuple.of(data.getString("name"), data.getString("username"),
						data.getString("password"), data.getString("userType"), data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						for (Row elem : resultSet) {
							System.out.println("Elementos " + elem);
							result.add(JsonObject
									.mapFrom(new User(elem.getInteger("id"), elem.getString("username"),
											elem.getString("password"), elem.getString("name"),
											UserType.valueOf(elem.getString("userType")))));
						}

					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
						// resultado = "Error: " + res.cause().getLocalizedMessage();
					}
					getOneUser(message);
					// message.reply(result.toString());
				});
	}
	
	/**
	 * @param message @param Message<?> Recibe el cuerpo de la comunicacion con el
	 *                verticle que maneja el APIREST E imprime por pantalla el
	 *                resultado obtenido
	 */
	public static void deleteUser(Message<?> message) {
		JsonArray result = new JsonArray();
		JsonObject data = JsonObject.mapFrom(message.body());
		data.remove("CLASS");
		getOneUser(message);
		Database.mySqlClient.preparedQuery("DELETE FROM dad.user WHERE id = ?", Tuple.of(data.getInteger("id")),
				res -> {
					if (res.succeeded()) {
					} else {
						System.out.println("Failure: " + res.cause().getMessage());
						result.add(JsonObject.mapFrom("Error: " + res.cause().getLocalizedMessage()));
					}
					// message.reply(result.toString());
				});
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userList == null) ? 0 : userList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserImpl other = (UserImpl) obj;
		if (userList == null) {
			if (other.userList != null)
				return false;
		} else if (!userList.equals(other.userList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserImpl [userList=" + userList + "]";
	}
	
	
}
