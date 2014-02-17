/**
 * FIX API: http://www.quickfixj.org/
 */ 

package BitFixClient;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.fix42.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix42.ExecutionReport;


public class FIXApplicationClient extends MessageCracker implements Application {

	@Override
	public void fromAdmin(Message msg, SessionID sessID) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub

	}

	@Override
	public void fromApp(Message msg, SessionID sessID) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		 crack(msg, sessID);
	}

	@Override
	  public void onMessage(ExecutionReport message, SessionID sessionID)
	            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	        System.out.println("Received Execution report from server");
	        System.out.println("Order Id : " + message.getOrderID().getValue());
	        System.out.println("Order Status : " + message.getOrdStatus().getValue());
	        System.out.println("Order Price : " + message.getPrice().getValue());
	        System.out.println("Order cumQty : " + message.getCumQty().getValue());
	        System.out.println("Order Side : " + message.getSide().getValue());
	    }
	  
	@Override
	public void onCreate(SessionID sessID) {
		System.out.println("Created!");
	}

	@Override
	public void onLogon(SessionID sessID) {
		System.out.println("Client: I have Logged on to session: "+sessID);
	}

	@Override
	public void onLogout(SessionID sessID) {
		System.out.println("Client: I have Logged out of session: "+sessID);
	}

	@Override
	public void toAdmin(Message msg, SessionID sessID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toApp(Message msg, SessionID sessID) throws DoNotSend {
		System.out.println("Client: I have sent a message.");
	}

}
