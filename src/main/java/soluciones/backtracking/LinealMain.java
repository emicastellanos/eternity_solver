package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.master_slave.TareaRunnable;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasAleatorio;
import utilidades.GeneradorFichasUnicas;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;


public class LinealMain {
    public static ArrayList<Ficha> fichas;

    private static final int N = 8;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    private static boolean primeraPuesta = true;


    public static void main(String[] args) {
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,N);
        fichas = generadorFichas.getFichasUnicas();
        Tablero tablero = new Tablero(N);
        if(primeraPuesta) {
        	tablero.insertarFinal(fichas.get(0));
        	tablero.aumentarPosicion();
        	fichas.get(0).setUsada(true);
        }

        Collections.shuffle(fichas);

        int nhilo= 1;
        TareaRunnable tarea = new TareaRunnable(tablero,fichas,String.valueOf(nhilo));
        resultLog.info("----- INICIO  " + ZonedDateTime.now());

        long startTime = System.nanoTime();
        tarea.backRichi();
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = duration.divide(new BigDecimal(1000000000));
        ZonedDateTime zdt ;
        resultLog.info(tarea.solucion);
        resultLog.info("----- TERMINO " + ZonedDateTime.now());

        resultLog.info("TIEMPO " + durationSecs.toString().replace('.',',') + " SEGUNDOS");

    }
}