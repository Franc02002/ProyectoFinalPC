
//*Utilizo Runnable en vesde Thread ya que java no me permite herencia multiple y no podria heredar nada mas que Thread
// En cambio con implementar la interfaz Runnable puedo heredar comportamiento de otras clases */

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Visitante implements Runnable{

    private int fichas;
    private String nombre;
    private ArrayList<Premio> inventarioPremios;
    private Parque parque;

    public Visitante(String nombre,Parque parque){
        this.nombre=nombre;
        fichas= 0;
        this.parque= parque;
    }


    @Override
    public void run(){
        parque.entrarPorMolineteParque(this);
        for (int i = 0; i < 3; i++) {
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
                case 4:
                    parque.irSalaRv(this);
                    break;
            }

            // Pausa entre atracciones para simular caminata por el parque
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public synchronized void obtenerFichas(int cantidad){
        fichas+= cantidad;
    }

    public String getName(){
        return nombre;
    }

    public synchronized int getCantidadFichas(){
        return fichas;
    }

    public synchronized void restarFichas(int cantidad){
        fichas-= cantidad;
    }

    public synchronized void agregarPremio(Premio p){
        inventarioPremios.add(p);
    }



}