import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Teatro {
    private Semaphore capacidadTeatro = new Semaphore(20);
    // NUEVO: Barrera para agrupar de a 5 en la entrada
    private CyclicBarrier puertaGrupo = new CyclicBarrier(5, () -> {
        System.out.println("Un grupo de 5 personas ingresa junto a la sala del teatro.");
    });
    
    private ReentrantLock lock = new ReentrantLock();
    private Condition alertaControlador = lock.newCondition();
    private Condition alertaEspectadores = lock.newCondition();
    private int espectadoresAdentro = 0;
    private boolean espectaculoComenzo = false;

    public void entrarTeatro(Visitante visitante) {
    boolean entro = false;
    try {
        capacidadTeatro.acquire(); 
        
        try {
            // Espera a que se junten 5 personas
            puertaGrupo.await(5, TimeUnit.SECONDS);
            entro = true; // Si pasa la barrera con éxito, cambiamos la bandera
        } catch (TimeoutException e) {
            System.out.println(visitante.getName() + " se cansó de esperar a que se complete el grupo para entrar al teatro y se marcha.");
            puertaGrupo.reset(); // Se reestablece la barrera.
            capacidadTeatro.release(); // Libera el permiso ya que no entró por su propia cuenta.
        }
        
        // Solo intenta ver el show si superó la barrera
        if (entro) {
            lock.lock(); // ¡El lock debe ir ANTES de intentar modificar variables compartidas o usar variables de condición!
            try {
                espectadoresAdentro++;
                if (espectadoresAdentro == 1 || espectadoresAdentro == 20) {
                    alertaControlador.signal();
                }
                while (!espectaculoComenzo) {
                    alertaEspectadores.await(); 
                }
                while (espectaculoComenzo) {
                    alertaEspectadores.await();
                }
                System.out.println(visitante.getName() + " sale de la sala al finalizar el show.");
            } finally {
                lock.unlock(); // Garantiza que se libere el cerrojo pase lo que pase
            }
        }
        
    } catch (InterruptedException | BrokenBarrierException e) {
        System.out.println(visitante.getName() + " se retira de la fila del teatro.");
        capacidadTeatro.release(); // ¡VITAL! Libera el permiso si el hilo es interrumpido o si la barrera se rompió por culpa del timeout de otro visitante.
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}

    public void comenzarShow() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                lock.lock();
                try {
                    while (espectadoresAdentro == 0) {
                        alertaControlador.await();
                    }
                    System.out.println("El teatro tiene al menos un espectador, inicia la cuenta regresiva de 15 segundos");
                    alertaControlador.await(15, TimeUnit.SECONDS);

                    System.out.println("Comienza la funcion del teatro con " + espectadoresAdentro + " espectadores");
                    espectaculoComenzo = true;
                    alertaEspectadores.signalAll(); 
                } finally {
                    lock.unlock();
                }

                Thread.sleep(7000); 

                lock.lock();
                try {
                    System.out.println("La funcion del teatro termino, se encenderan las luces");
                    espectaculoComenzo = false;
                    int cantidadALiberar = espectadoresAdentro;
                    espectadoresAdentro = 0; 
                    alertaEspectadores.signalAll(); 
                    capacidadTeatro.release(cantidadALiberar); 
                } finally {
                    lock.unlock();
                }
                System.out.println("El personal se encuentra limpiando la sala del teatro...");
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            System.out.println("El encargado del teatro finaliza su jornada.");
        }
    }
}