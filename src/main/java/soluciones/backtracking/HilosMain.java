package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import soluciones.master_slave.TareaRunnable;

import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;


import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HilosMain {
    public static ArrayList<Ficha> fichas;

    private static final int N = 5;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {
        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();
    
        

        //Collections.shuffle(fichas);
        fichasLog.info("-----Fichas Generadas-----");

        List<TareaRunnable> tareas = new ArrayList<TareaRunnable>();
        int nhilo= 1;
        Ficha auxF;
        for (Ficha f : fichas) {
            Tablero auxTablero = new Tablero(N);
            auxF = new Ficha(f.getIzq(),f.getArr(),f.getDer(),f.getAbj(),f.getId());
            auxTablero.insertarFinal(auxF);
            if(auxTablero.esSolucion()){
                ArrayList<Ficha> aux = new ArrayList<Ficha>();
                for (Ficha e : fichas) {
                    if (e.getId() != f.getId()) {
                        aux.add(e);
                    }
                }
                TareaRunnable tarea = new TareaRunnable(auxTablero,aux,String.valueOf(nhilo));
                nhilo++;
                tareas.add(tarea);
            }
        }
        ZonedDateTime zdt ;
        resultLog.info("INICIO " + ZonedDateTime.now());
        for(TareaRunnable t :tareas)
        	t.start();     
        

    }
}