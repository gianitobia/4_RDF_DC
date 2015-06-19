
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.InputStream;

public class MainSparql {

    public static void main(String[] args) {

        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open("news_collection.rdf");
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: news_collection.rdf not found");
        }

        // read the RDF/XML file
        model.read(in, null);

		// write it to standard out
        //model.write(System.out);
        Selector selector = new SimpleSelector(null, DC.creator, "Topo Gigio");

    }

}
