/**
 * FIX API: http://www.quickfixj.org/
 */ 
package BitFixClient;
import java.io.FileInputStream;
import java.io.IOException;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.BeginString;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.HeartBtInt;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.ResetSeqNumFlag;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix42.Logon;
import quickfix.fix42.Message.Header;
import quickfix.fix42.NewOrderSingle;

public class testMainClient {

	public static void main(String[] args) throws ConfigError, IOException, SessionNotFound, InterruptedException{
//		 if(args.length != 1) return;
//		    String fileName = args[0];
		String fileName = "testSettings.txt";
		    // FIXApplication is a class that implements the Application interface
		    Application application = new FIXApplicationClient();

		    // set the settings file
		    SessionSettings settings = new SessionSettings(new FileInputStream(fileName));
		    
		    MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		    LogFactory logFactory = new FileLogFactory(settings);
		    MessageFactory messageFactory = new DefaultMessageFactory();
		    
		    // client starts an initiator
		    Initiator initi = new SocketInitiator(application, storeFactory, settings, logFactory, messageFactory);
		    initi.start();

	        SessionID sessionId = initi.getSessions().get(0);
            Session.lookupSession(sessionId).logon();
            sendLogonRequest(sessionId);
            while(!Session.lookupSession(sessionId).isLoggedOn()){
                sendLogonRequest(sessionId);
                System.out.println("Waiting for login success");
                Thread.sleep(1000);
            }
            System.out.println("Logged In...");
             
            Thread.sleep(5000);

	        bookSingleOrder(sessionId);
	        // wait for output to quit
//		    System.out.println("press to send");
//	        System.in.read();
//            sendLogonRequest(sessionId);
	        System.out.println("press to quit");
	        System.in.read();
	        // stop
		    initi.stop();
		
	}
	
	
//	// send longon request
	private static void sendLogonRequest(SessionID sessionId)
            throws SessionNotFound {
        Logon logon = new Logon();
        Header header = (Header) logon.getHeader();
        header.setField(new BeginString("FIX.4.2"));
        logon.set(new HeartBtInt(30));
        logon.set(new ResetSeqNumFlag(true));
        boolean sent = Session.sendToTarget(logon, sessionId);
        System.out.println("Logon Message Sent : " + sent);
    }
    private static void bookSingleOrder(SessionID sessionID){
        //In real world this won't be a hardcoded value rather than a sequence.
        ClOrdID orderId = new ClOrdID("1");
        //to be executed on the exchange
        HandlInst instruction = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE);
        //Since its FX currency pair name
        Symbol mainCurrency = new Symbol("EUR/USD");
        //Which side buy, sell
        Side side = new Side(Side.BUY);
        //Time of transaction
        TransactTime transactionTime = new TransactTime();
        //Type of our order, here we are assuming this is being executed on the exchange
        OrdType orderType = new OrdType(OrdType.FOREX_MARKET);
        NewOrderSingle newOrderSingle = new NewOrderSingle(orderId,instruction,mainCurrency, side, transactionTime,orderType);
        //Quantity
        newOrderSingle.set(new OrderQty(100));
        try {
            Session.sendToTarget(newOrderSingle, sessionID);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
    }
}
