
public class Auxiliar {
    Ficha f;
    int n;

    public Auxiliar(Ficha f, int i){
        this.f=f;
        this.n=i;
    }

    public Ficha getF() {
        return f;
    }

    public int getPosicionUsada() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setF(Ficha f) {
        this.f = f;
    }
}

