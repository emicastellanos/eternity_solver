package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.master_slave.TareaRunnable;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasAleatorio;
import utilidades.GeneradorFichasUnicas;
import utilidades.Utils;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;


public class LinealMain {
    public static ArrayList<Ficha> fichas;

    private static final int N = 6;
    private static Boolean UBICAR_PRIMERA = Boolean.FALSE;

    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,N);
        fichas = generadorFichas.getFichasUnicas();

        Tablero tablero = new Tablero(N);

        if(UBICAR_PRIMERA) {
            tablero.insertarFinal(fichas.get(0));
            tablero.aumentarPosicion();
            fichas.get(0).setUsada(true);
        }
        Collections.shuffle(fichas);



        /*if(UBICAR_PRIMERA){
            Utils.backUbicarPrimera(tablero,fichas);
        }*/

        int nhilo= 1;
        TareaRunnable tarea = new TareaRunnable(tablero,fichas,String.valueOf(nhilo));
        resultLog.info("----- INICIO  " + ZonedDateTime.now());

        long startTime = System.nanoTime();
        tarea.backRichi();
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
        resultLog.info(tarea.solucion);
        resultLog.info("----- TERMINO " + ZonedDateTime.now());

        resultLog.info("TIEMPO " + durationSecs.toString().replace('.',',') + " SEGUNDOS");
        BigDecimal threadUserTime = new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadUserTime());
        BigDecimal threadCpuTime = new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
        resultLog.info("threadUserTime " + (threadUserTime.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP)) ;
        resultLog.info("threadCPUTime " + (threadCpuTime.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP)) ;
        resultLog.info("active-Threads " + Thread.activeCount());

    }
}