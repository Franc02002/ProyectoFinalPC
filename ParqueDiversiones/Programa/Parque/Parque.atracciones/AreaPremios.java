import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AreaPremios {
    private int fichas;
    private Premio peluches,aviones,barcos,figurasColeccion,espadas;
    private List<Premio> inventario;


    public AreaPremios(){
        fichas= 0;
        peluches= new Premio("peluche", 30);
        aviones=new Premio("avion", 60);
        barcos=new Premio("barco", 70);
        figurasColeccion=new Premio("figuraColeccion", 100);
        espadas=new Premio("espada", 50);  
        inventario= new ArrayList<>();
        inventario.add(peluches);
        inventario.add(aviones);
        inventario.add(barcos);
        inventario.add(figurasColeccion);
        inventario.add(espadas);
    }

    public synchronized void canjearPremio(Visitante visitante){
        System.out.println(visitante.getName() + " Ha ingresado al area de premios");
        
        int fichasVisitante = visitante.getCantidadFichas();

        if (fichasVisitante >= 30) {
            List<Premio> premiosAlcanzables = new ArrayList<>();
            
            for (Premio p : inventario) {
                
                if (fichasVisitante >= p.getValor()) { 
                    premiosAlcanzables.add(p);
                }
            }

            int indiceRandom = ThreadLocalRandom.current().nextInt(premiosAlcanzables.size());
            Premio premioElegido = premiosAlcanzables.get(indiceRandom);

            
            visitante.restarFichas(premioElegido.getValor());
            
            System.out.println(visitante.getName() + " cambió " + 
                               premioElegido.getValor() + " fichas por un/a: " + premioElegido.getNombre());

        } else {
            System.out.println(visitante.getName() + " Aun no tiene fichas suficientes para canjear un premio");
        }
    }
    
}
