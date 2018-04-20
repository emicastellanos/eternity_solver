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


    private Integer nivelComienzo;
    private static boolean dividir; // es la señal que se va a activar para que la primera tarea que la vea genere hijos

    private String nombre;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    private long id ;


    public Tarea(Estado estado) {
        this.actual = estado;
        this.dividir = false;
        this.id = Manager.getNextContador();
        this.nombre = "TAREA-" + this.id;
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

    /** Metodo de inicializacion toma el estado definido como actual y obtiene de el un tablero y una lista
     * de fichas para comenzar la ejecucion. Luego actual = null */
    public void inicializar (){
        resultLog.info(" --------- INICIALIZA EL THREAD: " + ((Tarea)Thread.currentThread()).getNombre());
        tablero = actual.getTablero(); //TODO o una copia de tablero y fichas ????
        fichas = actual.getFichas();
        nivelComienzo = actual.getNivel();
        finalizado = false;
        actual = null;
    }

    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
        boolean encontro = false;
        synchronized (this){
            if(isDividir() && fichas.size()>1){
                this.setDividir(false);
                encontro = true;
            }
        }
        if(encontro){
            Manager.addEstado(new Estado(tablero.clone(), Utils.getCopia(fichas),nivel));
        }else{
            for (Ficha f : fichas) {
                if(!f.isUsada()) {
                    for(int i=0; i<4;i++) {
                        tablero.insertarFinal(f);
                        if(tablero.esSolucion()) {
                            if (tablero.esSolucionFinal()) {
                                resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + ((Tarea)Thread.currentThread()).getNombre());
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
                finalizado = true;
            } else {
                try {
                    Thread.sleep(10);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            try {
                if (actual == null){
                    actual = Manager.getProximoEstado();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
