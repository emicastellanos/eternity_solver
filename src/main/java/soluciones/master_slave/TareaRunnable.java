package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TareaRunnable extends Thread {

    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    public int nivelComienzo;
    public String nombreThread;
    public static int solucion;
    final Logger threadsLogger = Logger.getLogger("threadLogger");


    public TareaRunnable(Tablero tablero, List<Ficha> fichas, String nombre) {
        this.tablero = tablero;
        this.fichas = new ArrayList<Ficha>();
        solucion = 0;
        Ficha aux;
        for(Ficha f : fichas){
            aux = new Ficha(f.getIzq(),f.getArr(),f.getDer(),f.getAbj(),f.getId(),f.isUsada());
            this.fichas.add(aux);
        }
        this.nombreThread = nombre;
    }

    
    public void backRichi(){
        for (Ficha f : fichas) {
        		if(!f.isUsada()) {
		        	for(int i=0; i<4;i++) {
		            tablero.insertarFinal(f);
		            if(tablero.esSolucion()) {
		                if (tablero.esSolucionFinal()) {
		                    solucion++;
		                    //threadsLogger.info("SOLUCION");
		                    //resultLog.info(tablero.imprimirse());
		                    //listaSoluciones.add(tablero.clone());
		                    
		                } else {
		                		f.setUsada(true);
		                    tablero.aumentarPosicion();
		                    backRichi();
		                    tablero.retrocederPosicion();
		                    f.setUsada(false);
		                }
		            }
		            tablero.eliminarUltima();
		            f.rotar();
		        }
        		}
        }
    }




    public void backRichiLineal(List<Ficha> fichas){
        for (Ficha f : fichas) {
            tablero.insertarFinal(f);
            if(tablero.esSolucion()) {
                if(tablero.esSolucionFinal()){
                    solucion++;
                    //threadsLogger.info("SOLUCION");
                    //resultLog.info(tablero.imprimirse());
                }
                else{
                    ArrayList<Ficha> aux = new ArrayList<Ficha>();
                    for (Ficha e : fichas) {
                        if (e.getId() != f.getId()) {
                            aux.add(e);
                        }
                    }
                    tablero.aumentarPosicion();
                    backRichiLineal(aux);
                    tablero.retrocederPosicion();
                }
            }
            tablero.eliminarUltima();
        }
    }

    @Override
    public void run() {
        ZonedDateTime zdt ;
        threadsLogger.info("\n----- ARRANCA EL THREAD " + nombreThread + " " + ZonedDateTime.now());
        backRichi();
        threadsLogger.info("\n----- TERMINO EL THREAD " + nombreThread+" "+ZonedDateTime.now());
        
    }
}