package br.com.bc.jarless.repository.prevayler;

import java.io.IOException;

import org.prevayler.Prevayler;

public class SnapshotTimer extends Thread {  
    
    Prevayler prevayler;  
      
    public SnapshotTimer(Prevayler prevayler) {  
        this.prevayler = prevayler;  
    }  
          
    public void run()  {  
        super.run();  
         
   try {          
           while (true) {  
                   Thread.sleep(1000 * 5);  
                   prevayler.takeSnapshot();  
                   //System.out.println("Snapshot disparado as " + new java.util.Date() + "...");  
           }  
   } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }            
    }  
}  
