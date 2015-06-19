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


public class MainRDF {

	public static void main(String[] args) {
		
		final File folder = new File("news_collection");
		ArrayList<File> files = listFilesForFolder(folder);
		
		Model model = ModelFactory.createDefaultModel();
		BufferedReader br = null;
		 
		try {
			
			String sCurrentLine;
			List<Resource> risorse = new ArrayList<>();
			for(int i = 0; i<files.size() ; i++){
				br = new BufferedReader(new FileReader(files.get(i)));
				Resource r = model.createResource("http://somewhere/"+files.get(i).getName());
				int j=0;
				String text = "";
				String lastLine = "";
				while ((sCurrentLine = br.readLine()) != null) {
					if(j>=6)
						text += lastLine;
					if(j==4)
						r.addProperty(DC.title, sCurrentLine);
					else{
						if(j==6)
							r.addProperty(DC.description, sCurrentLine);
						
							lastLine = sCurrentLine;
					}	
					j++;
				}
				
				r.addProperty(DC.date,lastLine);
				
				r.addProperty(DC.source, text);
				
				if(i==7 || i==3)
					r.addProperty(DC.creator, "Topo Gigio");
				else
					r.addProperty(DC.creator, "Tobia Giani");
				
				r.addProperty(DC.publisher, "BBC");
				risorse.add(r);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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


}
