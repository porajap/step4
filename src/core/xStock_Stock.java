package core;

import general.DateTime;
import general.Number;
import java.sql.ResultSet;
import org.zkoss.zul.Messagebox;
import connect.OperationData;
import connect.SqlOperation;
import connect.SqlSelection;

public class xStock_Stock {
	
	public String xUser = null;
	public String xPwd = null;
	public String xMFGDate = "";
	public String xEXPDate = "";
	private String getSqlConfigLog( String BranchCode ){
		return 	"SELECT " +
				"cf_lot.Lot_Code," +
				"cf_lot.Lot_St_Code," +
				"cf_lot.IsShowTable," +
				"cf_lot.IsChkBarCode," +
				"cf_lot.IsEmployee," +
				"cf_lot.Branch_Code " +
				"FROM	cf_lot " +
				"WHERE Branch_Code = " + BranchCode;
	}
	
	public int[] chkConfigLot( String BranchCode ) throws Exception{
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
        int[] chk = new int[10];
        
		try{	
			
			rs = sqlsel.getReSultSQL(getSqlConfigLog( BranchCode ) );
			while(rs.next()){
				
				 chk[0] = Integer.parseInt( rs.getString("Lot_St_Code") );
				 chk[1] = Integer.parseInt( rs.getString("IsShowTable") );
				 chk[2] = Integer.parseInt( rs.getString("IsChkBarCode") );
				 chk[3] = Integer.parseInt( rs.getString("IsEmployee") );
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();
            if (rs != null) {
                rs.close();
            }
		}
		return chk;
	}

	// ========================= Inventory ============================
	   
	public String CheckID(String Branch_Code,String Item_Code, String LotNo, String Shelf_Code) throws Exception{
	 String Sql = "SELECT Inv_Code " +
				"FROM 	wh_inventory " +
				"WHERE	Branch_Code = '" + Branch_Code + "' " +
				"AND	LotNo = '" + LotNo + "' " +
				"AND	Shelf_Code = '" + Shelf_Code + "' " +
				"AND	Item_Code = '" + Item_Code + "' " +
				"LIMIT 1 ";
	 ResultSet rs = null;
     SqlSelection sqlsel = new SqlSelection();
     sqlsel.uName = xUser;
     sqlsel.Pwd = xPwd;
		try{
			rs = sqlsel.getReSultSQL( Sql );
			if(rs.next()){
				return rs.getString(1);
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

         if (rs != null) {
             rs.close();
         }
		}
		return null;
	}
	
	public String CheckItemInventoryBalance(String Branch_Code,String Item_Code, String Wh_Code,String S_code,String LotNo,String Balance_Date) throws Exception{
		String Sql = "SELECT InvBalance_Code " +
				"FROM 	wh_inventory_balance " +
				"WHERE	Item_Code = '" + Item_Code + "' " +
				"AND	Wh_Code = '" + Wh_Code + "' " +
				"AND	Balance_Date = '" + Balance_Date + "' " +
				"AND	Branch_Code = " + Branch_Code + " " +
				"AND	Shelf_Code = '" + S_code + "' " +
				"AND	LotNo = " + LotNo + " " +
				"LIMIT 1";
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
		try{
			rs = sqlsel.getReSultSQL( Sql );

			if(rs.next()){
				return rs.getString(1);
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
		return null;
	}
	
	// ========================= wh_inventory_detail ============================
	
	private String getSQLStockQty(int Circle,String B_Code,String L_Code,String S_Code,String T_Date,String IT_Code) throws Exception{
	String  SQLStockQty = "SELECT " +
			"item.Item_Code," +
			"item.Barcode," +
			"item.NameTH," +
			"item.SalePrice AS Price," +
			"wh_inventory.LotNo," +
			"wh_inventory.Shelf_Code,";
			
			SQLStockQty += "(" +
			/*"SELECT " +
			"wh_inventory_detail.StockQty " +
			"FROM " +
			"wh_inventory_detail " +
			"INNER JOIN item ON wh_inventory_detail.Item_Code = item.Item_Code " +
			"WHERE " +
			"wh_inventory_detail.Branch_Code = " + B_Code + " AND " +
			"wh_inventory_detail.Create_Date < '" + T_Date + "' AND " +
			"wh_inventory_detail.Item_Code = '" + IT_Code + "' AND " +
			"wh_inventory_detail.StockQty <> 0 AND " +
			"wh_inventory_detail.Circle = 1 " +*/
			"SELECT " +
			"wh_inventory_balance.Qty " +
			"FROM wh_inventory_balance " +
			"WHERE " +
			"wh_inventory_balance.Balance_Date < '" + T_Date + "' AND " +
			"wh_inventory_balance.Branch_Code = " + B_Code + " AND " +
			"wh_inventory_balance.Item_Code = '" + IT_Code + "' AND " +
			"wh_inventory_balance.LotNo = '" + L_Code + "' AND " +
			"wh_inventory_balance.Shelf_Code = '" + S_Code + "' " +
			"ORDER BY wh_inventory_balance.Balance_Date DESC " +
			"LIMIT 1  " + 
			") AS Carried," +
			"( " +
			"SELECT " +
			"sum(wh_stock_receive_sub.Qty) AS Qty " +
			"FROM " +
			"wh_stock_receive_sub " +
			"INNER JOIN wh_stock_receive ON wh_stock_receive.DocNo = wh_stock_receive_sub.DocNo " +
			"WHERE " +
			"wh_stock_receive_sub.Branch_Code = " + B_Code + " AND " +
			"wh_stock_receive_sub.LotNo = '" + L_Code + "' AND " +
			"wh_stock_receive_sub.Shelf_Code = '" + S_Code + "' AND " +
			"wh_stock_receive_sub.Item_Code = '" + IT_Code + "' AND " +
			"wh_stock_receive.DocDate = '" + T_Date + "' " +
			") AS Receive," +
			"( " +
			"SELECT " +
			"sum(wh_stock_transmit_sub.Qty) AS Qty " +
			"FROM wh_stock_transmit_sub " +
			"INNER JOIN wh_stock_transmit ON wh_stock_transmit_sub.DocNo = wh_stock_transmit.DocNo " +
			"WHERE wh_stock_transmit_sub.Branch_Code = " + B_Code + " " +
			"AND wh_stock_transmit_sub.LotNo = '" + L_Code + "' " +
			"AND wh_stock_transmit_sub.Shelf_Code = '" + S_Code + "' " +
			"AND wh_stock_transmit_sub.Item_Code = '" + IT_Code + "' " +
			"AND wh_stock_transmit.Reason_Code = 1 " +
			"AND wh_stock_transmit.DocDate = '" + T_Date + "' " +
			") AS Transmit1," +
			"( " +
			"SELECT " +
			"sum(wh_stock_transmit_sub.Qty) AS Qty " +
			"FROM wh_stock_transmit_sub " +
			"INNER JOIN wh_stock_transmit ON wh_stock_transmit_sub.DocNo = wh_stock_transmit.DocNo " +
			"WHERE wh_stock_transmit_sub.Branch_Code = " + B_Code + " " +
			"AND wh_stock_transmit_sub.LotNo = '" + L_Code + "' " +
			"AND wh_stock_transmit_sub.Shelf_Code = '" + S_Code + "' " +
			"AND wh_stock_transmit_sub.Item_Code = '" + IT_Code + "' " +
			"AND wh_stock_transmit.Reason_Code = 2 " +
			"AND wh_stock_transmit.DocDate = '" + T_Date + "' " +
			") AS Transmit2," +
			"( " +
			"SELECT " +
			"sum(wh_stock_transmit_sub.Qty) AS Qty " +
			"FROM wh_stock_transmit_sub " +
			"INNER JOIN wh_stock_transmit ON wh_stock_transmit_sub.DocNo = wh_stock_transmit.DocNo " +
			"WHERE wh_stock_transmit_sub.Branch_Code = " + B_Code + " " +
			"AND wh_stock_transmit_sub.LotNo = '" + L_Code + "' " +
			"AND wh_stock_transmit_sub.Shelf_Code = '" + S_Code + "' " +
			"AND wh_stock_transmit_sub.Item_Code = '" + IT_Code + "' " +
			"AND wh_stock_transmit.Reason_Code = 3 " +
			"AND wh_stock_transmit.DocDate = '" + T_Date + "' " +
			") AS Transmit3," +
			"( " +
			"SELECT " +
			"sum(sale_detail.Qty) AS Qty " +
			"FROM sale " +
			"INNER JOIN sale_detail ON sale.DocNo = sale_detail.DocNo " +
			"WHERE sale_detail.Branch_Code = " + B_Code + " " +
			"AND sale_detail.LotNo = '" + L_Code + "' " +
			"AND sale_detail.Shelf_Code = '" + S_Code + "' " +
			"AND sale_detail.Item_Code = '" + IT_Code + "' " +
			"AND sale_detail.IsCancel = 0 " +
			"AND sale.DocDate = '" + T_Date + "' " +
			") AS Sale, " +
			"( " +
			"SELECT " +
			"sum(sale_detail.Price) AS Price " +
			"FROM sale " +
			"INNER JOIN sale_detail ON sale.DocNo = sale_detail.DocNo " +
			"WHERE sale_detail.Branch_Code = " + B_Code + " " +
			"AND sale_detail.LotNo = '" + L_Code + "' " +
			"AND sale_detail.Shelf_Code = '" + S_Code + "' " +
			"AND sale_detail.Item_Code = '" + IT_Code + "' " +
			"AND sale_detail.IsCancel = 0 " +
			"AND sale.DocDate = '" + T_Date + "' " +
			") AS SalePrice," +
			"( " +
			"SELECT " +
			"sum(sale_detail.DiscountBath) AS DiscountBath " +
			"FROM sale " +
			"INNER JOIN sale_detail ON sale.DocNo = sale_detail.DocNo " +
			"WHERE sale_detail.Branch_Code = " + B_Code + " " +
			"AND sale_detail.LotNo = '" + L_Code + "' " +
			"AND sale_detail.Shelf_Code = '" + S_Code + "' " +
			"AND sale_detail.Item_Code = '" + IT_Code + "' " +
			"AND sale_detail.IsCancel = 0 " +
			"AND sale.DocDate = '" + T_Date + "' " +
			") AS DiscountBath," +
			"wh_inventory.Qty AS StockQty " +
			"FROM " +
			"item " +
			"INNER JOIN wh_inventory ON item.Item_Code = wh_inventory.Item_Code " +
			"WHERE " +
			"wh_inventory.Branch_Code = " + B_Code + " AND " +
			"wh_inventory.LotNo = '" + L_Code + "' AND " +
			"wh_inventory.Shelf_Code = '" + S_Code + "' AND " +
			"item.Item_Code = '" + IT_Code + "' " +
			"ORDER BY " +
			"item.NameTH ASC";
		//System.out.println( SQLStockQty );	
		return SQLStockQty;
	}
	
	public void Update_Inventory(String B_Code,String L_Code,String S_Code,String IT_Code,int St,String xDateNow) throws Exception{
    	ResultSet rs = null;
    	ResultSet rs_receive = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
        //Branch_Code,LotNo,Shelf_Code,T_Date,Item_Code
		try{
			String Id = null;
			String DateNow = xDateNow;
			//String gDateNow = DateTime.getDate_Format("yyyy-MM-dd");
			int Circle = 0;
			String xCarried = "0.00";
			String xSaleQty = "0.00";
			String xReceiveQty = "0.00";
			String xReceiveQty1 = "0.00";
			String xReceiveQty2 = "0.00";
			String xReceiveQty3 = "0.00";
			String xReceiveQty4 = "0.00";
			String xReceiveQty5 = "0.00";
			String xStockQty = "0.00";
			String xReceiveDocNo = "0.00";
			String xPrice = "0.00";
			String xDiscountBath = "0.00";
			String xTotal = "0.00";
			String Reason_Code1 = "0.00";
			String Reason_Code2 = "0.00";
			String Reason_Code3 = "0.00";
			Circle = 1;

			//===== Update	
			Id = null;
			if( St == 1 ){
				Id = CheckID(B_Code,IT_Code,L_Code,S_Code);
				if(Id != null){
					try{
						// Operation Update
						new OperationData(
							xUser,
							xPwd,
					   	 	"Update",
							"wh_inventory", 
							new String[][] {
						   	 	{"Qty", "'" + xStockQty + "'" },
						   	 	{"Modify_Date", "NOW()" },
							},
							new String[][] {
									{"Branch_Code","'" + B_Code + "'" },
								   	{"LotNo","'" +  L_Code + "'" },
								   	{"Shelf_Code","'" +  S_Code + "'" },
								   	{"Item_Code", "'" + IT_Code + "'" }
					   	 	}
						);
					}catch(Exception e){
						Messagebox.show("1 " + e.getMessage());
					}
		    	}else{
		    		try{
			    		// Operation Insert
		    			if(xMFGDate.equals("")){
		    				xMFGDate = DateTime.convertDate( DateTime.getDateNow() );
		    				xEXPDate = DateTime.convertDate( DateTime.getDateNow() );
		    			}
						new OperationData(
								xUser,
								xPwd,
				   	 		"Insert",
							"wh_inventory", 
							new String[][] {
				   	 			{"Item_Code", "'" + IT_Code + "'" },
							  	{"Shelf_Code", "'" + S_Code + "'" },
							    {"LotNo", "'" + L_Code + "'" },
					   	 		{"MFGDate", "'" + xMFGDate + "'" },
					   	 		{"EXPDate", "'" + xEXPDate + "'" },			
					   	 		{"Price", xPrice  },
					   	 		{"Qty", xStockQty },
					   	 		{"Create_Date", "NOW()" },
					   	 		{"Modify_Date", "NOW()" },
					   	 		{"Branch_Code", B_Code },
							},
							null
						);
						
			    	}catch(Exception e){
			    		Messagebox.show("2 " +e.getMessage());
					}
		    	}
				
				// Add to Inventory Balance
				Id = CheckItemInventoryBalance(
					B_Code,	
					IT_Code, 
					S_Code.substring(0, 2),
					S_Code,
					L_Code,
					DateNow
				);
				if(Id != null){
					try{
						// Operation Update
						new OperationData(
								xUser,
								xPwd,
					   	 	"Update",
							"wh_inventory_balance", 
							new String[][] {
								{"Qty", xStockQty },
							},
							new String[][] {
							   	 {"InvBalance_Code", Id }
					   	 	}
						);
					}catch(Exception e){
						Messagebox.show("3 " +e.getMessage());
					}
		    	}else{
		    		try{
						new OperationData(
								xUser,
								xPwd,
				   	 		"Insert",
							"wh_inventory_balance", 
							new String[][] {
				   	 			{"Item_Code", "'" + IT_Code + "'" },
							  	{"Wh_Code", "'" + S_Code.substring(0, 2) + "'" },
							  	{"Shelf_Code", "'" + S_Code + "'" },
							    {"LotNo", "'" + L_Code + "'" },
							  	{"Qty", xStockQty },
					   	 		{"Balance_Date", "'" + DateNow + " 00:00:00'" },
					   	 		{"Branch_Code", B_Code },
							},
							null
						);
			    	}catch(Exception e){
			    		Messagebox.show("4 " +e.getMessage());
					}
		    	}	
			}	
			
//===================================================================
//===============
//===================================================================
			
			String Sql = getSQLStockQty(Circle,B_Code,L_Code,S_Code,DateNow,IT_Code);  

			rs = sqlsel.getReSultSQL( Sql );
			while(rs.next()){
				xReceiveQty1 = "0.00";
				xReceiveQty2 = "0.00";
				xReceiveQty3 = "0.00";
				xReceiveQty4 = "0.00";
				xReceiveQty5 = "0.00";
				if(rs.getString("Carried") != null ) xCarried = rs.getString("Carried");
				xReceiveQty1 = getReceive("Receive1",B_Code,DateNow,Circle,rs.getString("Item_Code"));
				xReceiveQty2 = getReceive("Receive2",B_Code,DateNow,Circle,rs.getString("Item_Code"));
				xReceiveQty3 = getReceive("Receive3",B_Code,DateNow,Circle,rs.getString("Item_Code"));
				xReceiveQty4 = getReceive("Receive4",B_Code,DateNow,Circle,rs.getString("Item_Code"));
				xReceiveQty5 = getReceive("Receive5",B_Code,DateNow,Circle,rs.getString("Item_Code"));
				if(rs.getString("Receive") != null ){
					xReceiveQty = rs.getString("Receive");
				}/*else{
					xReceiveQty1 = "0";
					xReceiveQty2 = "0";
					xReceiveQty3 = "0";
					xReceiveQty4 = "0";
					xReceiveQty5 = "0";
				}*/
				if(rs.getString("Transmit1") != null ) Reason_Code1 = rs.getString("Transmit1");
				if(rs.getString("Transmit2") != null ) Reason_Code2 = rs.getString("Transmit2");
				if(rs.getString("Transmit3") != null ) Reason_Code3 = rs.getString("Transmit3");
				if(rs.getString("Sale") != null ) xSaleQty = rs.getString("Sale");
				if(rs.getString("Price") != null ) xPrice = rs.getString("Price");
				if(rs.getString("DiscountBath") != null ) xDiscountBath = rs.getString("DiscountBath");
				xTotal = Number.UnComma2d( (( Double.parseDouble(xSaleQty) * Double.parseDouble(xPrice) ) - Double.parseDouble(xDiscountBath)) + "" );
				xStockQty = ((Double.parseDouble(xCarried) + Double.parseDouble(xReceiveQty)) - ( Double.parseDouble(Reason_Code1) + Double.parseDouble(Reason_Code2) + Double.parseDouble(Reason_Code3) + Double.parseDouble(xSaleQty)))+"";
				
						Id = CheckInvID(B_Code,DateNow,Circle,rs.getString("Item_Code"),L_Code,S_Code);
						if(Id != null){
						
								new OperationData(
									xUser,
									xPwd,
						   	 		"Update",
									"wh_inventory_detail", 
									new String[][] {
								   	 			//{"Item_Code"		, "'" + rs.getString("Item_Code") + "'" },
											  	//{"Branch_Code"		, "'" + B_Code + "'" },
											    //{"Create_Date"		, "'" + DateNow + "'" },
									   	 		{"Carried"			, "'" + xCarried + "'" },
									   	 		{"Receive"			, "'" + xReceiveQty + "'" },			
									   	 		{"Receive1"			, "'" + xReceiveQty1 + "'" },
									   	 		{"Receive2"			, "'" + xReceiveQty2 + "'" },
									   	 		{"Receive3"			, "'" + xReceiveQty3 + "'" },
									   	 		{"Receive4"			, "'" + xReceiveQty4 + "'" },
									   	 		{"Receive5"			, "'" + xReceiveQty5 + "'" },
											   	{"Restore"			, "'" + Reason_Code1 + "'" },
											   	{"Lose"				, "'" + Reason_Code2 + "'" },
											   	{"Adjust"			, "'" + Reason_Code3 + "'" },
											   	{"Sale"				, "'" + xSaleQty + "'" },
											   	{"Price"			, "'" + xPrice + "'" },
											   	{"DiscountBath"		, "'" + xDiscountBath + "'" },
											   	{"Total"			, "'" + xTotal + "'" },
											   	{"StockQty"			, "'" + xStockQty + "'" },
											   	//{"BarCode"			, "''" },
											   	//{"CategorySub_Code"	, "'0'" },
											   	//{"Row_No"			, "0" },
											   	{"ReceiveDoc"		, "'" + xReceiveDocNo + "'"  },
											   	//{"ReceiveDocSale"	, "''"  },
											   	//{"Circle"			, "'" + Circle + "'" },
											   	//{"LotNo"			, "'" + L_Code + "'" },
											   	//{"Shelf_Code"		, "'" + S_Code + "'" },
									},
									new String[][] {
										{"RowID"			, Id },
										{"Circle"			, "'" + Circle + "'" },
								   	}
								);						
						}else{
								new OperationData(
									xUser,
									xPwd,
						   	 		"Insert",
									"wh_inventory_detail", 
									new String[][] {
						   	 			{"Item_Code"		, "'" + rs.getString("Item_Code") + "'" },
									  	{"Branch_Code"		, "'" + B_Code + "'" },
									    {"Create_Date"		, "'" + DateNow + "'" },
							   	 		{"Carried"			, "'" + xCarried + "'" },
							   	 		{"Receive"			, "'" + xReceiveQty + "'" },			
							   	 		{"Receive1"			, "'" + xReceiveQty1 + "'" },
							   	 		{"Receive2"			, "'" + xReceiveQty2 + "'" },
							   	 		{"Receive3"			, "'" + xReceiveQty3 + "'" },
							   	 		{"Receive4"			, "'" + xReceiveQty4 + "'" },
							   	 		{"Receive5"			, "'" + xReceiveQty5 + "'" },
									   	{"Restore"			, "'" + Reason_Code1 + "'" },
									   	{"Lose"				, "'" + Reason_Code2 + "'" },
									   	{"Adjust"			, "'" + Reason_Code3 + "'" },
									   	{"Sale"				, "'" + xSaleQty + "'" },
									   	{"Price"			, "'" + xPrice + "'" },
									   	{"DiscountBath"		, "'" + xDiscountBath + "'" },
									   	{"Total"			, "'" + xTotal + "'" },
									   	{"StockQty"			, "'" + xStockQty + "'" },
									   	{"BarCode"			, "''" },
									   	{"CategorySub_Code"	, "'0'" },
									   	{"Row_No"			, "0" },
									   	{"ReceiveDoc"		, "'" + xReceiveDocNo + "'"  },
									   	{"ReceiveDocSale"	, "''"  },
									   	{"Circle"			, "'" + Circle + "'" },
									   	{"LotNo"			, "'" + L_Code + "'" },
									   	{"Shelf_Code"		, "'" + S_Code + "'" },
									},
									null
								);
						}

						//===== Update
					    Id = null;
						if( St == 1 ){
							Id = CheckID(B_Code,IT_Code,L_Code,S_Code);
							if(Id != null){
								try{
									// Operation Update
									new OperationData(
										xUser,
										xPwd,
								   	 	"Update",
										"wh_inventory", 
										new String[][] {
									   	 	{"Qty", "'" + xStockQty + "'" },
									   	 	//{"Modify_Date", "NOW()" },
										},
										new String[][] {
												{"Branch_Code","'" + B_Code + "'" },
											   	{"LotNo","'" +  L_Code + "'" },
											   	{"Shelf_Code","'" +  S_Code + "'" },
											   	{"Item_Code", "'" + IT_Code + "'" }
								   	 	}
									);
								}catch(Exception e){
									Messagebox.show("1 " + e.getMessage());
								}
					    	}else{
					    		try{
						    		// Operation Insert
									new OperationData(
											xUser,
											xPwd,
							   	 		"Insert",
										"wh_inventory", 
										new String[][] {
							   	 			{"Item_Code", "'" + IT_Code + "'" },
										  	{"Shelf_Code", "'" + S_Code + "'" },
										    {"LotNo", "'" + L_Code + "'" },
								   	 		{"MFGDate", "'" + xMFGDate + "'" },
								   	 		{"EXPDate", "'" + xEXPDate + "'" },			
								   	 		{"Price", xPrice  },
								   	 		{"Qty", xStockQty },
								   	 		{"Create_Date", "NOW()" },
								   	 		{"Modify_Date", "NOW()" },
								   	 		{"Branch_Code", B_Code },
										},
										null
									);
									
						    	}catch(Exception e){
						    		Messagebox.show("2 " +e.getMessage());
								}
					    	}
							
							// Add to Inventory Balance
							Id = CheckItemInventoryBalance(
								B_Code,	
								IT_Code, 
								S_Code.substring(0, 2),
								S_Code,
								L_Code,
								DateNow
							);

							if(Id != null){
								
								try{
									// Operation Update
									new OperationData(
											xUser,
											xPwd,
								   	 	"Update",
										"wh_inventory_balance", 
										new String[][] {
											{"Qty", xStockQty },
										},
										new String[][] {
										   	 {"InvBalance_Code", Id }
								   	 	}
									);
								}catch(Exception e){
									Messagebox.show("3 " +e.getMessage());
								}
					    	}else{
					    		try{
									new OperationData(
											xUser,
											xPwd,
							   	 		"Insert",
										"wh_inventory_balance", 
										new String[][] {
							   	 			{"Item_Code", "'" + IT_Code + "'" },
										  	{"Wh_Code", "'" + S_Code.substring(0, 2) + "'" },
										  	{"Shelf_Code", "'" + S_Code + "'" },
										    {"LotNo", "'" + L_Code + "'" },
										  	{"Qty", xStockQty },
								   	 		{"Balance_Date", "'" + DateNow + " 00:00:00'" },
								   	 		{"Branch_Code", B_Code },
										},
										null
									);
						    	}catch(Exception e){
						    		Messagebox.show("4 " +e.getMessage());
								}
					    	}	
						}			
						
				//ชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชชช								
				xCarried = "0.00";
				xSaleQty = "0.00";
				xReceiveQty = "0.00";
				xReceiveQty1 = "0.00";
				xReceiveQty2 = "0.00";
				xReceiveQty3 = "0.00";
				xReceiveQty4 = "0.00";
				xReceiveQty5 = "0.00";
				xStockQty = "0.00";
				xReceiveDocNo = "0.00";
				xPrice = "0.00";
				xDiscountBath = "0.00";
				xTotal = "0.00";
				Reason_Code1 = "0.00";
				Reason_Code2 = "0.00";
				Reason_Code3 = "0.00";
				
			    Sql = "SELECT " +
						"wh_stock_receive_sub.Item_Code," +
						"wh_stock_receive_sub.Qty," +
						"wh_stock_receive.RefDocNo " +
						"FROM " +
						"wh_stock_receive_sub " +
						"INNER JOIN wh_stock_receive ON wh_stock_receive_sub.DocNo = wh_stock_receive.DocNo " +
						"INNER JOIN item ON wh_stock_receive_sub.Item_Code = item.Item_Code " +
			    		"WHERE " +
						"wh_stock_receive_sub.Branch_Code = " + B_Code + " AND " +
						"wh_stock_receive_sub.LotNo = '" + L_Code + "' AND " +
						"wh_stock_receive_sub.Shelf_Code = '" + S_Code + "' AND " +
						"wh_stock_receive_sub.Item_Code = '" + IT_Code + "' AND " +
						"wh_stock_receive.DocDate = '" + DateNow + "'";		
			    //System.out.println( Sql );
			    rs_receive = sqlsel.getReSultSQL( Sql );
				int cv = 1;
				String DocNoR = "";
				String xWh = "WHERE  Branch_Code = " + B_Code + " AND Item_Code = '" + IT_Code + "' ";
					   xWh += "AND LotNo = '" + L_Code + "' AND Shelf_Code = '" + S_Code + "' ";
					   xWh += "AND Circle = '" + Circle + "' AND Create_Date = '" + DateNow + "'";
				while(rs_receive.next()){
					DocNoR += rs_receive.getString("RefDocNo")+",";
					switch(cv){
						case 1:
							Sql = "UPDATE wh_inventory_detail SET Receive1 = '" + rs_receive.getString("Qty") + "' " + xWh; 
							break;
						case 2:
							Sql = "UPDATE wh_inventory_detail SET Receive2 = '" + rs_receive.getString("Qty") + "' " + xWh;
							break;
						case 3:
							Sql = "UPDATE wh_inventory_detail SET Receive3 = '" + rs_receive.getString("Qty") + "' " + xWh;
							break;
						case 4:
							Sql = "UPDATE wh_inventory_detail SET Receive4 = '" + rs_receive.getString("Qty") + "' " + xWh;
							break;
						case 5:
							Sql = "UPDATE wh_inventory_detail SET Receive5 = '" + rs_receive.getString("Qty") + "' " + xWh;
							break;
					}
					//System.out.println(cv + " :" + Sql );
					Add_Data( Sql );
					cv++;
				}
				rs_receive.close();
				if(DocNoR.length()>0){
			        Sql = "UPDATE wh_inventory_detail SET ReceiveDoc = '" + DocNoR.substring(0, DocNoR.length()-1 ) + "' " + xWh;
			        Add_Data( Sql );	
				}
		    }

		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage());
		}finally{
			sqlsel.closeConnection();
            if (rs != null) {
                rs.close();
            }
		}
	}

    public int UpdateInventory(int chk,String Symbol,String Branch_Code,String LotNo,String SqlDetail,String Wh_Code,int Select,String xDate) throws  Exception{
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
        int Count = 0;
        try{	
			rs = sqlsel.getReSultSQL( SqlDetail );
			while(rs.next()){
				Update_Inventory(Branch_Code, rs.getString("LotNo"), rs.getString("Shelf_Code"),rs.getString("Item_Code"),Integer.parseInt( rs.getString("St_Code") ),xDate);
				Count++;
			}
			return Count;
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage());
			return Count;
		}finally{
			sqlsel.closeConnection();

            if (rs != null) {
                rs.close();
            }
		}
    }

	public String getStockQty(String Id,String FL) throws Exception{
		String Sql = "SELECT " + FL + " " +
				"FROM 	wh_inventory_detail " +
				"WHERE	RowID = '" + Id + "' "; 
		
		ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					return rs.getString(1);
				}else{
					return "0";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return "0";
	}
	
	public String getReceive(String FL,String Branch_Code,String C_Date, int Circle, String Item_Code) throws Exception{
		String Sql = "SELECT " + FL + " " +
					"FROM 	wh_inventory_detail " +
					"WHERE	Branch_Code = '" + Branch_Code + "' " +
					"AND	Create_Date = '" + C_Date + "' " +
					"AND	Circle = '" + Circle + "' " +
					"AND	Item_Code = '" + Item_Code + "'";
		//System.out.println( Sql );
		 ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					return rs.getString(1);
				}else{
					return "0";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return "0";
	}
	
	public String CheckCircleType(String Branch_Code) throws Exception{
		 String Sql = "SELECT Circle_Type " +
					"FROM 	branch " +
					"WHERE	Branch_Code = '" + Branch_Code + "'";
		 ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					return rs.getString(1);
				}else{
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return null;
		}

	public String getSaleQty(int xSel,String Branch_Code,String xDate,String ItemCode) throws Exception{
		 String Sql = "SELECT SUM( sale_detail.Qty ) AS Qty " +
				 	  "FROM sale_detail " +
				 	  "INNER JOIN sale ON sale.DocNo = sale_detail.DocNo " +
				 	  "WHERE Branch_Code = '" + Branch_Code + "' " +
				 	  "AND Item_Code = '" + ItemCode + "' " +
		 			  "AND sale.DocDate = '" + xDate + "' " +
		 			  "AND sale_detail.IsCancel = 0 GROUP BY sale_detail.Item_Code";

		 ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					return rs.getString(1);
				}else{
					return "0";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return "0";
		}
	
	public String ChkUpdateStock(String Branch_Code,String xDate,String FL) throws Exception{
		 String Sql = "SELECT " + FL + " " +
						"FROM updatestatus " +
						"WHERE " +
						"updatestatus.Branch_Code = " + Branch_Code + " AND " +
						"updatestatus.CreateDate = '" + xDate + "'";
 
		 ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					return rs.getString(1);
				}else{
					return "0";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return "0";
		}
	
	public String UpdateStock(String Branch_Code,String Circle,String L_Code,String S_Code,String xDate) throws Exception{
			 String DateNow = xDate;
			 String Id = null;
			 /*
			 String SqlUP = "SELECT wh_inventory_detail.Item_Code,wh_inventory_detail.StockQty,wh_inventory_detail.LotNo,wh_inventory_detail.Shelf_Code,item.St_Code FROM " +
								"wh_inventory_detail " +
								"INNER JOIN item ON wh_inventory_detail.Item_Code = item.Item_Code " +    
								"WHERE " +
								"wh_inventory_detail.Branch_Code = " + Branch_Code + " AND " +
								"wh_inventory_detail.StockQty <> 0 AND " +
								"wh_inventory_detail.Create_Date < '" + DateNow + "' " +
								"AND wh_inventory_detail.LotNo <> '' " +
								"AND wh_inventory_detail.Shelf_Code  <> '' " +
								"GROUP BY " +
								"wh_inventory_detail.Item_Code, " +
								"wh_inventory_detail.LotNo, " +
								"wh_inventory_detail.Shelf_Code " +
								"ORDER BY wh_inventory_detail.Create_Date";
			 */
			 String SqlUP = "SELECT " +
				"wh_inventory_balance.Qty " +
				"FROM wh_inventory_balance " +
				"WHERE " +
				"wh_inventory_balance.Balance_Date < '" + DateNow + "' AND " +
				"wh_inventory_balance.Branch_Code = " + Branch_Code + " AND " +
				"wh_inventory_balance.Qty <> 0 AND " +
				"wh_inventory_balance.LotNo <> '' AND " +
				"wh_inventory_balance.Shelf_Code <> '' " +
				"ORDER BY wh_inventory_balance.Balance_Date DESC ";

					 System.out.println( SqlUP );
					 ResultSet rs = null;
				     SqlSelection sqlsel = new SqlSelection();
				     sqlsel.uName = xUser;
				     sqlsel.Pwd = xPwd;
				     /*
						try{
								rs = sqlsel.getReSultSQL( SqlUP );
								while(rs.next()){
									Id = CheckInvID(Branch_Code,DateNow,1,rs.getString("Item_Code"),rs.getString("LotNo"),rs.getString("Shelf_Code"));
									if(Id == null){
										new OperationData(
											xUser,
											xPwd,
								   	 		"Insert",
											"wh_inventory_detail", 
											new String[][] {
								   	 			{"Item_Code"		, "'" + rs.getString("Item_Code") + "'" },
											  	{"Branch_Code"		, "'" + Branch_Code + "'" },
											    {"Create_Date"		, "'" + DateNow + "'" },
									   	 		{"Carried"			, "'" + rs.getString("StockQty") + "'" },
									   	 		{"Receive"			, "'0'" },			
									   	 		{"Receive1"			, "'0'" },
									   	 		{"Receive2"			, "'0'" },
									   	 		{"Receive3"			, "'0'" },
									   	 		{"Receive4"			, "'0'" },
									   	 		{"Receive5"			, "'0'" },
										   	 	{"Restore"			, "'0'" },
											   	{"Lose"				, "'0'" },
											   	{"Adjust"			, "'0'" },
											   	{"Sale"				, "'0'" },
											   	{"Price"			, "'0'" },
											   	{"DiscountBath"		, "'0'" },
											   	{"Total"			, "'0'" },
											   	{"StockQty"			, "'" + rs.getString("StockQty") + "'" },
											   	{"BarCode"			, "''" },
											   	{"CategorySub_Code"	, "0" },
											   	{"Row_No"			, "0" },
											   	{"ReceiveDoc"		, "''"  },
											   	{"ReceiveDocSale"	, "''"  },
											   	{"Circle"			, "'1'" },
											   	{"LotNo"			, "'" + L_Code + "'" },
											   	{"Shelf_Code"		, "'" + S_Code + "'" },
											},
											null
										);
									}
								}

								new OperationData(
										xUser,
										xPwd,
						   	 		"Insert",
									"updatestatus", 
									new String[][] {
									  	{"Branch_Code"		, "'" + Branch_Code + "'" },
									    {"CreateDate"		, "'" + DateNow + "'" },
							   	 		{"xStatus"			, "'1'" },
									},
									null
								);
						}catch(Exception e){
								e.printStackTrace();
						}finally{
								sqlsel.closeConnection();
								if (rs != null) {
					             rs.close();
								}
						}
			*/
			return null;
		}
	
	public String CheckInvID(String Branch_Code,String C_Date, int Circle, String Item_Code, String LotNo, String Shelf_Code) throws Exception{
		 String Sql = "SELECT RowID " +
					"FROM 	wh_inventory_detail " +
					"WHERE	Branch_Code = '" + Branch_Code + "' " +
					"AND	Create_Date = '" + C_Date + "' " +
					"AND	Circle = '" + Circle + "' " +
					"AND	Item_Code = '" + Item_Code + "' " +
					"AND	LotNo = '" + LotNo + "' " +
					"AND	Shelf_Code = '" + Shelf_Code + "'";

		ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					return rs.getString(1);
				}else{
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return null;
		}

	public String getTableName(String DocNo) throws Exception{
		 String Sql = "SELECT xtable.Table_Name FROM sale " +
					"INNER JOIN xtable ON xtable.Table_Code = sale.Table_Code " +
					"WHERE	sale.DocNo = '" + DocNo + "' " +
					"AND	sale.IsCancel = 0";
		 String xRDocNo = "";
		 
		 
		 ResultSet rs = null;
	     SqlSelection sqlsel = new SqlSelection();
	     sqlsel.uName = xUser;
	     sqlsel.Pwd = xPwd;
			try{
				rs = sqlsel.getReSultSQL( Sql );
				if(rs.next()){
					xRDocNo = rs.getString("Table_Name");
					if(xRDocNo.length() > 5){
						xRDocNo = xRDocNo.substring(5, xRDocNo.length());
					}
					return xRDocNo;
				}else{
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sqlsel.closeConnection();

	         if (rs != null) {
	             rs.close();
	         }
			}
			return null;
		}
	
		private void Add_Data(String StrSql) throws Exception{
			SqlOperation sqlopt = new SqlOperation();
			sqlopt.uName = xUser;
			sqlopt.Pwd = xPwd;
	
			try{
				sqlopt.executeUpdateSQL(StrSql);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		sqlopt.closeConnection();
	    	}	
		}
	
}
