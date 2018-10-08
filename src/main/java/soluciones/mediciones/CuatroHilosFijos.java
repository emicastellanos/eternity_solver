package soluciones.mediciones;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.master_slave.TareaRunnable;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasUnicas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class CuatroHilosFijos {

    static final Logger MEDICIONES_LOGGER = Logger.getLogger("medicionesLogger");

    private boolean desordenar = true;

    private static int N = 8;

    private static int NIVEL_BACK_INICIAL = 6;

    private static int colores = 8;

    public static void ejecucionParalela(TareaRunnable tarea1,TareaRunnable tarea2,TareaRunnable tarea3,TareaRunnable tarea4){
        tarea1.start();
        tarea2.start();
        tarea3.start();
        tarea4.start();

    }

    public static void ejecucionLineal(TareaRunnable tarea1,TareaRunnable tarea2,TareaRunnable tarea3,TareaRunnable tarea4){

        long startTime = System.nanoTime();
        tarea1.run();
        tarea2.run();
        tarea3.run();
        tarea4.run();
        long endTime = System.nanoTime();
        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);

        MEDICIONES_LOGGER.info("\n----- TERMINO EL THREAD " + ZonedDateTime.now());
        MEDICIONES_LOGGER.info("\n TIEMPO " + durationSecs.toString().replace('.',',') + " SEGUNDOS");

        //TODO redondear a dos
        System.out.println("TERMINA EL MAIN CuatroHijosFijos");
        MEDICIONES_LOGGER.info("********************************");
        MEDICIONES_LOGGER.info("TAMANO TABLERO                  = " + N);
        MEDICIONES_LOGGER.info("CANTIDAD COLORES                = " + colores) ;

    }

    public static void main(String[] args){

        GeneradorFichas generadorFichas1 = new GeneradorFichasUnicas(N,colores);
        GeneradorFichas generadorFichas2 = new GeneradorFichasUnicas(N,colores);
        GeneradorFichas generadorFichas3 = new GeneradorFichasUnicas(N,colores);
        GeneradorFichas generadorFichas4 = new GeneradorFichasUnicas(N,colores);
        System.out.println("GENERACION FICHAS \n");
        ArrayList<Ficha> fichas1 = generadorFichas1.getFichasUnicas();
        ArrayList <Ficha> fichas2 = generadorFichas2.getFichasUnicas();
        ArrayList <Ficha> fichas3 = generadorFichas3.getFichasUnicas();
        ArrayList <Ficha> fichas4 = generadorFichas4.getFichasUnicas();
        System.out.println("FIN GENERACION FICHAS \n");

        int n1 = 0;
        int n2 = 7;
        int n3 = 56;
        int n4 = 63;



        System.out.println("TABLERO 1 (ficha "+ n1 +"): \n");
        int j = 0;
        for(Ficha f: fichas1){
            System.out.print(f.imprimirse() + "|");
            j++;
            if(j==N){
                j=0;
                System.out.println("");
            }
        }
        System.out.println("TABLERO 2 (ficha "+ n2 +"): \n");
        j = 0;
        for(Ficha f: fichas2){
            System.out.print(f.imprimirse() + "|");
            j++;
            if(j==N){
                j=0;
                System.out.println("");
            }
        }

        System.out.println("TABLERO 3 (ficha "+ n3 +"): \n");
        j = 0;
        for(Ficha f: fichas3){
            System.out.print(f.imprimirse() + "|");
            j++;
            if(j==N){
                j=0;
                System.out.println("");
            }
        }
        System.out.println("TABLERO 4 (ficha "+ n4 +"): \n");
        j = 0;
        for(Ficha f: fichas4){
            System.out.print(f.imprimirse() + "|");
            j++;
            if(j==N){
                j=0;
                System.out.println("");
            }
        }

        Tablero tablero1 = new Tablero(N);
        Ficha f1 = fichas1.get(n1);
        tablero1.insertarFinal(f1);
        tablero1.aumentarPosicion();
        fichas1.get(n1).setUsada(true);

        Tablero tablero2 = new Tablero(N);
        Ficha f2 = fichas2.get(n2);
        f2.rotar();
        f2.rotar();
        f2.rotar();
        tablero2.insertarFinal(f2);
        tablero2.aumentarPosicion();
        fichas2.get(n2).setUsada(true);

        Tablero tablero3 = new Tablero(N);
        Ficha f3 = fichas3.get(n3);
        f3.rotar();
        tablero3.insertarFinal(f3);
        fichas3.get(n3).setUsada(true);
        tablero3.aumentarPosicion();

        Tablero tablero4 = new Tablero(N);
        Ficha f4 = fichas4.get(n4);
        f4.rotar();
        f4.rotar();
        tablero4.insertarFinal(f4);
        fichas4.get(n4).setUsada(true);
        tablero4.aumentarPosicion();


        TareaRunnable tarea1 = new TareaRunnable(tablero1,fichas1,"t1");
        TareaRunnable tarea2 = new TareaRunnable(tablero2,fichas2,"t2");
        TareaRunnable tarea3 = new TareaRunnable(tablero3,fichas3,"t3");
        TareaRunnable tarea4 = new TareaRunnable(tablero4,fichas4,"t4");


        MEDICIONES_LOGGER.info("\n----- MAIN ------\n");
        //ejecucionParalela(tarea1,tarea2,tarea3,tarea4);
        ejecucionParalela(tarea1,tarea2,tarea3,tarea4);



    }

}