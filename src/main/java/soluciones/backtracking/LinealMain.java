package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.master_slave.TareaRunnable;
import utilidades.GeneradorFichas;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LinealMain {
    public static ArrayList<Ficha> fichas;

    private static final int N = 7;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {
        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();
    
      
        //Collections.shuffle(fichas);
        fichasLog.info("-----Fichas Generadas-----");


        List<TareaRunnable> tareas = new ArrayList<TareaRunnable>();
        Integer sol = 0;
        int nhilo= 1;
        TareaRunnable tarea = new TareaRunnable(new Tablero(N),fichas,String.valueOf(nhilo));
        resultLog.info("INICIO " + ZonedDateTime.now());
        long startTime = System.nanoTime();
        tarea.backRichi(fichas);
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns"); 
        ZonedDateTime zdt ;
        resultLog.info(tarea.solucion);
        resultLog.info("\n----- TERMINO  "+ZonedDateTime.now());

        

    }
}