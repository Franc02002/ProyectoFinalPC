import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SalaRv {
    // Recursos de la salaRV
    private Semaphore visores = new Semaphore(10);
    private Semaphore manoplas = new Semaphore(7);
    private Semaphore base = new Semaphore(10);

    // Utilizo una cola donde el encargado deja los kits completos
    private BlockingQueue<String> mostradorKits = new ArrayBlockingQueue<>(5);

    public Semaphore getVisores() {
        return visores;
    }

    public Semaphore getManoplas() {
        return manoplas;
    }

    public Semaphore getBase() {
        return base;
    }

    public BlockingQueue<String> getMostradorKits() {
        return mostradorKits;
    }

    public void ingresar(Visitante visitante) {
        System.out.println(visitante.getName() + " hace fila para VR...");
        boolean tieneKit = false;

        try {
            // Espera un máximo de 3 segundos reales por un kit
            String kit = mostradorKits.poll(3, TimeUnit.SECONDS);

            if (kit == null) {
                // Si devuelve null, se acabó el tiempo y no consiguió kit
                System.out.println(visitante.getName() + " se cansó de esperar un kit de RV y se marcha.");
            } else {
                // Si el kit no es nulo, marca la bandera como verdadera y empieza a jugar
                tieneKit = true;
                System.out.println(visitante.getName() + " recibió su " + kit + " y empieza a jugar.");

                Thread.sleep(8000); // Recuerda balancear este tiempo con el de tu Reloj

                System.out.println(visitante.getName() + " terminó VR, devuelve las partes y gana 20 fichas.");
                visitante.obtenerFichas(20);
            }

        } catch (InterruptedException e) {
            System.out.println(visitante.getName() + " se fue de VR por cierre del parque.");
            Thread.currentThread().interrupt(); // Restablecemos la bandera
        } finally {
            // Como 'tieneKit' solo se vuelve true dentro del 'else',
            // los que abandonaron no devolverán permisos que nunca tuvieron.
            if (tieneKit) {
                visores.release();
                manoplas.release(2);
                base.release();
            }
        }
    }

}