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
        if (fila != 0 && tablero[fila-1][columna].getAbj() != tablero[fila][columna].getArr()) {
            return false;
        }
        //izquierda
        if (columna != 0 && tablero[fila][columna-1].getDer() != tablero[fila][columna].getIzq()) {
            return false;
        }
        if (fila == 0) {
            if (tablero[fila][columna].getArr() != 0) {
                return false;
            }
        }
        if (columna == 0) {
            if (tablero[fila][columna].getIzq() != 0) {
                return false;
            }
        }
        if (fila == N - 1) {
            if (tablero[fila][columna].getAbj() != 0) {
                return false;
            }
        }
        if (columna == N - 1) {
            if (tablero[fila][columna].getDer() != 0) {
                return false;
            }
        }
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

}
