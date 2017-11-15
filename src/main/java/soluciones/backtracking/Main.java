package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;

import java.util.ArrayList;
import java.util.Collections;


public class Main {
    public static ArrayList<Ficha> fichas;

    private static final int N = 3;
    private static boolean encontrarTodasSoluciones = false;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void main(String[] args) {

        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();


        Collections.shuffle(fichas);
        fichasLog.info("-----Fichas Generadas-----");
        for (Ficha f : fichas) {
            fichasLog.info("F" + f.getId() + ": " + f.imprimirse());
        }

        Tablero tablero = new Tablero(N);

        SolverByBacktrack solucion = new SolverByBacktrack(tablero,encontrarTodasSoluciones,resultLog);

        resultLog.info("-----BACKTRACKING-----");

        long startTime = System.nanoTime();
        solucion.solucionRichi(fichas);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        resultLog.info("tiempo : " + duration);

    }
}