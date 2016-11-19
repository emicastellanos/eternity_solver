import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SolverByBacktrack {
    private int N;
    private ArrayList<Ficha> fichas;
    private Tablero tablero;
    private Boolean[] libres;
    private int TOTAL_FICHAS;
    public boolean encontro = false;
    private ArrayList<Integer> pilaUsados = new ArrayList<>();
    static final Logger resultLog = Logger.getLogger("resultadoLogger");





    public SolverByBacktrack(int N, ArrayList<Ficha> fichas, Tablero t, Boolean[] libres){
        this.N = N;
        this.fichas=fichas;
        tablero=t;
        this.libres=libres;
        TOTAL_FICHAS=N*N;
    }

    /**
     * @param libres lista de libres
     * @return true si lista libres[i] == true para toda i
     */
    public Boolean todasUsadas(Boolean[] libres){
        for(int i=0; i<TOTAL_FICHAS; i++){
            if(libres[i])
                return false;
        }
        return true;
    }

    /**
     * @param pos: posicion a partir de la cual se debe empezar a buscar la siguiente ficha libre de la lista de fichas
     * @return
     * busca a partir de una posicion, si no encuentra, busca desde el comienzo
     */
    public Auxiliar getFichaLibre(Integer pos){
        for(int i=pos;i<TOTAL_FICHAS;i++){
            if(libres[i]){
                return new Auxiliar(fichas.get(i),i);
            }
        }
        for(Integer i=0;i<pos;i++){
            if(libres[i]){
                return new Auxiliar(fichas.get(i),i);
            }
        }
        return null;
    }

    /*cantidad de potenciales hijos de un nodo del arbol*/
    public Integer getCantidadLibres (Boolean[] libres){
        Integer res = 0;
        for(int i=0;i<libres.length;i++){
            if(libres[i])
                res++;
        }
        return res;
    }

    public String usadas (Boolean[] libres){
        String resultado = "";
        for (int i=0; i<TOTAL_FICHAS ; i++){
            if (!libres[i]){
                resultado += i+"-";
            }
        }
        return resultado;
    }

    public int totalUbicados(){
        return tablero.getTotalFichasPuestaS();
    }

    public int totalUsados(){
        int t = 0;
        for (int i=0 ; i<TOTAL_FICHAS;i++){
            if(!libres[i])
                t+=1;
        }
        return t;

    }

    public Boolean detener(){
        return totalUbicados()!=totalUsados();
    }

    public void backtracking(Tablero tablero, int fila, int columna, Boolean[] libres, Integer ultimaUsada, Integer iteracion){

        Integer posibles = getCantidadLibres(libres);
        resultLog.info("*******");
        resultLog.info(tablero.imprimirse());
        if(detener()){
            resultLog.info("ACA SE DETUVO");
        }
        while (!todasUsadas(libres) && tablero.esSolucion() && posibles>0){
            resultLog.info("[" + fila + "," + columna + "]");
            Auxiliar aux = getFichaLibre(ultimaUsada);
            Ficha fichaUsada = aux.getF();
            resultLog.info("PONE EN [" + fila + "," + columna + "] <FICHA nro " + fichaUsada.getId() + ",pos en lista:" + ultimaUsada + ">: " + fichaUsada.imprimirse());
            if(!tablero.estaLibre(fila,columna)){
                resultLog.info("DETENER");
            }
            tablero.setPosicion(fila, columna, fichaUsada);

            libres[aux.getPosicionUsada()]=false;
            resultLog.info("fichas usadas (" + totalUsados() + ") : " + usadas(libres));
            if(columna<N-1){
                backtracking(tablero,fila,columna+1,libres, aux.getPosicionUsada(),iteracion+1);
            } else {
                backtracking(tablero, fila + 1, 0, libres, aux.getPosicionUsada(),iteracion+1);
            }
            posibles--;
            /*o bien no es una solucion parcial o es una solucion parcial pero ademas quedan movimientos posibles
            esta situacion se da cuando se ingresa una ficha en una posicion que encaja pero impide que otras encajen, seria un nodo del arbol
            que aparentemente funciona pero necesitamos verificar todos sus hijos para evidenciar que no*/
            if((!tablero.esSolucion() || tablero.esSolucion() && posibles>=0) &&(!tablero.esSolucionFinal())){
                tablero.liberarPosicion(fila, columna);
                libres[aux.getPosicionUsada()]=true;
                resultLog.info("SACA DE [" + fila + "," + columna + "] <FICHA nro " + fichaUsada.getId() + ",pos en lista:" + ultimaUsada + ">: " + fichaUsada.imprimirse());
                resultLog.info("fichas usadas (" + totalUsados() + ") : " + usadas(libres));
                if(aux.getPosicionUsada()<TOTAL_FICHAS-1){
                    ultimaUsada=aux.getPosicionUsada()+1;
                }else{
                    ultimaUsada=TOTAL_FICHAS;
                }
            }
        }
    }

    public void findSolution(){
        Integer fila = 0;
        Integer columna = 0;
        Integer ultimaUsada = 0;
        backtracking(tablero,fila,columna,libres,ultimaUsada,0);
        if(!tablero.esSolucionFinal()){
            resultLog.info("NO SE ENCONTRO SOLUCION");
        }else{
            resultLog.info("SOLUCION");
            resultLog.info(tablero.imprimirse());
        }
    }
    
    public void backRichi(List<Ficha> fichas,Ficha f){
	    	List<Ficha> aux ;
	    	tablero.insertarFinal(f);
	    	
	    	if(tablero.esSolucionFinal()&& tablero.esSolucion()){
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
		    			if(!encontro)
		    				backRichi(aux,proxima);
	    			}
	    			
	    		}
	    		
	    	}
	    	tablero.eliminarUltima();

    }

    
    public void solucionRichi(List<Ficha> fichas){
    	List<Ficha> aux ;
    	for(Ficha f: fichas){
    		aux = new ArrayList<Ficha>();
    		for(Ficha e: fichas)
    			if(e.getId()!=f.getId())
    				aux.add(e);
    		if(!encontro)
    			backRichi(aux,f);
    	}
    }
}
