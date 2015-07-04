
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;

/**
 *
 * @author Tobia Giani, Salvo Cirinà
 */
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
        queryCreator("Topo Gigio", model);
        System.out.println("============================");
        queryDescription("Bank of Ireland to raise 3.4bn euros.", model);
    }

    //creo la query per cercare tramite creatore
    public static void queryCreator(String creator, Model model) {
        String queryString = "PREFIX dc:  <http://purl.org/dc/elements/1.1/> SELECT ?doc ?title "
                + "WHERE { ?doc dc:creator \"" + creator + "\" . ?doc dc:title ?title .}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            System.out.println("\nDocumenti di " + creator + "\n----");
            for (; results.hasNext();) {
                QuerySolution soln = results.nextSolution();
                RDFNode x = soln.get("doc");
                Literal l = soln.getLiteral("title");
                System.out.println("Document\n" + x + "\nTitle\n" + l + "\n-----");
            }
        } finally {
            qexec.close();
        }
    }

    //creo la query per cercare tramite descrizione
    public static void queryDescription(String title, Model model) {
        String queryString = "PREFIX dc:  <http://purl.org/dc/elements/1.1/> SELECT ?doc ?description "
                + "WHERE { ?doc dc:title \"" + title + "\" . ?doc dc:description ?description .}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            System.out.println("\nDocumento \"" + title + "\"\n----");
            for (; results.hasNext();) {
                QuerySolution soln = results.nextSolution();
                RDFNode x = soln.get("doc");
                Literal l = soln.getLiteral("description");
                System.out.println("Document\n" + x + "\nDescription\n" + l + "\n-----");
            }
        } finally {
            qexec.close();
        }
    }

}
