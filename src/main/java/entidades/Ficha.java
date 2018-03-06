package entidades;


public class Ficha {
    private int izq;
    private int arr;
    private int der;
    private int abj;
    private int id;

    public Ficha(int izq, int arr,int der, int abj,int id) {
        this.izq = izq;
        this.der = der;
        this.arr = arr;
        this.abj = abj;
        this.id = id;
    }

    public int getIzq() {
        return izq;
    }

    public void setIzq(int izq) {
        this.izq = izq;
    }

    public int getDer() {
        return der;
    }

    public void setDer(int der) {
        this.der = der;
    }

    public int getArr() {
        return arr;
    }

    public void setArr(int arr) {
        this.arr = arr;
    }

    public int getAbj() {
        return abj;
    }

    public void setAbj(int abj) {
        this.abj = abj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
