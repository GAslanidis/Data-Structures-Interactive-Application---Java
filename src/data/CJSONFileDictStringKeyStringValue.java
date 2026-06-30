package data;

import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

// To use these packages you need to add to your project the json-simple-1.1.jar library
//    1. Copy the file json-simple-1.1.jar into your project's src folder
//    2. Right click on Libraries -> Add JAR/Folder. Choose the file from your src folder.

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class CJSONFileDictStringKeyStringValue
{
    private String fileName;
    
    //--------------------------------------------------------------------------
    public CJSONFileDictStringKeyStringValue(String p_sFileName)
    {
        this.fileName = p_sFileName;
    }
    //--------------------------------------------------------------------------
    public void LoadDict(CDictionary<String, String> p_oDict)
    {   //JSON parser object to parse read file
        JSONParser oJSONParser = new JSONParser();
        try (FileReader oReader = new FileReader(this.fileName))
        {
            // Parse the contents of the file into a JSON object that supports the put/get methods
            JSONObject oFileAsObject = (JSONObject)oJSONParser.parse(oReader);
            p_oDict.Clear(); // Clear all existing entries in the dictionary/hashtable
            // For each key in the JSON file set the proper key-value set in the dictionary/hashtable
            Iterator iterator = oFileAsObject.keySet().iterator(); 
            while(iterator.hasNext())
            {
                // Read K-V pair from JSON 
                String  sJSONKey    = (String)iterator.next();
                String  sJSONValue  = (String)oFileAsObject.get(sJSONKey);
                
                // Make any necessary convertions before setting the K-V pair
                String sKey         = sJSONKey;
                String sValue       = sJSONValue;
                
                p_oDict.setValue(sKey, sValue);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CJSONFileDictStringKeyStringValue.class.getName()).log(Level.SEVERE, "File not found", ex);
        } catch (IOException ex) {
            Logger.getLogger(CJSONFileDictStringKeyStringValue.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
        } catch (ParseException ex) {
            Logger.getLogger(CJSONFileDictStringKeyStringValue.class.getName()).log(Level.SEVERE, "Error while parsing JSON format", ex);
        } 
    }
    //--------------------------------------------------------------------------
    public void SaveDict(CDictionary<String, String> p_oDict)
    {
        try (FileWriter oWriter = new FileWriter(this.fileName))
        {
            // Create an empty JSON object
            JSONObject oJSON = new JSONObject();
            
            // Dictionary traversal: For each key value entry the dictionary
            // we create the proper pair in the JSON object. 
            // NOTE: We use while as the general approach to traversal.
            //       Because the dictionary is based on an array, we can use the index. 
            //       If it was a linked list the while should work by getting the next item.
            int nIndex = 0;
            while(nIndex < p_oDict.itemCount)
            {
                CKeyValueEntry oEntry = p_oDict.getEntry(nIndex);
                String  sKey     = (String)oEntry.Key;
                String  sValue   = (String)oEntry.Value;
                oJSON.put(sKey, sValue);
                nIndex ++;
            }
            
            // Writing the file on the disk from a JSON object.
            oWriter.write(oJSON.toJSONString());
            oWriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CJSONFileDictStringKeyStringValue.class.getName()).log(Level.SEVERE, "File not found", ex);
        } catch (IOException ex) {
            Logger.getLogger(CJSONFileDictStringKeyStringValue.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
        } 
    }
    //--------------------------------------------------------------------------    
    
    
    
}
