package ParqueDiversiones.Practica;

import java.util.concurrent.Semaphore;

public class Estacionamiento {
    private Semaphore recurso = new Semaphore(3);

    public void entrarEstacionamiento(){
        try {
            recurso.acquire();
            System.out.println(Thread.currentThread().getName() + "Entro al estacionamiento");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("No pudo tomar");
        }

    }

    public void salirEstacionamiento(){
        try {
            System.out.println(Thread.currentThread().getName() + "Salio del estacionamiento");
            recurso.release();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
