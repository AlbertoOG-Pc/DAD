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
  -  Resultado:
    ```Java
	[
	  {
	    "id": 1,                          //Identificador de la placa
	    "maxPower": 5.8,                  //Producción máxima
	    "coordinate":                     //Ubicación
        {                   
          "id": 2,                        //Id de la coordenada
          "longitude": 30.0,              //Longitud
          "latitude": 30.0                //Latitud
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
  - Código de Error:
  ```Java
    //TODO
    ```

Para obtener la información concreta de una placa
- Petición: 
``/api/board/{id}``

- Resultado


- Código de Error
```Java
//TODO
```
Para obtener información sobre todas las placas de unas mismas coordenadas
- ``/api/board/filtercoordinates/{id_coordinates}``
##### Peticiones POST
Para registrar una placa en el sistema
- ``/api/board``
##### Peticiones PUT
Para modificar los datos de una placa
- ``/api/board``

##### Peticiones DELETE
Para eliminar del sistema el registro de una placa
- ``/api/board/:id``

#### P
