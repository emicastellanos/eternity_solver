import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SolverByBacktrack {
    private Tablero tablero;
    public boolean encontro = false;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public SolverByBacktrack(Tablero t){
        tablero=t;

    }

    
    public void backRichi(List<Ficha> fichas,Ficha f, Integer nivel){
        List<Ficha> aux ;
        resultLog.info("NIVEL "+nivel);
        tablero.insertarFinal(f);

        if(tablero.esSolucionFinal()&& tablero.esSolucion()){
            resultLog.info("SOLUCION");
            resultLog.info(tablero.imprimirse());
            encontro=true;
        }
        else{
            if(tablero.esSolucion()){
                for(Ficha proxima : fichas){
                    aux = new ArrayList<Ficha>();
                    for(Ficha e: fichas)
                        if(e.getId()!=proxima.getId())
                            aux.add(e);
                    if(!encontro) {
                        nivel+=1;
                        backRichi(aux, proxima, nivel);
                        nivel-=1;
                        tablero.eliminarUltima();
                    }
                }
            }
        }
    }

    
    public void solucionRichi(List<Ficha> fichas){
    	List<Ficha> aux ;
        int nivel = 1;
    	for(Ficha f: fichas){
    		aux = new ArrayList<Ficha>();
    		for(Ficha e: fichas)
    			if(e.getId()!=f.getId())
    				aux.add(e);
    		if(!encontro)
    			backRichi(aux,f,nivel);
    	}
    }
}
