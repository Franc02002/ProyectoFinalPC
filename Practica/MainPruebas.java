package ParqueDiversiones.Practica;



public class MainPruebas {
    
    public static void main(String[] args) {
        CuentaBancaria cuenta= new CuentaBancaria();
        ClienteBanco cliente1 = new ClienteBanco(cuenta);
        ClienteBanco cliente2 = new ClienteBanco(cuenta);
        Thread cliente1Hilo = new Thread(cliente1);
        Thread cliente2Hilo = new Thread(cliente2);
        cliente1Hilo.start();
        cliente2Hilo.start(); 
    }

}