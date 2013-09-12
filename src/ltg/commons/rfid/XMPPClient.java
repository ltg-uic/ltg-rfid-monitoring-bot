package ltg.commons.rfid;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import ltg.commons.rfid.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class XMPPClient {

	protected XMPPConnection connection = null; 
	protected ChatManager  chatManager = null;
	protected MessageListener messageListener = null;
	protected ConnectionConfiguration config = null;
	protected MultiUserChat groupChat = null;
	protected PacketCollector packetCollector = null; 
	
	public XMPPClient(String username, String password, String hostname) {
		try {
			connection = new XMPPConnection(hostname);
			connection.connect();
		} catch (XMPPException e) {
			System.err.println("Impossible to CONNECT to the XMPP server, terminating");
			System.exit(-1);
		}
		try {
			connection.login(username, password);
		} catch (XMPPException e) {
			System.err.println("Impossible to LOGIN to the XMPP server, terminating");
			System.exit(-1);
		} catch (IllegalArgumentException e) {
			System.err.println("Impossible to LOGIN to the XMPP server, terminating");
			System.exit(-1);
		}
	}
	
	public XMPPClient(String username, String password, String hostname, String chatRoom) {
		this(username, password, hostname);
		if (connection.isAuthenticated() && chatRoom!=null) {
			groupChat = new MultiUserChat(connection, chatRoom);
			try {
				groupChat.join(connection.getUser());
			} catch (XMPPException e) {
				System.err.println("Impossible to join GROUPCHAT, terminating");
				System.exit(-1);
			}
		}
	}
	
	public Message nextMessage() {
		if (packetCollector==null)
			packetCollector = connection.createPacketCollector(new PacketTypeFilter(Message.class));
		return (Message) packetCollector.nextResult();
	}
	
	public void registerEventListener(final MessageListener eventListener) {
		PacketListener pl = new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				eventListener.processMessage((Message) packet);
			}
		};
		connection.addPacketListener(pl, new PacketTypeFilter(Message.class));
	}
	
	public void sendMessage(String to, String message) {
		if (connection==null || !connection.isAuthenticated()){
			System.err.println("Impossible to send message to " +to + ": we have been disconnected! Terminating");
			System.exit(-1);
		}
		Message m = new Message(to, Message.Type.normal);
		m.setBody(message);
		connection.sendPacket(m);
	}


	public void sendMessage(String message) {
		if (connection==null || !connection.isAuthenticated() || !groupChat.isJoined()){
			System.err.println("Impossible to send message to groupchat: we have been disconnected! Terminating");
			System.exit(-1);
		}
		Message m = new Message(groupChat.getRoom(), Message.Type.groupchat);
		m.setBody(message);
		connection.sendPacket(m);
	}

	public String getUsername() {
		return connection.getUser();
	}

	public void disconnect() {
		if (groupChat!=null && groupChat.isJoined()) {
			groupChat.leave();
			groupChat = null;
		}
		if (connection.isAuthenticated()) {
			connection.disconnect();
			connection = null;
		}
	}

	
	
	
	
	
	
}
