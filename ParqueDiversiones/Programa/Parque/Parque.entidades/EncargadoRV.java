public class EncargadoRV implements Runnable {
    private SalaRv sala;

    public EncargadoRV(SalaRv sala) {
        this.sala = sala;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // El encargado busca las piezas sueltas
                sala.getVisores().acquire();
                sala.getManoplas().acquire(2);
                sala.getBase().acquire();
                
                // Ensambla el kit y lo pone en el mostrador (BlockingQueue).
                // Si el mostrador está lleno (5), el encargado se bloquea hasta que alguien lo tome.
                sala.getMostradorKits().put("Kit RV Completo (1 Visor, 2 Manoplas, 1 Base)");
                System.out.println("Encargado RV armó un kit y lo dejó listo en el mostrador.");
            }
        } catch (InterruptedException e) {
            System.out.println("El encargado de RV guarda las piezas y finaliza su turno.");
        }
    }
}