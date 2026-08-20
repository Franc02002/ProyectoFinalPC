import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MontaniaRusa {
    //Barrera ciclica encargada de esperar a que se llene el carrito de la montaña rusa para iniciar el viaje y simularlo.
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
    private Semaphore asientosCarrito;

    public MontaniaRusa() {
        filaMontania = new Semaphore(7);
        asientosCarrito = new Semaphore(5);
    }

    public void ingresar(Visitante visitante) {
        boolean entroFila = false;
        boolean tomoAsiento = false;

        try {
            if (filaMontania.tryAcquire()) {
                entroFila = true;           
                vehiculoMontania.await();
                tomoAsiento = true;
                visitante.obtenerFichas(10);
            } else {
                System.out.println(visitante.getName() + " vio la fila de la montaña rusa llena y se fue a otro lado.");
            }

        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println(visitante.getName() + " abandonó la montaña rusa por el desalojo del parque.");
        } finally {
            if (tomoAsiento) {
                asientosCarrito.release();
            }
            if (entroFila) {
                filaMontania.release();
            }
        }
    }
}