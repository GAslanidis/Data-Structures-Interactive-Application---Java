package data;

// This supports the generic implementation, for any type of key and any type of value
public class CKeyValueEntry
{
    public Object Key = null;
    public Object Value = null;    
    public int    Index = -1; 
    
    //--------------------------------------------------------------------------
    public CKeyValueEntry(Object p_oKey)
    {
        this.Key = p_oKey;
    }
    
     public CKeyValueEntry(Object p_oKey, Object p_oValue)
    {
        this.Key = p_oKey;
        this.Value = p_oValue;
    }
    //--------------------------------------------------------------------------
}
