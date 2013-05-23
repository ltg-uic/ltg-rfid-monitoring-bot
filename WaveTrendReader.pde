boolean setReaderID(Client reader_, byte newID_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x00));
  command.add(byte(0x05));
  command.add(newID_);

  byte b2, b3, b4, b5, b6, b7;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);
  b7 = command.get(6);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();

    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}

boolean setNetworkID(Client reader_, byte newID_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x00));
  command.add(byte(0x04));
  command.add(newID_);

  byte b2, b3, b4, b5, b6, b7;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);
  b7 = command.get(6);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}


boolean setRSSI(Client reader_, byte rssi_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x00));
  command.add(byte(0x07));
  command.add(rssi_);

  byte b2, b3, b4, b5, b6, b7;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);
  b7 = command.get(6);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}

boolean getRSSI(Client reader_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x08));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 8) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 8) {  
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
      println(" RSSI:" + (int)responseArray[6]);
    }
  }
  println();
  return true;
}

boolean enableAutopolling(Client reader_) {
  println("enabling AUTOPOLLING");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x01));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}


boolean disableAutopolling (Client reader_) {
  println("disabling AUTOPOLLING");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x02));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}



boolean pingReader(Client reader_) {
  println("ping reader " + reader_);
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x03));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 8) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 8) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }

  println();
  return true;
}

boolean setAlarmFilter(Client reader_, int _filter) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x01));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x0D));
  command.add(byte(_filter));

  byte b2, b3, b4, b5, b6, b7;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);
  b7 = command.get(6);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}


boolean setGainMode(Client reader_, byte gain_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x01));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x0B));
  command.add(gain_);

  byte b2, b3, b4, b5, b6, b7;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);
  b7 = command.get(6);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6^=b7;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}

boolean getGainMode(Client reader_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x0C));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 8) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 8) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}




boolean clearBuffer(Client reader_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x13));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}

boolean evaluateNoise(Client reader_) {
  println("sending command...");
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x11));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  println(command);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);

  //Get response
  while (reader.available () < 7) {
  }
  if (reader.available()>0) {
    byte[] responseArray = reader.readBytes();
    if (responseArray.length == 7) {
      for (int i=0; i < responseArray.length; i++) {
        print(hex(responseArray[i]));
      }
    }
  }
  println();
  return true;
}


boolean getTagDataPacket(Client reader_) {
  ArrayList<Byte> command;
  ArrayList<Byte> response;
  Client reader = reader_;
  command = new ArrayList();
  command.add(byte(0xAA));
  command.add(byte(0x00));
  command.add(byte(0x01));
  command.add(getReceiverID(reader_.ip()));
  command.add(byte(0x00));
  command.add(byte(0x06));

  byte b2, b3, b4, b5, b6;
  b2 = command.get(1);
  b3 = command.get(2);
  b4 = command.get(3);
  b5 = command.get(4);
  b6 = command.get(5);

  byte checkSumByte = b2^=b3^=b4^=b5^=b6;
  command.add(checkSumByte);
  byte[] byteCommand = new byte[command.size()];
  for (int i=0; i<command.size(); i++) {
    byteCommand[i] = command.get(i);
  }
  reader.write(byteCommand);


  while (reader.available ()>0) {

    byte[] bufferArray = reader.readBytes();
    byte[] tagPacket = new byte[39];


    if ((bufferArray.length > 0) && (bufferArray.length % 39 == 0)) {
      int count = 0;   

      for (int i = 0; i < bufferArray.length / 39; i++) {
        for (int j = 0; j < 39; j++) {
          tagPacket[j] = bufferArray[i*39+j];
        }
        int numOfBytes = tagPacket[1];
        if (numOfBytes == 32) {
          //          println("Interval = " + tagPacket[9]);
          //          println("ReaderID = " + tagPacket[3]);
          //          println("NodeID = " + tagPacket[4]);
          //          println("TagRSSI = " + tagPacket[28]);
          //          println("ReaderSSI = " + tagPacket[34]);

          int rssi = tagPacket[28];
          int tagID = ((tagPacket[22] << 24) & 0xFFFFFFFF) 
            +((tagPacket[23] << 16) & 0xFFFFFF)
              +((tagPacket[24] << 8) & 0xFFFF)
                +((tagPacket[25]) & 0xFF);

          //println("RSSI " + tagPacket[28] + " Reader: " + reader.ip() + " ID" + tagID);

          byte tagCRC = byte((tagPacket[9]+tagPacket[10]+tagPacket[14]+tagPacket[15]
            +tagPacket[16]+tagPacket[17]+tagPacket[18]+tagPacket[19]
            +tagPacket[20]+tagPacket[21]+tagPacket[22]+tagPacket[23]
            +tagPacket[24]+tagPacket[25]+tagPacket[26]));

          byte tagCRC29 = tagPacket[29];
          //println(binary(tagCRC));
          //println(binary(tagPacket[29]));


          byte readerCRC=(tagPacket[1]^=tagPacket[2]^=tagPacket[3]^=tagPacket[4]^=tagPacket[5]
            ^=tagPacket[6]^=tagPacket[7]^=tagPacket[8]^=tagPacket[9]^=tagPacket[10]
            ^=tagPacket[11]^=tagPacket[12]^=tagPacket[13]^=tagPacket[14]^=tagPacket[15]
            ^=tagPacket[16]^=tagPacket[17]^=tagPacket[18]^=tagPacket[19]^=tagPacket[20]
            ^=tagPacket[21]^=tagPacket[22]^=tagPacket[23]^=tagPacket[24]^=tagPacket[25]
            ^=tagPacket[26]^=tagPacket[27]^=tagPacket[28]^=tagPacket[29]^=tagPacket[30]
            ^=tagPacket[31]^=tagPacket[32]^=tagPacket[33]^=tagPacket[34]^=tagPacket[35]
            ^=tagPacket[36]^=tagPacket[37]);

          byte readerCRC38 = tagPacket[38];
          //        println(readerCRC);
          //        println(tagPacket[38]);


          if ((tagCRC == tagCRC29) && (readerCRC == readerCRC38)) {
            String ip = reader_.ip();
            //println(getPatchNumber(reader_.ip())+ "  " + tagID);
            updateTagObject(tagID, ip, rssi );
            //clearBuffer(reader_);
          }
        }
      }
    }
  }

  return true;
}

byte getReceiverID(String _reader) {
  byte _id = byte(0x00);
  if (_reader.equals("131.193.161.121")) {
    _id = byte(0x01);
  }
  else if (_reader.equals("131.193.161.122")) {
    _id = byte(0x02);
  }
  else if (_reader.equals("131.193.161.123")) {
    _id = byte(0x03);
  }
  else if (_reader.equals("131.193.161.124")) {
    _id = byte(0x04);
  }
  else if (_reader.equals("192.168.0.25")) {
    _id = byte(0x05);
  }
  else if (_reader.equals("192.168.0.26")) {
    _id = byte(0x06);
  }
  else if (_reader.equals("192.168.0.27")) {
    _id = byte(0x07);
  }
  else if (_reader.equals("192.168.0.28")) {
    _id = byte(0x08);
  }

  return _id;
}

