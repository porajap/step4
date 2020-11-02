package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;

import com.poseintelligence.cssd.utillity.Cons;

import general.DateTime;

public class main extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventQueue<Event> qe;
	
	private Window Window_main;
	private Hbox Hbox_main;
	
	
	
	public void onCreate() throws Exception{
		init();
		
	}
	public void init() throws Exception{
		
		
			onDisplay(1);
			onDisplay(2);
			onDisplay(3);
			onDisplay(4);
	}
	
public void onDisplay(int selected) throws Exception{
		
//		com.poseintelligence.cssd.db.Conn c = new com.poseintelligence.cssd.db.Conn();
//        Class.forName(c.S_MYSQL_DRIVER);
//        Connection conn = java.sql.DriverManager.getConnection(c.getHost(S_DB), c.S_USER, c.S_PASSWORD);
//        conn.setAutoCommit(false);
//        
//        Statement stmt = conn.createStatement();
//    	ResultSet rs = null;
        
		try{	
			
			//System.out.print(getSql(selected));
			
//			rs = stmt.executeQuery(getSql(selected));
					
//			if(rs.next()){
				switch (selected) {
					case 1:
						
						Hbox_main.appendChild(getWindow("")); break;
					case 2:
						
						Hbox_main.appendChild(getWindow2("")); break;
					case 3:
						
						Hbox_main.appendChild(getWindow3("")); break;
					case 4:
						
						Hbox_main.appendChild(getWindow4("")); break;
					default: break;
				}
				
//			}
			
		}catch(Exception e){
			e.printStackTrace();
//		}finally{
//			if (rs != null) {
//                rs.close();
//            }
//            
//            if (stmt != null) {
//                stmt.close();
//            }
//            
//            if (conn != null) {
//                conn.close();
//            }
//		}
		}
    }

	private Window getWindow(String S_Count) {
		Window w = new Window();
//		w.setBorder("normal");
//		w.setSclass("sysWin");
//		w.setHeight("175px");
//		w.setWidth("350px");
		
		Button btn = new Button("อุปกรณ์ใกล้หมดอายุ");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("175px");
		btn.setWidth("350px");
		btn.setStyle("background: #FFC107; font-size: 23px; color:#ffffff;border-radius: 4px;");
		btn.setHref("warn_before_exp.zul");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				
//				System.out.println("22222222222222222222222222");
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_DEPARTMENT_RECEIVE, null, null));
	        }
	    });
		
		Separator sep = new Separator();
//		sep.setHeight("35px");
		
//		w.appendChild(new Caption("เอกสารใหม่รับเข้าสต๊อก"));
//		w.appendChild(new Label("(" + S_Count + " เอกสาร)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}
	
	private Window getWindow2(String S_Count) {
		Window w = new Window();
//		w.setBorder("normal");
//		w.setSclass("sysWin");
//		w.setHeight("175px");
//		w.setWidth("350px");
		
		Button btn = new Button("อุปกรณ์หมดอายุ");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("175px");
		btn.setWidth("350px");
		btn.setStyle("background: #DC3545; font-size: 23px; color:#ffffff;border-radius: 4px;");
		btn.setHref("warn_exp.zul");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_EXPIRING_SOON, null, null));
	        }
	    });
		
		Separator sep = new Separator();
//		sep.setHeight("35px");
		
//		w.appendChild(new Caption("อุปกรณ์ใกล้หมดอายุ"));
//		w.appendChild(new Label("(" + S_Count + " รายการ)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}
	
	private Window getWindow3(String S_Count) {
		Window w = new Window();
//		w.setBorder("normal");
//		w.setSclass("sysWin");
//		w.setHeight("175px");
//		w.setWidth("350px");
		
		Button btn = new Button("เอกสารรับเข้าสต็อกใหม่");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("175px");
		btn.setWidth("350px");
		btn.setStyle("background: #28A745; font-size: 23px; color:#ffffff;border-radius: 4px;");
		btn.setHref("receivein.zul");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_EXPIRE, null, null));
	        }
	    });
		
		Separator sep = new Separator();
//		sep.setHeight("35px");
		
//		w.appendChild(new Caption("อุปกรณ์หมดอายุ"));
//		w.appendChild(new Label("(" + S_Count + " รายการ)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}
	
	private Window getWindow4(String S_Count) {
		Window w = new Window();
//		w.setBorder("normal");
//		w.setSclass("sysWin");
//		w.setHeight("175px");
//		w.setWidth("350px");
		
		Button btn = new Button("เอกสารรับเข้าสต็อกย้อนหลัง");
		btn.setSclass("btn-success");
		//btn.setIconSclass("z-icon-ban");
		btn.setHeight("175px");
		btn.setWidth("350px");
		btn.setStyle("background: #D882CC; font-size: 23px; color:#ffffff;border-radius: 4px;");
		btn.setHref("receivein_retroact.zul");
		//btn.setImage("/images/ic_search_c.png");
		//btn.setHoverImage("/images/pose_favicon.png");
		
		btn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				
				System.out.println("22222222222222222222222222");
				qe = EventQueues.lookup(Cons.EVENTQUEUE_CONNECTION, EventQueues.DESKTOP, true);
				qe.publish(new Event(Cons.EVENT_CALL_EXPIRE, null, null));
	        }
	    });
		
		Separator sep = new Separator();
//		sep.setHeight("35px");
		
//		w.appendChild(new Caption("อุปกรณ์หมดอายุ"));
//		w.appendChild(new Label("(" + S_Count + " รายการ)")) ;		
		w.appendChild(sep);
		w.appendChild(btn);
		
		return w;
	}

}
