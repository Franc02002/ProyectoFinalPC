import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EncargadoAP implements Runnable {
    private AreaPremios area;

    public EncargadoAP(AreaPremios area) {
        this.area = area;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                //Espera que un visitante le de las fichas
                int fichasRecibidas = area.getIntercambioFichas().exchange(null);
                
                // Busca qué premio le alcanza
                List<Premio> alcanzables = new ArrayList<>();
                for (Premio p : area.getInventario()) {
                    if (fichasRecibidas >= p.getValor()) alcanzables.add(p);
                }
                
                Premio premioElegido = null;
                if (!alcanzables.isEmpty()) {
                    int idx = ThreadLocalRandom.current().nextInt(alcanzables.size());
                    premioElegido = alcanzables.get(idx);
                }
                
                // Le entrega el premio al visitante
                area.getIntercambioPremio().exchange(premioElegido);
            }
        } catch (InterruptedException e) {
            System.out.println("El encargado de Premios cierra su puesto y se retira.");
        }
    }
}