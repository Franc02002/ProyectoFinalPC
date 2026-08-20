====================================================================
TRABAJO OBLIGATORIO FINAL PROG CONCURRENTE: PARQUE DE DIVERSIONES
====================================================================

Descripción General
-------------------
Este proyecto es una simulación concurrente de un parque de diversiones desarrollada en Java. 

El objetivo del TP es aplicar los diferentes mecanismos de sincronización (Semáforos, Monitores/Locks) y 
las herramientas avanzadas de la API de concurrencia de Java (CyclicBarrier, Exchanger y BlockingQueue) para 
coordinar el comportamiento de los visitantes, los empleados y el ciclo de vida del parque[cite: 1].


Instrucciones de Compilación y Ejecución
----------------------------------------
1. Descomprimir el archivo .zip que contiene el código fuente.
2. Importar la carpeta del proyecto en su IDE de preferencia, o navegar hasta el directorio raíz desde la terminal de comandos.
3. El punto de entrada del programa es la clase `Main.java`.
4. Compilar y ejecutar la clase `Main.java`.


Funcionamiento de la Simulación
-----------------------------------------
Al iniciar la ejecución, la consola mostrará un log detallado de los eventos concurrentes. El flujo temporal está orquestado por un hilo `Reloj` 
que avanza de manera automática manejando el ciclo de vida del predio:

- 09:00 hs: Apertura del parque. Se habilitan los molinetes, el personal ocupa sus puestos y los visitantes comienzan a ingresar[cite: 1].
- 18:00 hs: Cierre de molinetes. Se prohíbe el ingreso de nuevos visitantes[cite: 1].
- 19:00 hs: Cierre de atracciones. Los visitantes finalizan la actividad en curso y se dirigen al área de premios antes de la salida[cite: 1].
- 23:00 hs: Desalojo total. El programa fuerza un apagado limpio, interrumpiendo de forma segura cualquier hilo remanente y dando por finalizado el turno del personal[cite: 1].

Detalle de Sincronización 
---------------------------------------------------
Para evitar condiciones de interbloqueo (deadlocks) y asegurar el progreso, se implementaron las siguientes herramientas[cite: 1]:

* Área de Premios: Utiliza un `Exchanger` combinado con un `ReentrantLock` para garantizar un intercambio bidireccional atómico (fichas por premio) respetando una fila individual[cite: 1].
* Realidad Virtual (RV): Se implementó una `BlockingQueue` que actúa como mostrador. El hilo encargado ensambla los recursos individuales y deposita kits completos para que los visitantes los consuman[cite: 1].
* Teatro: Implementa una `CyclicBarrier` que oficia de torniquete para garantizar el acceso estricto en grupos de a 5 personas, junto con variables de condición (Monitores) para el desarrollo del espectáculo[cite: 1].
* Comedor: Se utilizaron semáforos para el control del cupo general y una `CyclicBarrier` para retener a los comensales hasta llenar una mesa de 4 antes de comer[cite: 1].
* Montaña Rusa: Se empleó un semáforo para la fila de espera y una `CyclicBarrier` para llenar los 5 lugares del carro antes de iniciar el recorrido[cite: 1].

Notas Adicionales sobre el Diseño
--------------------------------------------------
El código fue diseñado para manejar correctamente las interrupciones concurrentes (`InterruptedException`). Durante el desalojo forzoso de las 23:00 hs, los bloques `finally` garantizan que ningún recurso (como piezas de RV o permisos de entrada) sufra una fuga de memoria o un estado inconsistente.