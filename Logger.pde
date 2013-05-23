void CreateLogFile()
{
  logger = createWriter("FG_"+nf(year(), 4)+"_"+nf(month(), 2)+"_"+nf(day(), 2)+"_"+nf(hour(), 2)+nf(minute(), 2)+nf(second(), 2)+".txt"); 
}

void AppendToLog(String message)
{
  String s =  nf(second(), 2);
  String m = nf(minute(), 2);  
  String h = nf(hour(), 2);   

  logger.println(h+":"+m+":"+s+", "+millis()+", "+message); 
}

void CloseLogFile(){
  logger.flush();
  logger.close();
}

