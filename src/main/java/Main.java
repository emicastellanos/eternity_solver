import org.apache.log4j.Logger;

import java.util.ArrayList;


public class Main {
    public static ArrayList<Ficha> fichas;
    public static Boolean[] libres;
    private static final int N = 3;
    static final Logger fichasLog = Logger.getLogger("debugLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void inicializarLibres(Boolean[] libres) {
        int M = N * N;
        for (int i = 0; i < M; i++) {
            libres[i] = true;
        }
    }

    public static void main(String[] args) {

        libres = new Boolean[N * N];
        inicializarLibres(libres);
        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();


        /*Collections.shuffle(fichas);*/
        fichasLog.info("-----Fichas-----");
        for (Ficha f : fichas) {
            fichasLog.info("F" + f.getId() + ": " + f.imprimirse());
        }

        Tablero tablero = new Tablero(N);

        SolverByBacktrack solucion = new SolverByBacktrack(N, fichas, tablero, libres);

        resultLog.info("-----BACKTRACKING-----");

        long startTime = System.nanoTime();
        solucion.solucionRichi(fichas);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        resultLog.info("tiempo : " + duration);

    }
}