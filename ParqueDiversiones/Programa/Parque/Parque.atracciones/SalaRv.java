import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

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
            // El visitante espera un kit listo
            String kit = mostradorKits.take();
            tieneKit = true; // La bandera se vuelve true si el visitante tomo un kit.

            System.out.println(visitante.getName() + " recibió su " + kit + " y empieza a jugar.");
            Thread.sleep(8000);

            System.out.println(visitante.getName() + " terminó VR, devuelve las partes y gana 20 fichas.");
            visitante.obtenerFichas(20);

        } catch (InterruptedException e) {
            System.out.println(visitante.getName() + " se fue de VR por cierre del parque.");
        } finally {
            // Solo devuelve las piezas si tomo un kit(bandera en true).
            if (tieneKit) {
                visores.release();
                manoplas.release(2);
                base.release();
            }
        }
    }
}