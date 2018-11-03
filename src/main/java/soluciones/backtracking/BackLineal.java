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


public class BackLineal {
    public static ArrayList<Ficha> fichas;

    private static Boolean UBICAR_PRIMERA = Boolean.TRUE;

    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {

        int n = 6;
        int c = 6;
        int des = 1;

        if(args.length == 3){
            n = Integer.valueOf(args[0]);
            c = Integer.valueOf(args[1]);
            des = Integer.valueOf(args[2]);
        }


        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(n,c);
        fichas = generadorFichas.getFichasUnicas();

        Tablero tablero = new Tablero(n);

        if(UBICAR_PRIMERA) {
            tablero.insertarFinal(fichas.get(0));
            tablero.aumentarPosicion();
            fichas.get(0).setUsada(true);
        }

        if(des == 1){
            Collections.shuffle(fichas);
        }

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
        //resultLog.info("active-Threads " + Thread.activeCount());

    }
}