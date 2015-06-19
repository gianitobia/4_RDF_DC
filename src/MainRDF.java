
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.Arrays;

public class MainRDF {

    public static void main(String[] args) {

        final File folder = new File("news_collection");
        ArrayList<File> files = listFilesForFolder(folder);

        Model model = ModelFactory.createDefaultModel();
        BufferedReader br = null;

        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> source = new ArrayList<>();
        ArrayList<String> creator = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();

        String sCurrentLine;
        try {
            for (int i = 0; i < files.size(); i++) {
                br = new BufferedReader(new FileReader(files.get(i)));
                name.add(files.get(i).getName());
                int j = 0;
                String text = "";
                String lastLine = "";
                while ((sCurrentLine = br.readLine()) != null) {
                    if (j >= 6) {
                        text += lastLine;
                    }
                    if (j == 4) {
                        title.add(sCurrentLine);
                    } else {
                        if (j == 6) {
                            description.add(sCurrentLine);
                        }

                        lastLine = sCurrentLine;
                    }
                    j++;
                }

                date.add(lastLine);

                source.add(text);

                if (i == 7 || i == 3) {
                    creator.add("Topo Gigio");
                } else {
                    creator.add("Tobia Giani");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Dizionario dizionario = new Dizionario(false, Dizionario.Lang.EN);
        dizionario.generaDizionario(source);
        String[] parole = dizionario.getDizionario();
        double[][] tfMatrix = calcolaTFMatrix(dizionario, source);
        for (int i = 0; i < files.size(); i++) {
            int[] index = best3(tfMatrix[i]);
            subject.add(parole[index[0]] + ", " + parole[index[1]] + ", " + parole[index[2]]);
        }

        for (int i = 0; i < files.size(); i++) {
            Resource r = model.createResource("http://somewhere/" + name.get(i));

            r.addProperty(DC.title, title.get(i));

            r.addProperty(DC.description, description.get(i));

            r.addProperty(DC.date, date.get(i));

            r.addProperty(DC.source, source.get(i));

            //TODO: cambiare text con le prime 3 parole della FV
            r.addProperty(DC.subject, subject.get(i));

            r.addProperty(DC.creator, creator.get(i));

            r.addProperty(DC.publisher, "BBC");
        }

        model.write(System.out);

        FileOutputStream f;

        try {
            f = new FileOutputStream("news_collection.rdf");
            model.write(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static ArrayList<File> listFilesForFolder(final File folder) {
        ArrayList<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }

    public static double[][] calcolaTFMatrix(Dizionario dict, ArrayList<String> source) {

        String[] parole;
        parole = dict.getDizionario();

        double[][] tf_matrix = new double[source.size()][parole.length];
        for (int i = 0; i < tf_matrix.length; i++) {
            for (int j = 0; j < tf_matrix[0].length; j++) {
                int[] docs = dict.getOccorrenze(parole[j]);
                double count = 0;
                for (int doc : docs) {
                    if (doc != 0) {
                        count++;
                    }
                }
                tf_matrix[i][j] = ((double) docs[i]) * Math.log(source.size() / count);
            }
        }
        return tf_matrix;
    }

    private static int[] best3(double[] values) {
        int[] index = new int[3];
        double max = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
                index[2] = index[1];
                index[1] = index[0];
                index[0] = i;
            }
        }
        return index;
    }

}
