package core;

import general.Number;

import java.sql.ResultSet;
import java.util.ArrayList;

import connect.SqlOperation;
import connect.SqlSelection;

public class Calculate {

	private String xMM;
	private String xYY;
	private String xArea;
	private String xUsername;
	private String xPassword;
	private String xCusId = "";
	//	======================================	
	public void setMonth(String xMM) {
	    this.xMM = xMM;
	}
	
	public void setYear(String xYY) {
	    this.xYY = xYY;
	}
	
	public void setUsername(String xUsername) {
	    this.xUsername = xUsername;
	}
	
	public void setPassword(String xPassword) {
	    this.xPassword = xPassword;
	}
	
	//======================================
	public void setArea(String Area) throws Exception{
		xArea = Area;
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String CusId = "";
		try{
			String Sql = "SELECT  Cus_Code FROM customer WHERE ";
			
			if(Area.equals("PP2")){
				Sql += " AreaCode = 'P02' OR AreaCode = 'P021' OR AreaCode = 'P022' ";
			}else if(Area.equals("PP3")){
				Sql += " AreaCode = 'P03' OR AreaCode = 'P031' OR AreaCode = 'P032' ";
			}else if(Area.equals("PP5")){
				Sql += " AreaCode = 'P05' OR AreaCode = 'P051' OR AreaCode = 'P052' ";
			}else if(Area.equals("PP9")){
				Sql += " AreaCode = 'P09' OR AreaCode = 'P091' OR AreaCode = 'P092' ";
			}else{
				Sql += " AreaCode = '" + Area + "'";
			}
			// System.out.println( Sql );
			rs = sqlsel.getReSultSQL( Sql );
			while(rs.next()){
				CusId += "dallycall.Cus_Code = '" + rs.getString("Cus_Code") + "' OR ";
			}
			
			if(!Area.equals("")){
				if(CusId.length() > 5)
					CusId = CusId.substring(0, CusId.length()-3);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null) {
                rs.close();
            }
            sqlsel.closeConnection();
		}
		xCusId = CusId;
		//Update_Price( CusId );		
	}
	
	public void Update_Price(String CusId,String sDate,String eDate) throws Exception{
		if(xArea.equals("")) return;
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String  Sql = "";
        String SqlUpdate = "";
        double WelfareB = 0.00;
        double BPrice = 0.00;
        double APrice = 0.00;
        int OutLet = 0;
        
		try{
					Sql =  	"SELECT " +
							"SUM( dallycall.Qty ) AS Qty," +
							"SUM( dallycall.Price ) AS Price," +
							"SUM( dallycall.Total ) AS Total," +
							"SUM( dallycall.DiscountB ) AS DiscountB," +
							"SUM( dallycall.VatB ) AS VatB," +
							"SUM( dallycall.CmsB ) AS CmsB," +
							"SUM( dallycall.Total2 ) AS Total2," +
							"SUM( dallycall.WelfareP ) AS WelfareP," +
							"SUM( dallycall.WelfareB ) AS WelfareB " +
							"FROM dallycall " +
							"INNER JOIN item ON dallycall.ItemCode = item.Item_Code " +
							"INNER JOIN customer ON dallycall.Cus_Code = customer.Cus_Code " +
							"INNER JOIN sale ON dallycall.DocNo = sale.DocNo " +
							"INNER JOIN contact ON dallycall.CT_Code = contact.CT_Code " +
							"WHERE " +
							"dallycall.xDate BETWEEN '" + sDate + "' AND '" + eDate + "' " +
							"AND ( " + CusId + " ) " +
							"AND dallycall.AreaCode = '" + xArea + "' " +
							"AND dallycall.AreaCode2 = '-' " +
							"AND dallycall.IsCancel = 0 " +
							//"AND sale.isArea = 0 " + 
							"AND sale.IsCancel = 0";
							//System.out.println( Sql );
							
							rs = sqlsel.getReSultSQL( Sql );
						     while (rs.next()) {
						    	 WelfareB = rs.getDouble( "WelfareB" );
						    	 BPrice = rs.getDouble( "Total" );
						    	 APrice = rs.getDouble( "Total2" );
						     }
						     OutLet = getOutLet(CusId,sDate,eDate);
						     //System.out.println( OutLet  ); 
							 if(getAreaForYear()){
								 SqlUpdate = "UPDATE quarter_price_by_month SET ";
								 SqlUpdate += "Q"+xMM+"Sale = " + WelfareB;
					 			 SqlUpdate += ",BQ"+xMM+"Sale = " + BPrice;
					 			 SqlUpdate += ",AQ"+xMM+"Sale = " + APrice;
					 			 SqlUpdate += ",Q"+xMM+"Outlet = " + OutLet;
								 SqlUpdate += " WHERE xArea = '" + xArea + "' AND xYear = '" + xYY + "'";
							 }else{
								 SqlUpdate = "INSERT INTO quarter_price_by_month (xArea,xYear,Q" + xMM + "Sale,Q" + xMM + "Outlet,BQ" + xMM + "Sale,AQ" + xMM + "Sale) VALUES ('" + xArea + "','" + xYY + "'," + WelfareB + "," + OutLet + "," + BPrice + "," + APrice + ")";
							 }
							 //System.out.println( SqlUpdate );
							 Add_Data(SqlUpdate);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (rs != null) {
                rs.close();
            }
            sqlsel.closeConnection();
		}	
	}

	private boolean getAreaForYear() throws Exception{
		SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		ResultSet rs = null;
		int Cnt = 0;
		try{
			rs = sqlsel.getReSultSQL( "SELECT COUNT(*) AS CNT FROM quarter_price_by_month WHERE xArea = '" + xArea + "' AND xYear = '" + xYY + "'" );
		    //System.out.println( "SELECT COUNT(*) AS CNT FROM quarter_price_by_month WHERE xArea = '" + xArea + "' AND xYear = '" + xYY + "'" ); 
			while (rs.next()) {
		    	 Cnt = rs.getInt( "CNT" );
		     }
		     if(Cnt == 0 )
		    	 return false;
		     else
		    	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}finally{
    		if (rs != null) {
                rs.close();
            }
    		sqlsel.closeConnection();
    	}	
	}	

	public void UpdateMonthForYear() throws Exception{
		SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		ResultSet rs = null;
		String Sql = "";
		double SumMM = 0.00;
		double SumBB = 0.00;
		double SumAA = 0.00;
		try{
			Sql = "SELECT Q01Sale,Q02Sale,Q03Sale,Q04Sale,Q05Sale,Q06Sale,Q07Sale,Q08Sale,Q09Sale,Q10Sale,Q11Sale,Q12Sale,";
			Sql += "BQ01Sale,BQ02Sale,BQ03Sale,BQ04Sale,BQ05Sale,BQ06Sale,BQ07Sale,BQ08Sale,BQ09Sale,BQ10Sale,BQ11Sale,BQ12Sale,";
			Sql += "AQ01Sale,AQ02Sale,AQ03Sale,AQ04Sale,AQ05Sale,AQ06Sale,AQ07Sale,AQ08Sale,AQ09Sale,AQ10Sale,AQ11Sale,AQ12Sale ";
			
			if(xArea.equals("PP2")){
				Sql += "FROM quarter_price_by_month WHERE ( xArea = 'P02' OR xArea = 'P021' OR xArea = 'P022' ) AND xYear = '" + xYY + "'";
			}else if(xArea.equals("PP3")){
				Sql += "FROM quarter_price_by_month WHERE ( xArea = 'P03' OR xArea = 'P031' OR xArea = 'P032' ) AND xYear = '" + xYY + "'";
			}else if(xArea.equals("PP5")){
				Sql += "FROM quarter_price_by_month WHERE ( xArea = 'P05' OR xArea = 'P051' OR xArea = 'P052' ) AND xYear = '" + xYY + "'";
			}else if(xArea.equals("PP9")){
				Sql += "FROM quarter_price_by_month WHERE ( xArea = 'P09' OR xArea = 'P091' OR xArea = 'P092' ) AND xYear = '" + xYY + "'";
			}else{
				Sql += "FROM quarter_price_by_month WHERE xArea = '" + xArea + "' AND xYear = '" + xYY + "'";
			}

			// System.out.println( Sql ); 
			
			rs = sqlsel.getReSultSQL( Sql );
			while (rs.next()) {
				for(int i=1;i<13;i++){
					SumMM += rs.getDouble( "Q" + Number.addZero(i+"", "00") + "Sale" );
					SumBB += rs.getDouble( "BQ" + Number.addZero(i+"", "00") + "Sale" );
					SumAA += rs.getDouble( "AQ" + Number.addZero(i+"", "00") + "Sale" );
				}
		    }
			
			//==================================
			String sDate = "";
			String eDate = "";
			
			Sql = "SELECT sDate,eDate FROM perioddallycall " +
			   	"WHERE Year = '" + xYY + "' AND Month = '01'";
				     rs = sqlsel.getReSultSQL( Sql );
				     while (rs.next()) {
				     	sDate = rs.getString( "sDate" );
				     }
			Sql = "SELECT sDate,eDate FROM perioddallycall " +
				"WHERE Year = '" + xYY + "' AND Month = '" + xMM + "'";
					rs = sqlsel.getReSultSQL( Sql );
					while (rs.next()) {
						eDate = rs.getString( "eDate" );
					}			
			int OutLet = getOutLet(xCusId,sDate,eDate);
			//==================================
			Sql = "UPDATE quarter_price_by_month SET SumSale = " + Number.addZero(SumMM+"", "#0.00") + "," + 
			"BSumSale = " + Number.addZero(SumBB+"", "#0.00") + 
			",ASumSale = " + Number.addZero(SumAA+"", "#0.00") +
			",SumOutlet = " + OutLet;
			Sql += " WHERE xArea = '" + xArea + "' AND xYear = '" + xYY + "'";
			//System.out.println( "===================" );
			//System.out.println( Sql );
			//System.out.println( "===================" );
		    Add_Data(Sql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if (rs != null) {
                rs.close();
            }
    		sqlsel.closeConnection();
    	}
	}

	public int getOutLet(String CusId,String sDate,String eDate) throws Exception{
		SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
		ResultSet rs = null;
	
		int OutLet = 0;
		try{
			String Sql =  	"SELECT COUNT(*) AS Cnt " +
					"FROM dallycall " +
					"INNER JOIN item ON dallycall.ItemCode = item.Item_Code " +
					"INNER JOIN customer ON dallycall.Cus_Code = customer.Cus_Code " +
					"INNER JOIN sale ON dallycall.DocNo = sale.DocNo " +
					"INNER JOIN contact ON dallycall.CT_Code = contact.CT_Code " +
					"WHERE " +
					"dallycall.xDate BETWEEN '" + sDate + "' AND '" + eDate + "' " +
					"AND ( " + CusId + " ) " +
					"AND dallycall.AreaCode = '" + xArea + "' " +
					"AND dallycall.AreaCode2 = '-' " +
					"AND dallycall.IsCancel = 0 " +
					"AND sale.IsCancel = 0 " +
					"GROUP BY dallycall.Cus_Code";
					
			//System.out.println( Sql ); 

			rs = sqlsel.getReSultSQL( Sql );
			while (rs.next()) {
				OutLet++;
		    }

		    return OutLet;
    	}catch(Exception e){
    		e.printStackTrace();
    		return OutLet;
    	}finally{
    		if (rs != null) {
                rs.close();
            }
    		sqlsel.closeConnection();
    	}
	}	
	
	public String GetCus(String sDate,String eDate) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String getCus = "";
        String Sql = "SELECT Cus_Code,FName FROM customer WHERE ";
		try{
			
			if(xArea.equals("PP2")){
				Sql += " AreaCode = 'P02' OR AreaCode = 'P021' OR AreaCode = 'P022' ";
			}else if(xArea.equals("PP3")){
				Sql += " AreaCode = 'P03' OR AreaCode = 'P031' OR AreaCode = 'P032' ";
			}else if(xArea.equals("PP5")){
				Sql += " AreaCode = 'P05' OR AreaCode = 'P051' OR AreaCode = 'P052' ";
			}else if(xArea.equals("PP9")){
				Sql += " AreaCode = 'P09' OR AreaCode = 'P091' OR AreaCode = 'P092' ";
			}else{
				Sql += " AreaCode = '" + xArea + "'";
			}

			rs = sqlsel.getReSultSQL( Sql );
			//System.out.println( "SELECT Cus_Code,FName FROM customer WHERE AreaCode = '" + xArea + "'" );
			while(rs.next()){   
				getCus += "dallycall.Cus_Code = '" + rs.getString("Cus_Code") + "' OR ";
			}
			rs.close();
				
			rs = sqlsel.getReSultSQL(
					"SELECT customer.Cus_Code,customer.FName FROM sale " +
					"INNER JOIN customer ON sale.Cus_Code = customer.Cus_Code " +		
					"WHERE sale.AreaCode = '" + xArea + "' AND sale.IsCancel = 0 AND sale.isArea = 1 " +
					"AND sale.DocDate BETWEEN '" + sDate + "' AND '" + eDate + "' GROUP BY sale.Cus_Code"
			);
			while(rs.next()){
				getCus += "dallycall.Cus_Code = '" + rs.getString("Cus_Code") + "' OR ";
			}
			if(getCus.length() > 3)	if(!xArea.equals("")) getCus = getCus.substring(0,getCus.length()-3);
			//System.out.println( getCus );
			return getCus;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}
	
	public String GetCus( String xTable ) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String getCus = "";
        String Sql = "SELECT Cus_Code,FName FROM customer WHERE ";
		try{
			
			if(xArea.equals("PP2")){
				Sql += " AreaCode = 'P02' OR AreaCode = 'P021' OR AreaCode = 'P022' ";
			}else if(xArea.equals("PP3")){
				Sql += " AreaCode = 'P03' OR AreaCode = 'P031' OR AreaCode = 'P032' ";
			}else if(xArea.equals("PP5")){
				Sql += " AreaCode = 'P05' OR AreaCode = 'P051' OR AreaCode = 'P052' ";
			}else if(xArea.equals("PP9")){
				Sql += " AreaCode = 'P09' OR AreaCode = 'P091' OR AreaCode = 'P092' ";
			}else{
				Sql += " AreaCode = '" + xArea + "'";
			}

			rs = sqlsel.getReSultSQL( Sql );
			//System.out.println( "SELECT Cus_Code,FName FROM customer WHERE AreaCode = '" + xArea + "'" );
			while(rs.next()){   
				getCus += xTable + ".Cus_Code = '" + rs.getString("Cus_Code") + "' OR ";
			}
			rs.close();
			
			if(!xArea.equals("")) getCus = getCus.substring(0,getCus.length()-3);
			//System.out.println( getCus );
			return getCus;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
	}
	
	public void UpdateAreaSpecial( String IdDRC ) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String getCus = "";
        String Sql = "";
        String SqlVul = "";
		int id_ref = 0;
		int Cnt = 0;
        try{
			rs = sqlsel.getReSultSQL( "SELECT Count(*) AS Cnt FROM dallycall WHERE Id_Ref = " + IdDRC );
			while(rs.next()){
				id_ref = rs.getInt("Cnt");
			}
			
			//=== PB00 ===
			/*System.out.println( "SELECT * FROM dallycall " +
									 "INNER JOIN item ON dallycall.ItemCode = item.Item_Code  " +
									 "WHERE Id = " + IdDRC + " AND dallycall.WelfareP < 100 AND item.CategorySub_Code <> '160801' AND dallycall.IsCancel = 0" );
			*/
			rs = sqlsel.getReSultSQL("SELECT * FROM dallycall " +
									 "INNER JOIN item ON dallycall.ItemCode = item.Item_Code  " +
									 "WHERE Id = " + IdDRC + " AND dallycall.WelfareP < 100 AND item.CategorySub_Code <> '160801' AND dallycall.IsCancel = 0" );

			while(rs.next()){
				if( id_ref > 0 ){
					Sql = "UPDATE dallycall SET " +
							"dallycall.OrdererCode = '" + rs.getString("OrdererCode") + "'," +
							"dallycall.xDate = '" + rs.getString("xDate") + "'," +
							"dallycall.AreaCode = '" + rs.getString("AreaCode") + "'," +
							"dallycall.Cus_Code = '" + rs.getString("Cus_Code") + "'," +
							"dallycall.DocNo = '" + rs.getString("DocNo") + "'," +
							"dallycall.ItemCode = '" + rs.getString("ItemCode") + "'," +
							"dallycall.Qty = '" + rs.getString("Qty") + "'," +
							"dallycall.Qty_tmp = '" + rs.getString("Qty_tmp") + "'," +
							"dallycall.BonusItemCode = '" + rs.getString("BonusItemCode") + "'," +
							"dallycall.Price = '" + rs.getString("Price") + "'," +
							"dallycall.Total = '" + rs.getString("Total") + "'," +
							"dallycall.DiscountP = '" + rs.getString("DiscountP") + "'," +
							"dallycall.DiscountB = '" + rs.getString("DiscountB") + "'," +
							"dallycall.VatB = '" + rs.getString("VatB") + "'," +
							"dallycall.CmsP = '" + rs.getString("CmsP") + "'," +
							"dallycall.CmsB = '" + rs.getString("CmsB") + "'," +
							"dallycall.Total2 = '" + rs.getString("Total2") + "'," +
							"dallycall.WelfareP = '" + ( 100 - rs.getInt("WelfareP") ) + "'," +
							"dallycall.WelfareB = '" + rs.getString("WelfareB") + "'," +
							"dallycall.Total3 = '0.00'," +
							"dallycall.DallyP = '0.00'," +
							"dallycall.DallyB = '0.00'," +
							"dallycall.IsCancel = '" + rs.getString("IsCancel") + "'," +
							"dallycall.Branch_Code = '" + rs.getString("Branch_Code") + "'," +
							"dallycall.Modify_Code = '" + rs.getString("Modify_Code") + "'," +
							"dallycall.Modify_Date = '" + rs.getString("Modify_Date") + "'," +
							"dallycall.CT_Code = '" + rs.getString("CT_Code") + "'," +
							"dallycall.AreaCode2 = '" + rs.getString("AreaCode2") + "'," +
							"dallycall.Sale_Detail_ID = " + rs.getString("Sale_Detail_ID") + "," +
							"dallycall.Id_Ref = '" + rs.getString("Id_Ref") + "'ม " +
							"dallycall.isArea = " + rs.getString("isArea") + ", " +
							"dallycall.Sale_Status = " + rs.getString("Sale_Status") + " " +
							"WHERE dallycall.Id = " + IdDRC;							
				
				}else{
					Sql = "INSERT INTO dallycall ( " +
							"dallycall.OrdererCode,dallycall.xDate,dallycall.AreaCode,dallycall.Cus_Code,dallycall.DocNo," +
							"dallycall.ItemCode,dallycall.Qty,dallycall.Qty_tmp,dallycall.BonusItemCode,dallycall.Price," +
							"dallycall.Total,dallycall.DiscountP,dallycall.DiscountB,dallycall.VatB,dallycall.CmsP,dallycall.CmsB," +
							"dallycall.Total2,dallycall.WelfareP,dallycall.WelfareB,dallycall.Total3,dallycall.DallyP,dallycall.DallyB," +
							"dallycall.IsCancel,dallycall.Branch_Code,dallycall.Modify_Code,dallycall.Modify_Date,dallycall.CT_Code," +
							"dallycall.AreaCode2,dallycall.Sale_Detail_ID,dallycall.Id_Ref,dallycall.isArea,dallycall.Sale_Status )";
					Sql += " VALUES ";
					Sql += "( ";
					SqlVul += "'" + rs.getString("OrdererCode") + "',";
					SqlVul += "'" + rs.getString("xDate") + "',";
					SqlVul += "'" + rs.getString("Cus_Code") + "',";
					SqlVul += "'" + rs.getString("DocNo") + "',";
					SqlVul += "'" + rs.getString("ItemCode") + "',";
					SqlVul += "'" + rs.getString("Qty") + "',";
					SqlVul += "'" + rs.getString("Qty_tmp") + "',";
					SqlVul += "'" + rs.getString("BonusItemCode") + "',";
					SqlVul += "'" + rs.getString("Price") + "',";
					SqlVul += "'" + rs.getString("Total") + "',";
					SqlVul += "'" + rs.getString("DiscountP") + "',";
					SqlVul += "'" + rs.getString("DiscountB") + "',";
					SqlVul += "'" + rs.getString("VatB") + "',";
					SqlVul += "'" + rs.getString("CmsP") + "',";
					SqlVul += "'" + rs.getString("CmsB") + "',";
					SqlVul += "'" + rs.getString("Total2") + "',";
					SqlVul += "'" + rs.getString("WelfareP") + "',";
					SqlVul += "'" + rs.getString("WelfareB") + "',";
					SqlVul += "'" + rs.getString("Total3") + "',";
					SqlVul += "'" + rs.getString("DallyP") + "',";
					SqlVul += "'" + rs.getString("DallyB") + "',";
					SqlVul += "'" + rs.getString("IsCancel") + "',";
					SqlVul += "'" + rs.getString("Branch_Code") + "',";
					SqlVul += "'" + rs.getString("Modify_Code") + "',";
					SqlVul += "'" + rs.getString("Modify_Date") + "',";
					SqlVul += "'" + rs.getString("CT_Code") + "',";
					SqlVul += "'" + rs.getString("AreaCode2") + "',";
					SqlVul += "'" + rs.getString("Sale_Detail_ID") + "',";
					SqlVul += "'" + rs.getString("Id_Ref") + "',";
					SqlVul += "'" + rs.getString("isArea") + "',";
					SqlVul += "'" + rs.getString("Sale_Status") + "',";
					
					//System.out.println(rs.getString(4).substring(0,2) + " ::: " + rs.getString(4).substring(0,1) );
					if( rs.getString("AreaCode").substring(0,2).equals("BB") )	
						SqlVul += "'PB00'";
					else if( rs.getString("AreaCode").substring(0,1).equals("P") )
						SqlVul += "'PO00'";
					else
						SqlVul += "'*'";
					
					Sql += SqlVul + " )";
				}
				Cnt++;
			}
			System.out.println(1 + " : " + Sql );
			if(!Sql.equals("")) Add_Data( Sql );
			//=== POS00 ===
			/*System.out.println( "2 :: SELECT * FROM dallycall " +
					 "INNER JOIN item ON dallycall.ItemCode = item.Item_Code  " +
					 "WHERE Id = " + IdDRC + " AND dallycall.WelfareP < 100 AND item.CategorySub_Code = '160801'" );
			*/
			Sql = "";
			rs = sqlsel.getReSultSQL("SELECT * FROM dallycall " +
					 "INNER JOIN item ON dallycall.ItemCode = item.Item_Code  " +
					 "WHERE Id = " + IdDRC + " AND dallycall.WelfareP < 100 AND item.CategorySub_Code = '160801'" );
			while(rs.next()){
				if( id_ref > 0 ){
					Sql = "UPDATE dallycall SET " +
							"dallycall.OrdererCode = '" + rs.getString("OrdererCode") + "'," +
							"dallycall.xDate = '" + rs.getString("xDate") + "'," +
							"dallycall.AreaCode = '" + rs.getString("AreaCode") + "'," +
							"dallycall.Cus_Code = '" + rs.getString("Cus_Code") + "'," +
							"dallycall.DocNo = '" + rs.getString("DocNo") + "'," +
							"dallycall.ItemCode = '" + rs.getString("ItemCode") + "'," +
							"dallycall.Qty = '" + rs.getString("Qty") + "'," +
							"dallycall.Qty_tmp = '" + rs.getString("Qty_tmp") + "'," +
							"dallycall.BonusItemCode = '" + rs.getString("BonusItemCode") + "'," +
							"dallycall.Price = '" + rs.getString("Price") + "'," +
							"dallycall.Total = '" + rs.getString("Total") + "'," +
							"dallycall.DiscountP = '" + rs.getString("DiscountP") + "'," +
							"dallycall.DiscountB = '" + rs.getString("DiscountB") + "'," +
							"dallycall.VatB = '" + rs.getString("VatB") + "'," +
							"dallycall.CmsP = '" + rs.getString("CmsP") + "'," +
							"dallycall.CmsB = '" + rs.getString("CmsB") + "'," +
							"dallycall.Total2 = '" + rs.getString("Total2") + "'," +
							"dallycall.WelfareP = '" + ( 100 - rs.getInt("WelfareP") ) + "'," +
							"dallycall.WelfareB = '" + rs.getString("WelfareB") + "'," +
							"dallycall.Total3 = '0.00'," +
							"dallycall.DallyP = '0.00'," +
							"dallycall.DallyB = '0.00'," +
							"dallycall.IsCancel = '" + rs.getString("IsCancel") + "'," +
							"dallycall.Branch_Code = '" + rs.getString("Branch_Code") + "'," +
							"dallycall.Modify_Code = '" + rs.getString("Modify_Code") + "'," +
							"dallycall.Modify_Date = '" + rs.getString("Modify_Date") + "'," +
							"dallycall.CT_Code = '" + rs.getString("CT_Code") + "'," +
							"dallycall.AreaCode2 = '" + rs.getString("AreaCode2") + "'," +
							"dallycall.Sale_Detail_ID = " + rs.getString("Sale_Detail_ID") + "," +
							"dallycall.Id_Ref = '" + rs.getString("Id_Ref") + "'ม " +
							"dallycall.isArea = " + rs.getString("isArea") + ", " +
							"dallycall.Sale_Status = " + rs.getString("Sale_Status") + " " +
							"WHERE dallycall.Id = " + IdDRC;	
				}else{
					Sql = "INSERT INTO dallycall ( " +
							"dallycall.OrdererCode,dallycall.xDate,dallycall.AreaCode,dallycall.Cus_Code,dallycall.DocNo," +
							"dallycall.ItemCode,dallycall.Qty,dallycall.Qty_tmp,dallycall.BonusItemCode,dallycall.Price," +
							"dallycall.Total,dallycall.DiscountP,dallycall.DiscountB,dallycall.VatB,dallycall.CmsP,dallycall.CmsB," +
							"dallycall.Total2,dallycall.WelfareP,dallycall.WelfareB,dallycall.Total3,dallycall.DallyP,dallycall.DallyB," +
							"dallycall.IsCancel,dallycall.Branch_Code,dallycall.Modify_Code,dallycall.Modify_Date,dallycall.CT_Code," +
							"dallycall.AreaCode2,dallycall.Sale_Detail_ID,dallycall.Id_Ref,dallycall.isArea,dallycall.Sale_Status )";
					Sql += " VALUES ";
					Sql += "( ";
					SqlVul += "'" + rs.getString("OrdererCode") + "',";
					SqlVul += "'" + rs.getString("xDate") + "',";
					SqlVul += "'" + rs.getString("Cus_Code") + "',";
					SqlVul += "'" + rs.getString("DocNo") + "',";
					SqlVul += "'" + rs.getString("ItemCode") + "',";
					SqlVul += "'" + rs.getString("Qty") + "',";
					SqlVul += "'" + rs.getString("Qty_tmp") + "',";
					SqlVul += "'" + rs.getString("BonusItemCode") + "',";
					SqlVul += "'" + rs.getString("Price") + "',";
					SqlVul += "'" + rs.getString("Total") + "',";
					SqlVul += "'" + rs.getString("DiscountP") + "',";
					SqlVul += "'" + rs.getString("DiscountB") + "',";
					SqlVul += "'" + rs.getString("VatB") + "',";
					SqlVul += "'" + rs.getString("CmsP") + "',";
					SqlVul += "'" + rs.getString("CmsB") + "',";
					SqlVul += "'" + rs.getString("Total2") + "',";
					SqlVul += "'" + rs.getString("WelfareP") + "',";
					SqlVul += "'" + rs.getString("WelfareB") + "',";
					SqlVul += "'" + rs.getString("Total3") + "',";
					SqlVul += "'" + rs.getString("DallyP") + "',";
					SqlVul += "'" + rs.getString("DallyB") + "',";
					SqlVul += "'" + rs.getString("IsCancel") + "',";
					SqlVul += "'" + rs.getString("Branch_Code") + "',";
					SqlVul += "'" + rs.getString("Modify_Code") + "',";
					SqlVul += "'" + rs.getString("Modify_Date") + "',";
					SqlVul += "'" + rs.getString("CT_Code") + "',";
					SqlVul += "'" + rs.getString("AreaCode2") + "',";
					SqlVul += "'" + rs.getString("Sale_Detail_ID") + "',";
					SqlVul += "'" + rs.getString("Id_Ref") + "',";
					SqlVul += "'" + rs.getString("isArea") + "',";
					SqlVul += "'" + rs.getString("Sale_Status") + "',";
					
					//System.out.println(rs.getString(4).substring(0,2) + " ::: " + rs.getString(4).substring(0,1) );
					if( rs.getString("AreaCode").substring(0,2).equals("BB") )	
						SqlVul += "'PB00'";
					else if( rs.getString("AreaCode").substring(0,1).equals("P") )
						SqlVul += "'PO00'";
					else
						SqlVul += "'*'";
					
					Sql += SqlVul + " )";
				}
				Cnt++;
			}
			System.out.println(2 + " : " + Sql );
			if(!Sql.equals("")) Add_Data( Sql );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}		
	}
	
	
	public void UpdateDailySale( String IdDRC ) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUsername;
        sqlsel.Pwd = xPassword;
        String Sql = "";
        try{
			rs = sqlsel.getReSultSQL("SELECT * FROM dallycall " +
									 "INNER JOIN item ON dallycall.ItemCode = item.Item_Code  " +
									 "WHERE Id = " + IdDRC + " AND dallycall.IsCancel = 0" );
			while(rs.next()){
					Sql = "UPDATE dallycall SET " +
							"dallycall.WelfareB = '" + Number.UnComma2d( ((rs.getDouble("Total2")*rs.getDouble("WelfareP"))/100 )+"" ) + "'," +
							"dallycall.Total3 = '" + Number.UnComma2d( (rs.getDouble("Total2") - rs.getDouble("WelfareB"))+"" ) + "' " +
							"WHERE dallycall.Id = " + IdDRC;							
					Add_Data( Sql );					
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}		
	}
	
	private void Add_Data(String StrSql) throws Exception{
		SqlOperation sqlopt = new SqlOperation();
		sqlopt.uName = xUsername;
		sqlopt.Pwd = xPassword;
		try{
			//System.out.println(StrSql);
    		sqlopt.executeUpdateSQL(StrSql);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		sqlopt.closeConnection();
    	}	
	}	
	
}
