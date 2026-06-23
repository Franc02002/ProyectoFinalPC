package ParqueDiversiones.Practica;

public class Auto implements Runnable{
    private String nombre;
    private Estacionamiento compartido;
    
    public Auto(String nombre,Estacionamiento estacionamiento){
        this.nombre=nombre;
        this.compartido= estacionamiento;
    }

     public String getName(){
        return this.nombre;
    }

    public void run(){    
        compartido.entrarEstacionamiento();
        compartido.salirEstacionamiento();
        
    }
}
