import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class Main {

    private static final int N = 6;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");



    public static void main (String[] args){

        GeneradorFichas generadorFichas = new GeneradorFichas(N);

        ArrayList<Ficha> fichas = generadorFichas.getFichasUnicas();

        Maestro maestro = new Maestro(new Tablero(N), fichas);
        maestro.generarHilos(10);


    }

    /*public static void main(String[] args) {

        Boolean [] libres1 = new Boolean[N * N];
        inicializarLibres(libres1);
        Boolean [] libres2 = new Boolean[N * N];
        inicializarLibres(libres2);
        GeneradorFichas generadorFichas = new GeneradorFichas(N);

        ArrayList<Ficha> fichas = generadorFichas.getFichasUnicas();
        ArrayList<Ficha> fichas2 = generadorFichas.getFichasUnicas();


        Collections.shuffle(fichas);
        Collections.shuffle(fichas2);
        *//*fichasLog.info("-----Fichas-----");
        for (Ficha f : fichas) {
            fichasLog.info("F" + f.getId() + ": " + f.imprimirse());
        }*//*

        Tablero tablero = new Tablero(N);

        SolverByBacktrack solucion = new SolverByBacktrack(tablero);

        resultLog.info("-----BACKTRACKING-----");

        long startTime = System.nanoTime();
        Tarea t1 = new Tarea(new Tablero(N),libres1,1, fichas);


        Tarea t2 = new Tarea(new Tablero(N),libres2,1,fichas2);


        ExecutorService e1 = Executors.newSingleThreadExecutor();
        Future f1 = e1.submit(t1);
        ExecutorService e2 = Executors.newSingleThreadExecutor();
        Future f2 = e2.submit(t2);

        while(true){
            if(f1.isDone()){
                resultLog.info("TERMINO EL EXECTUOR E1");
                e2.shutdownNow();
                break;
            }
            if(f2.isDone()){
                resultLog.info("TERMINO EL EXECTUOR E2");
                e1.shutdownNow();
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(!e1.isTerminated()){
            e1.shutdownNow();
        }
        if(!e2.isTerminated()){
            e2.shutdownNow();
        }


        *//*solucion.solucionRichi(fichas);*//*

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        resultLog.info("tiempo : " + duration);

    }*/

    /*static int i = 0;

    public static void main (String [] args){


        Runnable taskNUeva  = () -> {
            String threadName = Thread.currentThread().getName();

            while(i<100){
                if (i==50){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {

                    }
                }
                i++;
                System.out.println("N " + threadName +" " + i);
            }
            System.out.println("Hello " + threadName);
        };



        Thread thread = new Thread(taskNUeva);


        System.out.println("Done!");
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){

        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(taskNUeva);

        thread.start();


        System.out.println("main");



    }*/
}