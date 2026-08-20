import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Teatro {
    
    //Se utilizara un semaforo para limitar la capacidad maxima de la sala a 20 personas
    private Semaphore capacidadTeatro = new Semaphore(20);
    //Se utiliza un lock y conditions para coordinar la espera de los visitantes y el aviso del controlador
    private ReentrantLock lock = new ReentrantLock();
    private Condition alertaControlador = lock.newCondition();
    private Condition alertaEspectadores = lock.newCondition();

    private int espectadoresAdentro = 0;
    private boolean espectaculoComenzo = false;

    public void entrarTeatro(Visitante visitante) {
        // Este metodo sera ejecutado por los hilos visitantes, primero tomaran un lugar en la sala
        // y luego esperaran a que el show comience para poder verlo
        try {
            capacidadTeatro.acquire();

            lock.lock();
            try {
                espectadoresAdentro++;
                System.out.println(visitante.getName() + " Ha ingresado al teatro y tomo asiento");

                //Si es el primer visitante en entrar o el ultimo en llenar la capacidad maxima
                //se le da el aviso al hilo controlador para que gestione el cronometro
                if (espectadoresAdentro == 1 || espectadoresAdentro == 20) {
                    alertaControlador.signal();
                }

                //El hilo visitante espera a que el controlador inicie el show
                while (!espectaculoComenzo) {
                    alertaEspectadores.await(); 
                }

                //El hilo visitante se queda esperando a que el show termine
                while (espectaculoComenzo) {
                    alertaEspectadores.await();
                }

                System.out.println(visitante.getName() + " sale de la sala al finalizar el show.");

            } finally {
                lock.unlock();
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void comenzarShow() {
        // Este metodo sera ejecutado por el hilo controlador del teatro, este esta
        // encargado de comenzar la funcion por tiempo o por capacidad y de notificar cuando esta termino
        try {
            while (!Thread.currentThread().isInterrupted()) {
                
                lock.lock();
                try {
                    //El controlador espera a que ingrese al menos un visitante a la sala
                    while (espectadoresAdentro == 0) {
                        alertaControlador.await();
                    }

                    System.out.println("El teatro tiene al menos un espectador, inicia la cuenta regresiva de 15 segundos");

                    //El controlador espera a que se llene la sala o pasen 15 segundos maximo
                    alertaControlador.await(15, TimeUnit.SECONDS);

                    System.out.println("Comienza la funcion del teatro con " + espectadoresAdentro + " espectadores");
                    espectaculoComenzo = true;
                    
                    alertaEspectadores.signalAll(); 

                } finally {
                    lock.unlock();
                }

                //Se simula el tiempo que dura el espectaculo
                Thread.sleep(7000); 

                lock.lock();
                try {
                    System.out.println("La funcion del teatro termino, se encenderan las luces");
                    espectaculoComenzo = false;
                    
                    int cantidadALiberar = espectadoresAdentro;
                    espectadoresAdentro = 0; 

                    alertaEspectadores.signalAll(); 
                    
                    //El controlador libera todos los permisos del semaforo juntos para la proxima funcion
                    capacidadTeatro.release(cantidadALiberar); 

                } finally {
                    lock.unlock();
                }

                //Se simula el tiempo de limpieza de la sala antes de la proxima funcion
                System.out.println("El personal se encuentra limpiando la sala del teatro");
                Thread.sleep(3000);
                System.out.println("La sala del teatro esta limpia y lista para la proxima funcion");
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}