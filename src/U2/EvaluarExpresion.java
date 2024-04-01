package U2;

import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EvaluarExpresion {
    private String expresion;
    private LinkedList<String> coinciden;
    private LinkedList<String> noCoincide;

    public EvaluarExpresion(String[] cadenas, int lenguajeAnalizar) {
        coinciden = new LinkedList<>();
        noCoincide = new LinkedList<>();
        switch (lenguajeAnalizar) {
            case 1 -> {
                for (String cadena : cadenas) analizarLenguaje1(cadena);
            }
            case 2 -> {
                for (String cadena : cadenas) lenguaje2(cadena);
            }
            case 3 -> {
                for (String cadena : cadenas) exReg_pattern(cadena);
            }
        }
        System.out.println("Coincide: " + coinciden.toString());
        System.out.println("No coinciden: " + noCoincide.toString());
    }

    /*Este metodo comprueba caracter por caracter si la cadena pertenece a la expresion regylar "(a | e | i | o | u)+ b (0-9)*"
     * Recibe como parametro el string cadena, el cual ira desglozando y comprobando si pertenece o no a la expresion regular*/
    public void analizarLenguaje1(String cadena) {
        if (!esVocal(cadena.charAt(0)) || cadena.length() == 1) {
            noCoincide.add(cadena);
            return;
        }
        boolean coincide = false;
        int aux = 0;
        boolean togVocal = false, togNum = false, togConst = false;
        for (int i = 0; i < cadena.length(); i++) {
            char currChar = cadena.charAt(i);
            aux = i;
            while (esVocal(cadena.charAt(aux)) && !togVocal) { //Este metodo se activa una vez que se detecte una vocal, terminara su ejecucion cuando ya no existan mas vocales.
                if (cadena.charAt(aux+1) == 'b' || aux == cadena.length() - 2) { //Una vez que se analize todas las vocales se comprueba si la siguiente letra es 'b', si es asi se continuara con la evaluiacion.
                    togVocal = true;
                    currChar = cadena.charAt(aux+1);
                    i = aux + 1;
                    break;
                }
                aux++;
            }
            if (!togVocal) break; //Valida que exista aunque sea una vocal dentro de la cadena
            if(!(esNumerico(currChar) || esUnaLetra(currChar))) break;
            if (currChar != 'b' && !togConst)
                break; //Si el caracter actual no es b y no se ha toggleado el boolean de es constante se crasheara el ciclo
            if (togConst && currChar == 'b' || togConst && esUnaLetra(currChar))
                break; //Si hay mas de 1 b o una letra despues del a b
            togConst = true; //Se togglea la consstante
            togNum = esNumerico(currChar); //Se togglea si es un numero el caracter actual
            if (cadena.length() - 1 == i && !togNum)
                coincide = true; //Se comrpueba si la ultima letra es una b y no se ha detectado algun numero
            if (!(togNum && i == cadena.length() - 1))
                continue; //Si aun no se detecta un numero y la i aun no llega a su limite se sigue comprobando
            coincide = true; //Si salta todos los puntos de control, significara que la cadena SI pertenece a la expresion regular
        }
        if (!coincide) noCoincide.add(cadena);
        else coinciden.add(cadena);
    }

    public boolean esVocal(char caracter) { //Metodo para decir si la letra es vocal o no
        return (caracter == 'a' || caracter == 'e' || caracter == 'i' || caracter == 'o' || caracter == 'u');
    }

    public boolean esUnaLetra(char letra) { // Metodo para decir si es una letra o no
        return (letra >= 65 && letra <= 90) || (letra >= 97 && letra <= 122);
    }

    public boolean esNumerico(char numero) { //Metodo para decir si es un caracter numerico o no
        return (numero - '0') >= 0 && ((numero - '0') < 10);
    }

    //ADRIAN
    //para la expresion Expresión Regular: a(a-z)*(1|3|5|7|9)+
    public void lenguaje2(String cadena) {
        boolean coincide = false, imparEncontrado = false;
        if (cadena.charAt(0) != 'a' || cadena.length() == 1) { //Si el primer caracter no es 'a' entonces no tiene porque seguir con la ejecucion del programa, aqui se puede terminar de una.
            noCoincide.add(cadena);
            return;
        }
        for(int i = 0 ; i < cadena.length() ; i++) {
            char caracter = cadena.charAt(i);// se extrae el caracter con la posicion del caracter
            if(!(esNumerico(caracter) || esUnaLetra(caracter))) break;
            if(esUnaLetra(caracter) && !imparEncontrado) continue;
            if(esUnaLetra(caracter) && imparEncontrado) break;
            if (!esImpar(caracter)) break;
            imparEncontrado = true;
            if( i < cadena.length() - 1 && esImpar(caracter) && esNumerico(cadena.charAt(i+1))) continue;
            if(cadena.length()-1 == i) coincide = true;
        }
        if(coincide) coinciden.add(cadena);
        else noCoincide.add(cadena);
    }

    public boolean esImpar(char num){
        return ((int) num)%2 == 1;
    }

    public void exReg_pattern(String cadena) {
        String expReg = "^(ab|xy)+&\\d*";
        Pattern pattern = Pattern.compile(expReg);
        Matcher matcher = pattern.matcher(cadena);
        //MUESTRA SI LA EXPRESION SI PERTENECE O NO A LA ER
        if (matcher.find()) {
            coinciden.add(cadena);
        } else {
            noCoincide.add(cadena);
        }
    }

    public String[] returnCoincidencias() { // Metodo que regresa un arreglo con las cadenas que coinciden de la expresion regular
        return coinciden.toArray(new String[0]);
    }

    public String[] returnNoCoincidencias() { //Metodo que regresa las cadenas que NO coinciden en la expresion regular
        return noCoincide.toArray(new String[0]);
    }

}
