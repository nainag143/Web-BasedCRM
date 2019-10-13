<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="org.com.onepointone.bean.LoginBean" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
		<script src="/OpoWeb/dwr/engine.js" type="text/javascript"></script>
 <script src="/OpoWeb/dwr/interface/CtiAjax.js" type="text/javascript"></script>  
<script type='text/javascript' src='/OpoWeb/dwr/interface/AjaxClass.js'></script>
       
 <script src="/OpoWeb/dwr/util.js" type="text/javascript"></script>
 <script src="js/override.js" type="text/javascript"></script>
 
 <script src="js/jquery-1.10.2.js"></script>
 
 
<title> PANGEM</title>
        <link rel="shortcut icon" href="images/logo.png">
 
			<link rel="stylesheet" href="css/bootstrap.min.css">
			<link rel="stylesheet" href="css/style.css">
			<link rel="stylesheet" href="css/hover.css">
			<link rel="stylesheet" href="css/jquery.datetimepicker.min.css">
			
			<script src="js/jquery.min.js"></script>
			<script src="js/bootstrap.min.js"></script>
		
		 	 <script src="js/script.js"></script>
		 
			
<script src="js/moment.min.js"></script>
<script src="js/ez.countimer.js"></script>
<script>
 
</script>
   <%
    LoginBean bean=(LoginBean)session.getAttribute("loginBean");
        String username=bean.getAgentname();
        String extension=(String)session.getAttribute("DN");
        
        //out.println("extension : "+ extension + bean.getAgentname() + bean.getWinlogin() + bean.getAgentid());
 
  String agentid=bean.getAgentid();
  String agentopo=bean.getWinlogin();
  
      
        
        %>
        
    <script type="text/javascript">
  

  
 /*function actionSubmit()
 {
	 
	 
	 openConnection();
	 document.getElementById("phoneForm").submit();
 }
 
 */
 
 </script>
<script type="text/javascript">

/******************************CALLING LOG******************************/
 
/**************** getValues*************************/
function getValues(mycode)
{		
	//AjaxClass.getdetailes(ub,myCallBack);
	var id=mycode;
	console.log("getValues "+mycode);
	AjaxClass.getdetailes(id, function(data) {	
		sole.log(data);		
		dwr.util.setValues(data);

      });
	
	AjaxClass.getHistory( id, function(data){
		
		//sole.log(data);
		$("#custHistory").html(data);
		
	});

}
openConnection();
function openConnection() {
	
	 
	var username=<%=extension%>;
	//alert("entered connection "+username);
	var pushSocket = new WebSocket("ws://"+document.location.host+"/OpoWeb/websocketendpoint/"+username);
         
  //alert(pushSocket);
     //	var connid = utils.random_string(8);
     	//that.sessionId = connid;
     	//stompClient = Stomp.over(pushSocket);
     	// stompClient.connect({}, function (frame) {
       //      console.log("connected, session id: " + pushSocket.sessionId);
       //  });
         
     	var lastState=[]; //to save the last state of application
     	
         pushSocket.onmessage = function (event) {
        	 console.log(event.data);
        	// alert("on message" + event.data);
        	  //do ui update here
        	 // $("#statuscontainer").html(event.data);

        	  msg= event.data;
	     	    
	     	    
	     	  var str_array = msg.split('|');
	     	
	     	    if($( "#statuscontainer" ).text()!="AfterCallWork " && str_array[0]!="null" ) 
	     	    {
	     	    	 
	     	      


	   	     	 $('.timer').countimer('start');

	     	    $("#statuscontainer").html(str_array[0]);
	     		 buttonchangeToCall(str_array[0]);
	     	   }
	     	   var extn = $( "#extn" ).text();
	    
	     	  
	     	    
	     	    
	     	    
	     	    
	     	   if(str_array[1]!="null") 
	     	    {
	     	   $("#compaignname").html(str_array[1]);
	     	  $("#campaignname").val(str_array[1]);
				
	     	    }
	     	   
	     	   
	     	   
	     	  if(str_array[1]!="null") 
	     	    {
	     	   $("#compaignnamegroup").html(str_array[1]);
	     	    }
	     	   
	     	  
	     	  if(str_array[2]!="null") 
	     	    {
		     	  $("#compaignmode").val(str_array[2]);
		     	 $("#compaignmode1").html(str_array[2]);
		     	  
	     	    }
		     	 if(str_array[3]!="null") 
		     	    {
		     		 lastState["phonenumber"] = parseInt(str_array[3]);
		     		 
		     	 $("#phonenumbercontainer").html(lastState["phonenumber"]);//str_array[3]);
		     	$("#campaignphone").val(lastState["phonenumber"]);//str_array[3]);
		     	    }
		     	if(str_array[4]!="null") 
	     	    {
		     	 $("#connid").val(str_array[4]);
	     	     
		     	//$("#connid").val(str_array[4]);
	     	    }
		     	
		     	if(str_array[5]!="null") 
	     	    {
		     $("#connecttime").val(str_array[5]);
	     	    }

		     	if( parseInt(str_array[6]) > 0  ) 
	     	    {		 
		     		lastState["mycode"] = parseInt(str_array[6]);
		     		
			     $("#mycode").val(lastState["mycode"]);
			     console.log(str_array[6]);
			     
			     console.log("last state "+lastState);
			     
			     getValues(str_array[6]);
	     	    }
		     	
		     	if(str_array[7]!="null") 
	     	    {
		     $("#recpath").val(str_array[7]);
		     console.log($("#recpath").text(str_array[7]));
	     	    }
		     	if(str_array[8]!="null") 
	     	    {
		     $("#ocsapplicationid").val(str_array[8]);
		     console.log($("#ocsapplicationid").text(str_array[8]));
 	     	    }
		     	
		     	if(str_array[9]!="null") 
	     	    {
		     $("#callinglistname").val(str_array[9]);
		     console.log($("#callinglistname").text(str_array[9]));
		     
 	     	    }
		     	if(str_array[10]!="null") 
	     	    {
		     $("#recordhandle").val(str_array[10]);
		     console.log($("#recordhandle").text(str_array[10]));
 	     	    }
		     	

		     	
		     	if(str_array[0]=="EventUserEvent "+extn || str_array[0]=="EventAgentReady "+extn) 
	     	    {
		  		   
		  		 
		 		$("#submit_button").attr("style", "display:none;");
		 		$("#logout_button").attr("style", "display:block;");
				
	     	    }
		     	
		    	
			  	 
		    	
		    	 /* if(str_array[0]=="EventReleased "+extn) 
		     	    {
			  		    
		     	 function xxx(){
		     	    	
		     	    	entryCalLog();
		     	    }
 		     	    } */
		  	   if(str_array[0]=="EventAttachedDataChanged "+extn) 
	     	    {
		  		   
		  		 $("#break_button").attr("style", "display:none;");
		  		 
		  		$("#call_button").attr("style", "display:none;");
		  		$("#hangup_button").attr("style", "display:block;");
		  			
		 		$("#submit_button").attr("style", "display:block;");
		 		$("#logout_button").attr("style", "display:none;");

		  		   
	     		 var agentopo="<%=agentopo%>";
	     		 var conId=$( "#connid" ).val();
	     		 var campaignName=$( "#compaignname" ).text();
	     		 var campaignPhone=$( "#campaignphone" ).val();
	     		 var recordingPath =$( "#recpath" ).text();
	     		   
	     		   
	     	   
	     	   /*  function updateRecordingStartTime(){
	     	    	
	     	    	
	     	    	
	     	    	console.log(agentopo+conId+campaignName+campaignPhone+recordingPath);
	     	    	
	     		 CtiAjax.updateRecordingStartTime(agentopo,conId,campaignName,campaignPhone,recordingPath,function(ajaxResponse)
	     			    {
	     			
	     			
	     			
	     			console.log(agentopo+conId+campaignName+campaignPhone+recordingPath);
	     			
	     			    });
	     		 
	     		
	     		 
	     	    }
	     	   
	     	    
	     	   setTimeout(updateRecordingStartTime, 6000); */
	     	  
	     		 
	     	   }
		  	   
		  /*	 jQuery('#btnStartVisit').click(); if(str_array[0]=="EventReleased "+extn) 
	     	    {
		  	   
		  	   
		  		 function onhook(){
		  		var thisDN=$("#extn").text();
		    	
		  		
		      	CtiAjax.foroneventhook(thisDN,function(ajaxResponse)
		    		    {
		    		
		    		 alert(ajaxResponse);
		    		    });
		  		 }
		     	   //setTimeout(onhook, 5000);

		     	
     	}*/
		  	   
		  	   
		  	   
		  	   
		     	
        	}
     	
     		

        	pushSocket.onopen = function (event) {
        		//alert("connection is open now" + event.data);
        	    //send empty message to initialize socket connnection
        	    pushSocket.send("openconnection");
        	};
        	
        	
       	pushSocket.onclose = function (event) {
       		
        	    //send empty message to initialize socket connnection
        	   // alert(event.data+" onclose event");
        	   
        	   console.log("onclose");
       		pushSocket.close();
        	};
        	
        	pushSocket.onerror = function(event){ alert("onerror");};
        	}
        	
function closeConnection() {
	pushSocket.close();
 }


</script>    
        
        
</head>

<body>
<span id="recpath" style="display:none"></span>
<span id="arrowup"> <img src="images/up_arrow.png" style="
    width: 29px;    width: 28px;
    position: absolute;
    z-index: 1000;
       background: #284791;
" id="imageup">   </span>
<span id="arrowdown"> <img src="images/down_arrow.png" 
style="width: 29px; display: none;   width: 28px;   position: absolute; z-index: 1000;  
background: #284791; " id="imagedown">   </span>
      <div id="mainmenu1">
  <ul class="nav nav-pills" >
     
    <li class="active"><a data-toggle="pill" href="#home"><img src="images/voice.png" > </a></li>
    <li><a data-toggle="pill" href="#menu1"> <img src="images/email.png" ></a></li>
    <li><a data-toggle="pill" href="#menu2"> <img src="images/chat.png" ></a></li>
    <li><a data-toggle="pill" href="#menu3"> <img src="images/social.png" ></a></li>
    <li><a data-toggle="pill" href="#menu3"> <img src="images/orm.png" ></a></li>
  </ul>
  </div>
  <div class="tab-content col-md-12">
 <p> </p>
 
    <div id="home" class="tab-pane fade in active">
      <div id="mainmenu">
	  
	       <div class="col-md-3 col-sm-3">  
		   <div class="number">
		   <p>PHONE NUMBERS</p>
		    <b ><span id="phonenumbercontainer"></span> <input type="text" name="phonenumber"  class="phonenumber" id="phonenumber"> </b>
		   <input type="text" readonly name="connid" id="connid" class="phonenumber" value=""> </b>
		   </div>
		   <div class="number">
		    <table><tr> 
             <td>   
<span><div class="timer"></div></span><button  style="display:none" id="btnStartVisit" onClick="$('.timer').countimer('start');">Start</button> </td>
             <td> <p  id="statuscontainer">  <br>   </p> </td>
			</tr>  </table>
		   </div>
		   </div>
		<div class="col-md-3 col-sm-3">   
		       <table>
			   <tr>  
			      <td>
						  <table>
						  <tr>
						  <td>  
						   <input type="image" name="call_button" id="call_button"src="images/call.png" class="hvr-shrink" data-toggle="tooltip" data-placement="top" title="Call" onclick="dialCall()">
						  <input type="image" name="hangup_button" id="hangup_button" src="images/hangup.png" data-toggle="tooltip" data-placement="top" title="" onclick="hangupCall()" data-original-title="Call" style="display:none">
						  <td>
						  <td>  <input type="image" name="transfer_button" id="transfer_button" src="images/transfer.png" class="hvr-shrink" data-toggle="tooltip" data-placement="top" title="Transfer"> <td>
						  </tr>
						   <tr>
						  <td>   
						  <input type="image" name="hold_button" id="hold_button"  src="images/out.png"class="hvr-shrink" data-toggle="tooltip" data-placement="bottom" title="Hold" onclick="holdExt()">
						  <input type="image" name="unhold_button" id="unhold_button"  src="images/unhold.png"class="hvr-shrink" data-toggle="tooltip" data-placement="bottom" title="Unhold" style="display:none" onclick="unholdExt()">
						  
						  <td>
						  <td>  <input type="image" name="getnext_button" id="getnext_button" src="images/getnext.png" class="hvr-shrink" data-toggle="tooltip" data-placement="bottom" title="Get Next"> <td>
						  </tr>
						  
						  </table>
				      
			      </td>
			      <td>
		 <input type="image" name="hangup_button" id="break_button" src="images/break.png" class="hvr-shrink"  data-toggle="modal" data-target="#breakModal">
			    
			 


			    
			    
			      </td>
			       <td>
				      <input type="image" name="conference_button" id="conference_button" src="images/conference.png"  class="hvr-shrink">
			      </td>
			   
			   </tr>
			   </table>
			   
			   
		   </div>
		<div class="col-md-3 col-sm-3">   
		
		    <table>
			   <tr>
			   <td class="ljkdsfn">AGENT NAME  <td>
			    <td class="ruvghnj"><%=username%><td>
			   
			      
			   </tr>
			   <tr class="mart">
			      <td class="ljkdsfn"> CAMPAIGN MODE <td>
			      <input type="hidden" name="compaignmode1" id="compaignmode" value="0"/>
			    <td class="ruvghnj" id="compaignmode1"> Loading..  <td>
			   </tr>
			   
		    </table>
		
		
		   </div>
		<div class="col-md-3 col-sm-3">   
		  <table>
		  <tr>
		  <td>
			 <table> <tr>
			   <td class="ljkdsfn">AGENT ID  <td>
			    <td class="ruvghnj"><%=agentid%><td>
			   <div id="extn" style="display:none"><%=extension%></div>
			      
			   </tr>
			   <tr class="mart">
			      <td class="ljkdsfn">   <td>
			    <td > 
			     
			     <td>
			   </tr>
			   
		    </table>
			  </td>
			 <td class="plogotd"> <img src="images/logo.png" class="plogo hvr-grow"  style="    border:none;"></td>
			  
			  
			 </tr>
			
			
		    </table>
		   </div>
		<div class="col-md-3 col-sm-3" style="padding-left:0px;">
		</div>
		<div class="col-md-9" style="
    padding-left: 0;
    padding-right: 0;
">
		 <table class="ctable">
			      <tr>
			   <td class="ccp"> CAMPAIGN NAME </td>
			   <td class="ccp2" id="compaignname">Loading... </td>
			      
				  
			   <td class="ccp"> AGENT GROUP </td>
			   <td class="ccp2" id="compaignnamegroup">Loading... </td>
			   <td ><form method="POST" id="logoutform" action="/OpoWeb/logout.html"><input  type="image" name="logout_button" id="logout_button" src="images/logout.png" class="btns hvr-backward"  onclick="logoutform()"></form></td>
			   <td >
		
			    </td>
			    
			       </tr>
				   
				   
				</table>
		</div>
		
		<div class="col-md-12">
		 
		<p> </p>
		<table   class="bart table table-bordered">

       
       <tbody><tr>
         
 <td>BIO-IN <span id="lblbioin">00:00</span> </td>  
   
 <td> Total Login <span id="lblbioin">00:00</span></td>  
      
<td> Toss <span id="lblbioin">00:00</span></td>  
        
<td> Talk <span id="lblbioin">00:00</span></td>  
       
<td> Hold <span id="lblbioin">00:00</span></td>  
       
<td> Wrap <span id="lblbioin">00:00</span></td>  
      
<td>Tea <span id="lblbioin">00:00</span></td>  
     
<td>Lunch <span id="lblbioin">00:00</span></td>  
       
      <td>Quality <span id="lblbioin">00:00</span></td>  
       
<td>Training <span id="lblbioin">00:00</span></td>  
       
</tr>
 


</tbody></table>
</div>
</div>
</div>
<!--<div class="col-md-3 col-sm-3 know">
<div class="col-md-2 col-sm-2" style=" padding: 0; ">
<img src="images/home.png " style="    width: 43px;">
</div>
<div class="col-md-10 col-sm-10 " style=" padding: 0; ">
<input type="text" placeholder="SEARCH" class="form-control innput">
</div>
</div>-->
<div class="col-md-12 col-sm-12 " style="padding-right:0px">
<div class=" customerdetails" >
<div class="row  ">
 

<form id="regform" method="post">
 <input readonly type="text" name="agentopo" id="agentopo" value="<%=agentopo%>" class="ccp2" style="display:none">
 <input readonly type="text" name="connecttime" id="connecttime" value="0" class="ccp2"style="display:none">
<input readonly type="text" name="calltype" id="calltype" value="1655456" class="ccp2" style="display:none">		
<input readonly type="text" name="campaignname" id="campaignname" value="0" class="ccp2" style="display:none">
<input readonly type="text" name="campaignphone" id="campaignphone" value="0"  class="ccp2"style="display:none">	
<input readonly type="text" id="masterid" name="masterid" value="0"  class="ccp2" style="display:none"/> 
<input readonly type="text" id="leaddate" name="leaddate" value="0"  class="ccp2" style="display:none"/> 
<input readonly type="text" id="batchid"   name="batchid" value="0"  class="ccp2" style="display:none"/> 				


				<div class="col-md-6">
<div class="col-md-6">


<h2 style="color:transparent">ds .</h2> 
Subscriber Name<br>	 <input readonly type="text" id="subscribername" name="subscribername"    class="ccp2" /><br>	
Product	<br>	<input readonly type="text" id="product"   name="product"   class="ccp2" /><br>						
State	<br>	 <input readonly type="text" id="state"  name="state"   class="ccp2" value="0"/><br> 					
City<br>	<input readonly type="text" id="city"  name="city"    class="ccp2" value="0"/><br>
Ph-1		<br><input readonly type="text" id="ph1"  name="ph1" class="ccp2" value="0"/><br>




</div>

<div class="col-md-6">
<h2 style="color:whhite;padding-left:23px;font-size:22px;color:#6bbe5e;text-align:center;"> CUSTOMER DETAILS </h2> 

 App ref No <input readonly type="text" name="apprefno" id="apprefno"    class="ccp2" value="0" /><br>				 
Sms Id	<br>	<input readonly type="text" id="smsid" name="smsid"   class="ccp2" /><br> 
VC No	<br>	<input readonly type="text" id="vcno" name="vcno"    class="ccp2" /><br>
vipno<br><input readonly type="text" name="vipno" id="vipno" value="0"class="ccp2" >

Phone List	<br>	<input readonly type="text" id="phonelist" name="phonelist" value="0"  class="ccp2" /><br>						
		
		
		
			</div>
			</div>
			
<div class="col-md-6">
<div class="col-md-6" >
<h2 style="color:transparent"> dsds.</h2> 

mycode<br><input readonly type="text" name="mycode" id="mycode" value="0"class="ccp2">
Ph-2	<br><input readonly type="text" id="ph2" name="ph2"    class="ccp2" value="0"/><br>


<input readonly type="text" name="agentname" id="agentname" value="<%=username%>" class="ccp2" style="display:none">		

<input readonly type="hidden" id="disposition">
<input readonly type="hidden" id="subdisposition">
<input readonly type="hidden" id="subsubdisposition">
Tel Office<br>	<input readonly type="text" id="teloffice"  name="teloffice"  value="0"   class="ccp2" /><br>
Insno <br> <input  type="text" name="insno" id="insno" value="1655456" class="ccp2" >

</div>	

<div class="col-md-6" style="border-left:1px solid white">
	
		<h2 style="color:whhite;font-size:22px;color:#6bbe5e"> DISPOSE CALL </h2> 
		
Booking count <br><select name="bookingcount" id="bookingcount" value="bookingcount" class="ruvghnj1" required><option value="0" >Select</option><option value="1" >1</option><option value="2" >2</option><option value="3" >3</option><option value="4" >4</option></select>

*Disposition <select name="disposition1" class="ruvghnj1" id="disposition1" ></select>
*Sub Disposition <select class="ruvghnj1"  name="subdisposition1"  id="subdisposition1" required><option value="">select</option>
</select>

*sub sub disposition <select class="ruvghnj1"  name="subsubdisposition1" id="subsubdisposition1" required><option value="">select</option></select>

Remarks <br><textarea type="text" name="remark" id="remark" value="<%=agentopo%>"  class="ccp2"></textarea>



<input readonly type="hidden" name="callinglistname" id="callinglistname" value="" class="ccp2" >
<input readonly type="hidden" name="ocsapplicationid" id="ocsapplicationid" value="" class="ccp2" >
<input readonly type="hidden" name="recordhandle" id="recordhandle" value="" class="ccp2" >
<br>

	   <img     name="submit_button" id="submit_button" src="images/submit.png" 
			    class="btns hvr-forward" Onclick="saveData()" style="display:none">


 
 </div>	
</div>	
			  
				</form>
				
			 		
			 
		
</div>	
<!--  ------------------------------ -->	


</div>

</div>
<p style="clear:both"> </p>
<div class="custHistory" id="custHistory" style="">

</div>
</div>


	  </div>
	  
	  
    </div>
    <!--<div id="menu1" class="tab-pane fade">
      <h3>Menu 1</h3>
      <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
    </div>
    <div id="menu2" class="tab-pane fade">
      <h3>Menu 2</h3>
      <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.</p>
    </div>
    <div id="menu3" class="tab-pane fade">
      <h3>Menu 3</h3>
      <p>Eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.</p>
    </div>-->
  </div>
			<div class="col-md-12">
	<footer >
<img src="images/1p1.png" style="float:right">
	</footer>	
	</div>	
 <script>
 
 $("#arrowup").click(function(){
    $("#mainmenu").slideToggle(); 
    
    $("#mainmenu1").slideToggle(); 
    
    
	 $("#imageup").css("display", "none");
	 $("#imagedown").css("display", "block");
 
});
$("#arrowdown").click(function(){
    $("#mainmenu").slideToggle();
    $("#mainmenu1").slideToggle(); 
    
	 $("#imagedown").css("display", "none");
	 $("#imageup").css("display", "block");
 
});


$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
window.onload = maxWindow;

function maxWindow() {
    window.moveTo(0, 0);


    if (document.all) {
        top.window.resizeTo(screen.availWidth, screen.availHeight);
    }

    else if (document.layers || document.getElementById) {
        if (top.window.outerHeight < screen.availHeight || top.window.outerWidth < screen.availWidth) {
            top.window.outerHeight = screen.availHeight;
            top.window.outerWidth = screen.availWidth;
        }
    }
}
 </script>
 <!--  All Modals -->
 <!-- Modal -->
<div id="breakModal" class="modal fade" role="dialog">
  <div class="modal-dialog" styel="    background: url(images/background.png) no-repeat;">

    <!-- Modal content-->
  <div class="modal-content" style="
    background: url(images/background.png) no-repeat;
    background-size: 100% 100%;
    color: white;
">
      <div class="modal-header" style="
    border: 0;
">
        <button type="button" class="close" data-dismiss="modal">×</button>
       <center> <h4 class="modal-title">Select Type Of Break</h4></center>
      </div>
      <div class="modal-body">
        <p>
        <center>
<select class="ccp2" id="breakModal" onchange="getBreak(this);">
<option value="Tea Break ">Tea Break </option>
<option value="Lunch Break">Lunch Break </option>
<option value="Training Break">Training Break </option>
<option value="Break"> Break </option>
<option value="Break"> Break </option>
 

</select>

</center>
</p>
      </div>
    
    </div>

  </div>
</div> 

<!-- DISPOSITION CALLBACK -->


<div id="CALLBACK" class="modal fade" role="dialog">
  <div class="modal-dialog" styel="    background: url(images/background.png) no-repeat;">

    <!-- Modal content-->
  <div class="modal-content" style="
    background: url(images/background.png) no-repeat;
    background-size: 100% 100%;
    color: white;
">
      <div class="modal-header" style=" border: 0;">
         
       <center> <h4 class="modal-title">SELECT CALLBACK TIME</h4></center>
      </div>
      <div class="modal-body">
        <p>
        <center>
 	<input type="text" id="callbacktime" name="callbacktime" value=""/><br><br>
 

</center>
</p>
  <div class="modal-footer">
        <button type="button" class="close" data-dismiss="modal" class="btn btn-default" >OK</button>
      </div>
      </div>
    
    </div>

  </div>
</div> 
	<script src="js/jquery.datetimepicker.full.js"></script>
			<script src="js/jquery.datetimepicker.full.min.js"></script>
 			<script src="js/jquery.datetimepicker.min.js"></script>

<script src="js/jquery.datetimepicker.min.js"></script>

<script src="js/footer.js"></script>

</body>
</html>