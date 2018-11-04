package soluciones.reduccion_threads;

public class ManagerDivisiones extends ManagerAbs {

    public ManagerDivisiones(String tipo, int n, int c, int h, int b, int des){
        super(tipo,n,c,h,b,des);
    }

    public void logicaDivisiones(){
        while (cantActivas()>0 || pendientes.size() > indice){
            if(cantActivas() < hilosParalelos && pendientes.size() == indice) { // si hay pendientes no divido
                TareaAbs.setDividir(true);
                //MEDICIONES_LOGGER.info("FLAG DIVIDIR = TRUE");
                cantdivisiones+=1;
            }

            try {
                wait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        int n = 8;
        int c = 8;
        int h = 12;
        int b = 3;
        int des = 0;

        if(args.length == 5){
            n = Integer.valueOf(args[0]);
            c = Integer.valueOf(args[1]);
            h = Integer.valueOf(args[2]);
            b = Integer.valueOf(args[3]);
            des = Integer.valueOf(args[4]);
        }

        ManagerAbs m = new ManagerDivisiones("DFS",n,c,h,b,des);
        m.start();
    }

}
