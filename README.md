
# Panel Solar Inteligente Distribuido
> Alberto Otero González y Pablo Ruiz Jurado
## Introducción
### Introducción del proyecto en la asignatura
En la asignatura de Desarrollo de Aplicaciones Distribuidas (DAD) se ha propuesto realizar un proyecto software-hardware en el que con la ayuda del SOC ESP8266, conocido comúnmente como NodeMCU, crearemos de 0 un sistema distribuido con el uso de sensores y actuadores todo esto gestionado mediante una API Rest.
### Paneles Solares Distribuidos
Nuestro proyecto consiste en un sistema de varios paneles solares orientables con comunicación entre todos ellos, de forma que se intente mantener en todo momento un voltaje total final. 

Como se trata de un proyecto de bajo presupuesto este voltaje final se fijará a mano, pero lo que se está consiguiendo es imitar las grandes granjas de paneles solares, las cuales en los días más soleados pueden generar tanta energía que podría llegar a sobrecargar el sistema que las gestiona debido a la alta tensión y romperlo. Es por ello que se limita la producción máxima.

### Inteligencia
La inteligencia del sistema proviene de un fichero .CSV generado con la ayuda de https://www.sunearthtools.com/, a la cual le introducimos las coordenadas de cada placa y nos genera un fichero .CSV del año entero con la elevación y el azimut con el intervalo horario seleccionado (30 minutos en nuestro caso) para poder calcular la posición del Sol en el cielo.

Como las placas deben ser fijas introduciremos manualmente las coordenadas y el CSV, ya que estas se deberían generar una única vez por placa. Es por eso que no hemos incorporado un módulo GPS al sistema.

### Cálculo de la posición del sol
La herramienta mencionada anteriormente nos ofrece además una imagen de la trayectoria solar como previsualización a la hora de generar el CSV. Aquí podemos ver un ejemplo en Nueva York:

![image](https://user-images.githubusercontent.com/52832300/117276164-f970a500-ae5e-11eb-8f41-f45ef7afc751.png)
Para calcular la posición del sol debemos tener en cuenta estos dos elementos:
- **Acimut:** El Azimut se refiere a un ángulo de la orientación sobre la superficie de una esfera real o virtual. El significado preciso de este término cambia dependiendo del ámbito en el que lo estemos usando, en este caso 
- **Altura:** La altura de un astro en astronomía es el arco vertical contado desde el horizonte hasta el astro. Su valor absoluto siempre será menor o igual a 90º, siendo además positivo en el caso de que el astro sea visible (Por encima del horizonte) y negativo si no es visible (oculto por el horizonte).
<p align="center">
<img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Altura.PNG/300px-Altura.PNG">
</p>
La altura y el acimut son coordenadas que dependen de la posición del observador. Es decir, un mismo astro en un mismo momento visto por diferentes observadores en distintas posiciones de la tierra tiene diferentes "coordenadas horizontales" (Altura y Acimut). Esto significa que las coordenadas son locales.

Actualmente, se utiliza un sextante para medir la altura de un astro. Las coordenadas horizontales pueden ser calculadas matemáticamente.
Esta información puede ser usada para calcular la radiación solar recibida por la tierra en un periodo determinado o la proyección de sombras de un elemento que aún no existe, como un edificio, entre otras muchísimas funciones.

Con estos dos datos nuestro sistema sabrá como orientar cada placa solar.

# API
## Clases
| **Class**| **Descripción**                                 |
|----------|--------|
| Board| Contiene la información de la ubicación de cada placa solar y su producción máxima|
| BoardProduction| Contiene información de la producción de la placa, en un momento determinado la posición del servo y su producción |
|Coordinates| Las coordenadas de cada placa|
|Log| Registro de acciones realizadas sobre las placas con su fecha y hora|
|SunPosition| Las coordenadas horizontales (Altura y Acimut) del sol en cada momento con un intervalo de 30 minutos entre ellas desde una coordenada concreta|

### Board
#### Puntos de acceso
##### Peticiones GET
- Para obtener la información respectiva a todas las placas
  - **Petición**: 
``/api/boards``
  -  **Resultado**:
    ```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 2,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		},
		{ 
		"id": 2,
		"maxPower": 8.77,
		"coordinate":
			{
			"id": 1,
			"longitude": 25.0,
			"latitude": 25.0
			}
		},
		.
		.
		.
	]
    ```
  - **Código de Error**:
  ```Java
    //TODO
    ```

- Para obtener la información concreta de una placa
  - **Petición**: 
``/api/board/{id}``

  - **Resultado**
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 2,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		}
	]
```
   
  - **Código de Error**
  ```Java
  //TODO
  ```
- Para obtener información sobre todas las placas de unas mismas coordenadas
  - ``/api/board/filtercoordinates/{id_coordinates}``

-  **Resultado**:
    ```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 1,				//Id de la coordenada
			"longitude": 25.0,			//Longitud
			"latitude": 25.0			//Latitud
			} 
		},
		{ 
		"id": 2,
		"maxPower": 8.77,
		"coordinate":
			{
			"id": 1,
			"longitude": 25.0,
			"latitude": 25.0
			}
		},
		.
		.
		.
	]
    ```
  - **Código de Error**:
	  ```Java
	    //TODO
	```
 
##### Peticiones POST
- Para registrar una placa en el sistema
  - **Petición**: 
``/api/board``
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 2,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		}
	]
```

  - **Resultado**
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 2,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		}
	]
```
   
  - **Código de Error**
  ```Java
  //TODO
  ```

##### Peticiones PUT
Para modificar los datos de una placa
- **Petición :**
	-  URI: ``/api/board/{id}``
	- Cuerpo: 
	Objeto JSON con los datos de la placa Actualizados
```javascript
	[
		{
		"id": 1,						//Identificador de la placa
		"maxPower": 9,					//Producción máxima
		"id_coordinate": 1				//Id_Coordinate
		}
	]
```
- **Resultado:**
Objeto JSON con los datos de la placa eliminada
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 2,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		}
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```

##### Peticiones PATCH
 Para modificar la localización de una placa, para ello usaremos una modificación del campo id_coordinates de la placa.

-  **Petición :**
	- URI: ``/api/board/coordinates/{id} //id board a modificar``
	- Cuerpo: 
Objeto JSON con los datos a Actualizar en este caso solo id_coordinates
```javascript
	[
		{
		"id_coordinate": 1				//Identificador de la nueva coordenada
		}
	]
```
- **Resultado:**
Objeto JSON con los datos de la placa eliminada
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 1,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		}
	]
```
 - **Código de Error**:
  ```Java
    //TODO
  ```
  
##### Peticiones DELETE
 Para eliminar del sistema el registro de una placa
- **Petición :**
	- URI: ``/api/board/{id}``
	- Cuerpo: `` {} ``
- **Resultado:**
Objeto JSON con los datos de la placa eliminada
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
		"maxPower": 5.8,				//Producción máxima
		"coordinate":					//Ubicación
			{
			"id": 2,				//Id de la coordenada
			"longitude": 30.0,			//Longitud
			"latitude": 30.0			//Latitud
			} 
		}
	]
```

 -  **Código de Error**:
  ```Java
    //TODO
  ```

### BoardProduction
#### Puntos de acceso
##### Peticiones GET
Para obtener la información respectiva a la producción de todas las placas
  - **Petición**: 
	-  URI: ``/api/boardsProduction``
	- Cuerpo: ``{}``
  -  **Resultado**:
```javascript
	[
		{
		"id": 1,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-16 10:00:00",	//Fecha del regristro
		"production": 20.27				//Produccion
		},
		{
		"id": 2,					
		"id_board": 2,				
		"positionServo": 442,					
		"date": "2021-04-15 10:00:00",
		"production": 20.27			
		},
		.
		.
		.
	]
```
	
  - **Código de Error**:
```javascript
    //TODO
```
Para obtener la información concreta de un registro de producción
- **Petición:**
	- URI: ``/api/boardProduction/{id}``
	- Cuerpo: ``{}``

- **Resultado:** 
```javascript
	[
		{
		"id": 1,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-16 10:00:00",	//Fecha del regristro
		"production": 20.27				//Produccion
		}
	]
```
- Código de Error
```Java
//TODO
```

Para obtener información sobre todas las placas de unas mismas coordenadas
- **Peticion:**
	- URI:  ``/api/boardProduction/board/{id_board}``
	- Cuerpo: ``{}``
	- 
- **Resultado**:
```javascript
	[
		{
		"id": 1,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-16 10:00:00",	//Fecha del regristro
		"production": 20.27				//Produccion
		},
		{
		"id": 4,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-16 11:00:00",	//Fecha del regristro
		"production": 20.27				//Produccion
		},
		.
		.
		.
	]
```

- Código de Error
```Java
//TODO
```

Para obtener información sobre los registros de producción de una placa concreta, por encima de un umbral de producción.

- **Peticion:**
	- URI:  ``/api/boardProduction/board/{id_board}/{production}``
	- Cuerpo: ``{}``
	- 
- **Resultado**:
```javascript
	[
		{
		"id": 1,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-16 10:00:00",	//Fecha del regristro
		"production": 20				//Produccion
		},
		{
		"id": 5,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-16 11:00:00",	//Fecha del regristro
		"production": 21				//Produccion
		},
		.
		.
		.
	]
```

- Código de Error
```Java
//TODO
```

Para obtener información sobre los registros de producción entre dos fechas.

- **Peticion:**
	- URI:  ``/api/boardProduction/datesFilter/``
	- Cuerpo: 
Objeto JSON con los datos de la fecha de a  filtrar
```javascript
	[
		{
		"fechaIni" : "?",		//Fecha inicial del filtro
		"fechaFin" : "?"}		//Fecha final del filtro	
		}
	]
```

- **Resultado**:
```javascript
	[
		{
		"id": 1,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-17 10:00:00",	//Fecha del regristro
		"production": 20				//Produccion
		},
		{
		"id": 7,						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 442,			//Posicion del servo
		"date": "2021-04-18 11:00:00",	//Fecha del regristro
		"production": 21				//Produccion
		},
		.
		.
		.
	]
```

- Código de Error
```Java
//TODO
```

##### Peticiones POST
Para insertar un registro de producción de una placa
- **Petición :**
	-  URI: ``/api/boardProduction``
	- Cuerpo: 
	Objeto JSON con los datos de la placa Actualizados
```javascript
	[
		{						
		"id_board": 2,					//Identificador de la placa
		"positionServo": 12,			//Posicion del servo
		"date": "2021-04-17 09:00:00",	//Fecha del regristro
		"production": 10				//Produccion
		},
	]
```
- **Resultado:**
Objeto JSON con los datos insertado
```javascript
	[
		{	
		"id" : 54						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 12,			//Posicion del servo
		"date": "2021-04-17 09:00:00",	//Fecha del regristro
		"production": 10				//Produccion
		},
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```
  
##### Peticiones PUT

Para modificar un registro de producción de una placa
- **Petición :**
	-  URI: ``/api/boardProduction``
	- Cuerpo: 
	Objeto JSON con los datos de produccion actualizados Actualizados
```javascript
	[
		{	
		"id" : 54					
		"id_board": 2,					//Identificador de la placa
		"positionServo": 156,			//Posicion del servo
		"date": "2021-04-17 09:00:00",	//Fecha del regristro
		"production": 10				//Produccion
		},
	]
```
- **Resultado:**
Objeto JSON con los datos insertado
```javascript
	[
		{	
		"id" : 54						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 156,			//Posicion del servo
		"date": "2021-04-17 09:00:00",	//Fecha del regristro
		"production": 10				//Produccion
		},
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```
##### Peticiones DELETE
 Para eliminar del sistema un registro de producción
- **Petición :**
	- URI: ``/api/boardProduction/{id}``
	- Cuerpo: `` {} ``
- **Resultado:**
Objeto JSON con los datos del registro eliminado
```javascript
	[
		{	
		"id" : 54						//Identificador del registro
		"id_board": 2,					//Identificador de la placa
		"positionServo": 156,			//Posicion del servo
		"date": "2021-04-17 09:00:00",	//Fecha del regristro
		"production": 10				//Produccion
		},
	]
```

 -  **Código de Error**:
  ```Java
    //TODO
  ```


### Coordinates
#### Puntos de acceso
##### Peticiones GET
- Para obtener la información respectiva a todas las coordenadas
  - **Petición:**
``/api/coordinates``
  -  **Resultado:**
    ```javascript
	[
		{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
		},
		{ 
		"id": 2,
		"longitude": 25.0,
		"latitude": 25.0
		},
		.
		.
		.
	]
    ```
  - **Código de Error:**
  ```Java
    //TODO
   ```

- Para obtener la información concreta de una coordenada
  - **Petición:** 
    - URI: ``/api/coordinates/{id}``
    - Body: ``{}``
  - **Resultado:**
    ```javascript
	{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
	}
    ```
   
  - **Código de Error:**
  ```Java
  //TODO
  ```
  
##### Peticiones POST
- Para registrar una coordenada en el sistema
  - **Petición:** 
    - URI: ``/api/coordinate``
    - Body:
    ```javascript
	{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
	}
    ```

  - **Resultado:**
    ```javascript
	{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
	}
    ```
   
  - **Código de Error:**
    ```java
    //TODO
    ```

##### Peticiones PUT
Para modificar los datos de una coordenada
- **Petición :**
	-  URI: ``/api/board/{id}``
	- Cuerpo: 
	```javascript
	{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
	}
    ```
	Objeto JSON con los datos de la coordenada actualizados
	```javascript
	{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
	}
    ```
 -  **Código de Error**:
    ```javascript
	//TODO
    ```
##### Peticiones DELETE
- Para eliminar del sistema el registro de una coordenada
  - **Petición :**
  	- URI: ``/api/board/{id}``
	- Cuerpo: `` {} ``
  - **Resultado:**
Objeto JSON con los datos de la coordenada eliminada
	```javascript
	{
		"id": 1,					//Identificador de las coordenadas
		"longitude": 30.0,				//Longitud
		"latitude": 30.0				//Latitud
	}
    ```

 -  **Código de Error**:
    ```javascript
	//TODO
    ```









