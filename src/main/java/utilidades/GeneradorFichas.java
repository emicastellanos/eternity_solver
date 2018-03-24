package utilidades;

import entidades.Ficha;
import entidades.Tablero;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GeneradorFichas {

    private int colorMayor;
    private int N;
    public static final int MAX_COLOR = 10;
    public int[] colores = new int[MAX_COLOR+1];
    private Tablero tablero;

    public GeneradorFichas(int N) {
        this.N = N;
        colorMayor =1 ;
        tablero = new Tablero(N);
    }

    public int[] getColores() {
        return colores;
    }

    public boolean existe (Ficha ficha){
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                Ficha fichaDeTablero = tablero.getPosicion(i,j);
                if(fichaDeTablero==null){
                    return false;
                }else{
                    if(ficha.equals(fichaDeTablero)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int getColor(){
        int color = 1;
        for(int i = 1; i<=MAX_COLOR; i++){
            if(colores[i]< colores[color]){
                color=i;
            }
        }
        colores[color]++;
        return color;
    }

    private int getColorAleatorio(){
        Double i= Math.random()*MAX_COLOR;
        while (i.intValue()==0){
            i= Math.random()*MAX_COLOR;
        }
        colores[i.intValue()]+=1;
        return i.intValue();
    }

    public ArrayList<Ficha> getFichasUnicas() {
        ArrayList<Ficha> generadas = new ArrayList<>();
        int contador = 1;
        Ficha ficha ;
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                ficha = new Ficha(contador);
                //pongo el lado de izquiero igual a 0 si es el borde izquierdo. Sino el valor del lado de la derecha de la ficha de su izquierda
                if(j==0){
                    ficha.setIzq(0);
                }else{
                    ficha.setIzq(tablero.getPosicion(i,j-1).getDer());
                }
                //pongo el lado de arriba igual a 0 si es el borde superior. Sino el valor del lado de abajo de la ficha superior a la que estoy poniendo
                if(i==0){
                    ficha.setArr(0);
                }else{
                    ficha.setArr(tablero.getPosicion(i-1,j).getAbj());
                }
                //pongo el lado de la derecha igual a 0 si es borde derecho. Sino pido un color que mantenga el balance de colores usados
                if(j==N-1){
                    ficha.setDer(0);
                }else{
                    ficha.setDer(getColor());
                }
                //pongo el lado de abajo igual a 0 si es borde inferior. Sino pido un color que mantenga el balance de colores usados
                if(i==N-1){
                    ficha.setAbj(0);
                }else{
                    ficha.setAbj(getColor());
                }

                //reviso si la ficha generada existe dentro de las generadas
                boolean corte = false;
                while(!corte){
                    if(existe(ficha)){
                        //si la ficha generada ya existe, se libera los colores de izq y abajo y se eligen al azar hasta que se encuentre una ficha valida.
                        if(j!=N-1){
                            colores[ficha.getDer()]-=1;
                            ficha.setDer(getColorAleatorio());
                        }
                        if(i!=N-1){
                            colores[ficha.getAbj()]-=1;
                            ficha.setAbj(getColorAleatorio());
                        }
                    }else{
                        generadas.add(ficha);
                        tablero.setPosicion(i,j,ficha);
                        corte=true;
                    }

                }
                contador++;
            }
        }

        /*System.out.print("COLORES: ");
        for (int i=0; i<=MAX_COLOR;i++){
            System.out.print(colores[i] +" - " );
        }*/
        return /*rotarFichas(*/generadas;/*);*/
    }

    private ArrayList<Ficha> rotarFichas(ArrayList<Ficha> fichastemp){
        ArrayList<Ficha> fichasRotadas = new ArrayList<>();
        Ficha ficha = null;
        for (Ficha f : fichastemp) {// las fichas originales son distintas pero una ficha rotada si puede ser igual a otra original,
            fichasRotadas.add(f);
            ficha = new entidades.Ficha(f.getAbj(), f.getIzq(), f.getArr(), f.getDer(), f.getId());
            fichasRotadas.add(ficha);
            ficha = new entidades.Ficha(f.getDer(), f.getAbj(), f.getIzq(), f.getArr(), f.getId());
            fichasRotadas.add(ficha);
            ficha = new entidades.Ficha(f.getArr(), f.getDer(), f.getAbj(), f.getIzq(), f.getId());
            fichasRotadas.add(ficha);
        }
        return  fichasRotadas;
    }



    /**............... A partir de aca para abajo hay codigo que ni se usa ............................*/
    public ArrayList<Ficha> getFichasUnicasRicardo() {
        ArrayList<Ficha> fichastemp = new ArrayList<Ficha>();
        ArrayList<Ficha> fichas = new ArrayList<Ficha>();
        Ficha ficha = null;
        Ficha[][] tablero = new Ficha[N][N];
        int id = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == 0 && j == 0) {
                    ficha = new Ficha(0, 0, 0, 0, id);
                    ponerColorDerAbj(ficha,fichastemp);
                    tablero[i][j] = ficha;
                    fichastemp.add(ficha);
                } else {
                    if (i == 0 && j != N - 1) {
                        ficha = new Ficha(tablero[i][j - 1].getDer(), 0, 0, 0, id);
                        ponerColorDerAbj(ficha,fichastemp);
                        tablero[i][j] = ficha;
                        fichastemp.add(ficha);
                    } else {
                        if (i == 0 && j == N - 1) {
                            ficha = new Ficha(tablero[i][j - 1].getDer(), 0, 0, 0, id);
                            ponerColorAbj(ficha,fichastemp);
                            tablero[i][j] = ficha;
                            fichastemp.add(ficha);
                        } else {
                            if (j == 0 && i != N - 1) {
                                ficha = new Ficha(0, tablero[i - 1][j].getAbj(), 0, 0, id);
                                ponerColorDerAbj(ficha,fichastemp);
                                tablero[i][j] = ficha;
                                fichastemp.add(ficha);
                            } else {
                                if (j != N - 1 && i != N - 1) {
                                    ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, id);
                                    ponerColorDerAbj(ficha,fichastemp);
                                    tablero[i][j] = ficha;
                                    fichastemp.add(ficha);
                                } else {
                                    if (j == N - 1 && i != N - 1) {
                                        ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, id);
                                        ponerColorAbj(ficha,fichastemp);
                                        tablero[i][j] = ficha;
                                        fichastemp.add(ficha);
                                    } else {
                                        if (j == 0 && i == N - 1) {
                                            ficha = new Ficha(0, tablero[i - 1][j].getAbj(), 0, 0, id);
                                            ponerColorDer(ficha,fichastemp);
                                            tablero[i][j] = ficha;
                                            fichastemp.add(ficha);
                                        } else {
                                            if (j != N - 1 && i == N - 1) {
                                                ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, id);
                                                ponerColorDer(ficha,fichastemp);
                                                tablero[i][j] = ficha;
                                                fichastemp.add(ficha);
                                            } else {
                                                if (j == N - 1 && i == N - 1) {
                                                    ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, id);
                                                    tablero[i][j] = ficha;
                                                    fichastemp.add(ficha);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                id++;
            }
        }
        for (Ficha f : fichastemp) {// las fichas originales son distintas pero una ficha rotada si puede ser igual a otra original,
            fichas.add(f);
            ficha = new entidades.Ficha(f.getAbj(), f.getIzq(), f.getArr(), f.getDer(), f.getId());
            fichas.add(ficha);
            ficha = new entidades.Ficha(f.getDer(), f.getAbj(), f.getIzq(), f.getArr(), f.getId());
            fichas.add(ficha);
            ficha = new entidades.Ficha(f.getArr(), f.getDer(), f.getAbj(), f.getIzq(), f.getId());
            fichas.add(ficha);
        }

        return fichas;
    }

    private boolean existeFicha(Ficha aux, ArrayList<Ficha> fichastemp) {
        for (Ficha f : fichastemp) {
            if (aux.getIzq() == f.getIzq() && aux.getArr() == f.getArr() && aux.getDer() == f.getDer() && aux.getAbj() == f.getAbj())
                return true;
            if (aux.getIzq() == f.getAbj() && aux.getArr() == f.getIzq() && aux.getDer() == f.getArr() && aux.getAbj() == f.getDer())
                return true;
            if (aux.getIzq() == f.getDer() && aux.getArr() == f.getAbj() && aux.getDer() == f.getIzq() && aux.getAbj() == f.getArr())
                return true;
            if (aux.getIzq() == f.getArr() && aux.getArr() == f.getDer() && aux.getDer() == f.getAbj() && aux.getAbj() == f.getIzq())
                return true;
        }
        return false;
    }

    private void ponerColorDerAbj(Ficha aux, ArrayList<Ficha> fichas) {
        boolean encontro = false;
        int colorDer = 1;
        int colorAbj = 1;
        boolean AbjUltima = true;
        while (!encontro && colorMayor >= colorDer && colorMayor >= colorAbj) {
            aux.setDer(colorDer);
            aux.setAbj(colorAbj);
            if (!existeFicha(aux,fichas)) {
                encontro = true;
            } else {
                if (AbjUltima) {
                    colorDer++;
                    AbjUltima = false;
                } else {
                    colorAbj++;
                    AbjUltima = true;
                }
            }
        }

        colorDer = 1;
        colorAbj = 1;
        while (!encontro && colorMayor >= colorDer) {
            aux.setDer(colorDer);
            aux.setAbj(colorAbj);
            if (!existeFicha(aux,fichas)) {
                encontro = true;
            } else {
                colorDer++;

            }
        }

        colorDer = 1;
        colorAbj = 1;
        while (!encontro && colorMayor >= colorAbj) {
            aux.setDer(colorDer);
            aux.setAbj(colorAbj);
            if (!existeFicha(aux,fichas)) {
                encontro = true;
            } else {
                colorAbj++;

            }
        }
        if (!encontro) {
            colorMayor++;
            aux.setDer(colorMayor);
            aux.setAbj(1);
        }

    }

    private void ponerColorAbj(Ficha aux, ArrayList<Ficha> fichas) {
        boolean encontro = false;
        int color = 1;
        while (!encontro) {
            aux.setAbj(color);
            if (!existeFicha(aux,fichas)) {
                encontro = true;
                if (color > colorMayor)
                    colorMayor++;
            }
            color++;
        }
    }

    private void ponerColorDer(Ficha aux,ArrayList<Ficha> fichas) {
        boolean encontro = false;
        int color = 1;
        while (!encontro) {
            aux.setDer(color);
            if (!existeFicha(aux,fichas)) {
                encontro = true;
                if (color > colorMayor)
                    colorMayor++;
            }
            color++;
        }
    }

    public ArrayList<Ficha> crearFichasDesdeArchivo(String location) throws FileNotFoundException, IOException {
        String cadena;
        Ficha ficha = null;
        ArrayList<Ficha> fichas = new ArrayList<Ficha>();
        String[] colores;
        FileReader f = new FileReader(location);
        BufferedReader b = new BufferedReader(f);
        int i = 0;
        while ((cadena = b.readLine()) != null) {

            colores = cadena.split("-");
            ficha = new Ficha(Integer.valueOf(colores[0]), Integer.valueOf(colores[1]), Integer.valueOf(colores[2]), Integer.valueOf(colores[3]), i);
            fichas.add(ficha);
            i++;
        }
        b.close();
        return fichas;
    }
}
