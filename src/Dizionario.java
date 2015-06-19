
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Dizionario {

    static public enum Lang {

        EN, IT
    };

    static HashMap<String, int[]> dizionario;
    static private Tokenizer_EN tokenizerEN;

    // flag per effettuare la tokenizzazione italiana tramite babelnet id's
    // piuttosto che
    // lemmi
    static private boolean babel;

    public Dizionario(boolean flag, Lang language) {
        babel = flag;
        dizionario = new HashMap<>();

        tokenizerEN = new Tokenizer_EN();
    }

    public void generaDizionario(ArrayList<String> text) {
        for (int i = 0; i < text.size(); i++) {
            ArrayList<String> testiAnalizzati = tokenizerEN.analizzaTesto(text.get(i));
            for (String parola : testiAnalizzati) {
                if (!"".equals(parola)) {
                    if (dizionario.containsKey(parola)) {
                        int[] documenti = dizionario.get(parola);
                        documenti[i]++;
                        dizionario.put(parola, documenti);
                    } else {
                        int[] documenti = new int[text.size()];
                        documenti[i] = 1;
                        dizionario.put(parola, documenti);
                    }
                }
            }
        }
    }

    // restituisce il dizionario in un array di stringhe
    public String[] getDizionario() {
        return dizionario.keySet().toArray(
                new String[dizionario.keySet().size()]);
    }

    public void addToDizionario(String linea, int[] valore) {
        dizionario.put(linea, valore);
    }

    // restituisce il numero di occorrenze della parola in ogni documento
    public int[] getOccorrenze(String parola) {
        return dizionario.get(parola);
    }

}
