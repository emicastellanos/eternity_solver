package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TareaRunnable implements Runnable {

    private Tablero tablero;
    private List<Ficha> fichas;
    public boolean encontro = false;
    public int nivelComienzo;
    public Logger resultLog;
    public String nombreThread;
    final Logger threadsLogger = Logger.getLogger("threadLogger");

    public TareaRunnable(Tablero tablero, List<Ficha> fichas, int nivelComienzo,  Logger logger) {
        this.tablero = tablero;
        this.fichas = fichas;
        this.nivelComienzo = nivelComienzo;
//        this.nombreThread = nombreThread;
        this.resultLog = logger;
    }

    public String getInfo(){
        StringBuffer result = new StringBuffer();
        result.append("Fichas: ");
        for (int i=0; i<fichas.size(); i++){
            Ficha f = fichas.get(i);
            result.append("["+f.getIzq()+","+f.getArr()+","+f.getDer()+","+f.getAbj()+ "]");
            if(i<fichas.size()-1){
                result.append(" - ");
            }
        }
        return result.toString();
    }

    public void back(List<Ficha> fichas,Ficha f, Integer nivel){
        List<Ficha> aux ;

        if(!tablero.estaUsada(f)) {
            resultLog.info("NIVEL "+nivel);
            tablero.insertarFinal(f,resultLog);

            if (tablero.esSolucionFinal() && tablero.esSolucion()) {
                resultLog.info(".............SOLUCION ENCONTRADA POR " + Thread.currentThread().getName() + ".............");
                resultLog.info(tablero.imprimirse());
                resultLog.info("\nFICHAS USADAS:");
                resultLog.info(tablero.imprimirUsadas());

                threadsLogger.info(".\n............SOLUCION ENCONTRADA POR " + Thread.currentThread().getName() + ".............");
                threadsLogger.info(tablero.imprimirse());
                threadsLogger.info("\nFICHAS USADAS x " + Thread.currentThread().getName() + ":");
                threadsLogger.info(tablero.imprimirUsadas());
                encontro = true;
            } else {
                if (tablero.esSolucion()) {
                    for (Ficha proxima : fichas) {
                        aux = new ArrayList<>();
                        for (Ficha e : fichas)
                            if (!e.getId().equals(proxima.getId()) && !tablero.estaUsada(e))
                                aux.add(e);
                        if (!encontro) {
                            nivel += 1;
                            back(aux, proxima, nivel);
                            nivel -= 1;
                        }
                    }
                }
            }
            tablero.eliminarUltima(resultLog);
        }
    }

    @Override
    public void run() {
        threadsLogger.info(Thread.currentThread().getName());
        back(new ArrayList<>(fichas.subList(1,fichas.size())),fichas.get(0),nivelComienzo);
        if(!encontro){
            resultLog.info("------ MURIO EL THREAD SIN ENCONTRAR ------");
            threadsLogger.info("\n----- MURIO EL THREAD SIN ENCONTRAR " + Thread.currentThread().getName());
        }
    }
}