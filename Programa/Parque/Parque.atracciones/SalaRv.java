import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SalaRv {
    private Semaphore visores,manoplas,base;
    private ReentrantLock encargado;


    public SalaRv(){
        visores= new Semaphore(10);
        manoplas= new Semaphore(7);
        base= new Semaphore(10);
        encargado = new ReentrantLock();
    }

    public void ingresar(Visitante visitante){
        boolean tomoVisor=false,
                tomoManopla=false,
                tomoBase=false;
        System.out.println(visitante.getName() + "Ha ingresado al sector de RV");
        try {
            visores.acquire();
            tomoVisor=true;
            System.out.println(visitante.getName() + "Se equipo con las gafas RV");
            manoplas.acquire(2);
            tomoManopla=true;
            System.out.println(visitante.getName() + "Se equipo con ambas manoplas");
            base.acquire();
            tomoBase=true;
            System.out.println(visitante.getName() + "Se equipo con la base");
            System.out.println(visitante.getName() + "Tiene el equipo completo y acaba de ingresar a la actividad RV");
            Thread.sleep(10000);
            System.out.println(visitante.getName() + "Devolvio todo el equipo RV,obtuvo fichas RV por participar!");
            visores.release();
            manoplas.release(2);
            base.release();
            visitante.obtenerFichas(20, 2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            if(tomoVisor){
                visores.release();
            }
            if(tomoManopla){
                manoplas.release(2);
            }
            if(tomoBase){
                base.release();
            }
            
          }
        

    }
}
