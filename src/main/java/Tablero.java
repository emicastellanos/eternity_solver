import org.apache.log4j.Logger;

public class Tablero {
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    private int N;

    private Ficha[][] tablero;


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
    
    public String dameUnPiso() {
    	StringBuffer r = new StringBuffer();
    	for(int i=0; i<N; i++) {
    		r.append("---------|");
    	}
    	return r.toString();
    }
    
    public  String imprimirse() {
        String result = "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tablero[i][j] != null) {
                    result += tablero[i][j].imprimirse() + "|";
                } else {
                    result += "*-*-*-*|";
                }

            }
            result += "\n";
        }
        return result;
    }

    public String imprimirseCheto() {
        String result = "";
        String resultTecho = "";
        String resultMedio = "";
        String resultAbajo = "";
        for (int i = 0; i < N; i++) {
        	resultTecho = "";
            resultMedio = "";
            resultAbajo = "";
            for (int j = 0; j < N; j++) {
                if (tablero[i][j] != null) {
                	resultTecho += tablero[i][j].imprimirTecho();
                	resultMedio += tablero[i][j].imprimirMedio();
                	resultAbajo += tablero[i][j].imprimirAbajo();
                } else {
                    result += "*-*-*-*|";
                }
            }
            result += resultTecho + "\n" + resultMedio + "\n" +resultAbajo +"\n";
            result += dameUnPiso() + "\n"; 
        }
        return result;
    }

    public Boolean estaLibre(int i, int j) {
        return tablero[i][j] == null;
    }


    public Ficha getPosicion(int fila, int col) {
        return tablero[fila][col];
    }


    public Boolean esSolucion(int fila, int columna, Ficha f) {

        if (fila == 0) {
            if (f.getArr() != 0)
                return false;
        }
        if (columna == 0) {
            if (f.getIzq() != 0)
                return false;
        }
        if (fila == N - 1) {
            if (f != null && f.getAbj() != 0) {
                return false;
            }
        }
        if (columna == N - 1) {
            if (f.getDer() != 0) {
                return false;
            }
        }
        if (fila != 0 && tablero[fila - 1][columna] != null && tablero[fila - 1][columna].getAbj() != f.getArr()) {
            return false;
        }
        if (columna != 0 && tablero[fila][columna - 1] != null && tablero[fila][columna - 1].getDer() != f.getIzq()) {
            return false;
        }

        return true;
    }

    public Boolean esSolucion() {
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

    public Boolean esSolucionFinal() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] == null)
                    return false;

        return true;
    }

    public void insertarFinal(Ficha f) {
        boolean seguir = true;
        for (int i = 0; i < N && seguir; i++)
            for (int j = 0; j < N && seguir; j++)
                if (tablero[i][j] == null) {
                    tablero[i][j] = f;
                    seguir = false;
                  //  resultLog.info("PONER [" + i + "," + j + "] \n");
                  //  resultLog.info(this.imprimirse());
                }
    }

    public void eliminarUltima() {
        boolean seguir = true;
        for (int i = 0; i < N && seguir; i++) {
            for (int j = 0; j < N && seguir; j++) {
                if (tablero[i][j] == null) {
                    if (j == 0) {
                        tablero[i - 1][N - 1] = null;
                       // resultLog.info("ELIMINAR [" + String.valueOf(i - 1) + "," + String.valueOf(N-1) + "]");
                    } else {
                        tablero[i][j - 1] = null;
                        //resultLog.info("ELIMINAR [" + i + "," + String.valueOf(j-1) + "]");
                    }
                    seguir = false;
                } else {
                    if (j == N - 1 && i == N - 1) {
                        tablero[i][j] = null;
                        seguir = false;
                      //  resultLog.info("ELIMINAR [" + i + "," + j + "]");
                    }
                }
            }
        }
    }

}
