import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.ReentrantLock;

public class AreaPremios {
    //Una lista de tipo Premio que representa el inventario de premios disponibles.
    private List<Premio> inventario;
    
    // Exchanger para fichas y premios, de manera que el visitante interactue con el encargadoAP y Lock para hacer fila
    private Exchanger<Integer> intercambioFichas = new Exchanger<>();
    private Exchanger<Premio> intercambioPremio = new Exchanger<>();
    private ReentrantLock mostrador = new ReentrantLock();

    public AreaPremios() {
        inventario = new ArrayList<>();
        inventario.add(new Premio("peluche", 30));
        inventario.add(new Premio("avion", 60));
        inventario.add(new Premio("barco", 70));
        inventario.add(new Premio("figuraColeccion", 100));
        inventario.add(new Premio("espada", 50));  
    }

    public List<Premio> getInventario() {
        return inventario; }

    public Exchanger<Integer> getIntercambioFichas() { 
        return intercambioFichas; }

    public Exchanger<Premio> getIntercambioPremio() { 
        return intercambioPremio; }

    public void canjearPremio(Visitante visitante) {
        int misFichas = visitante.getCantidadFichas();
        
        //Si no tengo la cantidad minima de fichas para canjear un premio, no hago nada. 
        if (misFichas >= 15) {
            mostrador.lock(); // El visitante hace fila para hablar con el encargado y solo uno puede estar en el mostrador a la vez.
            try {    
                System.out.println(visitante.getName() + " se acerca al mostrador de premios con " + misFichas + " fichas.");
                
                // Le da las fichas al encargado (el encargado se encarga de restarlas).
                intercambioFichas.exchange(misFichas);
                
                // Recibe el premio del encargado.
                Premio premioGanado = intercambioPremio.exchange(null);
                
                if (premioGanado != null) {
                    visitante.restarFichas(premioGanado.getValor());
                    visitante.agregarPremio(premioGanado);
                    System.out.println(visitante.getName() + " recibió su premio: " + premioGanado.getNombre());
                }
            } catch (InterruptedException e) {
                System.out.println(visitante.getName() + " abandonó la fila de premios.");
            } finally {
                mostrador.unlock();
            }
        } else {
            System.out.println(visitante.getName() + " no tiene fichas suficientes (Min: 15). Tiene: " + misFichas);
        }
    }
}