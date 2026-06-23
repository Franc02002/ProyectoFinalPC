
//*Utilizo Runnable en vesde Thread ya que java no me permite herencia multiple y no podria heredar nada mas que Thread
// En cambio con implementar la interfaz Runnable puedo heredar comportamiento de otras clases */

public class Visitante implements Runnable{

    private int fichasMR, fichasRV;
    private String nombre;

    public Visitante(String nombre){
        this.nombre=nombre;
        fichasMR=0;
        fichasRV=0;
    }


    @Override
    public void run(){

    }

    public void obtenerFichas(int cantidad,int tipo){
        if(tipo==1){
            fichasMR += cantidad;
        }else{
            fichasRV += cantidad;
        }
    }

}