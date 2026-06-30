package data;

// We can load/save any type of dictionary with key type K and value type V
// [OBJECT ORIENTED]: Generics. Class and methods.

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


public class CJSONFileDict<K, V>
{
    private String fileName;
    
    //--------------------------------------------------------------------------
    public CJSONFileDict(String p_sFileName)
    {
        this.fileName = p_sFileName;
    }
    //--------------------------------------------------------------------------
    public void LoadDict(CDictionary<K, V> p_oDict)
    {   //JSON parser object to parse read file
        JSONParser oJSONParser = new JSONParser();
        try (FileReader oReader = new FileReader(this.fileName))
        {
            // Parse the contents of the file into a JSON object that supports the put/get methods
            JSONObject oFileAsObject = (JSONObject)oJSONParser.parse(oReader);
            p_oDict.Clear(); // Clear all existing entries in the dictionary
            
            //[OO PRINCIPLES] Abstraction: We can view any JSONObject as an iterator, 
            //                that is an interface reference of the object with only the methods
            //                that allow us to sequentially iterate over all its entries.
            Iterator iIterator = oFileAsObject.keySet().iterator(); 
            
            // For each key in the JSON file set the proper key-value pair in the dictionary
            while(iIterator.hasNext())
            {
                // Read K-V pair from JSON 
                K  sJSONKey    = (K)iIterator.next();
                V  sJSONValue  = (V)oFileAsObject.get(sJSONKey);
                p_oDict.setValue(sJSONKey, sJSONValue);
            }
        } 
        //[JAVA]: Example for a try..catch block that catches multiple cases of exceptions.
        //      For each error case it will log the proper message to the default application error log
        catch (FileNotFoundException ex) {
            Logger.getLogger(CJSONFileDict.class.getName()).log(Level.SEVERE, "File not found", ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(CJSONFileDict.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
        } 
        catch (ParseException ex) {
            Logger.getLogger(CJSONFileDict.class.getName()).log(Level.SEVERE, "Error while parsing JSON format", ex);
        } 
    }
    //--------------------------------------------------------------------------
    public void SaveDict(CDictionary<K, V> p_oDict)
    {
        try (FileWriter oWriter = new FileWriter(this.fileName))
        {
            // Create an empty JSON object
            JSONObject oJSON = new JSONObject();
            
            // Dictionary traversal: For each key value entry the dictionary
            //                       we create the proper pair in the JSON object. 
            
            //[OO PRINCIPLES] Abstraction: We can view any CDictionary as an Iterator<CKeyValueEntry>. 
            //                Now with abstraction this code is not bound to a specific 
            //                implementation (array or linked-list).
            
            Iterator<CKeyValueEntry> iIterator = (Iterator)p_oDict;
            while(iIterator.hasNext())
            {
                CKeyValueEntry oEntry = iIterator.next();
                String nKey     = (String)oEntry.Key;
                String  sValue  = (String)oEntry.Value;
                oJSON.put(nKey, sValue);
            }
            
            
            // Writing the file on the disk from a JSON object.
            oWriter.write(oJSON.toJSONString());
            oWriter.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CJSONFileDict.class.getName()).log(Level.SEVERE, "File not found", ex);
        } catch (IOException ex) {
            Logger.getLogger(CJSONFileDict.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
        } 
    }
    //--------------------------------------------------------------------------    
    
    
    
}
