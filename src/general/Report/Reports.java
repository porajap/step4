package general.Report;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.zkoss.util.media.AMedia;

import core.WriteFile;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class Reports{

	//Variable
	public String uName;
    public String uPwd;
	
	private connect.DBConn objConnection = new connect.DBConn();
	
	private String ProjectPath = null;
	private ArrayList<String> StrParameter = new ArrayList<String>();
	private String FileName;
	private String FileType;
	
	//Set
	
	public String getPahtProjectDir(){
		read();
		return ProjectPath;
	}
	
	public void setStrParameter(ArrayList<String> strParameter) {
		StrParameter = strParameter;
	}
	
	public String getFileType() {
		return FileType;
	}

	public void setFileType(String fileType) {
		FileType = fileType;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}
	//Report
	public AMedia doReport() throws SQLException {
		//Step 1: Check Parameter
		if(getFileName() == null || StrParameter.size() < 1)
			return null;
		
		//Step 2: Get Path
		read();
		String reportFileName = ProjectPath + "Report\\" + getFileName() + ".jrxml";
		System.out.println("reportFileName : "+ reportFileName );
		WriteFile wf = new WriteFile();
		try {
			wf.OpenFile(ProjectPath+"/Config/output.txt",reportFileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Connection conn = objConnection.getConnection(uName,uPwd);
		
		//Step 3: Execute Show Report
		try {
			//Map<String, String> parameters = new HashMap<String, String>();
			Map<String, Object> parameters = new HashMap<String, Object>();
			//Step 4: Put Parameter
			
			for(int i = 0; i < StrParameter.size() ;i += 2){
				System.out.println("11111111 : "+StrParameter.get(i) + " - " + StrParameter.get(i+1));
				parameters.put(StrParameter.get(i), StrParameter.get(i+1));
			}
			parameters.put("PATH", ProjectPath);
			parameters.put("SUBREPORT_DIR", ProjectPath.replace('\\', '\\')+"Report//");
			// Step 5: Selected Types
			System.out.println("2222222222 : "+ reportFileName );
			System.out.println("parameters : "+ parameters );
			System.out.println("conn : "+ conn );
			System.out.println("uName : "+ uName );
			System.out.println("uPwd : "+ uPwd );
			JasperReport jasperReport = JasperCompileManager.compileReport(reportFileName);
					
			JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, conn );	
			System.out.println("55555555555555 : ");
			AMedia media = null;

			if(FileType.trim().equals("PDF")){	
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				
				
				media = new AMedia("reports/" + getFileName() + ".jasper", "pdf","application/pdf", data);
//			}else if(FileType.trim().equals("XLS")){
//	 			ByteArrayOutputStream output = new ByteArrayOutputStream();			
//	 			JExcelApiExporter exporterXLS = new JExcelApiExporter();
//	 			
//	 			exporterXLS.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasperPrint);
//	 			exporterXLS.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, output); 
//	 			exporterXLS.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE); 
//	 			exporterXLS.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
//	 			exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//	 			exporterXLS.exportReport();
//	 			InputStream mediais = new ByteArrayInputStream(output.toByteArray());	
//	 			media = new AMedia(getFileName(), "xls", "application/vnd.ms-excel", mediais);
			}else{
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				JasperExportManager.exportReportToHtmlFile(jasperPrint, "C://sample_report.html" );   
				media = new AMedia(getFileName(), "html", "application/html", data);
			}
			
			return media;	
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			conn.close();
		}
		
		return null;
	}

	private void read() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        
			Document doc = docBuilder.parse(new File("C:\\Tomcat 7.0\\webapps\\PHP-Step4\\Config\\ProjectPath.xml"));
//			Document doc = docBuilder.parse(new File("/usr/local/tomcat7/webapps/PHP-Step4/Config/ProjectPath.xml"));
			
			doc.getDocumentElement().normalize();
			NodeList listOfRow = doc.getElementsByTagName("row");

			for (int s = 0; s < listOfRow.getLength(); s++) {
				Node firstNode = listOfRow.item(s);
				if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode;
					NodeList DriverList = firstElement.getElementsByTagName("Path");
					Element DriverElement = (Element) DriverList.item(0);
					NodeList textDriverList = DriverElement.getChildNodes();
					ProjectPath = ((Node) textDriverList.item(0)).getNodeValue().trim();
//					System.out.println( ProjectPath );
				}
			}

		} catch (SAXParseException err) {
		} catch (SAXException e) {
		} catch (Throwable t) {
		}
	}

}
