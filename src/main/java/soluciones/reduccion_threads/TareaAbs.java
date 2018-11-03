package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public abstract class TareaAbs extends Thread {
    protected boolean finalizado; // cuando llega al final del metodo run ()
    protected Tablero tablero;
    protected ArrayList<Ficha> fichas;
    protected Estado actual;
    private Estado punteroAEstadoGenerador;
    protected ManagerAbs managerAbs;

    public Integer nivelComienzo;
    private static boolean dividir;

    protected String nombreTarea;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    static final Logger MEDICIONES_LOGGER = Logger.getLogger("medicionesLogger");
    private long id ;
    private boolean inicializado;

    public TareaAbs(){

    }

    public TareaAbs(ManagerAbs m) {
        this.actual = null;
        this.dividir = false;
        this.id = ManagerAbs.getNextContador();
        this.managerAbs = m;
        this.finalizado = true;
        inicializado = false;
    }

    public TareaAbs(Estado estado, ManagerAbs m) {
        this.actual = estado;
        this.dividir = false;
        this.id = ManagerAbs.getNextContador();
        this.managerAbs = m;
        this.finalizado = true;
        punteroAEstadoGenerador = null;
        inicializado = false;
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

    public void setActual(Estado e){
        this.actual = e;
    }

    public synchronized void despertar() {
 	   notify();
    }

    public boolean isInicializado(){
        return inicializado;
    }


    /** Metodo de inicializacion toma el estado definido como actual y obtiene de el un tablero y una lista
     * de fichas para comenzar la ejecucion. Luego actual = null */
    public void inicializar (){
        //resultLog.info(" --------- INICIALIZA EL THREAD: " + Thread.currentThread().getName());
        tablero = actual.getTablero(); //TODO o una copia de tablero y fichas ????
        fichas = actual.getFichas();
        nivelComienzo = actual.getNivel();
        nombreTarea = actual.getNombre();
        finalizado = false;
        punteroAEstadoGenerador = actual;
        actual = null;
        inicializado = true;
    }

    public void avanzarNodo(Ficha ficha, Integer nivel){
        nivel += 1;
        ficha.setUsada(true);
        tablero.aumentarPosicion();
    }

    public void retrocederNodo(Ficha ficha, Integer nivel){
        tablero.retrocederPosicion();
        ficha.setUsada(false);
        nivel -= 1;
    }

    public void backDFS ( Integer nivel){
        for (Ficha f : fichas) {
            if(!f.isUsada()) {
                for(int i=0; i<4;i++) {
                    tablero.insertarFinal(f);
                    if(tablero.esSolucion()) {
                        if (tablero.esSolucionFinal()) {
                            resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                            Tablero resultado = tablero.clone();
                            ManagerAbs.SOLUCIONES.add(resultado);
                        } else {
                            avanzarNodo(f,nivel);
                            backRichi(fichas, nivel);
                            retrocederNodo(f,nivel);
                        }
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
    }

    public abstract void backRichi(ArrayList<Ficha> fichas, Integer nivel);

    @Override
    public void run(){

        while (ManagerAbs.TIENE_TAREAS) {
            if (actual != null) {
                inicializar();
                long startTime = System.nanoTime();
                backRichi(this.fichas, nivelComienzo);
                long endTime = System.nanoTime();

                BigDecimal duration = new BigDecimal((endTime - startTime));
                BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
                MEDICIONES_LOGGER.info(Thread.currentThread().getName() +" TIEMPO CORRIENDO " + nombreTarea  +" : "+ durationSecs.toString().replace('.', ',') + " SEGUNDOS EN ALGUN core ");
                finalizado = true;
                punteroAEstadoGenerador.setEstadoExplorado(true);
            }else {
                managerAbs.despertar();
            }
            actual = ManagerAbs.getProximoEstado();
        }
        MEDICIONES_LOGGER.info("TIEMPO DE CPU " + Thread.currentThread().getName() + " TAREA " + nombreTarea + " : " + (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP).toString().replace('.', ','));
        managerAbs.despertar();
    }
}
