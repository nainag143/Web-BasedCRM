package ws.server;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint/{message}")
public class WsServer {
	public static Session webSocketSession;
	public static HashMap<String, Session> userMap=new HashMap<String, Session>();
	public String extn="";
	
	@OnOpen
	public void open(final @PathParam("message") String extenxion,Session session) {
		System.out.println("Before Added Extn In Map With Session Size is  : "+userMap.size());
		extn=extenxion;
		userMap.put(extenxion,session);
		System.out.println("after Added Extn In Map With Session Size is  : "+userMap.size());
		try {
		 session.getBasicRemote().sendText("EventAgentReady "+extenxion);
	     session.getUserProperties().put("extension" , extenxion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}     

	@OnClose
	public void onClose(final @PathParam("message") String extenxion,Session session){
		
		System.out.println("Close Connection ...");
		userMap.remove(extenxion);
		try {
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	@OnMessage
    public void onMessage(String message, final Session session,final @PathParam("message") String extenxion) {

	 System.out.println("Message from client:----sessionid: " + session.getId() + "---: " + message+"for Extension"+extenxion);
	 String echoMsg = "Echo from the server : " + message;
	 
	//session.getBasicRemote().sendText("message send by server"+echoMsg);
	}
	
	/*@OnMessage
	public String onMessage(String message){
		System.out.println("Message from the client: " + message);
		String echoMsg = "Echo from the server : " + message;
		return echoMsg;
	}*/

	@OnError
	public void onError(Throwable e){
		e.printStackTrace();
	}

	
	public static void sendMessageForClient(String extenxion,String msg)
	{
	
		if(userMap.containsKey(extenxion))
		{
			webSocketSession=userMap.get(extenxion);
			try {
				webSocketSession.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		}
	
	
	
}