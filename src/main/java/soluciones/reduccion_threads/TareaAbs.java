package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public abstract class TareaAbs extends Thread {
    protected boolean finalizado; // cuando llega al final del metodo run ()
    protected Tablero tablero;
    protected ArrayList<Ficha> fichas;
    private Estado actual;
    protected Manager manager ;
    //public static long it;


    private Integer nivelComienzo;
    private static boolean dividir; // es la señal que se va a activar para que la primera tarea que la vea genere hijos

    private String nombre;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    private long id ;

    public TareaAbs(){

    }


    public TareaAbs(Estado estado, Manager m) {
        this.actual = estado;
        this.dividir = false;
        this.id = Manager.getNextContador();
        this.nombre = "TAREA-" + this.id;
        this.manager = m;
        this.finalizado = true;
    }

    public String getNombre() {
        return nombre;
    }

    public synchronized static void setDividir (boolean b){
        dividir = b;
    }

    public synchronized static boolean isDividir() {
        return dividir;
    }

    public boolean isFinalizado(){
        return finalizado;
    }

    public Estado getActual(){
        return actual;
    }
    
    public synchronized void despertar() {
 	   notify();
    }
    
//    public static synchronized void sumar() {
//  	   it++;
//     }

    /** Metodo de inicializacion toma el estado definido como actual y obtiene de el un tablero y una lista
     * de fichas para comenzar la ejecucion. Luego actual = null */
    public void inicializar (){
        //resultLog.info(" --------- INICIALIZA EL THREAD: " + Thread.currentThread().getName());
        tablero = actual.getTablero(); //TODO o una copia de tablero y fichas ????
        fichas = actual.getFichas();
        nivelComienzo = actual.getNivel();
        finalizado = false;
        actual = null;
    }

    public abstract void backRichi(ArrayList<Ficha> fichas, Integer nivel);

    @Override
    public void run(){
        while (Manager.TIENE_TAREAS) {
            if (actual != null) {
                inicializar();

                long startTime = System.nanoTime();
                backRichi(this.fichas, nivelComienzo);
                long endTime = System.nanoTime();

                BigDecimal duration = new BigDecimal((endTime - startTime));
                BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);

                resultLog.info("TIEMPO " + durationSecs.toString().replace('.',',') + " SEGUNDOS ");
                //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());
                finalizado = true;
            } 
            /*else {
            	manager.despertar();
            }*/
           // actual = Manager.getProximoEstado();
            if(actual==null){
               // manager.despertar();
                break;
            }
        }
        resultLog.info( "MUERE" + Thread.currentThread().getName());
    }
}
