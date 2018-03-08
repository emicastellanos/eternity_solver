package entidades;

public class Tablero {
    private int N;

    private Ficha[][] tablero;

    public class Posicion{
        int fila;
        int columna;

        public Posicion(int i, int j) {
            this.fila = i;
            this.columna = j;
        }

		public int getFila() {
			return fila;
		}

		public void setFila(int fila) {
			this.fila = fila;
		}

		public int getColumna() {
			return columna;
		}

		public void setColumna(int columna) {
			this.columna = columna;
		}
        

    }
    
    private Posicion posicion = new Posicion(0,0);

    public Tablero(int n) {
        this.N = n;
        tablero = new Ficha[N][N];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tablero[i][j] = null;
    }

    public int getTotalFichasPuestas() {
        int t = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] != null) {
                    t++;
                }
        return t;
    }


    public void setPosicion(int fila, int columna, Ficha ficha) {
        tablero[fila][columna] = ficha;
    }

    public void liberarPosicion(int fila, int columna) {
        tablero[fila][columna] = null;
    }


    public Boolean estaLibre(int i, int j) {
        return tablero[i][j] == null;
    }


    public Ficha getPosicion(int fila, int col) {
        return tablero[fila][col];
    }
    
    public void aumentarPosicion() {
    	if(posicion.columna != N-1)
    		posicion.columna++;
    	else {
    		posicion.fila++;
    		posicion.columna = 0;
    	}
    }
    
    public void retrocederPosicion() {
    	if(posicion.columna != 0)
    		posicion.columna--;
    	else {
    		posicion.columna = N-1;
    		posicion.fila--;
    	}
    }
    


    public Boolean esSolucion() {

        if (posicion.fila == 0) {
            if (tablero[posicion.fila][posicion.columna].getArr() != 0)
                return false;
        }
        if (posicion.columna == 0) {
            if (tablero[posicion.fila][posicion.columna].getIzq() != 0)
                return false;
        }
        if (posicion.fila == N - 1) {
            if (tablero[posicion.fila][posicion.columna].getAbj() != 0) {
                return false;
            }
        }
        if (posicion.columna == N - 1) {
            if (tablero[posicion.fila][posicion.columna].getDer() != 0) {
                return false;
            }
        }
        //arriba
        if (posicion.fila != 0 && tablero[posicion.fila-1][posicion.columna].getAbj() != tablero[posicion.fila][posicion.columna].getArr()) {
            return false;
        }
        //izquierda
        if (posicion.columna != 0 && tablero[posicion.fila][posicion.columna-1].getDer() != tablero[posicion.fila][posicion.columna].getIzq()) {
            return false;
        }
        return true;
    }


    public Boolean esSolucionFinal() {
       if(posicion.fila == N-1 && posicion.columna == N-1)
    	   return true;
       else
    	   return false;
    }

    public void insertarFinal(Ficha f) {
       tablero[posicion.fila][posicion.columna] = f; 
    }



    public void eliminarUltima() {
    	 tablero[posicion.fila][posicion.columna] = null;
    }


    @Override
    public Tablero clone() {
        Tablero result = new Tablero(this.N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result.setPosicion(i, j, getPosicion(i, j));
            }
        }
        return result;
    }





}
