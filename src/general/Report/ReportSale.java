package general.Report;

import general.DateTime;
import general.Number;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import connect.SqlSelection;

public class ReportSale extends Borderlayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2946044092657050866L;
	
	public String SessionUserCode, 
	Branch_Code,
	Branch_Name,
    Branch_ID;
	
	private ResultSet rs = null;
	private String BranchCode = null;
	private String BranchName = null;
	private String xSearch = null;
	private String CgCode = null;
	private String CgM = null;
	private String CgX = null;
	private String CgS = null;
	private String sDate = null;
	private String eDate = null;
	private String xUsername = null;
	private String xPassword = null;
	private double xTotal = 0;
	private String xSumTotal = null;
	private String Date_Time = null;
	
	private String[] StrDataCombo = {
		//0.ComponentName, 1.TableName, 2.ID, 3.Name
		"ComboboxWarehouse", "wh_warehouse", "Wh_Code", "Wh_Name",
		"Combobox_MainCategory", "item_category_main", "CategoryMain_Code", "CategoryMain_Name",
		"ComboboxBranch", "branch", "Branch_Code", "Branch_Name",
	};
	
	public void onCreate() throws Exception {
		Session session = Sessions.getCurrent();

		if (session.getAttribute("UserCode") == null) {
			Executions.sendRedirect("/Login.zul");
		} else {
			SessionUserCode = (String)session.getAttribute("UserCode");
			Branch_Code = (String)session.getAttribute("Branch_Code");
			Branch_Name = (String)session.getAttribute("Branch_Name");
	        Branch_ID = (String)session.getAttribute("Branch_ID");
	        xUsername = (String)session.getAttribute("xUsrName");
	    	xPassword = (String)session.getAttribute("xPassword");
			init();
        }
    }

	public void init() throws Exception{
		((Borderlayout) getFellow("i")).setVisible(true);
		
		//Default Date
		((Datebox) getFellow("DateboxSDocDate")).setText(DateTime.getDateTimeNow());
		((Datebox) getFellow("DateboxEDocDate")).setText(DateTime.getDateTimeNow());
				
		// Difine
		addData(0, false);
		defindPrint();
		//Set Default
		((Combobox) getFellow("ComboboxBranch")).setText(Branch_Name);
	}
	
    private void addData(int StartIndex, boolean isBreak) throws Exception {
    	//0.ComponentName, 1.TableName, 2.ID, 3.Name
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        try {
            for (int i = (StartIndex * 4); i < StrDataCombo.length; i = i + 4) {
                rs = null;
                String 
                
                StrSql =  "SELECT  " 	+ StrDataCombo[i + 2] + ","
                						+ StrDataCombo[i + 3] + " ";
                
                StrSql += "FROM  " 		+ StrDataCombo[i + 1] + " ";

                StrSql += "ORDER BY " 	+ StrDataCombo[i + 2] + " ASC ";

                rs = sqlsel.getReSultSQL(StrSql);

                ((Combobox) getFellow(StrDataCombo[i])).getItems().clear();

                while (rs.next()) {
                    Comboitem citem = new Comboitem();
                    citem.setLabel(rs.getString(StrDataCombo[i + 3]));
                    citem.setValue(rs.getString(StrDataCombo[i + 2]));
                    
                    ((Combobox) getFellow(StrDataCombo[i])).appendChild(citem);
                }
                
                if(isBreak)
                	break;
            }   
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }

            sqlsel.closeConnection();
        }
    }
	
	private String getSqlBalance(){
		String Sql =  	"SELECT " +
				"item.BarCode, " +
				"item.NameTH, " +
				"SUM(sale_detail.Qty) AS Qty, " +
				"item_unit.Unit_Name, " +
				"wh_inventory_balance.Qty AS StockQty, " +
				"branch.Branch_Name " +

				"FROM sale " +
				
				"INNER JOIN sale_detail ON sale.DocNo = sale_detail.DocNo " +
				"INNER JOIN item ON sale_detail.Item_Code = item.Item_Code " +
				"INNER JOIN item_unit ON item.Unit_Code = item_unit.Unit_Code " +
				"INNER JOIN wh_inventory_balance ON wh_inventory_balance.Item_Code = item.Item_Code " +
				"INNER JOIN branch ON wh_inventory_balance.Branch_Code = branch.Branch_Code " +
				
				"WHERE " +
				"(sale.DocDate >= '" + DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() ) + "' " +
				"AND sale.DocDate <= '" + DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() ) + "') " +
				"AND (wh_inventory_balance.Balance_Date >= '" + DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() ) + "' " +
				"AND wh_inventory_balance.Balance_Date <= '" + DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() ) + "') " +
				"AND branch.Branch_Code = " + (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue() + " " + 
				"GROUP BY item.Item_Code" ;
			
	
		System.out.println(Sql);
		
		return Sql;
	}
	
	//Define
	public void defineCategory(Combobox cbb, String id) throws ComponentNotFoundException, Exception{						
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{
			
			String Sql =  	"SELECT  	 Category_Code, " +
										"Category_Name  " +
						
							"FROM 		item_category " +
							
							"WHERE		CategoryMain_Code = '" + id + "' " +

							"ORDER BY 	Category_Name ASC ";
			

			rs = sqlsel.getReSultSQL(Sql);

			cbb.getItems().clear();

            while (rs.next()) {
                Comboitem citem = new Comboitem();
                citem.setLabel(rs.getString("Category_Name"));
                citem.setValue(rs.getString("Category_Code"));
                cbb.appendChild(citem);
            }
		}catch(Exception e){
			
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}
	
	public void defineSubCategory(Combobox cbb, String id) throws ComponentNotFoundException, Exception{						
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{
			
			String Sql =  	"SELECT  	 CategorySub_Code, " +
										"CategorySub_Name  " +
						
							"FROM 		item_category_sub " +
							
							"WHERE		Category_Code = '" + id + "' " +

							"ORDER BY 	CategorySub_Name ASC ";
			
			//System.out.println(Sql);
				  
			rs = sqlsel.getReSultSQL(Sql);

			cbb.getItems().clear();

            while (rs.next()) {
                Comboitem citem = new Comboitem();
                citem.setLabel(rs.getString("CategorySub_Name"));
                citem.setValue(rs.getString("CategorySub_Code"));
                cbb.appendChild(citem);
            }
		}catch(Exception e){
			
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}
	
	public void onDisplay() throws Exception{
		if( ! isSelected( ((Combobox) getFellow("ComboboxBranch")) ) ){
			Messagebox.show("ยังไม่ได้เลือกสาขา");
    		return;
		}

		if(((Combobox) getFellow("ComboboxSelectReport")).getSelectedItem().getValue().equals("ReportSale01")){
			Format01();
			((Listbox) getFellow("ListboxBalance")).setVisible( true );
			((Listbox) getFellow("ListboxBill")).setVisible( false );
			((Listbox) getFellow("ListboxDocItem")).setVisible( false );
		}else if(((Combobox) getFellow("ComboboxSelectReport")).getSelectedItem().getValue().equals("ReportSale04")){
			Format04();
			((Listbox) getFellow("ListboxDocItem")).setVisible( true );
			((Listbox) getFellow("ListboxBalance")).setVisible( false );
			((Listbox) getFellow("ListboxBill")).setVisible( false );
		}else{
			Format02();
			((Listbox) getFellow("ListboxBalance")).setVisible( false );
			((Listbox) getFellow("ListboxBill")).setVisible( true );
			((Listbox) getFellow("ListboxDocItem")).setVisible( false );
		}
	}
	
	private void Format01() throws Exception{
		
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{
			rs = sqlsel.getReSultSQL(getSqlBalance());
			sDate = DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() );
			eDate = DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() );
			BranchCode = (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue();
			
			((Listbox) getFellow("ListboxBalance")).getItems().clear();
				
			while(rs.next()){
				Listitem list = new Listitem();
				list.appendChild(new Listcell(rs.getString("BarCode")));
				list.appendChild(new Listcell(rs.getString("NameTH")));
				list.appendChild(new Listcell(rs.getString("Unit_Name")));
				list.appendChild(new Listcell(Number.addComma2d( rs.getString("Qty") )));
				list.appendChild(new Listcell(rs.getString("Branch_Name")));
				
				//System.out.println( sDate +" == "+ eDate );
				
				if( sDate.equals( eDate ) ){
					list.appendChild(new Listcell(Number.addComma2d( rs.getDouble("StockQty") )));
				}else{
					list.appendChild(new Listcell(""));
				}
				
				
				((Listbox) getFellow("ListboxBalance")).appendChild(list);
			}
			((Label) getFellow("xSum")).setVisible( false );
			((Textbox) getFellow("TextboxSumTotal")).setVisible( false );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}
	
	private String getSqlBilltoDay(){
		String Sql =  	"SELECT " +
				"sale.DocNo," +
				"sale.DocDate," +
				"sale.Productcost," +
				"sale.DiscountBath," +
				"sale.TaxCost," +
				"sale.Productcosttotal," +
				"sale.TaxCostTotal," +
				"sale.PayMoney," +
				"sale.Change," +
				"sale.Total " +
				"FROM sale " +
				"WHERE " +
				"(sale.DocDate BETWEEN '" + DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() ) + "' " +
				"AND '" + DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() ) + "') " +
				"AND sale.Branch_Code = " + (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue() + " " +
				"AND sale.IsCancel = 0 " +
				"ORDER BY sale.DocNo ASC";
	
		System.out.println(Sql);
		return Sql;
	}

	private void Format02() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{

			rs = sqlsel.getReSultSQL( getSqlBilltoDay() );
			sDate = DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() );
			eDate = DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() );
			BranchCode = (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue();
			
			((Listbox) getFellow("ListboxBill")).getItems().clear();
			Listitem list = new Listitem();
			
			while(rs.next()){
				list = new Listitem();
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(Number.addComma2d(rs.getString("Productcost"))));
				list.appendChild(new Listcell(Number.addComma2d(rs.getString("DiscountBath"))));
				list.appendChild(new Listcell(Number.addComma2d(rs.getString("TaxCost"))));
				list.appendChild(new Listcell(Number.addComma2d( rs.getDouble("Total") )));
				list.appendChild(new Listcell(Number.addComma2d( rs.getDouble("PayMoney") )));
				list.appendChild(new Listcell(Number.addComma2d( rs.getDouble("Change") )));
				xTotal = xTotal +  Number.unCommaReturnDouble( String.valueOf( rs.getDouble("Total")) );
				((Listbox) getFellow("ListboxBill")).appendChild(list);
			}
			((Textbox) getFellow("TextboxSumTotal")).setText( Number.addComma2d( xTotal ) );
			((Label) getFellow("xSum")).setVisible( true );
			((Textbox) getFellow("TextboxSumTotal")).setVisible( true );
			xSumTotal = Number.addComma2d( xTotal );
			xTotal = 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}	
	
	private String getSqlBillItemDetail(){
		String SubCgCode = "%";
		String Sql =  	"SELECT " +
				"sale.DocNo," +
				"sale.DocDate," +
				"item.BarCode," +
				"item.NameTH," +
				"item_unit.Unit_Name," +
				"sale_detail.Qty," +
				"sale_detail.Price," +
				"sale_detail.Total " +

				"FROM sale " +
				
				"INNER JOIN sale_detail ON sale.DocNo = sale_detail.DocNo " +
				"INNER JOIN item ON sale_detail.Item_Code = item.Item_Code " +
				"INNER JOIN item_unit ON item.Unit_Code = item_unit.Unit_Code " +
				"LEFT JOIN 	item_category_sub " +
				"ON			item_category_sub.CategorySub_Code = item.CategorySub_Code " +
				"LEFT JOIN 	item_category " +
				"ON			item_category.Category_Code = item_category_sub.Category_Code " +
				"LEFT JOIN 	item_category_main " +
				"ON			item_category_main.CategoryMain_Code = item_category.CategoryMain_Code "+
				
				
				"WHERE " +
				"(sale.Modify_Date BETWEEN '" + DateTime.convertDateTime( ((Datebox) getFellow("DateboxSDocDate")).getText() ) + "' " +
				"AND '" + DateTime.convertDateTime( ((Datebox) getFellow("DateboxEDocDate")).getText() ) + "') " +
				"AND sale.Branch_Code = " + (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue() + " " ;

				String txt = ((Textbox) getFellow("TextboxItemCode")).getText();
				xSearch = txt.replace(" ", "%") + "%";
				
				Sql +=		"AND	( item.NameTH LIKE '%" + txt.replace(" ", "%") + "%' " ;
				Sql +=		"OR		item.NameEN LIKE '%" + txt.replace(" ", "%") + "%' " ;
				Sql +=		"OR		item.Barcode LIKE '%" + txt.replace(" ", "%") + "%' ) " ;
				
				if( isSelected(((Combobox) getFellow("Combobox_MainCategory")) ) ){
					SubCgCode = (String)((Combobox) getFellow("Combobox_MainCategory")).getSelectedItem().getValue();
					if( isSelected(((Combobox) getFellow("Combobox_Category")) ) ){
						SubCgCode = (String)((Combobox) getFellow("Combobox_Category")).getSelectedItem().getValue();
						if( isSelected( ((Combobox) getFellow("Combobox_SubCategory")) ) ){
							SubCgCode = (String)((Combobox) getFellow("Combobox_SubCategory")).getSelectedItem().getValue();
						}
					}
				}
				
				CgCode = SubCgCode + "%";
				
				Sql +=	"AND	item.CategorySub_Code LIKE '" + SubCgCode + "%' ";
				
				Sql +=	"AND sale.IsCancel = 0 ";
				Sql +=	"AND sale_detail.IsCancel = 0 " +
				
				"ORDER BY item.NameTH,sale.DocNo ASC";
				System.out.println(Sql);
		return Sql;
	}
	
	private void Format04() throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		try{
			rs = sqlsel.getReSultSQL(getSqlBillItemDetail());
			sDate = DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() );
			eDate = DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() );
			BranchCode = (String)((Combobox) getFellow("ComboboxBranch")).getSelectedItem().getValue();

			((Listbox) getFellow("ListboxDocItem")).getItems().clear();
				
			while(rs.next()){
				Listitem list = new Listitem();
				list.appendChild(new Listcell(rs.getString("DocNo")));
				list.appendChild(new Listcell(rs.getString("DocDate")));
				list.appendChild(new Listcell(rs.getString("BarCode")));
				list.appendChild(new Listcell(rs.getString("NameTH")));
				list.appendChild(new Listcell(rs.getString("Unit_Name")));
				list.appendChild(new Listcell(Number.addComma0d( rs.getString("Qty") )));
				list.appendChild(new Listcell(Number.addComma2d( rs.getString("Price") )));
				list.appendChild(new Listcell(Number.addComma2d( rs.getString("Total") )));
				
				((Listbox) getFellow("ListboxDocItem")).appendChild(list);
			}
			
			((Label) getFellow("xSum")).setVisible( false );
			((Textbox) getFellow("TextboxSumTotal")).setVisible( false );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}		
	
	// Utility
	public boolean isSelected(Combobox cbb){
		try {
			return cbb.getSelectedItem().getValue() != null; 
	  	} catch (Exception e) {
	        return false;
	   	}
	}
	
	/*
	 * ===================== Close windows =====================
	 */
	public void openWindow(String StrWindow) throws Exception {
		((Window) getFellow(StrWindow)).setVisible(true);
		((Window) getFellow(StrWindow)).setFocus(true);
		((Window) getFellow(StrWindow)).setPosition("center");
		((Window) getFellow(StrWindow)).setMode("modal");
	}
	
	public void closeWindow(String StrWindow) throws Exception {
		((Window) getFellow(StrWindow)).setMode("embedded");
		((Window) getFellow(StrWindow)).setVisible(false);
		((Window) getFellow(StrWindow)).setFocus(false);
	}
	//Report
	public void onPrint() throws Exception{
		String StrReportFile = (String)((Combobox) getFellow("ComboboxSelectReport")).getSelectedItem().getValue();
			
		//Call Report
		Reports r = new Reports();
		r.uName = xUsername;
		r.uPwd = xPassword;
		r.setFileName(StrReportFile);

		r.setFileType("PDF");
		
		Date_Time =  DateTime.getDate_Format("dd MMM yyyy HH.mm.ss"); //  DateTime.getDateNow();
		System.out.println( StrReportFile + " == ReportSale06" );
		
		if(StrReportFile.equals("ReportSale06")){
			sDate = DateTime.convertDate( ((Datebox) getFellow("DateboxSDocDate")).getText() );
			eDate = DateTime.convertDate( ((Datebox) getFellow("DateboxEDocDate")).getText() );
		}else{
			sDate = DateTime.convertDateTime( ((Datebox) getFellow("DateboxSDocDate")).getText() );
			eDate = DateTime.convertDateTime( ((Datebox) getFellow("DateboxEDocDate")).getText() );
		}
		CgM = (String)((Combobox) getFellow("Combobox_MainCategory")).getText();
		CgX = (String)((Combobox) getFellow("Combobox_Category")).getText();
		CgS = (String)((Combobox) getFellow("Combobox_SubCategory")).getText();
		BranchName = (String)((Combobox) getFellow("ComboboxBranch")).getText();
		System.out.println( sDate +" == "+ eDate );
		
		r.setStrParameter(new ArrayList<String>() {
			private static final long serialVersionUID = -2892797364135239183L;
			{
				add("BranchName");
				add(BranchName);
				add("BranchCode");
				add(BranchCode);
				add("sDate");
				add(sDate);
				add("eDate");
				add(eDate);
				add("xSumTotal");
				add(xSumTotal);
				add("Date_Time");
				add(Date_Time);
				add("xSearch");
				add(xSearch);
				add("CgCode");
				add(CgCode);
				add("CgM");
				add(CgM);
				add("CgX");
				add(CgX);
				add("CgS");
				add(CgS);
			}
		});

		//Open Window
		Iframe iframe = new Iframe();
		iframe.setWidth("100%");
		iframe.setHeight("100%");
		iframe.setContent(r.doReport());
			
		((Window) getFellow("Windows")).getChildren().clear();
		((Window) getFellow("Windows")).setTitle("รายงานค่าบริการ");
		((Window) getFellow("Windows")).setWidth("100%");
		((Window) getFellow("Windows")).setHeight("100%");
		((Window) getFellow("Windows")).appendChild(iframe);
			
		openWindow("Windows");
	}
	
	private void defindPrint() throws Exception{
		((Combobox) getFellow("ComboboxSelectReport")).getItems().clear();
		((Combobox) getFellow("ComboboxSelectReport")).setText("");
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String df = null;
		try{	
			rs = sqlsel.getReSultSQL("SELECT cd_print.Print_Name,cd_print.Print_File FROM cd_print WHERE xDefault = 1 AND Section = 1");
			if( rs.next() ) df = rs.getString( "Print_Name" );
			
			
			rs = sqlsel.getReSultSQL("SELECT cd_print.Print_Name,cd_print.Print_File FROM cd_print WHERE Section = 1");
			
			while( rs.next() ){
				Comboitem citem = new Comboitem();
		        citem.setLabel( rs.getString( "Print_Name" ) );
		        citem.setValue( rs.getString( "Print_File" ) );
		        
		        ((Combobox) getFellow("ComboboxSelectReport")).appendChild( citem ); 
			}
			
			((Combobox) getFellow("ComboboxSelectReport")).setText( df );
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
		
	}	
}
