
import java.util.ArrayList;
import java.util.HashMap;

public class Dizionario {

    static HashMap<String, int[]> dizionario;
    static private Tokenizer_EN tokenizerEN;

    public Dizionario() {
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
