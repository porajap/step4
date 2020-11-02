package process;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Window;

public class menu extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7286373636062421801L;
	
	private Window WinProcess;
	private Include Include_Src;

	public void onCreate() {
		
	}
	// Click
	public void onClick$Treeitem_0(Event event) throws Exception {
		callPage("main.zul");
	}
	public void onClick$Treeitem_1(Event event) throws Exception {
		callPage("receivein.zul");
	}
	public void onClick$Treeitem_2(Event event) throws Exception {
		callPage("sendsterile.zul");
	}
	public void onClick$Treeitem_3(Event event) throws Exception {
		callPage("payout.zul");
	}
	
	public void onClick$Treeitem_4(Event event) throws Exception {
		callPage("trackingnew.zul");
	}
	
	public void onClick$Treeitem_5(Event event) throws Exception {
		callPage("stock.zul");
	}
	
	public void onClick$Treeitem_6(Event event) throws Exception {
		callPage("");
	}
	public void onClick$Treeitem_7(Event event) throws Exception {
		callPage("reports_hn.zul");
	}
	public void onClick$Treeitem_8(Event event) throws Exception {
		callPage("report.zul");
	}
	public void onClick$Treeitem_9(Event event) throws Exception {
		callPage("item.zul");
	}
	
	public void onClick$Treeitem_10(Event event) throws Exception {
		callPage("../assets/pdf/คู่มือ.pdf");
	}
	
	public void onClick$Treeitem_11(Event event) throws Exception {
		callPage("");
	}
	
	public void onClick$wbe(Event event) throws Exception {
		callPage("warn_before_exp.zul");
	}
	
	public void onClick$we(Event event) throws Exception {
		callPage("warn_exp.zul");
	}
	
	public void onClick$rec(Event event) throws Exception {
		callPage("receivein.zul");
	}
	
	public void onClick$rec_retro(Event event) throws Exception {
		callPage("receivein_retroact.zul");
	}
	
	public void onURIChange$Include_Src(Event event) throws Exception {
		String S_Src = Include_Src.getSrc();
    	S_Src.replace("/", "");
    	
    	
	}
	private void callPage(String page)throws Exception {
		onProcess(true);
		
		Include_Src.setSrc(null);
		Include_Src.setSrc(page);
		
		onProcess(false);
	}
	
	 public void onProcess(boolean b) throws Exception {
	    	
	    	WinProcess.setVisible(b);
	    	
	    	if(b) {
		    	WinProcess.setFocus(b);
		    	WinProcess.setPosition("center");
		    	WinProcess.setMode("modal");
	    	}
	    }
}
