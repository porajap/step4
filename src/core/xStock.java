package core;

import java.sql.ResultSet;

import org.zkoss.zul.Messagebox;

import connect.OperationData;
import connect.SqlSelection;

public class xStock {
	
	public String xUser = null;
	public String xPwd = null;
	
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
	 
	 System.out.println("///////" + Sql);
	 
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
	
	public String CheckID2(String Branch_Code,String Item_Code, String LotNo, String Shelf_Code,String RefCode) throws Exception{
		 String Sql = "SELECT Inv_Code " +
					"FROM 	wh_inventory " +
					"WHERE	Branch_Code = '" + Branch_Code + "' " +
					"AND	LotNo = '" + LotNo + "' " +
					"AND	Shelf_Code = '" + Shelf_Code + "' " +
					"AND	Item_Code = '" + Item_Code + "' " +
					"AND	Ref_Code = '" + RefCode + "' " +
					"LIMIT 1 ";
		 
		 System.out.println("///////" + Sql);
		 
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
	
	
	
	public String CheckItemInventoryBalance(String Branch_Code,String Item_Code, String Wh_Code, String Balance_Date) throws Exception{
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
		try{
			rs = sqlsel.getReSultSQL(
				"SELECT InvBalance_Code " +
				"FROM 	wh_inventory_balance " +
				"WHERE	Item_Code = '" + Item_Code + "' " +
				"AND	Wh_Code = '" + Wh_Code + "' " +
				"AND	Balance_Date = '" + Balance_Date + "' " +
				"AND	Branch_Code = " + Branch_Code + " " +
				"LIMIT 1 "
			);

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
	
    public int UpdateInventory(int chk,String Symbol,String Branch_Code,String LotNo,String SqlDetail) throws  Exception{
    	System.out.println("print3");
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
        int Count = 0;
        
		try{	
			rs = sqlsel.getReSultSQL( SqlDetail );
			String Id = null;
			
			while(rs.next()){
				// Check Update Stock
				if( (Integer.parseInt( rs.getString("St_Code") ) == 1) ){
					// Add to Inventory
					LotNo = rs.getString("LotNo") ;

					Id = CheckID(Branch_Code,rs.getString("Item_Code"),rs.getString("LotNo"),rs.getString("Shelf_Code"));
					//System.out.println("******" + Id + "*" + rs.getString("Item_Code") + "*" + rs.getString("Shelf_Code") + "*****");
					//"item.St_Code," +

					if(Id != null){
						try{
							// Operation Update
							new OperationData(
								xUser,
								xPwd,
						   	 	"Update",
								"wh_inventory", 
								new String[][] {
							   	 	{"Qty", "Qty " + Symbol + rs.getString("Qty") },
							   	 	{"Modify_Date", "NOW()" },
							   	 	//{"Branch_Code", Branch_Code },
								},
								new String[][] {
								   	{"Inv_Code","'" +   Id  + "'"},
						   	 	}
							);
						}catch(Exception e){
							Messagebox.show("1 " + e.getMessage());
							continue;
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
					   	 			{"Item_Code", "'" + rs.getString("Item_Code") + "'" },
								  	{"Shelf_Code", "'" + rs.getString("Shelf_Code") + "'" },
								    {"LotNo", "'" + rs.getString("LotNo") + "'" },
						   	 		{"MFGDate", "'" + rs.getString("MFGDate") + "'" },
						   	 		{"EXPDate", "'" + rs.getString("EXPDate") + "'" },			
						   	 		{"Price", rs.getString("Productcost")  },
						   	 		{"Qty", rs.getString("Qty") },
						   	 		{"Create_Date", "NOW()" },
						   	 		{"Modify_Date", "NOW()" },
						   	 		{"Branch_Code", Branch_Code },
								},
								null
							);
				    	}catch(Exception e){
				    		Messagebox.show("2 " +e.getMessage());
							continue;
						}
			    	}
					
					// Add to Inventory Balance
					Id = CheckItemInventoryBalance(
						Branch_Code,	
						rs.getString("Item_Code"), 
						rs.getString("Wh_Code"), 
						rs.getString("DocDate")
					);
					
					System.out.println("************" + rs.getString("Wh_Code"));
					//System.out.println("******" + Id + "*******");
					if(Id != null){
						try{
							// Operation Update
							new OperationData(
									xUser,
									xPwd,
						   	 	"Update",
								"wh_inventory_balance", 
								new String[][] {
							   	 	{"Qty", 
							   	 		"(SELECT 	COALESCE(SUM(wh_inventory.Qty),0) " +
							   	 		"FROM 		wh_inventory " +
							   	 				
								   	 	"INNER JOIN wh_shelf " +
										"ON			wh_shelf.Shelf_Code = wh_inventory.Shelf_Code " +
										
										"INNER JOIN wh_area " +
										"ON			wh_area.Area_Code = wh_shelf.Area_Code " +
									
							   	 		"WHERE 		wh_inventory.Item_Code = '" + rs.getString("Item_Code") + "' " +
							   	 		"AND 		wh_area.Wh_Code = '" + rs.getString("Wh_Code") + "' " +
							   	 		
							   	 		"AND		wh_inventory.Branch_Code = " + Branch_Code + " " +
							   	 		")"
							   	 	},
							   	 	{"Balance_Date", "'" + rs.getString("DocDate") + "'"  },
							   	 	{"Branch_Code", Branch_Code },
							   	 	//{"LotNo","'" +   rs.getString("LotNo")  + "'"},
								   	//{"Shelf_Code","'" +   rs.getString("Shelf_Code")  + "'"},
								},
								new String[][] {
								   	 {"InvBalance_Code", Id }
						   	 	}
							);
						}catch(Exception e){
							Messagebox.show("3 " +e.getMessage());
							continue;
						}
			    	}else{
			    		try{
				    		// Operation Insert
							new OperationData(
									xUser,
									xPwd,
					   	 		"Insert",
								"wh_inventory_balance", 
								new String[][] {
					   	 			{"Item_Code", "'" + rs.getString("Item_Code") + "'" },
								  	{"Wh_Code", "'" + rs.getString("Wh_Code") + "'" },
								  	{"Qty", 
							   	 		"(SELECT 	SUM(wh_inventory.Qty) " +
							   	 		"FROM 		wh_inventory " +
							   	 				
								   	 	"INNER JOIN wh_shelf " +
										"ON			wh_shelf.Shelf_Code = wh_inventory.Shelf_Code " +
										
										"INNER JOIN wh_area " +
										"ON			wh_area.Area_Code = wh_shelf.Area_Code " +
									
							   	 		"WHERE 		wh_inventory.Item_Code = '" + rs.getString("Item_Code") + "' " +
							   	 		"AND 		wh_area.Wh_Code = '" + rs.getString("Wh_Code") + "' " +
							   	 		
							   	 		"AND		wh_inventory.Branch_Code = " + Branch_Code + " " +
							   	 		")"
							   	 	},
						   	 		{"Balance_Date", "'" + rs.getString("DocDate") +"'" },
						   	 		{"Branch_Code", Branch_Code },
							   	 	//{"LotNo","'" +   rs.getString("LotNo")  + "'"},
								   	//{"Shelf_Code","'" +   rs.getString("Shelf_Code")  + "'"},
								},
								null
							);
				    	}catch(Exception e){
				    		Messagebox.show("4 " +e.getMessage());
							continue;
						}
			    	}	
					Count++;
				}else{
					Count = 1;
				}
				
			}
			//System.out.println(Count);
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
    
    
    
    public int UpdateInventory2(int chk,String Symbol,String Branch_Code,String LotNo,String SqlDetail) throws  Exception{
    	System.out.println("print3");
    	ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
        int Count = 0;
        
		try{	
			rs = sqlsel.getReSultSQL( SqlDetail );
			String Id = null;
			
			while(rs.next()){
				// Check Update Stock
				if( (Integer.parseInt( rs.getString("St_Code") ) == 1) ){
					// Add to Inventory
					LotNo = rs.getString("LotNo") ;

					Id = CheckID2(Branch_Code,rs.getString("Item_Code"),rs.getString("LotNo"),rs.getString("Shelf_Code"),rs.getString("InvRef_Code"));
					//System.out.println("******" + Id + "*" + rs.getString("Item_Code") + "*" + rs.getString("Shelf_Code") + "*****");
					//"item.St_Code," +

					if(Id != null){
						try{
							// Operation Update
							new OperationData(
								xUser,
								xPwd,
						   	 	"Update",
								"wh_inventory", 
								new String[][] {
							   	 	{"Qty", "Qty " + Symbol + rs.getString("Qty") },
							   	 	{"Modify_Date", "NOW()" },
							   	 	//{"Branch_Code", Branch_Code },
								},
								new String[][] {
								   	{"Inv_Code","'" +   Id  + "'"},
						   	 	}
							);
						}catch(Exception e){
							Messagebox.show("1 " + e.getMessage());
							continue;
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
					   	 			{"Item_Code", "'" + rs.getString("Item_Code") + "'" },
								  	{"Shelf_Code", "'" + rs.getString("Shelf_Code") + "'" },
								    {"LotNo", "'" + rs.getString("LotNo") + "'" },
						   	 		{"MFGDate", "'" + rs.getString("MFGDate") + "'" },
						   	 		{"EXPDate", "'" + rs.getString("EXPDate") + "'" },			
						   	 		{"Price", rs.getString("Productcost")  },
						   	 		{"Qty", rs.getString("Qty") },
						   	 		{"Create_Date", "NOW()" },
						   	 		{"Modify_Date", "NOW()" },
						   	 		{"Branch_Code", Branch_Code },
								},
								null
							);
				    	}catch(Exception e){
				    		Messagebox.show("2 " +e.getMessage());
							continue;
						}
			    	}
					
					// Add to Inventory Balance
					Id = CheckItemInventoryBalance(
						Branch_Code,	
						rs.getString("Item_Code"), 
						rs.getString("Wh_Code"), 
						rs.getString("DocDate")
					);
					
					System.out.println("************" + rs.getString("Wh_Code"));
					//System.out.println("******" + Id + "*******");
					if(Id != null){
						try{
							// Operation Update
							new OperationData(
									xUser,
									xPwd,
						   	 	"Update",
								"wh_inventory_balance", 
								new String[][] {
							   	 	{"Qty", 
							   	 		"(SELECT 	COALESCE(SUM(wh_inventory.Qty),0) " +
							   	 		"FROM 		wh_inventory " +
							   	 				
								   	 	"INNER JOIN wh_shelf " +
										"ON			wh_shelf.Shelf_Code = wh_inventory.Shelf_Code " +
										
										"INNER JOIN wh_area " +
										"ON			wh_area.Area_Code = wh_shelf.Area_Code " +
									
							   	 		"WHERE 		wh_inventory.Item_Code = '" + rs.getString("Item_Code") + "' " +
							   	 		"AND 		wh_area.Wh_Code = '" + rs.getString("Wh_Code") + "' " +
							   	 		
							   	 		"AND		wh_inventory.Branch_Code = " + Branch_Code + " " +
							   	 		")"
							   	 	},
							   	 	{"Balance_Date", "'" + rs.getString("DocDate") + "'"  },
							   	 	{"Branch_Code", Branch_Code },
							   	 	//{"LotNo","'" +   rs.getString("LotNo")  + "'"},
								   	//{"Shelf_Code","'" +   rs.getString("Shelf_Code")  + "'"},
								},
								new String[][] {
								   	 {"InvBalance_Code", Id }
						   	 	}
							);
						}catch(Exception e){
							Messagebox.show("3 " +e.getMessage());
							continue;
						}
			    	}else{
			    		try{
				    		// Operation Insert
							new OperationData(
									xUser,
									xPwd,
					   	 		"Insert",
								"wh_inventory_balance", 
								new String[][] {
					   	 			{"Item_Code", "'" + rs.getString("Item_Code") + "'" },
								  	{"Wh_Code", "'" + rs.getString("Wh_Code") + "'" },
								  	{"Qty", 
							   	 		"(SELECT 	SUM(wh_inventory.Qty) " +
							   	 		"FROM 		wh_inventory " +
							   	 				
								   	 	"INNER JOIN wh_shelf " +
										"ON			wh_shelf.Shelf_Code = wh_inventory.Shelf_Code " +
										
										"INNER JOIN wh_area " +
										"ON			wh_area.Area_Code = wh_shelf.Area_Code " +
									
							   	 		"WHERE 		wh_inventory.Item_Code = '" + rs.getString("Item_Code") + "' " +
							   	 		"AND 		wh_area.Wh_Code = '" + rs.getString("Wh_Code") + "' " +
							   	 		
							   	 		"AND		wh_inventory.Branch_Code = " + Branch_Code + " " +
							   	 		")"
							   	 	},
						   	 		{"Balance_Date", "'" + rs.getString("DocDate") +"'" },
						   	 		{"Branch_Code", Branch_Code },
							   	 	//{"LotNo","'" +   rs.getString("LotNo")  + "'"},
								   	//{"Shelf_Code","'" +   rs.getString("Shelf_Code")  + "'"},
								},
								null
							);
				    	}catch(Exception e){
				    		Messagebox.show("4 " +e.getMessage());
							continue;
						}
			    	}	
					Count++;
				}else{
					Count = 1;
				}
				
			}
			//System.out.println(Count);
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

	public String UpdatePricePM(String Branch_Code,String Item_Code, String Wh_Code, String Balance_Date) throws Exception{
		
		ResultSet rs = null;
        SqlSelection sqlsel = new SqlSelection();
        sqlsel.uName = xUser;
        sqlsel.Pwd = xPwd;
		try{
			rs = sqlsel.getReSultSQL(
				"SELECT InvBalance_Code " +
				"FROM 	wh_inventory_balance " +
				"WHERE	Item_Code = '" + Item_Code + "' " +
				"AND	Wh_Code = '" + Wh_Code + "' " +
				"AND	Balance_Date = '" + Balance_Date + "' " +
				"AND	Branch_Code = " + Branch_Code + " " +
				"LIMIT 1 "
			);

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
	    
    
}
