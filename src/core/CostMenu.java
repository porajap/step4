package core;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

public class CostMenu extends Window{
	String xUsername;
	private Session session = Sessions.getCurrent();
	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {
		if (session.getAttribute("User") == null) {
			Executions.sendRedirect("/CostLogin.zul");
		} else {
			xUsername = (String)session.getAttribute("User");
        }
	}

	public void xB01_Click(){
		Executions.sendRedirect("/CostMenuEffective.zul");
	}
	
	public void xB02_Click(){
		Executions.sendRedirect("/Cost.zul");
	}
	
	public void xB03_Click(){
		Executions.sendRedirect("/CostMenuDosing.zul");
	}
	
	public void xB04_Click(){
		session.removeAttribute("User");
		Executions.sendRedirect("/CostMenu.zul");
	}
}
