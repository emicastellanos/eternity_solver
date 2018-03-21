package entidades;

public class Tablero {
    private int N;
    private int fila;
    private int columna;
    private Ficha[][] tablero;


    public Tablero(int n) {
        this.N = n;
        tablero = new Ficha[N][N];
        fila = 0;
        columna = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tablero[i][j] = null;
    }

    
    public void aumentarPosicion() {
	    	if(columna != N-1) {
	    		columna++;
	    	}
	    	else {
	    		fila++;
	    		columna = 0;
	    	}
    }
    
    public void retrocederPosicion() {
	    	if(columna != 0) {
	    		columna--;
	    	}
	    	else {
	    		columna = N-1;
	    		fila--;
	    	}
    }
    


    public Boolean esSolucion() {
    	
    	//arriba
        if (fila != 0 && tablero[fila-1][columna].getAbj() != tablero[fila][columna].getArr()) 
            return false;
        
        //izquierda
        if (columna != 0 && tablero[fila][columna-1].getDer() != tablero[fila][columna].getIzq()) 
            return false;
            
        if (columna != N-1 && tablero[fila][columna].getDer() == 0 )
        	return false;
        
        if (fila != N-1 && tablero[fila][columna].getAbj() == 0 )
        	return false;
       
        if (fila == 0 && tablero[fila][columna].getArr() != 0) 
                return false;
        
        if (columna == 0 && tablero[fila][columna].getIzq() != 0) 
                return false;
            
        
        if (fila == N-1 && tablero[fila][columna].getAbj() != 0) 
                return false;
           
        
        if (columna == N - 1 && tablero[fila][columna].getDer() != 0)
                return false;
        
        
        return true;
    }


    public Boolean esSolucionFinal() {
       if(fila == N-1 && columna == N-1) {
    	   		return true;
       }
       else {
    	   		return false;
       }
    }

    public void insertarFinal(Ficha f) {
       tablero[fila][columna] = f; 
    }


    public void eliminarUltima() {
    	 tablero[fila][columna] = null;
    }
    
    //**---------------------------------------------------------***

    public Boolean esSolucionVieja() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tablero[i][j] != null) {
                    if (i == 0 && tablero[i][j].getArr() != 0) {
                        return false;
                    }
                    if (i == N - 1 && tablero[i][j].getAbj() != 0) {
                        return false;
                    }
                    if (j == 0 && tablero[i][j].getIzq() != 0) {
                        return false;
                    }
                    if (j == N - 1 && tablero[i][j].getDer() != 0) {
                        return false;
                    }
                    if (j >= 0 && j < N - 1) {
                        if (tablero[i][j + 1] != null && tablero[i][j].getDer() != tablero[i][j + 1].getIzq()) {
                            return false;
                        }
                    }
                    if (i >= 0 && i < N - 1) {
                        if (tablero[i + 1][j] != null && tablero[i][j].getAbj() != tablero[i + 1][j].getArr()) {
                            return false;
                        }
                    }
                    if (i > 0 && i < N - 1) {
                        if (tablero[i][j] != null && (tablero[i][j].getAbj() == 0 || tablero[i][j].getArr() == 0)) {
                            return false;
                        }
                    }
                    if (j > 0 && j < N - 1) {
                        if (tablero[i][j] != null && (tablero[i][j].getIzq() == 0 || tablero[i][j].getDer() == 0)) {
                            return false;
                        }
                    }
                } else return true;
            }
        }
        return true;
    }

    public Boolean esSolucionFinalVieja() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] == null)
                    return false;

        return true;
    }

    public void insertarFinalVieja(Ficha f) {
        boolean seguir = true;
        for (int i = 0; i < N && seguir; i++)
            for (int j = 0; j < N && seguir; j++)
                if (tablero[i][j] == null) {
                    tablero[i][j] = f;
                    seguir = false;
                   // resultLog.info("\n"+Thread.currentThread().getName() + "\nPONER [" + i + "," + j + "] \n "+this.imprimirse());
                    /*resultLog.info("PONER [" + i + "," + j + "] \n");
                    resultLog.info(this.imprimirse());*/
                }
    }



    public void eliminarUltimaVieja() {
        boolean seguir = true;
        Ficha eliminada = null;
        for (int i = 0; i < N && seguir; i++) {
            for (int j = 0; j < N && seguir; j++) {
                if (tablero[i][j] == null) {
                    if (j == 0) {
                        eliminada = tablero[i - 1][N - 1];
                        tablero[i - 1][N - 1] = null;
                        //resultLog.info("ELIMINAR [" + String.valueOf(i - 1) + "," + String.valueOf(N-1) + "] " + Thread.currentThread().getName());
                    } else {
                        eliminada = tablero[i][j - 1] ;
                        tablero[i][j - 1] = null;
                        //resultLog.info("ELIMINAR [" + i + "," + String.valueOf(j-1) + "] " + Thread.currentThread().getName());
                    }
                    seguir = false;
                } else {
                    if (j == N - 1 && i == N - 1) {
                        eliminada = tablero[i][j];
                        tablero[i][j] = null;
                        seguir = false;
                        //resultLog.info("ELIMINAR [" + i + "," + j + "] " + Thread.currentThread().getName());
                    }
                }
            }
        }
    }

}
