package core;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

public class CostMenuDosing extends Window{
	private static final long serialVersionUID = 7394394042153638109L;
	
	public String SessionUserCode;

	public void onCreate() throws Exception {
		((Radio) getFellow("Rd1")).setChecked(true);
	}

	public void xB01_Click(){

		if( ((Radio) getFellow("Rd1")).isChecked() )
				Executions.sendRedirect("/Cost_1.zul");
		else if( ((Radio) getFellow("Rd2")).isChecked() )
				Executions.sendRedirect("/Cost_2.zul");
		else if( ((Radio) getFellow("Rd3")).isChecked() )
				Executions.sendRedirect("/Cost_3.zul");
		else if( ((Radio) getFellow("Rd4")).isChecked() )
				Executions.sendRedirect("/Cost_4.zul");
		else if( ((Radio) getFellow("Rd5")).isChecked() )
				Executions.sendRedirect("/Cost_5.zul");
	}
	
}
