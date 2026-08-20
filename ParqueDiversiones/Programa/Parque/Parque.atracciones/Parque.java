import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Parque {
    private MontaniaRusa montania;
    private SalaRv salaRv;
    private AreaPremios areaPremios;
    private Comedor comedorParque;
    private Teatro teatro;
    private Semaphore molinete;

    // Banderas de estado según horario
    private volatile boolean ingresoHabilitado = true;
    private volatile boolean actividadesHabilitadas = true;
    private volatile boolean parqueAbierto = true;

    // Hilos Activos
    private List<Thread> visitantesRegistrados = new ArrayList<>();
    private Thread hiloControladorTeatro;
    private Thread hiloEncargadoAP;
    private Thread hiloEncargadoRV;

    public Parque(MontaniaRusa montania, SalaRv salaRv, AreaPremios areaPremios, Comedor comedorParque, Teatro teatro,
            int cantMolinetes) {
        this.montania = montania;
        this.salaRv = salaRv;
        this.areaPremios = areaPremios;
        this.comedorParque = comedorParque;
        this.teatro = teatro;
        this.molinete = new Semaphore(cantMolinetes);
    }

    public synchronized void registrarVisitante(Thread hiloVisitante) {
        visitantesRegistrados.add(hiloVisitante);
    }

    // Registramos a los empleados
    public void setEmpleados(Thread teatro, Thread ap, Thread rv) {
        this.hiloControladorTeatro = teatro;
        this.hiloEncargadoAP = ap;
        this.hiloEncargadoRV = rv;
    }

    public boolean isIngresoHabilitado() {
        return ingresoHabilitado;
    }

    public boolean isActividadesHabilitadas() {
        return actividadesHabilitadas;
    }

    public boolean isParqueAbierto() {
        return parqueAbierto;
    }

    public void cerrarIngreso() {
        this.ingresoHabilitado = false;
    }

    public void cerrarActividades() {
        this.actividadesHabilitadas = false;
    }

    public synchronized void desalojoTotal() {
        //Coloco todas las bandeas en false para que no se pueda ingresar a ninguna actividad ni al parque.
        this.parqueAbierto = false;
        this.actividadesHabilitadas = false;
        this.ingresoHabilitado = false;

        // Desalojamos todos los visitantes dentro del parque.
        for (Thread visitante : visitantesRegistrados) {
            if (visitante.isAlive()) {
                visitante.interrupt();
            }
        }
        
        //Interrumpimos los hilos de los empleados del parque.
        interrumpirHilo(hiloControladorTeatro);
        interrumpirHilo(hiloEncargadoAP);
        interrumpirHilo(hiloEncargadoRV);
    }

    // Metodo auxiliar para la cancelacion de hilos.
    private void interrumpirHilo(Thread hilo) {
        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt();
        }
    }

    public void entrarPorMolineteParque(Visitante visitante) {
        if (!ingresoHabilitado) {
            System.out.println(visitante.getName() + " no puede entrar al parque.");
        } else {
            boolean pasoMolinete = false;
            try {
                molinete.acquire();
                pasoMolinete = true;
                Thread.sleep(800); // Simulo el que cada hilo tarda un poco en pasar por el molinete
                System.out.println(visitante.getName() + " ha pasado por el molinete e ingreso al parque");
            } catch (InterruptedException e) {
                System.out.println(visitante.getName() + " no pudo entrar al parque.");
            } finally {
                if (pasoMolinete)
                    molinete.release();
            }
        }

    }

    public void irMontania(Visitante visitante) {
        if (actividadesHabilitadas)
            montania.ingresar(visitante);
    }

    public void irSalaRv(Visitante visitante) {
        if (actividadesHabilitadas)
            salaRv.ingresar(visitante);
    }

    public void irTeatro(Visitante visitante) {
        if (actividadesHabilitadas)
            teatro.entrarTeatro(visitante);
    }

    public void irComedor(Visitante visitante) {
        if (actividadesHabilitadas)
            comedorParque.entrarComedor(visitante);
    }

    public void irAreaPremios(Visitante visitante) {
        if (parqueAbierto)
            areaPremios.canjearPremio(visitante);
    }
}