package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TareaRunnable extends Thread {

    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    public int nivelComienzo;
    public String nombreThread;
    public static int solucion;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    static final Logger MEDICIONES_LOGGER = Logger.getLogger("medicionesLogger");

    public TareaRunnable(Tablero tablero, ArrayList<Ficha> fichas, String nombre) {
        this.tablero = tablero;
        this.fichas = new ArrayList<>();
        solucion = 0;
        this.fichas = fichas;
        this.nombreThread = nombre;
    }

    public void backRichi(){
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        if (tablero.esSolucionFinal()) {
                            solucion++;
                            resultLog.info("SOLUCION");
                            resultLog.info(tablero.imprimirse());
                            //listaSoluciones.add(tablero.clone());
                        } else {
                            f.setUsada(true);
                            tablero.aumentarPosicion();
                            backRichi();
                            tablero.retrocederPosicion();
                            f.setUsada(false);
                        }
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
    }

    @Override
    public void run() {
        MEDICIONES_LOGGER.info("\n----- ARRANCA EL THREAD " + nombreThread + " " + ZonedDateTime.now());

        long startTime = System.nanoTime();
        backRichi();
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
        MEDICIONES_LOGGER.info("\n----- TERMINO EL THREAD " + nombreThread + " " + ZonedDateTime.now());
        MEDICIONES_LOGGER.info("\nTIEMPO " + nombreThread + " "+ durationSecs.toString().replace('.',',') + " SEGUNDOS");
        MEDICIONES_LOGGER.info("\nTIEMPO CurrentThreadCpuTime " + nombreThread + " "+ (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP));
        MEDICIONES_LOGGER.info("\nTIEMPO getThreadCpuTime " + nombreThread + " "+ ManagementFactory.getThreadMXBean().getThreadCpuTime(this.getId()));

    }
}