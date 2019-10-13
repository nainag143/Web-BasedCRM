package org.com.onepointone.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.genesyslab.ail.component.framework.Message;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.voice.protocol.ConnectionId;
import com.genesyslab.platform.voice.protocol.tserver.AgentWorkMode;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genesyslab.platform.voice.protocol.tserver.events.EventUserEvent;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestDistributeUserEvent;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestSendEvent;

import org.com.onepointone.bean.FormBean;
import org.com.onepointone.bean.LoginBean;
import org.com.onepointone.dao.DataAccess;
import org.com.onepointone.dao.UtilDao;
import org.com.onepointone.requesthandler.StartTserverThread;

public class CtiAjax {
	
	DataAccess da1=null;
	UtilDao dao=null;
	private static final long serialVersionUID = -2128122335811219481L;

	
	public static HashMap<String, Session> formMap=new HashMap<String, Session>();

	
	public CtiAjax(){
		
		
		try {
			da1=new DataAccess(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public String dialCall(String phone){
		
	 	System.out.println("in CTIAJAX   "+phone);
		StartTserverThread.dial_Call(phone);
		
		
		// return StartTserverThread.iMessage.toString();
		return "connecting";
		
		
		
		
	}
	
	public String hangupCall(String thisDN){
		
		
	
		StartTserverThread.hangup_Call(thisDN);
		return "hangup";
	}
	public String unholdExt(String thisDN){
		
		
		System.out.println("Unhold"+thisDN);
		StartTserverThread.unhold_Ext(thisDN);
		return "hold";
	}
	
public String holdExt(String thisDN){
		
	System.out.println("hold"+thisDN);
		
		StartTserverThread.hold_Ext(thisDN);
		return "unhold";
	}
	
public  String agentBreak(String thisDN, String reason){
	//System.out.println(thisDN+"_________"+reason);
	StartTserverThread.agentBreak(thisDN, reason);
	return reason;
	
	
}

public  String agentReady(String thisDN){
	
	
	StartTserverThread.AgentReady(thisDN);
	return "break";
	
	
}





	
	
public String getCallData(String extn) throws JsonGenerationException, JsonMappingException, IOException{
	//System.out.println("extn"+extn);
	
	String temp='"'+extn+'"';
	
	//System.out.println(StartTserverThread.iMessage.toString());
	
	//return StartTserverThread.iMessage.messageName() + "-"+StartTserverThread.iMessage.getMessageAttribute("ThisDN");
	//String nn=(String)StartTserverThread.iMessage.getMessageAttribute("ThisDN");
	//System.out.println(temp);
	 
	 
		
		if(StartTserverThread.iMessage.toString().contains(temp))
		{
			
			
	  return StartTserverThread.iMessage.messageName()+"_"+StartTserverThread.iMessage.getMessageAttribute("ANI");
			
			
		}
		else
		{
			return "false";
		}
		
		
		

	 
 }
	

public String saveDataDb(FormBean bean,HttpSession session) throws ParseException{
	
 
	System.out.println("saveDataDb : [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
	System.out.println(bean.toString());
	System.out.println("]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
	
	String dn=(String)session.getAttribute("DN");
	
	
	dao=new UtilDao();


dao.UpdateMasterTable(bean.getDisposition(), bean.getSubdisposition(), bean.getSubsubdisposition(), bean.getAgentname(), bean.getAgentopo(), bean.getBookingcount(), bean.getCallbacktime(), bean.getMycode());


//dao.insertHistoryTable(String.valueOf(bean.getMycode()),bean.getDisposition(),bean.getSubDisposition(),bean.getSubsubdisposition(),bean.getRemark(), bean.getCampaignphone(), bean.getBatchID(), bean.getAgentname(), bean.getAppRefNo(), bean.getAgentopo(), bean.getCampaignName(), bean.getBookingcount(), bean.getConnecttime(), bean.getCallBackTime(), bean.getInsno(), bean.getVipno(), bean.getConnID());



dao.insertHistoryTable(bean);


if(StartTserverThread.masterrecordingid>0)
{
dao.UpdateDispositionInMasterRecordingTable(bean.getSubdisposition(),bean.getApprefno(),"",bean.getMasterrecordingid());
StartTserverThread.masterrecordingid=0;
}

if(StartTserverThread.localrecordingid>0)
{
dao.UpdateDispositionInLocalRecordingTable(bean.getSubdisposition(),bean.getCalltype(),String.valueOf(bean.getMycode()),bean.getLocalrecordingid());
StartTserverThread.localrecordingid=0;
}

StartTserverThread.saveCallingList(bean,dn);


return "DATA SAVED";
	
	
	
	
	
}




/*public String updateRecordingStartTime(String agentopo, ConnectionId conId, String campaignName, String campaignPhone, String recordingPath)
{
	
	System.out.println("entered Update"+agentopo+conId+campaignName+campaignPhone+recordingPath);
	
		UtilDao dao=new UtilDao();
		int masterrecordingid=dao.MasterRecordingTableEntry(agentopo,conId.toString(),campaignName,campaignPhone,recordingPath);
		int localrecordingid=dao.LocalRecordingTableEntry(agentopo,conId.toString(),campaignName,campaignPhone,recordingPath);
		StartTserverThread.masterrecMap.put(conId, masterrecordingid);
		StartTserverThread.localrecMap.put(conId, localrecordingid);
		
		System.out.println(" ************   masterrecordingid :"+masterrecordingid + " localrecordingid "+localrecordingid);
		return "updateRecordingStartTime";

		

}*/








public String getDisposition()
{
	
	String dispSql = "select * from DISPOSITION";
	StringBuffer dispsb=new StringBuffer();
	

	 try {
			
			da1.setSql(dispSql);
			da1.executeQuery();
			dispsb.append("<option value=''>Select</option>");
			
			while(da1.next()){
				String id=da1.getString("Id");
				String Disp=da1.getString("DispName");
				dispsb.append("<option value='"+id+"'>"+Disp+"</option>");
			}
				
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {

			if (da1 != null) {
				try {
					da1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	         // System.out.println(dispsb.toString());
		return dispsb.toString();
	}



public String getSUBDisposition(String param)
{
	
	String subdispSql = "select * from SUBDISPOSITION WHERE dispid="+param;
	StringBuffer subdispsb=new StringBuffer();
	 try {
			da1.setSql(subdispSql);
			da1.executeQuery();
			subdispsb.append("<option value=''>Select</option>");
		
			while(da1.next()){
				String id=da1.getString("Id");
				String SubDisp=da1.getString("SubDispName");
				subdispsb.append("<option value='"+id+"'>"+SubDisp+"</option>");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {

			if (da1 != null) {
				try {
					da1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	       // System.out.println(subdispsb.toString());
		return subdispsb.toString();
	}





public String getSBSUBDisposition(String param1)
{
	
	String sbsubdispSql = "select * from SBSUBDISPOSITION WHERE subdispid="+param1;
	StringBuffer sbsubdispsb=new StringBuffer();
	 try {
			da1.setSql(sbsubdispSql);
			da1.executeQuery();
			sbsubdispsb.append("<option value=''>Select</option>");
			
			//System.out.println(dispSql);
			while(da1.next()){
				String id=da1.getString("Id");
				String sbSubDisp=da1.getString("SbSubDispName");
				sbsubdispsb.append("<option value='"+id+"'>"+sbSubDisp+"</option>");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		finally {

			if (da1 != null) {
				try {
					da1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	 		//System.out.println(sbsubdispsb.toString());
		return sbsubdispsb.toString();
	}

//-------------------------------//


	
/*
public SocketResponseBean getCallDetail(String extn){
	
	//System.out.println("SocketResponseBean xtn"+extn);
	
	String temp='"'+extn+'"';
	
  SocketResponseBean bean=new SocketResponseBean();
		
		if(StartTserverThread.iMessage.toString().contains(temp))
		{
			
			
	   bean.setConid(StartTserverThread.iMessage.getMessageAttribute("ConnID").toString());			
	   bean.setEventName(StartTserverThread.iMessage.messageName());			
	   bean.setPhone(StartTserverThread.iMessage.getMessageAttribute("ANI").toString());			
		return bean;
		}
		else
		{
return bean;			//return "false";
		}
		*/
		


public String foroneventhook(String extn){
	System.out.println("akagdk  foroneventhook");
	//RequestAgentNotReady requestAgentNotReady = RequestAgentNotReady.create(extn, AgentWorkMode.AfterCallWork);
	
	return extn;
}


	 
 }
	
	
		
	

