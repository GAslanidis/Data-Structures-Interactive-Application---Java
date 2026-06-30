package data;

import java.io.*;
import java.util.*;

public class CTextFile 
{
    private String fileName;
    
    // -------------------------------------------------------------------------
    public CTextFile(String p_sFileName) 
    {
        this.fileName = p_sFileName;
    }
    // -------------------------------------------------------------------------
    public void LoadArray(CArray<String> p_oLines)
    {
        // Clear the text that this object keeps.
        p_oLines.Clear();
        try 
        {
          File oFileObject = new File(this.fileName);
          Scanner oFileReader = new Scanner(oFileObject);
          while (oFileReader.hasNextLine()) 
          {
            // Reads a single line from the file using the nextLine() method until hasNextLine() returns false
            String sLine = oFileReader.nextLine();
            
            // Add the next line from the file to the text that this object keeps
            p_oLines.appendItem(sLine);
          }
          oFileReader.close();
        } 
        catch (FileNotFoundException e) 
        {
          System.out.println("Error while loading the file " + this.fileName);
          e.printStackTrace();
        }        
    }
    // -------------------------------------------------------------------------
    
}
