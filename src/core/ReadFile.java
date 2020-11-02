package core;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class ReadFile {

	public String OpenFile(String file_path) throws IOException{
		FileReader fr = new FileReader( file_path );
		BufferedReader textReader = new BufferedReader(fr);
		String textData = textReader.readLine();
		textReader.close();
		return textData;
	}
}
