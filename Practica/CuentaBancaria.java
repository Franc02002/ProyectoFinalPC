package ParqueDiversiones.Practica;

public class CuentaBancaria {
    private int saldo=100;
 

    public synchronized void retirar(int cantidad){
        if(saldo - cantidad > 0){
            saldo=saldo - cantidad;
            System.out.println( Thread.currentThread().getName() + "Ha retirado " + cantidad + "dolares");
        }else{
            System.out.println("No hay mas dinero en cuenta");
        }
    }
}
