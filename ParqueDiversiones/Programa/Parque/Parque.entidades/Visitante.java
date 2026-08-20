
//*Utilizo Runnable en vesde Thread ya que java no me permite herencia multiple y no podria heredar nada mas que Thread
// En cambio con implementar la interfaz Runnable puedo heredar comportamiento de otras clases */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Visitante implements Runnable {
    private int fichas;
    private String nombre;
    private Parque parque;
    private List<Premio> inventarioPremios;

    public Visitante(String nombre, Parque parque) {
        this.nombre = nombre;
        this.parque = parque;
        this.fichas = 0;
        this.inventarioPremios = new ArrayList<>();
    }

    @Override
    public void run() {
        if (!parque.isIngresoHabilitado()) {
            System.out.println(this.nombre + " llegó al parque después de las 18hs y encontró los molinetes cerrados.");
            return;
        }

        parque.entrarPorMolineteParque(this);

        for (int i = 0; i < 4; i++) {
            if (Thread.currentThread().isInterrupted()) {
                // Si fue interrumpido, es porque son las 23:00
                System.out.println(this.nombre + " fue desalojado y se retira sin hacer más filas.");
                break;
            } else if (!parque.isActividadesHabilitadas()) {
                // Si no está interrumpido, pero las actividades cerraron, son las 19:00
                System.out
                        .println(this.nombre + " deja de hacer filas porque las atracciones cerraron a las 19:00 hs.");
                break;
            }

            int atraccion = ThreadLocalRandom.current().nextInt(4);
            switch (atraccion) {
                case 0:
                    parque.irMontania(this);
                    break;
                case 1:
                    parque.irSalaRv(this);
                    break;
                case 2:
                    parque.irComedor(this);
                    break;
                case 3:
                    parque.irTeatro(this);
                    break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(this.nombre + " abandonó el parque por desalojo general.");
                return; // Corta la ejecución completa.
            }
        }

        // Al terminar el recorrido o al cerrar las actividades a las 19hs, va a los
        // premios.
        if (parque.isParqueAbierto() && !Thread.currentThread().isInterrupted()) {
            parque.irAreaPremios(this);
            System.out.println(this.nombre + " terminó su visita y sale del parque.");
        }
    }

    public synchronized void obtenerFichas(int cantidad) {
        fichas += cantidad;
    }

    public synchronized void restarFichas(int cantidad) {
        fichas -= cantidad;
    }

    public String getName() {
        return nombre;
    }

    public synchronized int getCantidadFichas() {
        return fichas;
    }

    public synchronized void agregarPremio(Premio p) {
        inventarioPremios.add(p);
    }
}