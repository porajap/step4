package connect;

import java.sql.ResultSet;

public class CusQrCode {
    //-------------------- Variable -------------------------   
    private String StrUsername, StrPassword;
    private ResultSet rs = null;
    //-------------------- Create (init) -------------------------	
    public void onCreate() {

    }

    //-------------------- Get(), Set() -------------------------	
    private String getUsername() {
        return StrUsername;
    }

    private String getPassword() {
        return StrPassword;
    }

    private void setUsername(String username) {
        StrUsername = username;
    }

    private void setPassword(String password) {
        StrPassword = password;
    }

    //-------------------- SET -------------------------	
    public boolean set(String StrUser, String StrPassword) {
        if (!StrUser.trim().equals("")) {
            setUsername(StrUser);
        } else {
            return false;
        }

        if (!StrPassword.trim().equals("")) {
            setPassword(StrPassword);
        } else {
            return false;
        }
        return true;
    }

    //-------------------- Events -------------------------	
    public String[] getCusName(String xBarcode) throws Exception {
        String StrSql = "SELECT dtoh.Barcode,customer.FName,dtoh.`password`"+
				  "FROM dtoh INNER JOIN customer ON dtoh.Cus_Code = customer.Cus_Code "+
				  "WHERE dtoh.Barcode = '" + xBarcode + "'";
       	
        SqlSelection sqlsel = new SqlSelection();
        
        //System.out.println(StrSql);
        String data[] = new String[1];
        try {
        	sqlsel.uName = getUsername();
            sqlsel.Pwd =	getPassword();
            rs = sqlsel.getReSultSQL(StrSql);

            if (rs.next()) {
            	data[0] = rs.getString("FName");
            	data[1] = rs.getString("password");
            }

        } catch (Exception e) {
        	e.printStackTrace();
            return data;
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            sqlsel.closeConnection();
        }
        return data;
    }
}
