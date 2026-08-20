import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.*;

public class Comedor {
    // El comedor tendra 3 mesas de 4 sillas cada una
    // Se plantea la concurrencia con una barrera unica de 4 permisos, y un semaforo
    // que simula la cntiad de sillas totales
    // los visitantes podran entrar siempre y cuando haya una silla desocupada
    private CyclicBarrier agrupadorMesas = new CyclicBarrier(4);
    private Semaphore sillasTotales = new Semaphore(12);

    public void entrarComedor(Visitante visitante) {      
        System.out.println("🚶‍♂️ " + visitante.getName() + " llegó al comedor e intenta sentarse en una mesa.");
        boolean espera = true;
        while (espera) {
            if (sillasTotales.tryAcquire()) {
                try {
                    System.out.println(visitante.getName() + " se sentó en una mesa y está esperando a comer.");
                    agrupadorMesas.await();
                    System.out.println(visitante.getName() + " está comiendo.");
                    Thread.sleep(5000);
                    System.out.println(visitante.getName() + " terminó de comer y se retira del comedor.");

                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println("Se interrumpió la comida de " + visitante.getName() + " por un problema en la mesa.");
                    // Si fue una interrupción ordinaria, restauramos el estado del hilo
                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                } finally {
                    // IMPRESCINDIBLE: No importa si el hilo comió feliz, si se rompió la barrera
                    // o si lo interrumpieron a mitad del sleep; la silla SE LIBERA SÍ O SÍ.
                    sillasTotales.release();
                    espera = false;
                }
            } else {
                boolean seQueda = ThreadLocalRandom.current().nextBoolean();
                if (!seQueda) {
                    espera = false;
                    System.out.println(visitante.getName() + " se retiró del comedor porque no hay sillas disponibles.");
                }else{
                    System.out.println(visitante.getName() + " Vio el comedor lleno pero decide quedarse a esperar");
                    try {
                    // El hilo duerme 1 segundo antes de volver a intentar en la siguiente iteración.
                    // Esto evita que consuma el 100% de la CPU mientras espera.
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    espera = false;
                }
                }

            }
        }

    }

}
