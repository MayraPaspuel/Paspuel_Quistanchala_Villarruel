/*
 * ESPE - DCC - PROGRAMACIÓN MÓVIL
 * Sistema: Calculadora
 * Creado 30/05/2020
 * Modificado 16/06/2020
 *
 * Los contenidos de este archivo son propiedad privada y estan protegidos por
 * la licencia BSD
 *
 * Se puede utilizar, reproducir o copiar el contenido de este archivo.
 */
package com.example.calculadora;

import java.util.Stack;
import java.util.StringTokenizer;
/**
 * Clase que implementa el modelo de la calculadora
 *
 * @author Paspuel Mayra
 * @author Quistanchala Karla
 * @author Villarruel Michael
 */
public class CalculadoraModelo implements Calculadora.Modelo{

    CalculadoraPresentador presentador;
    Operaciones operacion;

    /**
     * Constructor vacio
     */
    public CalculadoraModelo(){

    }

    /**
     * Constructor
     * @param presentador es el presentador de la aplicación
     */
    public CalculadoraModelo(CalculadoraPresentador presentador){
        this.presentador = presentador;
        this.operacion = new Operaciones();
    }

    private static String tokensValidos = "+-*/^!() ";

    /**
     * Operadores que pueden ser utilizados en la calculadora
     */
    private static enum Operador {
        oSuma, oResta, oMultiplicacion, oDivision, oPotencia, oFactorial, oParenIz, oParentDer, oInvalido, oMod, oLog, oRaiz, oCoseno, oSeno, oTangente
    };

    /**
     * Propiedad que define como se agrupan los operadores dependiendo su prioridad
     */
    private static enum Asociatividad {
        asoIzquierda, asoDerecha
    }

    /**
     * Metodo precedence el cual identifica el orden de las operaciones
     * @param op es el operador encontrado
     * @return orden para desarrollar las operaciones
     */
    private int precedence(Operador op) {
        switch (op) {
            case oSuma:
                return 2;
            case oResta:
                return 2;
            case oMod:
                return 3;
            case oMultiplicacion:
                return 4;
            case oDivision:
                return 4;
            case oPotencia:
                return 5;
            case oFactorial:
                return 6;
            case oRaiz:
                return 6;
            case oLog:
                return 6;
            case oCoseno:
                return 6;
            case oSeno:
                return 6;
            case oTangente:
                return 6;
            default:
                return 0;
        }
    }

    /**
     * Metodo associatity para saber que operacion realizar
     * @param op es el operador encontrado
     * @return enumeracion para realizar las operaciones
     */
    private Asociatividad asociatividad(Operador op) {
        switch (op) {
            case oMod:
            case oRaiz:
            case oLog:
            case oSeno:
            case oCoseno:
            case oTangente:
            case oPotencia:
            case oFactorial:
                return Asociatividad.asoDerecha;
            case oSuma:
            case oResta:
            case oMultiplicacion:
            case oDivision:
                return Asociatividad.asoIzquierda;
            default:
                return Asociatividad.asoIzquierda;
        }
    }

    /**
     * Metodo calcular descompone la cadena ingresada
     * @param cadena es la serie de operaciones ha realizar
     * @return resultado de las operaciones
     */
    @Override
    public double calcular(Cadena cadena) throws Exception {
        Stack pilaOperadores = new Stack<Operador>();
        Stack pilaPrincipal = new Stack<Object>();

        String cadenaOperacion = cadena.getDato();

        StringTokenizer strTok = new StringTokenizer(cadenaOperacion, tokensValidos, true);
        String tok="";
        int miBandera=1;
        String tokAnterior="";
        while (strTok.hasMoreTokens()) {


            if(!tok.equals(" ")) {
                tokAnterior = tok;
            }

            tok = strTok.nextToken();

            if(tok.equals(" ")){continue;}

            try {
                pilaPrincipal.push(miBandera*Double.parseDouble(tok));
                continue;
            } catch (NumberFormatException nfe) {
            }
            Operador op = tokenOperador(tok);

            miBandera=1;
            if( (tokenOperador(tokAnterior) != Operador.oInvalido && tokenOperador(tokAnterior) != Operador.oFactorial) || tokAnterior.equals("")){
                if(op==Operador.oResta){
                    miBandera=-1;
                    continue;
                }
            }

            if (op == Operador.oInvalido) {
                throw new Exception("Operador invalido " + tok );
            }
            if (op == Operador.oParenIz) {
                if((tokenOperador(tokAnterior) == Operador.oInvalido || tokenOperador(tokAnterior)== Operador.oFactorial) && !tokAnterior.equals("")){
                    throw new Exception("Ingreso de datos invalido, un signo valido debe colocarse previo a los parentesis.");
                }
                pilaOperadores.push(op);
                continue;
            }
            if (op == Operador.oParentDer) {
                boolean escapeLoop = false;
                while (!pilaOperadores.isEmpty()) {
                    Operador op1 = (Operador) pilaOperadores.pop();
                    if (op1 == Operador.oParenIz) {
                        escapeLoop = true;
                        break;
                    }
                    pilaPrincipal.push(op1);
                    if (pilaOperadores.isEmpty()) {
                        throw new Exception("Inconsistencia en parentesis");
                    }
                }
                if (escapeLoop = true) {
                    continue;
                }
            }

            if(op== Operador.oRaiz || op== Operador.oLog || op == Operador.oCoseno|| op == Operador.oSeno || op==Operador.oTangente){
                System.out.printf(tokAnterior);
                if((tokenOperador(tokAnterior) == Operador.oInvalido || tokenOperador(tokAnterior)== Operador.oFactorial) && !tokAnterior.equals("")){
                    throw new Exception("Entrada Invalida, revise los datos ingresados");
                }
            }

            if (asociatividad(op) == Asociatividad.asoIzquierda) {
                while (!pilaOperadores.isEmpty() && precedence(op) <= precedence((Operador) pilaOperadores.peek())) {
                    pilaPrincipal.push(pilaOperadores.pop());
                }
            } else if (asociatividad(op) == Asociatividad.asoDerecha) {
                while (!pilaOperadores.isEmpty() && precedence(op) < precedence((Operador) pilaOperadores.peek())) {
                    pilaPrincipal.push(pilaOperadores.pop());
                }
            }
            pilaOperadores.push(op);
        }
        while (!pilaOperadores.isEmpty()) {
            Operador op1 = (Operador) pilaOperadores.pop();
            if (op1 == Operador.oParenIz || op1 == Operador.oParentDer) {
                throw new Exception("Inconsistencia en parentesis");
            }
            pilaPrincipal.push(op1);
        }
        System.out.println(pilaPrincipal);
        if (pilaPrincipal.isEmpty()) {
            return 0.0;
        }
        System.out.println(pilaPrincipal);
        return evaluarCadena(pilaPrincipal);
    }

    /**
     * Metodo evaluarCadena es el que realiza las operaciones en orden
     * @param pilaPrincipal
     * @return resultado de las operaciones
     */
    private double evaluarCadena(Stack<Object> pilaPrincipal) throws Exception {
        Object obj = pilaPrincipal.pop();

        if (obj.getClass() == Double.class) {
            return ((Double) obj).doubleValue();
        }

        if (obj.getClass() == Operador.class) {
            Operador op = (Operador) obj;

            double a=0,b=0;
            try {
                a = evaluarCadena(pilaPrincipal);
                b = 0;
                if (op != Operador.oFactorial && op != Operador.oLog && op != Operador.oRaiz && op != Operador.oCoseno&& op != Operador.oSeno && op!=Operador.oTangente) {
                    b = evaluarCadena(pilaPrincipal);
                }
                System.out.println(a+" "+b);
            }catch(Exception ex){
                throw new Exception("Error en el ingreso de datos, revise los signos ingresados");
            }

            switch (op) {
                case oSuma:
                    return operacion.suma(a,b);
                case oResta:
                    return operacion.resta(b,a);
                case oMultiplicacion:
                    return operacion.multiplicacion(a,b);
                case oDivision:
                    return operacion.division(b,a);
                case oPotencia:
                    return operacion.potencia(b,a);
                case oFactorial:
                    return operacion.factorial(a);
                case oMod:
                    return operacion.mod(b,a);
                case oLog:
                    return operacion.logaritmo(a);
                case oRaiz:
                    return operacion.raiz(a);
                case oCoseno:
                    return operacion.coseno(a);
                case oSeno:
                    return operacion.seno(a);
                case oTangente:
                    return operacion.tangente(a);
                default:
                    throw new Exception("Operador Invalido");
            }
        }
        throw new Exception("Entrada no valida");
    }

    /**
     * Metodo tokenOperador el cual identifica la operacion segun el signo
     * @param tok contiene el signo encontrado en la cadena
     * @return  enumeracion de la operacion que se debe realizar
     */
    private Operador tokenOperador(String tok) {
        if (tok.contentEquals("+")) {
            return Operador.oSuma;
        }
        if (tok.contentEquals("-")) {
            return Operador.oResta;
        }
        if (tok.contentEquals("*")) {
            return Operador.oMultiplicacion;
        }
        if (tok.contentEquals("/")) {
            return Operador.oDivision;
        }
        if (tok.contentEquals("^")) {
            return Operador.oPotencia;
        }
        if (tok.contentEquals("!")) {
            return Operador.oFactorial;
        }
        if (tok.contentEquals("mod")) {
            return Operador.oMod;
        }
        if (tok.contentEquals("log")) {
            return Operador.oLog;
        }
        if (tok.contentEquals("sqrt")) {
            return Operador.oRaiz;
        }
        if (tok.contentEquals("cos")) {
            return Operador.oCoseno;
        }
        if (tok.contentEquals("sen")) {
            return Operador.oSeno;
        }
        if (tok.contentEquals("tan")) {
            return Operador.oTangente;
        }
        if (tok.contentEquals("(")) {
            return Operador.oParenIz;
        }
        if (tok.contentEquals(")")) {
            return Operador.oParentDer;
        }
        return Operador.oInvalido;
    }

    /**
     * Metodo mMas muestra el dato que aumenta en memoria
     * @param dato es el número para guardar en memoria
     */
    @Override
    public void mMas(double dato) {
        operacion.mMas(dato);
    }

    /**
     * Metodo mMenos muestra el dato a retirar de memoria
     * @param dato es el número para restar en memoria
     */
    @Override
    public void mMenos(double dato) {
        operacion.mMenos(dato);
    }

    /**
     * Metodo mR el cual muestra el resultado de memoria
     * @return dato que esta en memoria
     */
    @Override
    public double mR() {
        return operacion.mR();
    }

    /**
     * Metodo binario el cual muestra el binario de un numero
     * @param dato es el dato numerico
     * @return resultado binario
     */
    @Override
    public void binario(int dato) {
        presentador.mostrarResultado2(operacion.binario(dato));
    }

    /**
     * Metodo octal el cual muestra el octal de un numero
     * @param dato es el dato numerico
     * @return resultado octal
     */
    @Override
    public void octal(int dato) {
        presentador.mostrarResultado2(operacion.octal(dato));
    }

    /**
     * Metodo hexadecimal el cual muestra el hexadecimal de un numero
     * @param dato es el dato numerico
     * @return resultado hexadecimal
     */
    @Override
    public void hexadecimal(int dato) {
        presentador.mostrarResultado2(operacion.hexadecimal(dato));
    }

}
