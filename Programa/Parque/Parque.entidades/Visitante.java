
//*Utilizo Runnable en vesde Thread ya que java no me permite herencia multiple y no podria heredar nada mas que Thread
// En cambio con implementar la interfaz Runnable puedo heredar comportamiento de otras clases */

public class Visitante implements Runnable{

    private int fichas;
    private String nombre;

    public Visitante(String nombre){
        this.nombre=nombre;
        fichas= 0;
    }


    @Override
    public void run(){

    }

    public void obtenerFichas(int cantidad){
        fichas= cantidad;
    }

    public String getName(){
        return nombre;
    }

    public int getCantidadFichas(){
        return fichas;
    }

    public void restarFichas(int cantidad){
        
    }

}