package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.Utils;

import java.util.ArrayList;

public class Tarea extends Thread {
    private boolean finalizado; // cuando llega al final del metodo run ()
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Estado actual;
    private Manager manager ;


    private Integer nivelComienzo;
    private static boolean dividir; // es la señal que se va a activar para que la primera tarea que la vea genere hijos

    private String nombre;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    private long id ;


    public Tarea(Estado estado,Manager m) {
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

    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
        boolean encontro = false;
        //synchronized (this){
            if(isDividir() && fichas.size()>1){
                this.setDividir(false);
                encontro = true;
            }
       //}
        if(encontro){
            Manager.addEstado(new Estado(tablero.clone(), Utils.getCopia(fichas),nivel));
            //resultLog.info("dividi" + Thread.currentThread().getName());
        }else{
            for (Ficha f : fichas) {
                if(!f.isUsada()) {
                    for(int i=0; i<4;i++) {
                        tablero.insertarFinal(f);
                        if(tablero.esSolucion()) {
                            if (tablero.esSolucionFinal()) {
                                resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                                Tablero resultado = tablero.clone();
                                Manager.SOLUCIONES.add(resultado);
                            } else {
                                nivel += 1;
                                f.setUsada(true);
                                tablero.aumentarPosicion();
                                backRichi(fichas, nivel);
                                tablero.retrocederPosicion();
                                f.setUsada(false);
                                nivel -= 1;
                            }
                        }
                        tablero.eliminarUltima();
                        f.rotar();
                    }
                }
            }
        }
        //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());
    }

    @Override
    public void run(){
        while (Manager.TIENE_TAREAS) {
            if (actual != null) {
                inicializar();
                backRichi(this.fichas, nivelComienzo);
                //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());
                finalizado = true;
                
            } 
            else {
            manager.despertar();
            }
            actual = Manager.getProximoEstado();
        }
    }
}
