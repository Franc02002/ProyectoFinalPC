import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MontaniaRusa {
    //Se utiliza una cyclicBarrir para asegurar que la montania rusa inicie al haber 5 personas dentro, al iniciar
    //se simula la accion y luego los 5 hilos salen de la misma.
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

    //El ingreso a la montania esta dividido por el ingreso a la fila para luego ingresar al juego, todo hilo debe tomar esos 2 permisos para completar la accion
    public void ingresar(Visitante visitante){
        boolean entroFila=false;
        try {
            if(filaMontania.tryAcquire()){
                entroFila=true;
                asientosCarrito.acquire();
                filaMontania.release();
                vehiculoMontania.await();
                asientosCarrito.release();
                visitante.obtenerFichas(10);
            }
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(entroFila){
                filaMontania.release();
            }
            
        }

    }



}
