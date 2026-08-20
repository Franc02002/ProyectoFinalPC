import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Comedor {
    // El comedor tendra 3 mesas de 4 sillas cada una.
    // Se plantea la concurrencia con una barrera unica de 4 permisos, y un semaforo que simula la cntiad de sillas totales.
    // los visitantes podran entrar siempre y cuando haya una silla desocupada.
    private CyclicBarrier agrupadorMesas = new CyclicBarrier(4);
    private Semaphore sillasTotales = new Semaphore(12);

    public void entrarComedor(Visitante visitante) {      
        System.out.println(visitante.getName() + " llegó al comedor e intenta sentarse en una mesa.");
        boolean espera = true;
        
        while (espera) {
            if (sillasTotales.tryAcquire()) {
                try {
                    System.out.println(visitante.getName() + " se sentó en una mesa y está esperando a comer.");
                    
                    try {
                        // El visitante espera un máximo de 3 segundos reales a que se llene la mesa
                        agrupadorMesas.await(3, TimeUnit.SECONDS);
                        
                        // Si llega hasta aquí, la mesa se completó y pueden comer
                        System.out.println(visitante.getName() + " está comiendo.");
                        Thread.sleep(5000);
                        System.out.println(visitante.getName() + " terminó de comer y se retira del comedor.");
                        
                    } catch (TimeoutException e) {
                        System.out.println(visitante.getName() + " se cansó de esperar a que se llene la mesa y se marcha con hambre.");
                        agrupadorMesas.reset(); // Restablece la barrera si ocurre el timeout
                    }

                } catch (BrokenBarrierException e) {
                    System.out.println(visitante.getName() + " se levanta de la mesa porque alguien del grupo se marchó.");
                } catch (InterruptedException e) {
                    System.out.println("Se interrumpió la comida de " + visitante.getName() + " por el desalojo del parque.");
                    Thread.currentThread().interrupt(); // Restaura el estado del hilo
                } finally {
                    // Libero la silla independientemente de si comió, se cansó o lo echaron
                    sillasTotales.release();
                    espera = false;
                }
                
            } else {
                boolean seQueda = ThreadLocalRandom.current().nextBoolean();
                if (!seQueda) {
                    espera = false;
                    System.out.println(visitante.getName() + " se retiró del comedor porque no hay sillas disponibles.");
                } else {
                    System.out.println(visitante.getName() + " vio el comedor lleno pero decide quedarse a esperar.");
                    try {
                        Thread.sleep(2000); 
                    } catch (InterruptedException e) {
                        System.out.println(visitante.getName() + " abandona la espera del comedor por el desalojo del parque.");
                        Thread.currentThread().interrupt();
                        espera = false;
                    }
                }
            }
        }
    }

}
