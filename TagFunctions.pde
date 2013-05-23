void addTag(int id, String _reader, int _rssi) {
  tags.add(new Tag(id, _reader, _rssi));
  String patch = getPatchName(_reader);
  String jsonMessage =  "{\"event\":\"rfid_update\", \"destination\":\"" + patch + "\", \"payload\":{\"arrivals\" : [\""+id+"\"], \"departures\" :   [] }}";
  //sendMessage(jsonMessage, roomID);
  AppendToLog(patch+", "+ id +", arrival, "+ _rssi);
  println("RSSI " + _rssi + " Patch: " + patch + " ID:" + id + " Add " + "Time: " + nf(hour(), 2) + ":" + nf(minute(), 2) + ":" + nf(second(), 2));
}

void updateTagObject (int id, String _reader, int _rssi) {

  String patch = getPatchName(_reader);
  Integer tagThObj = (Integer)patchHashMap.get(patch).get(id);
  //println(patch);
  if (tagThObj != null) {
    int tagTh = tagThObj.intValue();
    //println(tagTh);

    if (_rssi > tagTh) {
      Tag b = findTag(id);

      if (b==null) {
        addTag(id, _reader, _rssi);
      }
      else {
        if (b.reader.equals(_reader)) {
          patch = getPatchName(_reader);
          b.updateLife(_rssi);
          println("RSSI " + _rssi + " Patch: " + patch + " ID:" + id + " Update" + "Time: " + nf(hour(), 2) + ":" + nf(minute(), 2) + ":" + nf(second(), 2));
          //AppendToLog(patch+", "+ id +", update, "+ b.rssi);
        }
        else {
          //if (_rssi > b.rssi) {
          if (getPatchName(b.reader).equals("fg-den") && getPatchName(_reader).equals("fg-den")) {
          }
          else {
            removeTagObject(id);
            //println("old tag new reader");
            addTag(id, _reader, _rssi);
            //println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++detected in other reader with rssi:" +  _rssi);
            //}
          }
          b.updateLife(b.rssi);
        }
      }
    }
  }
}


void removeTagObject(int id) {
  Tag b = findTag(id);
  if (b != null) {
    String patch = getPatchNameForTag(b);
    String jsonMessage =  "{\"event\":\"rfid_update\", \"destination\":\"" + patch + "\", \"payload\":{\"arrivals\" : [], \"departures\" :   [\""+id+"\"] }}";
    //sendMessage(jsonMessage, roomID);
    AppendToLog(patch+", "+ id +", departure, "+ b.rssi);
    println("RSSI " + b.rssi + " Patch: " + patch + " ID:" + id + " Departure " + "Time: " + nf(hour(), 2) + ":" + nf(minute(), 2) + ":" + nf(second(), 2));
    tags.remove(b);
  }
}

Tag findTag (int id) {
  for (int i=0; i < tags.size(); i++) {
    if ( tags.get(i).id == id) {
      return tags.get(i);
    }
  }
  return null;
}

String getPatchNameForTag(Tag b) {
  return (getPatchName(b.reader));
}

String getPatchName(String _reader) {

  String _patch = "";
  if (_reader.equals("131.193.161.121")) {
    _patch = "fg-patch-1";
  }
  else if (_reader.equals("131.193.161.122")) {
    _patch = "fg-patch-2";
  }
  else if (_reader.equals("131.193.161.123")) {
    _patch = "fg-patch-3";
  }
  else if (_reader.equals("131.193.161.124")) {
    _patch = "fg-patch-4";
  }
  else if (_reader.equals("192.168.0.25")) {
    _patch = "fg-patch-5";
  }
  else if (_reader.equals("192.168.0.26")) {
    _patch =  "fg-patch-6";
  }
  else if (_reader.equals("192.168.0.27")) {
    _patch =  "fg-den";
  }
  else if (_reader.equals("192.168.0.28")) {
    _patch =  "fg-den";
  }

  return _patch;
}




int getTagsOnReader(String _reader) {

  String reader = " ";
  int count=0;

  for (int i=0; i < tags.size(); i++) {
    reader = tags.get(i).reader;   
    if (reader.equals(_reader)) {
      count++;
    }
  }
  return count;
}

