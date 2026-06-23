package ParqueDiversiones.Practica;
public class TareaImpresion implements Runnable {
    private String nombre;

    public TareaImpresion(String nombre){
        this.nombre= nombre;
    }

    public String getName(){
        return this.nombre;
    }

    public void run() {
        int i=0;
        while(i<5){
            System.out.println("Hilo " +getName() + "imprimiendo");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           i++; 
        }
        
    }

}    