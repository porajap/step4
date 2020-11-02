package core;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class WriteFile {

	public void OpenFile(String file_path,String Data) throws IOException{
		File file = new File(file_path);
		FileWriter writer;
		try {
			writer = new FileWriter(file, true);  //True = Append to file, false = Overwrite
			writer.write(Data+"\r\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
