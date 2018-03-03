package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import soluciones.master_slave.TareaRunnable;

import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BacktrackingMain {
    public static ArrayList<Ficha> fichas;

    private static final int N = 5;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {
    	long startTime = System.nanoTime();
        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();
    
        

        Collections.shuffle(fichas);
        fichasLog.info("-----Fichas Generadas-----");
        for (Ficha f : fichas) {
            fichasLog.info("F" + f.getId() + ": " + f.imprimirse());
        }


        List<TareaRunnable> tareas = new ArrayList<TareaRunnable>();
        int nivel = 1;
        int nhilo= 1;
        for (Ficha f : fichas) {
        	ArrayList<Ficha> aux = new ArrayList<Ficha>();
            for (Ficha e : fichas) {
                if (!e.getId().equals(f.getId())) {
                    aux.add(e);
                }
            }
            TareaRunnable tarea = new TareaRunnable(new Tablero(N),aux,nivel,f,resultLog,String.valueOf(nhilo));
            nhilo++;
            tareas.add(tarea);
        }
        
        for(TareaRunnable t :tareas)
        	t.start();     
        
      //code
      long endTime = System.nanoTime();
      System.out.println("Took "+(endTime - startTime) + " ns"); 
    }
}