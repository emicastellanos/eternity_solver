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
    public Ficha inicial ;
    public Logger resultLog;
    public int nivelComienzo;
    public String nombreThread;
    private boolean buscarTodasSoluciones = true;
    final Logger threadsLogger = Logger.getLogger("threadLogger");

    public TareaRunnable(Tablero tablero, List<Ficha> fichas, int nivelComienzo,Ficha inicial, Logger logger) {
        this.tablero = tablero;
        this.inicial = inicial;
        this.fichas = fichas;
        this.nivelComienzo = nivelComienzo;
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
    
    public void backRichi(List<Ficha> fichas,Ficha f, Integer nivel){
        List<Ficha> aux ;
        resultLog.info("NIVEL "+nivel);
        tablero.insertarFinal(f,resultLog);

        if(tablero.esSolucionFinal()&& tablero.esSolucion()){
            resultLog.info("SOLUCION");
            resultLog.info(tablero.imprimirse());
            encontro=true;
        }
        else{
            if(tablero.esSolucion()){
                for(Ficha proxima : fichas){
                    aux = new ArrayList<>();
                    for(Ficha e: fichas)
                        if(!e.getId().equals(proxima.getId()))
                            aux.add(e);
                    if(buscarTodasSoluciones) {
                        nivel+=1;
                        backRichi(aux, proxima, nivel);
                        nivel-=1;
                    }else if(!encontro) {
                        nivel+=1;
                        backRichi(aux, proxima, nivel);
                        nivel-=1;
                    }
                }
            }
        }
        tablero.eliminarUltima(resultLog);
    }

    

    @Override
    public void run() {
        threadsLogger.info(Thread.currentThread().getName());
        backRichi(fichas,inicial,nivelComienzo);
        threadsLogger.info("\n----- TERMINO EL THREAD" + Thread.currentThread().getName());
        
    }
}