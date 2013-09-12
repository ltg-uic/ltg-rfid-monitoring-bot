package ltg.commons.rfid;

import org.jivesoftware.smack.packet.Message;

public interface MessageListener {

	public void processMessage(Message m);

}