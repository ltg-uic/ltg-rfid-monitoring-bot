void sendMessage(String messg, String to) {
  Message m = new Message(to, Message.Type.groupchat);
  m.setBody(messg);
  conn.sendPacket(m);
}

void configXMPP() {
  config = new ConnectionConfiguration("ltg.evl.uic.edu", 5222);
  conn = new XMPPConnection(config);
  try {
    conn.connect();
    conn.login("fg-rfid-dispatch", "fg-rfid-dispatch");
    joinGroupChat();
  } 
  catch (XMPPException e) {
    e.printStackTrace();
    System.out.println(e.getXMPPError());
  }
}

void joinGroupChat() {
  if (!conn.isConnected())
    throw new NullPointerException("XMPPService must be logged in before it can join a conference room!");

  MultiUserChat muc = new MultiUserChat(conn, roomID);
  try {
    muc.join("fg-rfid-dispatch");
  }
  catch (XMPPException e) {
    e.printStackTrace();
    System.out.println(e.getXMPPError());
  }
  //  Presence p = new Presence(Presence.Type.available);
  //  p.setStatus("chat");
  //  p.setTo(getRoomJid()+"/"+resource);
  //  xmpp.sendPacket(p);
}

