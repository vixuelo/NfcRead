package es.madrid.nfcreader.Utils.Coder;

import com.google.gson.Gson;

import org.json.JSONObject;

import es.madrid.nfcreader.Utils.Perfil.Perfil;

/*
Clase encargada de codificar y descodificar la informacion suministrada
 */
public class CoderDecoder {
    Gson gson;
    /*
    Si el modo es true se codificará la cadena con el numero de caracteres que tenga dicha cadena
    P.E: "1"->(1 caracter)->ascii("1")+1->"2"
     */

    public String Coder(String contenido, boolean modo){
        int longitud = contenido.length();
        //Buffer para poder incluir las letras codificadas
        StringBuffer nuevoContenido = new StringBuffer();
        //for each para iterar el String
        for (char letra: contenido.toCharArray()) {
            //letra to ascii
            int ascii = (int)letra;
            //permutador de modo en un if de una sola linea
            ascii=(modo)?ascii+longitud:ascii-longitud;
            //ascii to letra
            char letraNueva = (char)ascii;
            //inclusion de las letras codificadas en el buffer
            nuevoContenido.append(letraNueva);

        }
    //Buffer convertido a String
    return nuevoContenido.toString();
    }
}

