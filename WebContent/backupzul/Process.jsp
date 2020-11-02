<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">

<jsp:useBean id="Conn" class="connect.FormLogin" scope="request">
<jsp:setProperty name="Conn" property="*"/>
</jsp:useBean>

<%
	String[] Data = new String[]{
		"UserName",
        "Password",
        "EmpCode",
        "IsCancel",
        "Lang",
        "ID",
	
	};

	//Clear Session
	for(int i=0; i< Data.length;i++){
		session.setAttribute(Data[i], null);
		//System.out.println( Data[i] );	
	}	
	//Get Parameter
	String Username = request.getParameter("login[Username]");
	String Password = request.getParameter("login[Password]");

    session.setAttribute("xUsrName", Username);
    session.setAttribute("xPassword", Password);

    String xUsername = (String)session.getAttribute("xUsrName");
	String xPassword = (String)session.getAttribute("xPassword");
	//String xFromName = (String)session.getAttribute("FromName");
	//System.out.println(xUsername + " || " + xPassword);
	
	//System.out.println(Username+"-"+Password);
	
	//Check Enter Form
	String[] ss = Conn.onLogin(Username, Password);
	int i=0;
	//Set Session
	if(ss != null){
		for(i=0; i< ss.length; i++)
			

	    session.setMaxInactiveInterval(60*60*24);
	    
	    try{
	    	Conn.onLigin(ss[3]);
		}catch(Exception e){
			
			System.out.println("ERrror 22 :"+e.getMessage());	
			e.printStackTrace();
			response.sendRedirect("Login2.zul");	
		}
	    
	    response.sendRedirect("menu.zul");
	}else{
		response.sendRedirect("Login2.zul");	
	}
%>