public class Pila {

    private Nodo ultimo;
    private int size;

    private class Nodo{
        Arbol elemento;
        Nodo siguiente;

    }

    public Pila() {
        ultimo = null;
        size = 0; //inicialmente la pila está vacía

    }


    public boolean estaVacia() {
        return size == 0;

    }


    public void apilar(Arbol a) {
        if(estaVacia()){
            ultimo = new Nodo();
            ultimo.elemento = a;
            ultimo.siguiente = null;

        }
        else{
            Nodo aux = ultimo;
            ultimo = new Nodo();
            ultimo.elemento = a;
            ultimo.siguiente = aux;
        }

        size = size+1;
    }

    public Arbol desapilar() {
        if (!estaVacia()) {
            Arbol x = ultimo.elemento;
            if(size == 1){
                ultimo = null;
            }
            else{
                ultimo = ultimo.siguiente;
            }

            size = size -1;
            return x;

        }
        else{
            return null;
        }
    }

}


