
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
- **Azimut:** El Azimut se refiere a un ángulo de la orientación sobre la superficie de una esfera real o virtual. El significado preciso de este término cambia dependiendo del ámbito en el que lo estemos usando, en este caso 
- **Altura:** La altura de un astro en astronomía es el arco vertical contado desde el horizonte hasta el astro. Su valor absoluto siempre será menor o igual a 90º, siendo además positivo en el caso de que el astro sea visible (Por encima del horizonte) y negativo si no es visible (oculto por el horizonte).
<p align="center">
<img src = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Altura.PNG/300px-Altura.PNG">
</p>










 

