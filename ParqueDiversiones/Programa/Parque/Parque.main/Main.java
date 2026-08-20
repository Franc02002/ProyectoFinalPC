public class Main {
    public static void main(String[] args) {
        System.out.println("¡BIENVENIDOS AL PARQUE DE DIVERSIONES! \n");

        MontaniaRusa montania = new MontaniaRusa();
        SalaRv salaRv = new SalaRv();
        AreaPremios areaPremios = new AreaPremios();
        Comedor comedor = new Comedor();
        Teatro teatro = new Teatro();

        Parque parque = new Parque(montania, salaRv, areaPremios, comedor, teatro, 3);

        // 1. Instanciamos a todo el PERSONAL (Encargados)
        Thread controladorTeatro = new Thread(() -> teatro.comenzarShow());
        Thread hiloEncargadoAP = new Thread(new EncargadoAP(areaPremios));
        Thread hiloEncargadoRV = new Thread(new EncargadoRV(salaRv));
        
        // Registramos al personal en el parque
        parque.setEmpleados(controladorTeatro, hiloEncargadoAP, hiloEncargadoRV);
        
        // Los empleados empiezan su turno
        controladorTeatro.start();
        hiloEncargadoAP.start();
        hiloEncargadoRV.start();

        // 2. Iniciamos el RELOJ del parque
        Reloj reloj = new Reloj(parque);
        Thread hiloReloj = new Thread(reloj);
        hiloReloj.start();

        // 3. Generamos a los VISITANTES
        int cantidadVisitantes = 40;
        for (int i = 1; i <= cantidadVisitantes; i++) {
            Visitante visitante = new Visitante("Visitante-" + i, parque);
            Thread hiloVisitante = new Thread(visitante);
            
            // Se registra el hilo antes de dispararlo
            parque.registrarVisitante(hiloVisitante);
            hiloVisitante.start();

            // Intervalo corto de tiempo entre llegadas de visitantes
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}