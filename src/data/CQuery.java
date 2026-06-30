package data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author georgiosaslanidis
 */
public class CQuery {
    
    private static int queriesID = 1;
    
    private int ID;
//............................   
    public int getID() {
        
        return this.ID;
    }
//------------------------------------------- 
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    
    String TimeStamp;
//.........................
    public String getTime(){
        
        return this.TimeStamp;
    }
//-------------------------------------------   
    
    private String Title;
//..............................    
    public String getTitle(){
        
        return this.Title;
    }
    
//-------------------------------------------
    
    private String Content;
//..............................
    public String getContent(){
        
        return this.Content;
    }
    
//-------------------------------------------
    public CQuery(String p_sTitle, String p_sContent) {
        
        this.Title = p_sTitle;
        this.Content = p_sContent;
        this.TimeStamp = dateFormat.format(date);
        this.ID = CQuery.queriesID;
        
        CQuery.queriesID++;
        
    }
    
//--------------------------------------------
    
    @Override
    public String toString(){
        
        return  "Title: " + this.Title  
                +" - ID: " + this.ID 
                + " - " + this.TimeStamp + "\n";
    }
    
}
