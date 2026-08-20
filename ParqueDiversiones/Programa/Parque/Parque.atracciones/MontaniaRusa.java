import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MontaniaRusa {
    // Barrera ciclica encargada de esperar a que se llene el carrito de la montaña
    // rusa para iniciar el viaje y simularlo.
    private CyclicBarrier vehiculoMontania = new CyclicBarrier(5, () -> {
        System.out.println("El carro de la montania rusa esta completo y empezara en breve!");
        try {
            Thread.sleep(2000);
            System.out.println("Comenzo el circuito de la montania rusa!");
            Thread.sleep(20000);
            System.out.println("El recorrido de la montania rusa termino, todos los pasajeros se estan bajando!");
        } catch (InterruptedException e) {
            System.out.println("El viaje en montaña rusa fue abortado por el cierre del parque.");
        }
    });

    private Semaphore filaMontania;

    public MontaniaRusa() {
        filaMontania = new Semaphore(7);
    }

    public void ingresar(Visitante visitante) {
        boolean entroFila = false;
        try {
            if (filaMontania.tryAcquire()) {
                entroFila = true;

                try {
                    vehiculoMontania.await(6, TimeUnit.SECONDS);

                    // Si llega aca, el viaje comenzo con exito.
                    filaMontania.release();
                    entroFila = false;
                    visitante.obtenerFichas(10);

                } catch (TimeoutException e) {
                    System.out.println(visitante.getName() + " se cansó de esperar a que se llene el carro.");
                    vehiculoMontania.reset(); // Se reestablece la barrera.
                }
            } else {
                System.out.println(visitante.getName() + " vio la fila llena y se fue.");
            }
        } catch (BrokenBarrierException e) {
            // Esto ocurre cuando OTRA persona en la barrera provoca el timeout
            System.out.println(visitante.getName() + " sale de la montaña rusa porque el carro no se llenó a tiempo.");
        } catch (InterruptedException e) {
            // Esto ocurre a las 23:00 hs cuando el Reloj manda el desalojo
            System.out.println(visitante.getName() + " abandonó la montaña rusa por el desalojo del parque.");
            Thread.currentThread().interrupt(); // Se restaura la bandera
        } finally {
            if (entroFila) {
                filaMontania.release();
            }
        }
    }
}