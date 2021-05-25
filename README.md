
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


(WIP)

# Base de datos
Nuestra base de datos está diseñada la siguiente forma:

<p align="center">
<img src = "https://user-images.githubusercontent.com/52832300/119256060-09a6b500-bbbf-11eb-9f7c-142a572ff352.png">
</p>

# Hardware

## Componentes

Cada sistema de nuestro proyecto está compuesto de lo siguiente:

- 1 ESP8266 (<a href="https://www.amazon.es/SeeKool-Internet-Desarrollo-inal%C3%A1mbrico-Micropython/dp/B07DRF9YTV/ref=sr_1_5?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=1I6ECRR64SWZT&dchild=1&keywords=esp8266&qid=1621951147&sprefix=esp8266%2Caps%2C204&sr=8-5">Amazon</a>): El cual se encargará de comunicarse con la base de datos via WiFi y con las demás placas por MQTT
- 2 Placas Solares Mini con Vmax = 1.5V cada una (<a href="https://www.amazon.es/gp/product/B07VYS266S/ref=ppx_yo_dt_b_asin_title_o03_s00?ie=UTF8&psc=1">Amazon</a>): Estas placas serán las que irán conectadas al pin analógico A0 del ESP8266, para leer el valor del voltaje y en función de este actuar sobre los servos.
- 2 Servomotores (<a href="https://www.amazon.es/ZHITING-Walking-Helicopter-Airplane-Control/dp/B088NQTBPB/ref=sr_1_6?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&dchild=1&keywords=servos+arduino&qid=1621951592&sr=8-6">Amazon</a>): Los cuales se encargarán de mover la base de las placas y el eje de estas, significando cada movimiento una traducción del azimut y de la elevación solar en ese momento.
- 1 fuente de 5 voltios: Para alimentar correctamente los dos servomotores a la vez, ya que no es posible con los 3'3V que nos da nuestro ESP8266 (Actualmente el generador es la toma de 5V de un Arduino con nada más conectado

### Esquema del Circuito
![Esquema Circuito](https://user-images.githubusercontent.com/52832300/119522511-508dd980-bd7c-11eb-9bda-d2498917c3ba.png)


Como se indica en la imagen, la herramienta online gratuita Tinkercad nos permite realizar un diagrama del circuito de forma sencilla, pero el problema es que no podemos añadir un ESP8266, por lo que lo hemos sustituido por un Arduino y hemos conectado los componentes a los mismos pines que en el ESP8266:

- Pines D2 y D3 para los servos
  - D2 para el azimut
  - D3 para la elevación
- Pin A0 para el positivo de las placas solares, las cuales están conectadas en serie, funcionando entonces A0 como Polímetro
- Los 5V de salida del Arduino alimentan ambos servos

### Estructura del sistema (Impresión 3D)

La estructura que sujeta nuestro sistema está basad en una de la página <a href= "https://www.thingiverse.com/">Thingiverse</a>, la cual tiene multitud de diseños en 3D listos para imprimir.

Encontramos la siguiente estructura: <a href= "https://www.thingiverse.com/thing:2939509">Solar Cell Tracking by Michaelo</a> 
<p align="center">
<img src = "https://user-images.githubusercontent.com/52832300/119522579-613e4f80-bd7c-11eb-980a-23e86215d9d1.jpg">
</p>

Modificando las medidas para adaptarnos a nuestras placas solares tenemos una forma de sujetar y apuntar las placas solares al sol con los dos servos. El servo conectado a D2 (azimut) es el inferior y el servo conectado a D3 (Elevación) el superior con el eje paralelo al suelo.



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
#### Peticiones GET
##### Para obtener la información respectiva a todas las placas
  - **Petición**: 
  	- URI : ``/api/boards``
  	- body: ``{}``
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
##### Para obtener información sobre todas las placas de unas mismas coordenadas
- **Petición:**
	- URI : ``/api/board/filtercoordinates/{id_coordinates}``
  	- body: ``{}``
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
 
#### Peticiones POST
##### Para registrar una placa en el sistema
  - **Petición**: 
  	- URI : ``/api/board``
  	- body: ``{}`
```javascript
	[
		{
		"id_coordinate"  : 2				//Identificador de la coordenada
		"maxPower": 5.8,				//Producción máxima	
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

#### Peticiones PUT
##### Para modificar los datos de una placa
- **Petición :**
	- URI: ``/api/board/{id}``
	- body: 
	Objeto JSON con los datos de la placa Actualizados
```javascript
	[
		{
		"id": 1,					//Identificador de la placa
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

#### Peticiones PATCH
##### Para modificar la localización de una placa, para ello usaremos una modificación del campo id_coordinates de la placa.

-  **Petición :**
	- URI: ``/api/board/coordinates/{id} 	//id board a modificar``
	- body: 

Objeto JSON con los datos a Actualizar en este caso solo id_coordinates
```javascript
	[
		{
		"id_coordinate": 1	//Identificador de la nueva coordenada
		}
	]
```
- **Resultado:**
Objeto JSON con los datos de la placa eliminada
```javascript
	[
		{
		"id": 1,			//Identificador de la placa
		"maxPower": 5.8,		//Producción máxima
		"coordinate":			//Ubicación
			{
			"id": 1,			//Id de la coordenada
			"longitude": 30.0,		//Longitud
			"latitude": 30.0		//Latitud
			} 
		}
	]
```
 - **Código de Error**:
  ```Java
    //TODO
  ```
  
#### Peticiones DELETE
##### Para eliminar del sistema el registro de una placa
- **Petición :**
	- URI: ``/api/board/{id}``
	- body: `` {} ``
- **Resultado:**
Objeto JSON con los datos de la placa eliminada
```javascript
	[
		{
		"id": 1,			//Identificador de la placa
		"maxPower": 5.8,		//Producción máxima
		"coordinate":			//Ubicación
			{
			"id": 2,			//Id de la coordenada
			"longitude": 30.0,		//Longitud
			"latitude": 30.0		//Latitud
			} 
		}
	]
```

 -  **Código de Error**:
  ```Java
    //TODO
  ```

### BoardProduction
#### Peticiones GET
##### Para obtener la información respectiva a la producción de todas las placas
  - **Petición**: 
	- URI: ``/api/boardsProduction``
	- body: ``{}``
  -  **Resultado**:
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE": 442,			//Posicion del servo1
		"positionServoA": 442,			//Posicion del servo2
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"production": 20.27			//Produccion
		},
		{
		"id": 2,					
		"id_board": 2,				
		"positionServoE": 442,
		"positionServoA": 442,
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
##### Para obtener la información concreta de un registro de producción
- **Petición:**
	- URI: ``/api/boardProduction/{id}``
	- body: ``{}``

- **Resultado:** 
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE": 442,			//Posicion del servo1
		"positionServoA": 442,			//Posicion del servo2
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"production": 20.27			//Produccion
		}
	]
```
- Código de Error
```Java
//TODO
```

##### Para obtener información sobre todas las placas de unas mismas coordenadas
- **Peticion:**
	- URI:  ``/api/boardProduction/board/{id_board}``
	- body: ``{}``

- **Resultado**:
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE": 442,			//Posicion del servo1
		"positionServoA": 442,			//Posicion del servo2
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"production": 20.27			//Produccion
		},
		{
		"id": 4,					
		"id_board": 2,				
		"positionServoE": 442,
		"positionServoA": 442,
		"date": "2021-04-16 11:00:00",	
		"production": 20.27			
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

##### Para obtener información sobre los registros de producción de una placa concreta, por encima de un umbral de producción.

- **Peticion:**
	- URI:  ``/api/boardProduction/board/{id_board}/{production}``
	- body: ``{}``
	- 
- **Resultado**:
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE": 442,			//Posicion del servo1
		"positionServoA": 442,			//Posicion del servo2
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"production": 20			//Produccion
		},
		{
		"id": 5,						
		"id_board": 2,				
		"positionServoE": 442,
		"positionServoA": 442,
		"date": "2021-04-16 11:00:00",	
		"production": 21
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

##### Para obtener información sobre los registros de producción entre dos fechas.

- **Peticion:**
	- URI:  ``/api/boardProduction/datesFilter/``
	- body: 
Objeto JSON con los datos de la fecha de a  filtrar
```javascript
	[
		{
		"fechaIni" : "?",		//Fecha inicial del filtro
		"fechaFin" : "?"		//Fecha final del filtro	
		}
	]
```

- **Resultado**:
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE": 442,			//Posicion del servo1
		"positionServoA": 442,			//Posicion del servo2
		"date": "2021-04-17 10:00:00",		//Fecha del regristro
		"production": 20			//Produccion
		},
		{
		"id": 7,						
		"id_board": 2,					
		"positionServoE": 442,
		"positionServoA: 442,
		"date": "2021-04-18 11:00:00",	
		"production": 21	
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

#### Peticiones POST
##### Para insertar un registro de producción de una placa
- **Petición :**
	-  URI: ``/api/boardProduction``
	- body: 
	Objeto JSON con los datos de la placa Actualizados
```javascript
	[
		{						
		"id_board": 2,				//Identificador de la placa
		"positionServoE: 12,			//Posicion del servo1
		"positionServoA 12,			//Posicion del servo2
		"date": "2021-04-17 09:00:00",		//Fecha del regristro
		"production": 10			//Produccion
		}
	]
```
- **Resultado:**
Objeto JSON con los datos insertado
```javascript
	[
		{	
		"id" : 54				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE: 12,			//Posicion del servo1
		"positionServoA 12,			//Posicion del servo2
		"date": "2021-04-17 09:00:00",		//Fecha del regristro
		"production": 10			//Produccion
		}
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```
  
#### Peticiones PUT

##### Para modificar un registro de producción de una placa
- **Petición :**
	-  URI: ``/api/boardProduction``
	- body: 
	Objeto JSON con los datos de produccion actualizados Actualizados
```javascript
	[
		{	
		"id" : 54					
		"id_board": 2,				//Identificador de la placa
		"positionServoE: 12,			//Posicion del servo1
		"positionServoA 12,			//Posicion del servo2
		"date": "2021-04-17 09:00:00",		//Fecha del regristro
		"production": 10			//Produccion
		}
	]
```
- **Resultado:**
Objeto JSON con los datos insertado
```javascript
	[
		{	
		"id" : 54				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE: 12,			//Posicion del servo1
		"positionServoA 12,			//Posicion del servo2
		"date": "2021-04-17 09:00:00",		//Fecha del regristro
		"production": 10			//Produccion
		}
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```
#### Peticiones DELETE
##### Para eliminar del sistema un registro de producción
- **Petición :**
	- URI: ``/api/boardProduction/{id}``
	- body: `` {} ``
- **Resultado:**
Objeto JSON con los datos del registro eliminado
```javascript
	[
		{	
		"id" : 54				//Identificador del registro
		"id_board": 2,				//Identificador de la placa
		"positionServoE: 12,			//Posicion del servo1
		"positionServoA 12,			//Posicion del servo2
		"date": "2021-04-17 09:00:00",		//Fecha del registro
		"production": 10			//Produccion
		}
	]
```

 -  **Código de Error**:
  ```Java
    //TODO
  ```


### Coordinates
#### Peticiones GET
##### Para obtener la información respectiva a todas las coordenadas
  - **Petición:**
  	- URI: ``/api/coordinates``
	- body: `` {} ``

  -  **Resultado:**
    ```javascript
	[
		{
		"id": 1,				//Identificador de las coordenadas
		"longitude": 30.0,			//Longitud
		"latitude": 30.0			//Latitud
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

###### Para obtener la información concreta de una coordenada
  - **Petición:** 
    - URI: ``/api/coordinates/{id}``
    - Body: ``{}``
  - **Resultado:**
    ```javascript
	{
		"id": 1,			//Identificador de las coordenadas
		"longitude": 30.0,		//Longitud
		"latitude": 30.0		//Latitud
	}
    ```
   
  - **Código de Error:**
  ```Java
  //TODO
  ```
  
#### Peticiones POST
##### Para registrar una coordenada en el sistema
  - **Petición:** 
    - URI: ``/api/coordinate``
    - Body:
    ```javascript
	{
		"id": 1,			//Identificador de las coordenadas
		"longitude": 30.0,		//Longitud
		"latitude": 30.0		//Latitud
	}
    ```

  - **Resultado:**
    ```javascript
	{
		"id": 1,			//Identificador de las coordenadas
		"longitude": 30.0,		//Longitud
		"latitude": 30.0		//Latitud
	}
    ```
   
  - **Código de Error:**
    ```java
    //TODO
    ```

#### Peticiones PUT
##### Para modificar los datos de una coordenada
- **Petición :**
	-  URI: ``/api/board/{id}``
	- body: 
	```javascript
	{
		"id": 1,			//Identificador de las coordenadas
		"longitude": 30.0,		//Longitud
		"latitude": 30.0		//Latitud
	}
    ```
	Objeto JSON con los datos de la coordenada actualizados
	```javascript
	{
		"id": 1,			//Identificador de las coordenadas
		"longitude": 30.0,		//Longitud
		"latitude": 30.0		//Latitud
	}
    ```
 -  **Código de Error**:
    ```javascript
	//TODO
    ```
#### Peticiones DELETE
##### Para eliminar del sistema el registro de una coordenada
  - **Petición :**
  	- URI: ``/api/board/{id}``
	- body: `` {} ``
  - **Resultado:**
Objeto JSON con los datos de la coordenada eliminada
	```javascript
	{
		"id": 1,			//Identificador de las coordenadas
		"longitude": 30.0,		//Longitud
		"latitude": 30.0		//Latitud
	}
    ```

 -  **Código de Error**:
    ```javascript
	//TODO
    ```

### LOG
#### Peticiones GET
##### Para obtener la información respectiva a la registros Log almacenados
  - **Petición**: 
	- URI: ``/api/log``
	- body: ``{}``
  -  **Resultado**:
```javascript
	[
		{
		"id": 1,					//Identificador del registro
		"id_board": 2,					//Identificador de la placa		
		"date": "2021-04-16 10:00:00",			//Fecha del regristro
		"issue": "Primer Log"				//Asunto
		},
		{
		"id": 1,						
		"id_board": 1,						
		"date": "2021-04-16 10:00:00",	
		"issue": "Segundo Log"			
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
##### Para obtener la información concreta de un registro de log
- **Petición:**
	- URI: ``/api/log/{id}``
	- body: ``{}``

- **Resultado:** 
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log"			//Asunto
		}
	]
```
- Código de Error
```Java
//TODO
```

##### Para obtener información sobre todos los registros log de una placa
- **Peticion:**
	- URI:  ``/api/log/board/{id_board}``
	- body: ``{}``

- **Resultado**:
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log"			//Asunto
		},
		{
		"id": 6,										
		"id_board": 2,											
		"date": "2021-04-16 10:00:00",				
		"issue": "Servo Manipulado automaticamente"	
		}
		.
		.
		.
	]
```

- Código de Error
```Java
//TODO
```

##### Para obtener información sobre los registros de log entre dos fechas.

- **Peticion:**
	- URI:  ``/api/log/datesFilter/``
	- body: 
Objeto JSON con los datos de la fecha de a  filtrar
```javascript
	[
		{
		"fechaIni" : "?",	//Fecha inicial del filtro
		"fechaFin" : "?"	//Fecha final del filtro	
		}
	]
```

- **Resultado**:
```javascript
	[
		{
		"id": 1,				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log"			//Asunto
		},
		{
		"id": 5,										
		"id_board": 2,										
		"date": "2021-04-16 15:00:00",					
		"issue": "Servo Manipulado automaticamente"	
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

#### Peticiones POST
##### Para insertar un registro de log sobre de una placa
- **Petición :**
	- URI: ``/api/log``
	- body: 
	Objeto JSON con los datos a insertar
```javascript
	[
		{
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log"			//Asunto
		}
	]
```
- **Resultado:**
Objeto JSON con los datos insertado
```javascript
	[
		{
		"id" : 1				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log"			//Asunto
		}
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```
  
#### Peticiones PUT

##### Para modificar un registro de producción de una placa
- **Petición :**
	- URI: ``/api/log``
	- body: 
	Objeto JSON con los datos de produccion actualizados Actualizados
```javascript
	[
		{
		"id" : 1				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log editado"		//Asunto
		}
	]
```
- **Resultado:**
Objeto JSON con los datos actualizados
```javascript
	[
		{
		"id" : 1				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log editado"		//Asunto
		}
	]
```
  - **Código de Error**:
  ```Java
    //TODO
  ```
#### Peticiones DELETE
##### Para eliminar del sistema un registro de log
- **Petición :**
	- URI: ``/api/log/{id}``
	- body: `` {} ``
- **Resultado:**
Objeto JSON con los datos del registro eliminado
```javascript
	[
		{
		"id" : 1				//Identificador del registro
		"id_board": 2,				//Identificador de la placa		
		"date": "2021-04-16 10:00:00",		//Fecha del regristro
		"issue": "Primer Log editado"		//Asunto
		}
	]
```

 -  **Código de Error**:
  ```Java
    //TODO
  ```
### SunPosition
#### Peticiones GET
##### Para obtener la información respectiva a todas las Posiciones Solares
  - **Petición:**
  	- URI: ``/api/sunPosition``
	- body: `` {} ``
  -  **Resultado:**
 ```javascript
	[
		{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 4,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
		},
		{
		"id": 2,
		"id_coordinates": 4,
		"date": "12-12-1212 15:30:00"
		"elevation": 23.0,
		"azimut": 340.0
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

##### Para obtener la información concreta de una SunPosition
  - **Petición:** 
    - URI: ``/api/sunPosition/{id}``
    - body: ``{}``
  - **Resultado:**
    ```javascript
	[
		{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 4,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
		}
	]
    ```
   
  - **Código de Error:**
  ```Java
  //TODO
  ```
 
##### Para obtener todas las SunPosition de un día en concreto
  - **Petición:** 
    - URI: ``/api/sunPosition/dateFilter/``
    - body: ``{}``

    ```javascript
	{
		"date": "12-12-1212"
	}
    ```
    
  - **Resultado:**
    ```javascript
	[
		{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 4,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
	]
    ```
   
  - **Código de Error:**
  ```Java
  //TODO
  ```
   
##### Peticiones POST
##### Para registrar una Posición Solar en el sistema
  - **Petición:** 
    - URI: ``/api/sunPosition``
    - body:
    ```javascript
	{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 4,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
	}
	```

  - **Resultado:**
    ```javascript
	{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 4,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
	}
	```
   
  - **Código de Error:**
    ```java
    //TODO
    ```

#### Peticiones PUT
##### Para modificar los datos de una Posición Solar
- **Petición :**
	- URI: ``/api/sunPosition``
	- body: 
    ```javascript
	{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 3,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
	}
	```
	Objeto JSON con los datos de la SunPosition actualizados
    ```javascript
	{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 3,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
	}
	```
 -  **Código de Error**:
    ```javascript
	//TODO
    ```
#### Peticiones DELETE
##### Para eliminar del sistema el registro de una coordenada
  - **Petición :**
  	- URI: ``/api/sunPosition/{id}``
	- body: `` {} ``
  - **Resultado:**
Objeto JSON con los datos de la coordenada eliminada
    ```javascript
	{
		"id": 1,				//Identificador de la SunPosition
		"id_coordinates": 3,			//Identificador de las coordenadas
		"date": "12-12-1212 12:00:00"		//Fecha y hora de esa SunPosition
		"elevation": 30.0,			//Elevacion del sol
		"azimut": 30.0				//Azimut
	}
	```

 -  **Código de Error**:
    ```javascript
	//TODO
    ```











