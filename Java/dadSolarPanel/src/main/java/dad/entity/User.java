package dad.entity;

import dad.entity.enums.UserType;

/**
 * @author Alberto
 * 
 *         Proyecto Placas solares - DAD
 * 
 *         Class User - Entidad que maneja el modelo de datos de los usuarios.
 */
public class User {
	/**
	 * Identificador del usuario
	 */
	private int id;
	
	/**
	 * Username del usuario
	 */
	private String username;
	
	/**
	 * Contraseña del usuario
	 */
	private String password;

	/**
	 * Nombre completo del usuario
	 */
	private String name;

	/**
	 * Tipo de usuario
	 */
	private UserType usertype;

	
	/**
	 * Contrucctor vacio de la clase user
	 */
	
	public User() {
		super();
	}

	/**
	 * Construtor con todos los datos del user
	 * 
	 * @param id             Tipo Entero que guarda el identificar del usuario
	 * @param username		 Tipo String que guarda el nombre para identificar al usuario
	 * @param password		 Tipo String que guarda el password del usuario
	 * @param name           Tipo String que guarda el nombre completo del usuario
	 * @param UserType       Tipo UserType que guarda el tipo de usuario
	 */
	public User(int id, String username, String password, String name, UserType usertype) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.usertype = usertype;
	}

	/**
	 * @return Devuelve el identificador del user
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param int Identificador Modifica o establece el identificador del user
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserType getUsertype() {
		return usertype;
	}

	public void setUsertype(UserType usertype) {
		this.usertype = usertype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((usertype == null) ? 0 : usertype.hashCode());
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (usertype != other.usertype)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", usertype="
				+ usertype + "]";
	}

	


}
