package ParqueDiversiones.Practica;

public class ClienteBanco implements Runnable {
    private CuentaBancaria cuenta;
    
    public ClienteBanco(CuentaBancaria cuenta){
        this.cuenta= cuenta;
    }

    public void run(){
        cuenta.retirar(30);
    }

    
}
