class Tag {
  String reader;
  int id;
  int life;
  int rssi;
  Tag(int _id, String _reader, int _rssi) {   
    id = _id;
    reader = _reader;
    life = TagLife;
    rssi = _rssi;
  } 

  void updateLife(int _rssi) {
    life = TagLife;
    rssi = _rssi;
  }

  boolean checkLife() {
    if (life < 0) {
      return true;
    }
    else {
      life-=lifeDecrement;   
      return false;
    }
  }
}

