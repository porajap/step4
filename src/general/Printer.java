package general;

import java.io.ByteArrayInputStream;  
import java.io.InputStream;  
import java.util.ArrayList;
   
import javax.print.Doc;  
import javax.print.DocFlavor;  
import javax.print.DocPrintJob;  
import javax.print.PrintService;  
import javax.print.PrintServiceLookup;  
import javax.print.SimpleDoc;  

import org.zkoss.zul.Messagebox;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Printer {
	public int xPage = 0;
	public int nPage = 0;
	public void executePrint(String sample){  
		//data for printing  
		String data = sample;  
		  
		try {  
			//locate printer  
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
			System.out.println("Printer online: "+printService);  
			Messagebox.show("Printer online: "+printService);
			//create a print job  
			DocPrintJob job = printService.createPrintJob();  
			  
			//define the format of print document  
			InputStream is = new ByteArrayInputStream(data.getBytes());  
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
			  
			//print the data  
			Doc doc = new SimpleDoc(is, flavor, null);  
			job.print(doc, null);  
			  
			is.close();  
			System.out.println("Printing Done!!");  
			  
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
	}
	
	public void PrintBill01(final ArrayList<String> xHeader,
			final ArrayList<String> xBodyRow_No,
			final ArrayList<String> xBodyBarcode,
			final ArrayList<String> xBodyNameTH,
			final ArrayList<String> xBodyQty,
			final ArrayList<String> xBodyPrice,
			final ArrayList<String> xBodyTotal
			){
	      
		   //try {
			   	  PrinterJob pjob = PrinterJob.getPrinterJob();
			      //Messagebox.show( pjob.getPrintService().getName() );
			      //System.out.println( pjob.getPrintService().getName() );
			      pjob.setJobName("Printout");
			      pjob.setCopies(1);
			      //Messagebox.show("2");
			      PageFormat pf = pjob.defaultPage();
			      
			      
			      int chkPage = (xBodyRow_No.size()%20);
			      nPage = (xBodyRow_No.size()/20);
			      //Messagebox.show("3");
			      //System.out.println(chkPage + " : " + xBodyRow_No.size() + " : " + nPage );
			      if(chkPage > 0)
			    	  nPage++;
			      
			      //System.out.println(nPage + "|" + chkPage);
			      //Messagebox.show("4");
			      for(int i = 0;i < nPage;i++){
			    	  xPage = i;
				      Paper paper = new Paper();
				      double margin = 5; // half inch
				      paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight()
				          - margin * 2);
				      pf.setPaper(paper);
	
				      pjob.setPrintable(new Printable() {
				        public int print(Graphics pg, PageFormat pf, int pageNum) {
				          if (pageNum > 0) // we only print one page
				            return Printable.NO_SUCH_PAGE; // ie., end of job

				          pg.setFont(new Font("Angsana New", Font.PLAIN, 24));
				          pg.drawString("ใบส่งของชั่วคราว", 150, 30);
				          pg.setFont(new Font("Angsana New", Font.PLAIN, 18));
				          pg.drawString((xPage+1) + "/" + nPage, 385, 30);
				          pg.drawString("ฝ้ายเบเกอรี่", 185, 50);
				          pg.setFont(new Font("Angsana New", Font.PLAIN, 16));
				          pg.drawString("106 ถ.อินทวโรรส ต.ศรีภูมิ อ.เมือง เชียงใหม่ 50200", 102, 65);
				          pg.drawString("โทร. 053-222828 / 053-212672 แฟกซ์. 053-416157", 100, 80);
				          pg.drawString("Email : faibakerycm@gmail.com", 143, 95);
				          pg.setFont(new Font("Angsana New", Font.PLAIN, 16));
				          pg.drawLine(10, 105, 230, 105);pg.drawLine(235, 105, 410, 105);
				          pg.drawLine(10, 105, 10, 180);pg.drawLine(235, 105, 235, 180);
				          pg.drawLine(230, 105, 230, 180);pg.drawLine(410, 105, 410, 180);
				          pg.drawLine(10, 180, 230, 180);pg.drawLine(235, 180, 410, 180);
				          //============================
				          pg.drawString("ลูกค้า", 15, 120);pg.drawString( xHeader.get(0).toString() , 45, 120);
				          pg.drawString("ที่อยู่", 15, 137);pg.drawString( xHeader.get(1).toString() , 45, 137);
				          pg.drawString("โทร", 15, 171);pg.drawString( xHeader.get(4).toString() , 45, 171);
				          //============================
				          pg.drawString("เลขที่เอกสาร", 240, 120);pg.drawString( xHeader.get(5).toString() , 310, 120);
				          pg.drawString("วันที่เอกสาร", 240, 137);pg.drawString( xHeader.get(6).toString() , 310, 137);
				          pg.drawString("ส่วนลด (%)", 240, 154);pg.drawString( xHeader.get(8).toString() , 310, 154);
				          pg.drawString("ส่วนลด (บาท)", 240, 171);pg.drawString( xHeader.get(9).toString() , 310, 171);
				          //============================
				          //========== Body ============
				          //============================
				          int xLine = 195;	//Start Line
				          int hLine = 15;	//Height Line
				          int chkPage = 0;
				          int Cnt = xBodyRow_No.size();
				          //System.out.println(xPage);
				          chkPage = xPage * 20;
				          //System.out.println("***" + (Cnt%20));
				          
				          if((Cnt - chkPage) > 20 ) 
				        	  Cnt = 20;
				          else
				        	  Cnt = Cnt - chkPage;
				          
				          for(int j = 0;j<Cnt;j++){
				        	    System.out.println("*" + j);
				        	  	if(j<20){
				        		  pg.drawString(xBodyRow_No.get((j+chkPage)), 15, (xLine+(hLine*j)));pg.drawString(xBodyBarcode.get(j+chkPage), 45, (xLine+(hLine*j)));pg.drawString(xBodyNameTH.get(j+chkPage), 115, (xLine+(hLine*j)));
				        		  pg.setFont(new Font("Angsana New", Font.BOLD, 18));
				        		  pg.drawString(xBodyQty.get(j+chkPage), 285, (xLine+(hLine*j)));
				        		  pg.setFont(new Font("Angsana New", Font.PLAIN, 16));
				        		  pg.drawString(xBodyPrice.get(j+chkPage), 315, (xLine+(hLine*j)));pg.drawString(xBodyTotal.get(j+chkPage), 365, (xLine+(hLine*j)));
				        	  	}
				        	  	
				          }
				          //============================
				          if(Cnt < 20 ){
				        	  pg.drawString("รวมเป็นเงินทั้งสิ้น", 220, 500);
				        	  pg.drawString(Number.addComma2d( xHeader.get(10) ), 355, 500);
				        	  pg.drawLine(355, 502, 405, 502);
				        	  pg.drawLine(355, 504, 405, 504);
				          }
				          pg.drawString(xHeader.get(11), 25, 545);
				          pg.drawLine(25, 550, 125, 550);pg.drawLine(175, 550, 275, 550); pg.drawLine(300, 550, 400, 550);
				          pg.drawString("ผู้ออกเอกสาร", 50, 565);pg.drawString("ผู้ตรวจนับ", 205, 565);pg.drawString("ผู้รับสินค้า", 325, 565);
				          pg.drawString(xHeader.get(12), 58, 580);
				          //============================
				          return Printable.PAGE_EXISTS;
				        }
				      },pf);
	
				      //if (pjob.printDialog() == false) // choose printer
				      //  return;
				      
				    try {
						pjob.print();
					} catch (PrinterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			      }
			      
			      
			//} catch (PrinterException pe) {
			      //pe.printStackTrace();
			//      Messagebox.show( pe.getMessage() );
			//}
	}
	
	
}

