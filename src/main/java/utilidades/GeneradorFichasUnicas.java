package utilidades;

import entidades.Ficha;

import java.util.ArrayList;

public class GeneradorFichasUnicas extends GeneradorFichas {
    private int sumaColor = 1;

    public GeneradorFichasUnicas(int N, int cant_colores){
        super(N,cant_colores);
    }

    private int getColorUnico(){
        int sum = sumaColor;
        colores[sumaColor]+=1;
        sumaColor++;
        if(sumaColor > cantidadColores)
            sumaColor = 1;
        return sum;
    }


    public boolean hacerValidacionesFicha (Ficha ficha, int i, int j, ArrayList<Ficha> generadas){
        boolean corte = false;
        while(!corte){
            if(existe(ficha)){
                if(j!=N-1){
                    colores[ficha.getDer()]-=1;
                    ficha.setDer(getColorUnico());
                }
                if(i!=N-1){
                    colores[ficha.getAbj()]-=1;
                    ficha.setAbj(getColorUnico());
                }
                if(i==N-1 && j==N-1){
                    return false;
                }
            }else{
                generadas.add(ficha);
                return true;
            }
        }
		return true;
    }

    public static boolean sonIguales(ArrayList<Ficha> fichas , ArrayList<Ficha> fichasComparador ){
        for(int i=0; i<fichas.size();i++){
            if(!fichas.get(i).equals(fichasComparador.get(i))){
                return false;
            }
        }
        return true;
    }

    public static void main (String [] args){
        GeneradorFichas g1 = new GeneradorFichasUnicas(8,8);
        ArrayList<Ficha> fichas = g1.getFichasUnicas();
        for(int prueba = 1 ; prueba < 25; prueba++){
            GeneradorFichas comparativa = new GeneradorFichasUnicas(8,8);
            System.out.println("Compara " + prueba);
            if (!sonIguales(fichas, comparativa.getFichasUnicas())){
                System.out.println("NOOOOOO");
                break;
            }
        }
        System.out.println("termino");
        System.out.println(getOcurrenciasCadaColor(fichas));
    }

}
