package mvc;
import java.io.FilenameFilter;
import java.io.File;

public class Filtre implements FilenameFilter {
	String extensio;
	
	Filtre (String extensio){
		this.extensio = extensio;
	}

	public boolean accept(File dir, String name) {
		return name.endsWith(extensio);
	}
}
