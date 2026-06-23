import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MontaniaRusa {
    private CyclicBarrier vehiculoMontania = new CyclicBarrier(5,() -> {
        System.out.println("El carro de la montania rusa esta completo y empezara en breve!"); 
        try {
            Thread.sleep(2000);
            System.out.println("Comenzo el circuito de la montania rusa!");
            Thread.sleep(20000);
            System.out.println("El recorrido de la montania rusa termino, todos los pasajeros se estan bajando!");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    });
    private Semaphore filaMontania;
    private Semaphore asientosCarrito;

    public MontaniaRusa(){
        filaMontania = new Semaphore(7);
        asientosCarrito= new Semaphore(5);

    }

    public void ingresar(Visitante visitante){
        try {
            if(filaMontania.tryAcquire(1)){
                asientosCarrito.acquire();
                filaMontania.release();
                vehiculoMontania.await();
                asientosCarrito.release();
                visitante.obtenerFichas(10,1);
            }
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            filaMontania.release();
        }

    }



}
