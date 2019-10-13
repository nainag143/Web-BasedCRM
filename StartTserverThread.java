package org.com.onepointone.requesthandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.com.onepointone.bean.FormBean;
import org.com.onepointone.bean.LoginBean;
import org.com.onepointone.dao.DBConnection;
import org.com.onepointone.dao.UtilDao;

import ws.server.WsServer;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.voice.protocol.ConnectionId;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.AddressType;
import com.genesyslab.platform.voice.protocol.tserver.AgentWorkMode;
import com.genesyslab.platform.voice.protocol.tserver.CallType;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genesyslab.platform.voice.protocol.tserver.ControlMode;
import com.genesyslab.platform.voice.protocol.tserver.MakeCallType;
import com.genesyslab.platform.voice.protocol.tserver.RegisterMode;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAbandoned;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAttachedDataChanged;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDNBackInService;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDNOutOfService;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDestinationBusy;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDialing;
import com.genesyslab.platform.voice.protocol.tserver.events.EventError;
import com.genesyslab.platform.voice.protocol.tserver.events.EventEstablished;
import com.genesyslab.platform.voice.protocol.tserver.events.EventHeld;
import com.genesyslab.platform.voice.protocol.tserver.events.EventNetworkReached;
import com.genesyslab.platform.voice.protocol.tserver.events.EventOffHook;
import com.genesyslab.platform.voice.protocol.tserver.events.EventOnHook;
import com.genesyslab.platform.voice.protocol.tserver.events.EventReleased;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRetrieved;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRinging;
import com.genesyslab.platform.voice.protocol.tserver.events.EventUserEvent;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;
import com.genesyslab.platform.voice.protocol.tserver.requests.party.RequestHoldCall;
import com.genesyslab.platform.voice.protocol.tserver.requests.party.RequestMakeCall;
import com.genesyslab.platform.voice.protocol.tserver.requests.party.RequestReleaseCall;
import com.genesyslab.platform.voice.protocol.tserver.requests.party.RequestRetrieveCall;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestDistributeUserEvent;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestSendEvent;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


public class StartTserverThread implements  Runnable{

	final  static Logger logger = Logger.getLogger(StartTserverThread.class);
	 Thread receiver;
	public static Message iMessage;
	static TServerProtocol tServerProtocol ;
	public static  boolean isRunning;
	public static  boolean isConnectionOpen;
	public static String extn;
	static ConnectionId connID = null;
	public static String campaignmode="";
	public static int modeid=0;
	public static long MyCode = 0;
	private static String calltype = "";
	private static int CALLTYPE = 0;
	public static String campaignphone = "";
	public static long RecordId = 0;
	private static int CurrentStatusId = 0;
	public static long callservertime=0;
	private static long callid=0;
	static ConnectionId IVRconnID = null;
	public static String BreakStatus = "";
	public static String callingListName;
	public static String agentGroupName;
	public static long ani=0;
	public static long dnis=0;
	public static String connecttime="";
	public static String RecordingPath = "";
	public static String disptype="";
	public static int recordHandle=0;
	private static int attempts;
	private static int ocsApplicationID;
	public static String campaignName;
	public Session webSocketSession;
	public static long masterrecordingid=0;
	public static long localrecordingid=0;
	/* */
	
	public static ConnectionId rcid=null;
	/* */
	public static HashMap<String, Session> userMap=new HashMap<String, Session>();
	
	public static Session websocketsession;
	
	public static HashMap<String,Session> dnMap=new HashMap<String,Session>();
	
	public static HashMap<String,ConnectionId> callMap=new HashMap<String,ConnectionId>();
	
	public static HashMap<String,Integer> callLogMap=new HashMap<String,Integer>();

	public static HashMap<String,Integer> masterrecMap=new HashMap<String,Integer>();

	public static HashMap<String,Integer> localrecMap=new HashMap<String,Integer>();

	public static HashMap<String,String> opoMap=new HashMap<String,String>();
	public static HashMap<String,String> agentNameMap=new HashMap<String,String>();

	
	
	public static String msg;
	
	
	StartTserverThread()
	{
		//System.out.println("StartTserverThread Counstructor ");
		
	}
	
	public void start()
	{
		run();
	
	
	}
	
	
	
	@Override
	 public void run() {
		// TODO Auto-generated method stub
	
		while (isRunning)
		{
			
			////System.out.println("isRunning true ");
			if (tServerProtocol.getState() != ChannelState.Opened)
			{
				try 
				{
					Thread.currentThread().sleep(50);

				} 
				catch (InterruptedException e) 
				{
					//System.out.println(" run Error : Thread.currentThread().sleep(50)");
				}
				continue;
			}
			try 
			{
				iMessage =  tServerProtocol.receive();
				if(iMessage != null)
				{
					//WsServer.sendMessageForClient(extn,iMessage.messageName());
					////System.out.println("[FROM RUN] :" + iMessage.toString());
					String thisDN="";
					if(!iMessage.messageName().equals("EventLinkConnected") && !iMessage.messageName().equals("EventUserEvent") && !iMessage.messageName().equals("EventAttachedDataChanged"))
					{
						thisDN=iMessage.getMessageAttribute("ThisDN").toString();
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						WsServer.sendMessageForClient(thisDN,iMessage.messageName()+" "+thisDN+"|null|null|null|null|null|null|null|null|null|null");
					
					
					
					}
					
					
					
					
					if(iMessage.messageName().equals("EventAgentNotReady"))
					{
						EventAgentNotReady eventAgentNotReady = (EventAgentNotReady) iMessage;
						System.out.println("[EventAgentNotReady] "+iMessage.toString());
						
						KeyValueCollection reasonCodes = new KeyValueCollection();
						reasonCodes=eventAgentNotReady.getReasons();
						String status="";
						if (eventAgentNotReady.getUserData() != null)
						{
						thisDN=iMessage.getMessageAttribute("ThisDN").toString();
						
						status=iMessage.getMessageAttribute("AgentWorkMode").toString();
						
						if(status.equals("ManualIn"))
						{
							WsServer.sendMessageForClient(thisDN,"ManualIn"+"|"+campaignName+"|"+campaignmode+"|null|null|null|null|null|null|null|null");
	
						}
						else if(status.equals("AfterCallWork"))
						{
							WsServer.sendMessageForClient(thisDN,"AfterCallWork"+"|"+campaignName+"|"+campaignmode+"|null|null|null|null|null|null|null|null");
	
						}
						
						}
						
					
						//JOptionPane.showMessageDialog(null, eventagentnotready.getReasons());
						/*if(eventAgentNotReady.getReasons().containsKey("ReasonCode"))
						{
							//BreakStatus=reasonCodes.getPair("ReasonCode").getStringValue();
							WsServer.sendMessageForClient(thisDN,reasonCodes.getPair("ReasonCode").getStringValue()+"|"+campaignName+"|"+campaignmode+"|null|null|null|null|null|null|null|null");

						}
						
						WsServer.sendMessageForClient(thisDN,iMessage.messageName()+"|"+campaignName+"|"+campaignmode+"|null|null|null|null|null|null|null|null");
*/
					}
				 
					
					
					/************ comment by jaydeep */
					//if(iMessage.messageName().equals("EventUserEvent") || iMessage.messageName().equals("EventAgentReady") )
					
					
					
					
					
					if(iMessage.messageName().equals("EventUserEvent"))
					{
						EventUserEvent eventUserEvent = (EventUserEvent) iMessage;
						////System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
						
						////System.out.println(eventUserEvent.getUserData().getPair("GSW_USER_EVENT").getStringValue()+"{{{{{{{{}}}}}}}}}}");
						
						////System.out.println("[EventUserEvent] "+iMessage.toString());
						if (eventUserEvent.getUserData() != null)
						{
						thisDN=iMessage.getMessageAttribute("ThisDN").toString();
						
						
						
						TimeUnit.SECONDS.sleep(5);
						WsServer.sendMessageForClient(thisDN,iMessage.messageName()+" "+thisDN+"|"+campaignName+"|"+campaignmode+"|null|null|null|null|null|null|null|null");
						}
						if(eventUserEvent.getUserData().containsKey("GSW_CAMPAIGN_MODE")){
							
							campaignmode=eventUserEvent.getUserData().getPair("GSW_CAMPAIGN_MODE").getStringValue();
							
						}
						
						
						
						
				//|| eventUserEvent.getUserData().getPair("GSW_AGENT_REQ_TYPE").getStringValue().equals("RecordReschedule")
						if(eventUserEvent.getUserData().getPair("GSW_USER_EVENT").getStringValue().equals("RecordProcessedAcknowledge")||  eventUserEvent.getUserData().getPair("GSW_USER_EVENT").getStringValue().equals("RecordRescheduleAcknowledge") )
					
						{
							////System.out.println(" after dispose call, in  if loop********************************************************");
							thisDN=iMessage.getMessageAttribute("ThisDN").toString();
							
							getAgentReady(thisDN);
						}
					}
					
					/*
					 * Event Estabslished Recording start
					 * 
					 */
					
					if(iMessage.messageName().equals("EventEstablished") )
					{
						EventEstablished eventEstablished = (EventEstablished) iMessage;
						if (eventEstablished.getUserData() != null)
						{
							
							if(eventEstablished.getUserData().containsKey("GSW_CAMPAIGN_MODE")){
								
								campaignmode=eventEstablished.getUserData().getPair("GSW_CAMPAIGN_MODE").getStringValue();
							
								/*if(campaignmode.equals("Predictive") || campaignmode.equals("Progressive") )
								{
								UtilDao dao =new UtilDao();
								CallLogRecordId=dao.CallLogEntryPredictive(campaignphone,agentname, extn,PangeumLogin.opoid, apprefno, "Predictive", String.valueOf(MyCode));
								logger.info("CallLogRecordId : "+CallLogRecordId);
								}*/
								thisDN=iMessage.getMessageAttribute("ThisDN").toString();
								
							
	
							}
							
							
							
						
						}
							
						
						WsServer.sendMessageForClient(thisDN,iMessage.messageName()+"|"+campaignName+"|"+campaignmode+"|null|null|null|null|null|null|null|null");

						
						
					}
					
					if(iMessage.messageName().equals("EventAttachedDataChanged") )
					{
						////System.out.println("[FROM EVNTDATA] :" + iMessage.toString());
						
						EventAttachedDataChanged eventattacheddatachanged = (EventAttachedDataChanged) iMessage;
						//eventEstablished.getUserData().containsKey("GSIP_REC_FN")
						
						if (eventattacheddatachanged.getUserData() != null)
						{
					// String mobileNumber=eventattacheddatachanged.getANI();
							ocsApplicationID = eventattacheddatachanged.getUserData().getPair("GSW_APPLICATION_ID").getIntValue();
							campaignName=eventattacheddatachanged.getUserData().getPair("GSW_CAMPAIGN_NAME").getStringValue();
											
							callingListName = eventattacheddatachanged.getUserData().getPair("GSW_CALLING_LIST").getStringValue();
							recordHandle = eventattacheddatachanged.getUserData().getPair("GSW_RECORD_HANDLE").getIntValue();

		
			
							
							
					 String mobileNumber=eventattacheddatachanged.getUserData().getPair("GSW_PHONE").getStringValue();
					 mobileNumber=StringUtils.right(mobileNumber,10);
					
					////System.out.println("<<<<<<<RecordingPath>>>>>>" + RecordingPath);
					 String connectionID=eventattacheddatachanged.getConnID().toString();
					 thisDN=iMessage.getMessageAttribute("ThisDN").toString();
					 ////System.out.println("[EventAttachedDataChanged] :"+thisDN+iMessage.messageName()+"_"+campaignName+"_"+campaignMode+"_"+mobileNumber+"_"+connectionID);
					 int mycode=0;
					 String appRefNum="";
					 if(eventattacheddatachanged.getUserData().containsKey("TMasterID"))
                 	{
						 
				 mycode= eventattacheddatachanged.getUserData().getPair("TMasterID").getIntValue();
						
                 	}
					 if(eventattacheddatachanged.getUserData().containsKey("TMasterID"))
	                 	{
							 
					   appRefNum= eventattacheddatachanged.getUserData().getPair("APPREFNO").getStringValue();
							
					 
	                 	}
						if(eventattacheddatachanged.getUserData().containsKey("GSIP_REC_FN"))
						{
							
							
							RecordingPath = eventattacheddatachanged.getUserData().getPair("GSIP_REC_FN").getStringValue();
							
							RecordingPath = RecordingPath + "_pcmu.wav";
							

						}

						/*//System.out.println("[[[[[[[[[[[[[eventattacheddatachanged[[[[[[[[[[[");
						//System.out.println("ocsApplicationID : "+ocsApplicationID + " callingListName : "+callingListName +" recordHandle :"+recordHandle + " mobileNumber :"+mobileNumber +" mycode : "+mycode +" connectionID in string :"+connectionID +" RecordingPath :"+RecordingPath);
						//System.out.println("]]]]]]]]]]eventattacheddatachanged]]]]]]]]]]]]]]");
		*/
				callMap.put(thisDN, eventattacheddatachanged.getConnID());

	updateRecordingStartTime(thisDN,opoMap.get(thisDN),  eventattacheddatachanged.getConnID(),  campaignName,  mobileNumber,  RecordingPath);
				
	
	
	entryCallLog( mobileNumber, agentNameMap.get(thisDN), thisDN,  opoMap.get(thisDN), appRefNum, campaignmode,Integer.toString(mycode));
	
 
				
					 WsServer.sendMessageForClient(thisDN,iMessage.messageName()+" "+thisDN+"|"+campaignName+"|"+campaignmode+"|"+mobileNumber+"|"+connectionID+"|"+new java.util.Date()+"|"+mycode +"|"+RecordingPath+"|"+ocsApplicationID+"|"+callingListName+"|"+recordHandle);
						}
						
						
						
						
					
					}
					
					if(iMessage.messageName().equals("EventReleased") )
					{
						EventReleased eventReleased=(EventReleased)iMessage;
						thisDN=iMessage.getMessageAttribute("ThisDN").toString();
						
						RequestAgentNotReady requestAgentNotReady = RequestAgentNotReady.create(thisDN, AgentWorkMode.AfterCallWork);
						iMessage = tServerProtocol.request(requestAgentNotReady);
						//System.out.println("EventReleased : "+iMessage.toString());
						UtilDao dao=new UtilDao();
						//dao.updateCurrentStatus(PangeumLogin.opoid,CurrentStatusId,extn,processname);
//System.out.println("[EventReleased] : "+callLogMap.get(thisDN) + callMap.get(thisDN) + campaignName + masterrecMap.get(connID) + localrecMap.get(connID));
		updateRecordingEndTime(callLogMap.get(thisDN),callMap.get(thisDN),campaignName,"customer",masterrecMap.get(thisDN),localrecMap.get(thisDN));

						
						
						WsServer.sendMessageForClient(thisDN,"AfterCallWork |null|null|null|null|null|null|null|null|null|null");
						
					}
					
					
					String conid="";
					
					if(connID==null)
					{
						conid="";
					}
					else
					{
						conid=connID.toString();
					}
					
					/*
					 * Based On campaignmode decide the modeid value
					 * 1-Preview
					 * 2-Predictive
					 * 3-Progressive
					 */
					if(campaignmode=="Preview")
					{
						modeid=1;
					}
					else if(campaignmode=="Predictive")
					{
						modeid=2;
					}
					else  if(campaignmode=="Progressive")
					{
						modeid=3;
					}
					////System.out.println("ConnID_"+connID.toString()+" campaignmode_"+campaignmode+" campaignphone_"+campaignphone+iMessage.messageName());

					/*try{
						UtilDao dao=new UtilDao();
						EventBean bean=new EventBean();

						bean.setConnid(conid);
						//logger.info(iMessage.messageName() +" MyCode : " +MyCode);
						bean.setMycode(MyCode);
						
						////System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+LoginServelet.extension);
						// change this agneid from after db operation
						bean.setAgentid(1);
						bean.setEventcode(iMessage.messageId());
						
						 *  Based on Campaign Name decide the campaignid
						 
						bean.setCampaignid(1);
						//bean.setCampaignid(campaignid); change the name
						bean.setAgentgroup(1); //change the name
						bean.setQueueid(0);//
						
						
						 *  Based on Campaign Name decide the campaignid
						 *  1-Inbound
						 *  2-outbound
						 
						
						bean.setCalltype(CALLTYPE);
						if(campaignphone!=null && !campaignphone.equals(""))
						{
						//bean.setPhone(Long.valueOf(campaignphone));
						bean.setPhone(Long.valueOf(StringUtils.right(campaignphone,10)));
						}
						else
						{
							bean.setPhone(0);
						}

						bean.setMode(modeid);
						bean.setRecordid(RecordId);
						bean.setCurrentstatusid(CurrentStatusId);
						bean.setGenysystime(callservertime);
						bean.setGenysysid(callid);
						// Called Stat Function In My Sql DB
						dao.setEventLog(bean);
						}
					catch(Exception e)
					{
						//System.out.println("Error IN DB OPERTATION :");
						e.printStackTrace();
						logger.error(e.getMessage());
					}*/
				}
				else
				{
					
					//System.out.println("Do Nothing");
				}
			}
			
			catch (InterruptedException e) 
			{
				//System.out.println("run InterruptedException "+e.getMessage());
			}
			
			catch (Exception e) 
			{
				//System.out.println("run Exception ");
				e.printStackTrace();
			}
			//checkReturnedMessage(iMessage);
		}

	}
	
	
	
	public static  void CreateConnection(String TIP, int TPort) {
		// TODO Auto-generated method stub

		
		
		
		try
		{
			tServerProtocol = new TServerProtocol(new Endpoint(TIP,TIP,TPort));
			tServerProtocol.setClientName("AgentDesktop");
			isRunning = true; 
			tServerProtocol.open();
		 
		} 
		catch (RegistrationException e) 
		{
			//System.out.println("CreateConnection RegistrationException");
 			isConnectionOpen = false;
			isRunning = false;
			return;
		} 
		catch (ProtocolException e) 
		{
			//System.out.println("CreateConnection ProtocolException");
 			isConnectionOpen = false;
			isRunning = false;
			return;
		} 
 
		catch (InterruptedException e) 
		{
			//System.out.println("CreateConnection InterruptedException");
 			isConnectionOpen = false;
			isRunning = false;
			return;
		}
		catch (Exception e) 
		{
			//System.out.println("CreateConnection Exception");
 			isConnectionOpen = false;
			isRunning = false;
			return;
		}
	}

	public static void checkReturnedMessage(Message msg) {
		// TODO Auto-generated method stub
		
		try
		{
			if(msg != null)
			{
				switch(msg.messageId())
				{		
				
				case EventError.ID:						
					EventError eventerror = (EventError) iMessage;
					if (eventerror.getThisDN().equals(extn))
					{
						//System.out.println(iMessage.messageName());
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						//JOptionPane.showMessageDialog(null, eventerror.toString());
						callservertime=eventerror.getTime().getTimeinSecs();
						//callid=eventerror.getConnID().toLong();
						CurrentStatusId=13;	
						
						switch (eventerror.getErrorCode())
						{
						case 527:
							//System.out.println("DN : " + extn + " is already active at another console ");
							break;
						case 1706:
							//System.out.println("Extension " + extn + " is already logged in ");
							break;
						case 59:
							//System.out.println("DN " + extn + " is not configured in CME ");

							break;
						case 61:
							//System.out.println("Invalid Calling DN.Prefix is not configured properly in CME ");

							break;
						case 71:
							//System.out.println("Invalid Calling DN.Prefix is not configured properly in CME");

							break;
						case 223:
							//System.out.println("Bad Parameter Passed To Function >> Logout && Re-Login");
							break;
						case 231:
							//System.out.println("DN Is Busy >> Logout && Re-Login");
							break;
						
						case 52:
							//System.out.println("DN Is not Mapped In This Process >> Close && Re-Login");
						default:
							//System.out.println( eventerror.getErrorMessage());
							break;
						}

					}
					break;	
					
				case EventDialing.ID:
					EventDialing eventdialing = (EventDialing) iMessage;
					//JOptionPane.showMessageDialog(null, eventdialing.getTime());
					
					if (eventdialing.getThisDN().equals(extn))
					{
						// WsServer.onMessage(iMessage.toString());
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						CurrentStatusId = 2;
					
						
						callservertime=eventdialing.getTime().getTimeinSecs();
						
						if (eventdialing.getCallType() == CallType.Consult && CurrentStatusId==8)
						{
							IVRconnID = eventdialing.getConnID();
							
						}
						else
						{
							connID = eventdialing.getConnID();
						}
						
						
						
					}

					break;
				case EventNetworkReached.ID:
					EventNetworkReached eventnetworkreached = (EventNetworkReached) iMessage;
					if (eventnetworkreached.getThisDN().equals(extn))
					{	
						// WsServer.onMessage(iMessage.toString());
						CurrentStatusId = 2;
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						//System.out.println(iMessage.messageName());
						callservertime=eventnetworkreached.getTime().getTimeinSecs();
						//callid=eventnetworkreached.getConnID().toLong();
						campaignphone = eventnetworkreached.getOtherDN();
						//JOptionPane.showMessageDialog(null, "campaignphone :"+campaignphone);
						
					}
					break;

				case EventAgentLogout.ID:
					EventAgentLogout eventagentlogout = (EventAgentLogout) iMessage;

					if (eventagentlogout.getThisDN().equals(extn))
					{
						// WsServer.onMessage(iMessage.toString());
						CurrentStatusId = 9;
						
						callservertime=eventagentlogout.getTime().getTimeinSecs();
					}

					break;

				case EventDNOutOfService.ID:
					EventDNOutOfService eventdnoutofervice = (EventDNOutOfService) iMessage;

					if (eventdnoutofervice.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventdnoutofervice.getTime().getTimeinSecs();
						//callid=eventdnoutofervice.getConnID().toLong();
					}


					break;

				case EventDNBackInService.ID:
					EventDNBackInService eventdnbackinservice = (EventDNBackInService) iMessage;
					if (eventdnbackinservice.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventdnbackinservice.getTime().getTimeinSecs();
						//callid=eventdnbackinservice.getConnID().toLong();

					}
					break;

				case EventDestinationBusy.ID:
					EventDestinationBusy eventdestinationbusy = (EventDestinationBusy) iMessage;
					if (eventdestinationbusy.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventdestinationbusy.getTime().getTimeinSecs();
						
					}
					break;

				case EventAgentNotReady.ID:
					EventAgentNotReady eventagentnotready = (EventAgentNotReady) iMessage;
					if (eventagentnotready.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						KeyValueCollection reasonCodes = new KeyValueCollection();
						reasonCodes=eventagentnotready.getReasons();
					
						//JOptionPane.showMessageDialog(null, eventagentnotready.getReasons());
						if(eventagentnotready.getReasons().containsKey("ReasonCode"))
						{
							BreakStatus=reasonCodes.getPair("ReasonCode").getStringValue();
							CurrentStatusId =DBConnection.Map_Status.get(BreakStatus);
							
							
						}

						else
						{
						callservertime=eventagentnotready.getTime().getTimeinSecs();
						CurrentStatusId = 1;
						
						}
						
						
						
					}
					break;

				case EventAgentReady.ID:
					EventAgentReady eventagentready = (EventAgentReady) iMessage;
					if (eventagentready.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						CurrentStatusId = 1;
						
						
						if (campaignmode == "Preview" && MyCode > 0)
                        {
							
							CurrentStatusId = 4;
							
                        }
						callservertime=eventagentready.getTime().getTimeinSecs();
						
					}

					break;
				case EventRinging.ID:

					EventRinging eventRinging = (EventRinging) iMessage;
					if (eventRinging.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventRinging.getTime().getTimeinSecs();
						//callid=eventRinging.getConnID().toLong();
						
						if(eventRinging.getUserData() != null)
                		{
							callingListName = eventRinging.getUserData().getPair("GSW_CALLING_LIST").getStringValue();
                    		if(eventRinging.getUserData().containsKey("RTargetAgentGroup"))
	                        {
                    			agentGroupName = eventRinging.getUserData().getPair("RTargetAgentGroup").getStringValue();
	                        	
	                        }
                		}
						//JOptionPane.showMessageDialog(null, eventRinging.toString());
						try
						{
							/*
							URL url = this.getClass().getClassLoader().getResource("resources/ringing.wav");
							AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
							Clip clip = AudioSystem.getClip();
							clip.open(audioIn);
							clip.start();
							*/								
						}
						catch(Exception ex)
						{
							// setstatusText("Error while ringging sound play :", Color.RED);
							logger.error("Error while ringging sound play : " + ex.getMessage());
						}
						calltype = eventRinging.getCallType().toString();
						ani = Long.parseLong(eventRinging.getANI());
						dnis = Long.parseLong(eventRinging.getDNIS());
						connID = eventRinging.getConnID();
						campaignphone = eventRinging.getANI();
						

					}


					break;
				case EventEstablished.ID:
					EventEstablished eventEstablished = (EventEstablished) iMessage;

					if (eventEstablished.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						//System.out.println(iMessage.messageName());
						// WsServer.onMessage(iMessage.toString());
						if(MyCode!=0)
						{
						//String finalurl=DBConnection.URL+"Mycode="+MyCode+"&AgentID="+person_dbid;
						//logger.info("EventEstablished URL : "+finalurl);
						//openBrowser(finalurl);
						//browser_status=1;
						}
						
						connecttime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
						CurrentStatusId = 3;
						
						callservertime=eventEstablished.getTime().getTimeinSecs();
						callid=eventEstablished.getConnID().toLong();
						calltype = eventEstablished.getCallType().toString();
						
						
						if (connID == null)
						{	                        		                        	
							connID = eventEstablished.getConnID();
						}

						if(eventEstablished.getCallType().equals(CallType.Inbound))
						{
							ani=Long.parseLong(eventEstablished.getANI());
							CALLTYPE=1;
							
						}
						else
						{
							dnis=Long.parseLong(eventEstablished.getDNIS());
							CALLTYPE=2;
							if(campaignmode.equals("Predictive") || campaignmode.equals("Progressive") )
							{
							UtilDao dao =new UtilDao();
							//CallLogRecordId=dao.CallLogEntryPredictive(campaignphone,agentname, extn,PangeumLogin.opoid, apprefno, "Predictive", String.valueOf(MyCode));
							//logger.info("CallLogRecordId : "+CallLogRecordId);
							}
						}

						if (eventEstablished.getUserData() != null)
						{
							if(eventEstablished.getUserData().containsKey("GSIP_REC_FN"))
							{
								//RecordingPath = eventEstablished.getUserData().getPair("GSIP_REC_FN").getStringValue();
								//RecordingPath = RecordingPath + "_pcmu.wav";

							}
						}
						UtilDao dao=new UtilDao();
						//dao.updateCurrentStatus(PangeumLogin.opoid,CurrentStatusId,extn,processname);
						//updateRecordingStartTime();
					}

					break;
				case EventReleased.ID:
					EventReleased eventReleased = (EventReleased) iMessage;

					if (eventReleased.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						//System.out.println(iMessage.messageName());
						// WsServer.onMessage(iMessage.toString());
						if(disptype.equals(""))
						{
						disptype="Customer";
						}
						CurrentStatusId = 4;
						
						UtilDao dao=new UtilDao();
						//dao.updateCurrentStatus(PangeumLogin.opoid,CurrentStatusId,extn,processname);
						//updateRecordingEndTime();
						
					}

					break;

				case EventOnHook.ID: // Disconnect by customer
					EventOnHook eventOnhook = (EventOnHook) iMessage;
					if (eventOnhook.getThisDN().equals(extn))
					{	
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						//System.out.println(iMessage.messageName());
						// WsServer.onMessage(iMessage.toString());
						CurrentStatusId = 4;
                		
					  
						callservertime=eventOnhook.getTime().getTimeinSecs();                      
                        try 
                        {
                        	/*
    							URL url = this.getClass().getClassLoader().getResource("resources/beep.wav");
    							AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
    							Clip clip = AudioSystem.getClip();
    							clip.open(audioIn);
    							clip.start();
    							*/								
    							/*RequestAgentNotReady requestAgentNotReady = RequestAgentNotReady.create(extn, AgentWorkMode.AfterCallWork);
    							iMessage = tServerProtocol.request(requestAgentNotReady);
							*/
							
                        	
						} 
                        catch (Exception e) 
                        {
							//e.printStackTrace();
                        	logger.error(e.getMessage());
						} 
                       
                        
					}
					

					break;

				case EventOffHook.ID: // Disconnect by agent
					EventOffHook eventOffHook = (EventOffHook) iMessage;
					if (eventOffHook.getThisDN().equals(extn))
					{
						//System.out.println(iMessage.messageName());
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventOffHook.getTime().getTimeinSecs();
						
						CurrentStatusId = 4;
						
						
						
					}

					break;

				case EventAbandoned.ID:
					EventAbandoned eventAbandoned = (EventAbandoned) iMessage;

					if (eventAbandoned.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventAbandoned.getTime().getTimeinSecs();
						
						CurrentStatusId = 4;
						

					}

					break;

				case EventHeld.ID:
					EventHeld eventHeld = (EventHeld) iMessage;
					if (eventHeld.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						CurrentStatusId = 5;
						
						callservertime=eventHeld.getTime().getTimeinSecs();
						

					}
					break;

				case EventRetrieved.ID:
					EventRetrieved eventRetrieved = (EventRetrieved) iMessage;
					if (eventRetrieved.getThisDN().equals(extn))
					{
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						CurrentStatusId = 3;
						
						callservertime=eventRetrieved.getTime().getTimeinSecs();
				
						

					}
					break;

				case EventAttachedDataChanged.ID:
					EventAttachedDataChanged eventattacheddatachanged = (EventAttachedDataChanged) iMessage;
					//logger.info(eventattacheddatachanged.toString());				
					if (eventattacheddatachanged.getThisDN().equals(extn))
					{
						//System.out.println(iMessage.messageName());
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						connID=eventattacheddatachanged.getConnID();
						
						if(eventattacheddatachanged.getUserData() != null)
						{
							
							if(eventattacheddatachanged.getUserData().containsKey("record_type") )
                        	{
								
                        		int callback= eventattacheddatachanged.getUserData().getPair("record_type").getIntValue();
                        		//logger.info("callback status: "+callback);
                        		if(callback==6)
                        		{
                        			// lblCallBack.setText("Call Back ");
                        			
                        		}
                        		
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("GSW_CALLING_LIST"))
                        	{
                        		callingListName = eventattacheddatachanged.getUserData().getPair("GSW_CALLING_LIST").getStringValue();
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("GSW_RECORD_HANDLE"))
                        	{
                        		recordHandle = eventattacheddatachanged.getUserData().getPair("GSW_RECORD_HANDLE").getIntValue();
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("GSW_ATTEMPTS"))
                        	{
                        		attempts = eventattacheddatachanged.getUserData().getPair("GSW_ATTEMPTS").getIntValue();
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("GSW_PHONE"))
                        	{
                        		String phone = StringUtils.right(eventattacheddatachanged.getUserData().getPair("GSW_PHONE").getStringValue(),10);
                        		if(phone != null)
                        		{
                        			
                        			campaignphone = phone;
                        		}
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("TMasterID"))
                        	{
                        		MyCode =eventattacheddatachanged.getUserData().getPair("TMasterID").getIntValue();
                        		/*
                        		if(browser_status==0)
        						{
        						String finalurl=DBConnection.URL+"Mycode="+MyCode+"&AgentID="+person_dbid;
        						logger.info("EventAttachedDataChanged URL : "+finalurl);
        						openBrowser(finalurl);
        						browser_status=1;
        						}
        						*/
                        		
                        	}
                        	
                        	if(eventattacheddatachanged.getUserData().containsKey("VIP_No"))
                        	{
                        		//vipno =eventattacheddatachanged.getUserData().getPair("VIP_No").getStringValue();
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("BatchID") )
                        	{                        		
                        		//batchid=eventattacheddatachanged.getUserData().getPair("BatchID").getStringValue();
                        		
                        	}
                        	
                        	
                        	if(eventattacheddatachanged.getUserData().containsKey("GSIP_REC_FN"))
                        	{
                        		/*RecordingPath = null;
                        		RecordingPath = eventattacheddatachanged.getUserData().getPair("GSIP_REC_FN").getStringValue();
                        		RecordingPath = RecordingPath + "_pcmu.wav";*/
                        		UtilDao dao=new UtilDao();
                        		//dao.UpdateRecordingFileName(RecordingPath,masterrecordingid,localrecordingid);
                        		
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("record_id"))
                        	{
                        		//apprefno=eventattacheddatachanged.getUserData().getPair("record_id").getStringValue();
                        		
                        		
                        	}
                        	if(eventattacheddatachanged.getUserData().containsKey("APPREFNO"))
                        	{
                        		//apprefno=eventattacheddatachanged.getUserData().getPair("APPREFNO").getStringValue();
                        		
                        		
                        	}
                        	
						}
						
						
					}
					break;

				case EventUserEvent.ID:
					EventUserEvent eventUserEvent = (EventUserEvent) iMessage;
					if (eventUserEvent.getThisDN().equals(extn))
					{	
						//System.out.println(iMessage.messageName());
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						//JOptionPane.showMessageDialog(null, iMessage.toString());
						callservertime=eventUserEvent.getTime().getTimeinSecs();
						if(eventUserEvent.getUserData().getString("GSW_USER_EVENT") != null)
						{
							if(eventUserEvent.getUserData().getString("GSW_USER_EVENT").toString().equals("CampaignStarted"))
							{
								//JOptionPane.showMessageDialog(null, iMessage.toString());
								ocsApplicationID = eventUserEvent.getUserData().getPair("GSW_APPLICATION_ID").getIntValue();
								campaignName = eventUserEvent.getUserData().getPair("GSW_CAMPAIGN_NAME").getStringValue();
								campaignmode = eventUserEvent.getUserData().getPair("GSW_CAMPAIGN_MODE").getStringValue();
								agentGroupName=eventUserEvent.getUserData().getPair("GSW_CAMPAIGN_GROUP_NAME").getStringValue();
								
								
								if(eventUserEvent.getUserData().getPair("GSW_CAMPAIGN_MODE").getStringValue().equals("Preview"))
								{
							//btnGetNext.setEnabled(true);
								}
								else
								{
									//btnGetNext.setEnabled(false);
								}
								
								if(eventUserEvent.getUserData().getString("GSW_USER_EVENT").toString().equals("CampaignStopped"))
								{
									//campaignField.setText("CampaignStopped");
									
								}
							}
						}
						
						
						if(eventUserEvent.getUserData().getString("GSW_USER_EVENT") != null)
						{
							if(eventUserEvent.getUserData().getString("GSW_USER_EVENT").toString().equals("PreviewRecord"))
							{
								callingListName = eventUserEvent.getUserData().getPair("GSW_CALLING_LIST").getStringValue();
								recordHandle = eventUserEvent.getUserData().getPair("GSW_RECORD_HANDLE").getIntValue();
								attempts = eventUserEvent.getUserData().getPair("GSW_ATTEMPTS").getIntValue();
								MyCode = eventUserEvent.getUserData().getPair("TMasterID").getIntValue();
								if(MyCode > 0)
								{
									campaignphone = eventUserEvent.getUserData().getPair("GSW_PHONE").getStringValue();
									
									
								}
							}
						}
					if(eventUserEvent.getUserData().getString("GSW_ERROR") != null)
					{
					
						if(eventUserEvent.getUserData().getString("GSW_ERROR").toString().equals("No Records Available"))
						{
							//System.out.println("No Records Available");
							CurrentStatusId = 1;
							
						}
						else
						{
							logger.error("GENYSYS GSW_ERROR : "+eventUserEvent.getUserData().getString("GSW_ERROR").toString());
							logger.error("GENYSYS GSW_ERROR_NUMBER "+eventUserEvent.getUserData().getInt("GSW_ERROR_NUMBER"));
							
						
							//// DB Operation insert genysys error into genysyserrorlog table
							UtilDao dao=new UtilDao();
							//dao.setGenysysErrorLog(eventUserEvent.getUserData().getInt("GSW_ERROR_NUMBER"), eventUserEvent.getUserData().getString("GSW_ERROR").toString(),MyCode, processname,agentid,extn,campaignphone,connID.toString(),callservertime);
						
						}
						
					}
					
					}
					
					break;

				case EventAgentLogin.ID:
					EventAgentLogin eventAgentLogin = (EventAgentLogin) iMessage;
					if (eventAgentLogin.getThisDN().equals(extn))
					{
						//System.out.println(iMessage.messageName());
						//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
						// WsServer.onMessage(iMessage.toString());
						callservertime=eventAgentLogin.getTime().getTimeinSecs();
						CurrentStatusId = 1;
					
						
					}

					break;
					
					
				default:
					
					break;
					}
			}
			
		}
		catch(Exception ex)
		{
			//logger.info("msg.ID : "+msg.messageId());
			//logger.info("msg : "+msg.toString());
			//logger.error(ex.getMessage());
			//logger.error(ex.getLocalizedMessage());
			//logger.error(ex.getCause());
			//logger.error(ex.getStackTrace());
		}

	}
	
	public static  void RegisterExtension(String DN) {
		// TODO Auto-generated method stub
		
		dnMap.put(DN, websocketsession);
		

		
		////System.out.println("HashMap Size " + dnMap.size());
		boolean isRegistered;
		try
		{
			RequestRegisterAddress requestRegisterAddress =
					RequestRegisterAddress.create(DN,RegisterMode.ModeShare,ControlMode.RegisterDefault,AddressType.DN);
			Message iMessage = tServerProtocol.request(requestRegisterAddress);
			
			////System.out.println("********Register***** " + iMessage.toString());

			//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
			/*if(dnMap.containsKey(DN))
			{
			try {
				Session obj=dnMap.get(DN);
				obj.getBasicRemote().sendText("RegisterExtension" +DN);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
			if(iMessage.messageName().equals("EventError"))
			{
				//System.out.println("RegisterExtension : "+iMessage.toString());
				isRegistered = false;

				checkReturnedMessage(iMessage);

			}
			else
			{
				////System.out.println("RegisterExtension : "+ iMessage.toString());
				isRegistered = true;
				
				checkReturnedMessage(iMessage);

			}



		}
		catch (ProtocolException e) 
		{
			//System.out.println("RegisterExtension ProtocolException");
			isRegistered = false;			
 
		} 
		catch (IllegalStateException e) 
		{
			//System.out.println("RegisterExtension IllegalStateException");
			isRegistered = false;
 
		}
	}

	public static  boolean AgentLogin(String DN,String AgentId)
	{
		// Here DB operation for get user properties Based on OPO ID and HostName
		boolean flag=true;
		
		try 
		{
			
			
			
			RequestAgentLogin requestAgentLogin = RequestAgentLogin.create(DN, AgentWorkMode.ManualIn);
			requestAgentLogin.setAgentID(AgentId);
			requestAgentLogin.setPassword(null);
			iMessage = tServerProtocol.request(requestAgentLogin);
			//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);

			/*if(dnMap.containsKey(DN))
			{
			try {
				Session obj=dnMap.get(DN);
				obj.getBasicRemote().sendText("RegisterExtension" +DN);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
			 if(iMessage.messageName().equals("EventError"))
			{				 
				//System.out.println("RequestAgentLogin EventError :" +iMessage.toString());
				
				 if(iMessage.getMessageAttribute("ErrorCode").equals("527")){
					
					 flag=false;
					 return flag;
					 
				 }
				 
			}
			else
			{
				
				//logger.info(" Agent ID : ["+Pangeum.agentid +"] Agent Name : ["+Pangeum.agentname +"] DN :["+DN +"]");
				return flag;
			}			 
		}
		catch (ProtocolException e) 
		{
			//System.out.println("AgentLogin ProtocolException ");

			
		} 
		catch (IllegalStateException e) 
		{
			//System.out.println("AgentLogin IllegalStateException ");

			
		}
		catch (Exception e)
		{
			//System.out.println("AgentLogin Exception ");

			
		}
		return flag;
	}

	public static  void AgentReady(String DN)
	{
		try 
		{			 
			RequestAgentReady requestAgentReady = RequestAgentReady.create(DN, AgentWorkMode.ManualIn);
			iMessage = tServerProtocol.request(requestAgentReady);
			//WsServer.sendMessageForClient(extn,iMessage.messageName() + "_"+campaignphone+"_"+campaignName+"_"+campaignmode+"_"+agentGroupName+"_"+callingListName+"_"+connID);
			/*if(dnMap.containsKey(DN))
			{
			try {
				Session obj=dnMap.get(DN);
				obj.getBasicRemote().sendText("RegisterExtension" +DN);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
			////System.out.println(iMessage.toString()+"_____AgentReady____");

			
			
			if(iMessage.messageName().toString().equals("EventError"))
			{
				checkReturnedMessage(iMessage);
			}
			else
			{
				//CurrentStatusId=1;
				checkReturnedMessage(iMessage);	        			

			}			 
		} 
		catch (ProtocolException e) 
		{
			

		} 
		catch (IllegalStateException e) 
		{
			

		}
	}

	
	
	
	
	public static  void dial_Call(String DialPhone) {
		// TODO Auto-generated method stub
		
						//System.out.println("In Dial Call");
						try{
							//RequestAgentReady requestAgentReady = RequestAgentReady.create(extn, AgentWorkMode.AuxWork);
	    					//iMessage = tServerProtocol.request(requestAgentReady);
	    					
	    					RequestMakeCall requestMakeCall = RequestMakeCall.create(extn, DialPhone, MakeCallType.Regular);
		    				iMessage = tServerProtocol.request(requestMakeCall);
		    				//System.out.println("dial_Call : " + iMessage.messageName());
		        			checkReturnedMessage(iMessage);
	    					
						}
						catch (ProtocolException e) 
		    			{
							//System.out.println("ProtocolException ");
							e.printStackTrace();
						} 
		    			catch (IllegalStateException e) 
		    			{
		    				//System.out.println("IllegalStateException ");
		    				e.printStackTrace();
		    			}
    				}
					

	
	
	// Web Socket Functions
	
	

		
   
	public static  void logOut_Ext(LoginBean bean,String dn,String HostName) {
		// TODO Auto-generated method stub
		try
		{  
			RequestAgentLogout requestAgentLogout = RequestAgentLogout.create(dn);
			iMessage = tServerProtocol.request(requestAgentLogout);
			
			UtilDao dao=new UtilDao();
			
			//System.out.println("Time Of LOGOUT BEAN VALUE : ");
			//System.out.println(bean.getAgentname() + bean.getWinlogin() + dn + bean.getAgentid()  + HostName);
			dao.agentLogin(bean.getAgentname(),bean.getWinlogin() , "LOGOUT",  dn, String.valueOf(bean.getAgentid()), "test_process", HostName);
			
			//logger.info(LoginId);
			//dao.update_endtime_in_loginlogoutlog(LoginId);
			if(iMessage.messageName().toString().equals("EventError"))
			{
				checkReturnedMessage(iMessage);
			}
			else
			{
				checkReturnedMessage(iMessage);
			}		
		
		}
	catch(Exception e){
	}
	
	}

	public static  void hangup_Call(String thisDN) {
		// TODO Auto-generated method stub
		//System.out.println("hangup_Call"+thisDN);
		
		
		try{
			
			
			 if(callMap.containsKey(thisDN)){
				 
					RequestReleaseCall requestreleasecall1 = RequestReleaseCall.create(thisDN, callMap.get(thisDN));
					iMessage = tServerProtocol.request(requestreleasecall1);
					callMap.remove(thisDN);
					if(iMessage.messageName().toString().equals("EventError"))
					{
						checkReturnedMessage(iMessage);
					}
					else
					{ checkReturnedMessage(iMessage);
					 
					}	
					
				 
			 }
			 
			
			
			
			 

		}
		catch (ProtocolException e2) 
		{
 		} 
		catch (IllegalStateException e2) 
		{
 		}        		
		catch (Exception e2) 
		{
 		}        		

	}
	
	
	//********************************Hold EXTN***********************************
	
	public static void hold_Ext(String thisDN) {
		// TODO Auto-generated method stub
		try 
		{
			//JOptionPane.showMessageDialog(null, "connID :"+connID.toString());
			 
			 
					RequestHoldCall requestHoldCall = RequestHoldCall.create(thisDN, callMap.get(thisDN));
					iMessage = tServerProtocol.request(requestHoldCall);
					//System.out.println("Hold====="+iMessage.toString());

					checkReturnedMessage(iMessage);
				
		 
		} 
		catch (ProtocolException e1) 
		{
 		} 
		catch (IllegalStateException e1) 
		{
 		}             

	}
	
	
	
	//****************************UNHOLD EXTN**********
	
	public static void unhold_Ext(String thisDN) {
		// TODO Auto-generated method stub
		try 
		{
			RequestRetrieveCall requestRetrieveCall = RequestRetrieveCall.create(thisDN, callMap.get(thisDN));
			iMessage = tServerProtocol.request(requestRetrieveCall);
			//System.out.println("UNHOLD====="+iMessage.toString());

			checkReturnedMessage(iMessage);
		} 
		catch (ProtocolException ex) 
		{
 		} 
		catch (IllegalStateException ex) 
		{
 		}

	}
	
	 
	public static void agentBreak(String thisDN, String reason) 
	{
		
		
		try {
			//BreakStatus=comboBoxBreakReason.getSelectedItem().toString();
			//JOptionPane.showMessageDialog(null, "BreakStatus : "+BreakStatus);
		 
			KeyValueCollection reasonCodes = new KeyValueCollection();
	        reasonCodes.addString("ReasonCode", reason);//check before the reasoncode is configured in CCPulseStat
	        RequestAgentNotReady requestAgentNotReady = RequestAgentNotReady.create(thisDN, AgentWorkMode.AuxWork, null, reasonCodes, reasonCodes);
	        try 
	        {
				iMessage = tServerProtocol.request(requestAgentNotReady);
				
				
				
				
				
			} 
	       catch (ProtocolException e) 
	       {
				//e.printStackTrace();
	    	   logger.error(e.getMessage());
			} 
	       catch (IllegalStateException e) 
	       {
				//e.printStackTrace();
	    	   logger.error(e.getMessage());
	       }       
	    } catch (Exception ex) {
	        //ex.printStackTrace();
	    	logger.error(ex.getMessage());
	    }
		
		
	
		
	}
	
	
	
 
	/***********************************CALL LIST UPDATE GENESYS
	 * @throws java.text.ParseException *************************************/		
	public static void  saveCallingList(FormBean bean, String DN) throws java.text.ParseException 
	{
		
		
		if(bean.getDisposition().equals("Call Back"))
		{
			
			
			
			try
	    	 {
	    		 KeyValueCollection kvp = new KeyValueCollection();
	    		 kvp.addString("GSW_AGENT_REQ_TYPE", "RecordReschedule");
	    		 kvp.addString("GSW_CALLBACK_TYPE", "Campaign");
	    		 DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				 Date date = (Date)formatter.parse(bean.getCallbacktime());
				 SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				 String tempDate = newFormat.format(date);
				//System.out.println("converted time : "+tempDate);
	    		 kvp.addString("GSW_DATE_TIME",tempDate);
	    		
	    		 kvp.addInt("GSW_RECORD_HANDLE", Integer.parseInt(bean.getRecordhandle()));
	    		
	    		 kvp.addInt("GSW_APPLICATION_ID", Integer.parseInt(bean.getOcsapplicationid()));
	    		 
	    		 kvp.addString("GSW_CAMPAIGN_NAME", bean.getCampaignname());
	    		
	    		 kvp.addString("Disposition", bean.getDisposition());
	    		
	    		 kvp.addString("Fields1",bean.getSubdisposition());
	    		
	    		 kvp.addString("Remarks", bean.getRemark());
	    		
	    		 //kvp.addString("GSW_RECORD_STATUS", "6");
	    		 ////System.out.println("CallBack CommonProperties tempDate "+tempDate +" Integer.parseInt(bean.getRecordhandle() : "+Integer.parseInt(bean.getRecordhandle()+" Integer.parseInt(bean.getOcsapplicationid()) :"+Integer.parseInt(bean.getOcsapplicationid())+" bean.getCampaignname():"+bean.getCampaignname() +" bean.getRemark() :"+bean.getRemark()));
	    		 CommonProperties commonProperties = CommonProperties.create();
	    		 
	    		 commonProperties.setUserData(kvp);
	    		
	    		 RequestDistributeUserEvent requestDistributeUserEvent1 = RequestDistributeUserEvent.create(DN, commonProperties);
	    		 iMessage = tServerProtocol.request(requestDistributeUserEvent1);
	    		 //System.out.println("Call Back requestDistributeUserEvent1"+ iMessage.toString());
	    		 
	    		
	    	}
			catch(ProtocolException pe)
			{
				//System.out.println("saveCallingList Call Back ProtocolException : "+pe.getMessage());
				//logger.error("saveCallingList Call Back ParseException : "+pe.getMessage());
			}
			catch(ParseException pe)
			{
				//System.out.println("saveCallingList Call Back ParseException : "+pe.getMessage());
				//logger.error("saveCallingList Call Back ParseException : "+pe.getMessage());
			}
	         catch (IllegalStateException e) 
	         {
	        	 //System.out.println("saveCallingList Call Back IllegalStateException : "+e.getMessage());
	        	 //logger.error("saveCallingList Call Back IllegalStateException : "+e.getMessage());
	         }
		}
		
		
		else if (callingListName != null && campaignName != null && recordHandle > 0 && !bean.getDisposition().equals("Call Back"))
		{
	        try   {
				//JOptionPane.showMessageDialog(null, "callingListName : " + callingListName +" campaignName :"+campaignName+" recordHandle :"+recordHandle);
				KeyValueCollection kvp = new KeyValueCollection();
				kvp.addInt("GSW_RECORD_HANDLE", recordHandle);
				//kvp.addInt("GSW_CALL_RESULT", 33);
				kvp.addInt("GSW_APPLICATION_ID", ocsApplicationID);
				kvp.addString("GSW_CAMPAIGN_NAME", campaignName);
				kvp.addString("GSW_CALLING_LIST", callingListName);
				kvp.addString("GSW_AGENT_REQ_TYPE", "RecordProcessed");
				//kvp.addInt("attempt", attempts + 1);
				//kvp.addString("Remarks", "test remarks");
				kvp.addString("Disposition", bean.getDisposition());
				//kvp.addString("SubDisposition", comboBoxSubDisposition.getSelectedItem().toString());
				kvp.addString("Remarks", bean.getRemark());
				RequestSendEvent RequestSendEvent1 = RequestSendEvent.create();
				RequestSendEvent1.setUserEvent(CommonProperties.create());
				RequestSendEvent1.getUserEvent().setUserData(kvp);
				RequestSendEvent1.getUserEvent().setUserEvent(EventUserEvent.ID);
				RequestSendEvent1.getUserEvent().setThisDN(extn);
				iMessage = tServerProtocol.request(RequestSendEvent1);
				//System.out.println("with out Call Back requestDistributeUserEvent1"+iMessage.toString());
 				 }
	        catch (ProtocolException ex)
			{
				ex.getMessage();
			}
			catch (Exception ex)
			{
				ex.getMessage();
			}
		
		}
		
		//getAgentReady(DN);
		
		
	}
	
	
	
	public static void getAgentReady(String DN) 
	{
		
		//System.out.println("Agent ready//////////////////////////"+iMessage +DN);
		try{
		RequestAgentReady requestAgentReady = RequestAgentReady.create(DN, AgentWorkMode.ManualIn);
		iMessage = tServerProtocol.request(requestAgentReady);		
		 WsServer.sendMessageForClient(DN,iMessage.messageName()+  DN+"null|null|null|null|null|null|null");
			
		if(iMessage.messageName().toString().equals("EventError"))
		{
			checkReturnedMessage(iMessage);
			
	 //WsServer.sendMessageForClient(DN,iMessage.messageName()+"null|null|null|null|null|null|null");
				
			
		}
		else
		{
			CurrentStatusId=1;
			
			checkReturnedMessage(iMessage);	        			
			 //WsServer.sendMessageForClient(DN,iMessage.messageName()+"null|null|null|null|null|null|null");
				
		}			
	}
		catch(ProtocolException e)
		{
			logger.error("ProtocolException getAgentReady() : "+e.getMessage());
		}
		catch(IllegalStateException e)
		{
			logger.error("IllegalStateException getAgentReady() : "+e.getMessage());
		}
} 
	
/*	public void updateRecordingStartTime(FormBean fbean)
	{
		
			UtilDao dao=new UtilDao();
			masterrecordingid=dao.MasterRecordingTableEntry(fbean.getAgentopo(),fbean.getConnid(),fbean.getCampaignname(),fbean.getCampaignphone(),RecordingPath);
			localrecordingid=dao.LocalRecordingTableEntry(fbean.getAgentopo(),fbean.getConnid(),fbean.getCampaignname(),fbean.getCampaignphone(),RecordingPath);
		//masterrecMap.put(this, arg1)
			
			
			
			//System.out.println("masterrecordingid :"+masterrecordingid + " localrecordingid "+localrecordingid);
	}*/
	
	
	
	

	
	public void updateRecordingEndTime(Integer CallLogRecordId,ConnectionId Connid,String campaignName,String disptype,Integer masterrecordingid,Integer localrecordingid)
	{
		
		
		//System.out.println("[updateRecordingEndTime] :"+CallLogRecordId+Connid.toString()+"_"+campaignName+"_"+disptype+"_"+masterrecordingid+"_"+localrecordingid);
		
		if(CallLogRecordId >0)
		{
			UtilDao dao=new UtilDao();
			dao.UpdateEndTimeInCallLog(CallLogRecordId, Connid.toString(), campaignName);
		}
		
		if(CallLogRecordId >0 && !disptype.equals(""))
		{
			UtilDao dao=new UtilDao();
			dao.UpdateDisconnectTypeInCallLog(disptype, CallLogRecordId);
		}
		
		if(masterrecordingid >0 && localrecordingid >0)
		{
			UtilDao dao=new UtilDao();
			dao.updateEndTimeRecording(masterrecordingid,localrecordingid);
		}
	}
	
	

	public void updateRecordingStartTime(String dn,String agentopo, ConnectionId conId, String campaignName, String campaignPhone, String recordingPath)
	{
		
		//System.out.println("entered Update"+agentopo+conId+campaignName+campaignPhone+recordingPath);
		
			UtilDao dao=new UtilDao();
			int masterrecordingid=dao.MasterRecordingTableEntry(agentopo,conId.toString(),campaignName,campaignPhone,recordingPath);
			int localrecordingid=dao.LocalRecordingTableEntry(agentopo,conId.toString(),campaignName,campaignPhone,recordingPath);
			StartTserverThread.masterrecMap.put(dn, masterrecordingid);
			StartTserverThread.localrecMap.put(dn, localrecordingid);
			
			//System.out.println(" ************   "+conId+"masterrecordingid :"+masterrecordingid + " localrecordingid "+localrecordingid);
			//return "updateRecordingStartTime";

			

	}
	public void entryCallLog(String mobile,String agentName,String dn, String opoid,String apprefNo,String mode,String mycode)
	{
		
		
 		if(mode.equals("Predictive") || mode.equals("Progressive") )
		{
 
		UtilDao dao =new UtilDao();
		int CallLogRecordId=dao.CallLogEntryPredictive(mobile,
				agentName, dn,opoid, apprefNo,
				mode, mycode);
		
		
		//System.out.println("entryCallLog : "+CallLogRecordId);
		StartTserverThread.callLogMap.put(dn,CallLogRecordId );
		
		
		
		}
		
		
		
		 
		
		
		
	}


}
