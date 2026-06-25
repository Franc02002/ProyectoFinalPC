import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Parque {
    private MontaniaRusa montania;
    private SalaRv salaRv;
    private AreaPremios areaPremios;
    private ComedorParque comedorParque;
    private Teatro teatro;
    private Semaphore molinete;
    

    public Parque(MontaniaRusa montania, SalaRv salaRv, AreaPremios areaPremios, ComedorParque comedorParque, Teatro teatro, int cantMolinetes){
        this.montania= montania;
        this.salaRv= salaRv;
        this.areaPremios = areaPremios;
        this.comedorParque = comedorParque;
        this.teatro = teatro;
        this.molinete= new Semaphore(cantMolinetes);
        };
    


    public void entrarPorMolineteParque(Visitante visitante){
        //Metodo que controla el ingreso al parque utilizando un semaforo con N permisos segun la cantidad de molinetes que se establecio
        try {
            molinete.acquire();
            Thread.sleep(1000);
            System.out.println("Una persona ha pasado por el molinete e ingreso al parque");
            molinete.release();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void irMontania(Visitante visitante){
        montania.ingresar(visitante);
        
    }

    public void irSalaRv(Visitante visitante){
        salaRv.ingresar(visitante);
    }


}
