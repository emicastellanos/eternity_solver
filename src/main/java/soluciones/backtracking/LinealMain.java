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

    private static final int N = 10;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {
        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();
    
        

        Collections.shuffle(fichas);
        fichasLog.info("-----Fichas Generadas-----");


        List<TareaRunnable> tareas = new ArrayList<TareaRunnable>();
        int nivel = 1;
        int nhilo= 1;
        TareaRunnable tarea = new TareaRunnable(new Tablero(N),fichas,String.valueOf(nhilo));
        resultLog.info("INICIO " + ZonedDateTime.now());
        tarea.backRichi(fichas,nivel);
        ZonedDateTime zdt ;
        resultLog.info(tarea.solucion);
        resultLog.info("\n----- TERMINO  "+ZonedDateTime.now());

        

    }
}