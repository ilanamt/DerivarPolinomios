import java.util.*;

public class Derivar {

    public static Pila armar(String exp){
        Pila pila = new Pila();
        for (String simbolo : exp.split(" ")){
            char sim = simbolo.charAt(0);

            if(sim == '*' || sim == '/' || sim == '+' || sim == '-'){
                Arbol der = pila.desapilar();
                Arbol izq = pila.desapilar();
                Arbol a = new Arbol(simbolo, izq, der);

                pila.apilar(a);

            }

            else{
                Arbol a = new Arbol(simbolo, null, null);
                pila.apilar(a);
            }
        }

        return pila;
    }

    public static Arbol simplificar(Arbol arb){
        if(arb != null) {
            if (arb.raiz.charAt(0) == '*') {
                if ((arb.der != null && arb.der.raiz.charAt(0) == '0') || (arb.izq != null && arb.izq.raiz.charAt(0) == '0')) {
                    return new Arbol("0", null, null);
                }
                else if (arb.der != null && arb.der.raiz.charAt(0) == '1') {
                    return simplificar(arb.izq);

                }
                else if (arb.izq != null && arb.izq.raiz.charAt(0) == '1') {
                    return simplificar(arb.der);

                }
                else {
                    return new Arbol(arb.raiz, simplificar(arb.izq), simplificar(arb.der));

                }
            }
            else if (arb.raiz.charAt(0) == '/') {
                if (arb.izq != null && arb.izq.raiz.charAt(0) == '0') {
                    return new Arbol("0", null, null);
                }
                else if (arb.der != null && arb.der.raiz.charAt(0) == '1') {
                    return simplificar(arb.izq);
                }
                else {
                    return new Arbol(arb.raiz, simplificar(arb.izq), simplificar(arb.der));

                }
            }
            else if (arb.raiz.charAt(0) == '+') {

                if ((arb.izq != null && arb.izq.raiz.charAt(0) == '0') && (arb.der != null && arb.der.raiz.charAt(0) == '0')) {
                    return new Arbol("0", null, null);
                }
                else if (arb.izq != null && arb.izq.raiz.charAt(0) == '0') {
                    return simplificar(arb.der);
                }
                else if (arb.der != null && arb.der.raiz.charAt(0) == '0') {
                    return simplificar(arb.izq);
                }
                else {
                    return new Arbol(arb.raiz, simplificar(arb.izq), simplificar(arb.der));

                }

            }
            else if (arb.raiz.charAt(0) == '-') {
                if (arb.der != null && arb.der.raiz.charAt(0) == '0') {
                    return simplificar(arb.izq);

                }
                else {
                    return new Arbol(arb.raiz, simplificar(arb.izq), simplificar(arb.der));

                }
            }
            else{
                return new Arbol(arb.raiz, simplificar(arb.izq), simplificar(arb.der));
            }

        }
        return null;
    }



    public static Arbol derivar(Arbol arb, String var){

        char e = arb.raiz.charAt(0);
        char v = var.charAt(0);


        if(e == v){
            arb.raiz = "1";
        }

        else if(e == '+' || e == '-'){
            Arbol arbiz = new Arbol(arb.izq.raiz, arb.izq.izq, arb.izq.der);
            Arbol arbde = new Arbol(arb.der.raiz, arb.der.izq, arb.der.der);

            Arbol a = derivar(arbiz, var);
            Arbol b = derivar(arbde, var);

            arb.izq = simplificar(a);
            arb.der = simplificar(b);



        }

        else if(e == '*'){

            arb.raiz = "+";

            Arbol arbiz11 = new Arbol(arb.izq.raiz, arb.izq.izq, arb.izq.der);
            Arbol arbde11 = new Arbol(arb.der.raiz, arb.der.izq, arb.der.der);

            Arbol arbiz = simplificar(arbiz11);
            Arbol arbde = simplificar(arbde11);

            Arbol arbiz1 = simplificar(arbiz11);
            Arbol arbde1 = simplificar(arbde11);

            Arbol a1 = derivar(arbiz, var);
            Arbol b1 = derivar(arbde, var);

            Arbol a = simplificar(a1);
            Arbol b = simplificar(b1);

            Arbol izq = new Arbol("*", a, arbde1);
            Arbol der = new Arbol("*", arbiz1, b);

            arb.izq = simplificar(izq);
            arb.der = simplificar(der);


        }

        else if(e == '/') {
            Arbol arbiz11 = new Arbol(arb.izq.raiz, arb.izq.izq, arb.izq.der);
            Arbol arbde11 = new Arbol(arb.der.raiz, arb.der.izq, arb.der.der);

            Arbol arbiz = simplificar(arbiz11);
            Arbol arbde = simplificar(arbde11);

            Arbol arbiz1 = simplificar(arbiz11);
            Arbol arbde1 = simplificar(arbde11);

            Arbol a1 = derivar(arbiz, var);
            Arbol b1 = derivar(arbde, var);

            Arbol a = simplificar(a1);
            Arbol b = simplificar(b1);

            Arbol izqq1 = new Arbol("*", a, arbde1);
            Arbol izqd1 = new Arbol ("*", arbiz1, b);

            Arbol izqq = simplificar(izqq1);
            Arbol izqd = simplificar(izqd1);

            Arbol izq = new Arbol("-", izqq, izqd);
            Arbol der = new Arbol("*", arbde1, arbde1);

            arb.izq = simplificar(izq);
            arb.der = simplificar(der);

        }

        else{
            arb.raiz = "0";
        }

        return simplificar(arb);
    }

    public static void parIzq(Arbol a){
        if(a.izq == null){
            a.izq = new Arbol("(", null, null);
        }
        else{
            parIzq(a.izq);
        }

    }

    public static void parDer(Arbol a){
        if(a.der == null){
            a.der = new Arbol(")", null, null);
        }
        else{
            parDer(a.der);
        }

    }

    public static void parentesis(Arbol arb){
        if(arb.raiz.charAt(0) == '*' || arb.raiz.charAt(0) == '/') {
            if (arb.izq.raiz.charAt(0) == '+' || arb.izq.raiz.charAt(0) == '-') {
                parIzq(arb.izq);
                parDer(arb.izq);
            }

            if (arb.der.raiz.charAt(0) == '+' || arb.der.raiz.charAt(0) == '-') {
                parIzq(arb.der);
                parDer(arb.der);
            }

        }

        if(arb.raiz.charAt(0) == '-' && arb.der != null && (arb.der.raiz.charAt(0) == '+' || arb.der.raiz.charAt(0) == '-')){
            parIzq(arb.der);
            parDer(arb.der);
        }

        if(arb.izq != null) parentesis(arb.izq);
        if(arb.der != null) parentesis(arb.der);

    }

    public static void leer(Arbol arb) {
        if (arb != null) {

            if(arb.izq == null && arb.der == null) {
                System.out.print(arb.raiz);
                System.out.print(" ");
            }

            else if(arb.izq != null && arb.der == null) {
                leer(arb.izq);
                System.out.print(arb.raiz);
                System.out.print(" ");
            }

            else if(arb.izq == null) {
                if(arb.raiz.charAt(0) == '/' && (arb.der.raiz.charAt(0) == '/' || arb.der.raiz.charAt(0) == '*')){
                    System.out.print(arb.raiz);
                    System.out.print(" ( ");
                    leer(arb.der);
                    System.out.print(") ");
                }
                else{
                    System.out.print(arb.raiz);
                    System.out.print(" ");
                    leer(arb.der);
                }

            }

            else {
                if(arb.raiz.charAt(0) == '/' && (arb.der.raiz.charAt(0) == '/' || arb.der.raiz.charAt(0) == '*')){
                    leer(arb.izq);
                    System.out.print(arb.raiz);
                    System.out.print(" ( ");
                    leer(arb.der);
                    System.out.print(") ");
                }

                else{
                    leer(arb.izq);
                    System.out.print(arb.raiz);
                    System.out.print(" ");
                    leer(arb.der);
                }

            }
        }
    }



    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.print("Escriba la expresion que desea derivar ");
        String exp = s.nextLine();
        System.out.print("¿Con respecto a que variable desea derivar? ");
        String var = s.nextLine();

        Pila pila = armar(exp);

        Arbol inicial = pila.desapilar();


        Arbol inicial1 = inicial;

        parentesis(inicial1);

        System.out.println("La expresion corresponde a:");
        leer(inicial1);
        System.out.println("");

        Arbol derivado = derivar(inicial, var);

        parentesis(derivado);

        System.out.print("Su derivada con respecto a ");
        System.out.print(var);
        System.out.println(" es:");
        leer(derivado);

    }
}
