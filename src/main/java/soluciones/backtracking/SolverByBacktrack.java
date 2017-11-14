package soluciones.backtracking;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SolverByBacktrack {
    private Tablero tablero;
    public boolean encontro = false;
    private boolean buscarTodasSoluciones;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public SolverByBacktrack(Tablero t, boolean buscarTodas){
        this.tablero=t;
        this.buscarTodasSoluciones = buscarTodas;
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

    public void solucionRichi(List<Ficha> fichas) {
        List<Ficha> aux;
        int nivel = 1;
        for (Ficha f : fichas) {
            aux = new ArrayList<>();
            for (Ficha e : fichas)
                if (!e.getId().equals(f.getId()))
                    aux.add(e);
            if (buscarTodasSoluciones) {
                backRichi(aux, f, nivel);
            } else if (!encontro) {
                backRichi(aux, f, nivel);
            }
        }
    }
}
