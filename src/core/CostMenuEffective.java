package core;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;


public class CostMenuEffective extends Window{
	public String SessionUserCode;

	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {

	}

	public void xB01_Click(){
		Executions.sendRedirect("/CostEffective.zul");
	}

}
