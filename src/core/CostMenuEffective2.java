package core;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class CostMenuEffective2 extends Window{
	public String SessionUserCode;

	private static final long serialVersionUID = 7394394042153638109L;
	
	public void onCreate() throws Exception {

	}

	public void xB01_Click(){
		Executions.sendRedirect("/CostEffective2.zul");
	}
	
	public void xB04_Click(){

		Executions.sendRedirect("/CostLogin2.zul");
	}
}
