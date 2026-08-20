public class Reloj implements Runnable {
    private Parque parque;

    public Reloj(Parque parque) {
        this.parque = parque;
    }

    @Override
    public void run() {
        System.out.println("[09:00 hs] ¡Parque ABIERTO! Se habilitan los molinetes y las actividades.");

        for (int hora = 9; hora <= 23; hora++) {
            try {
                // Cada hora simulada dura 1.5 segundos reales
                Thread.sleep(1500);
                System.out.println("\n[RELOJ] Son las " + hora + ":00 hs.");

                if (hora == 18) {
                    System.out.println("[18:00 hs] CIERRE DE MOLINETES: Ya no pueden ingresar nuevos visitantes.");
                    parque.cerrarIngreso();
                } else if (hora == 19) {
                    System.out.println("[19:00 hs] CIERRE DE ACTIVIDADES: Las atracciones dejan de operar.");
                    parque.cerrarActividades();
                } else if (hora == 23) {
                    System.out.println(
                            "[23:00 hs] CIERRE TOTAL: Desalojo completo del parque y fin de turno del personal.");
                    parque.desalojoTotal();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
