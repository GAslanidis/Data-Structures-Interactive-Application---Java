package data;

public class CStringQueue extends CArray<String>
{
    //--------------------------------------------------------------------------
    public CStringQueue(int p_nPageSize) 
    {
        super(p_nPageSize);
    }
    //--------------------------------------------------------------------------
    public boolean IsEmpty()
    {
        return (this.itemCount == 0);
    }
    //--------------------------------------------------------------------------
    public boolean Enqueue(String p_sItem)
    {
        this.appendItem(p_sItem);
        return true;
    }
    //--------------------------------------------------------------------------
    public String Dequeue()  
    {
       int nFirstItemIndex =  0;
       String sItem = (String)this.items[nFirstItemIndex];
       this.deleteFirstItem();
       return sItem;
    }      
    //--------------------------------------------------------------------------
    public String Peek(int p_nIndex)
    {
        int nFirstItemIndex =  0;
        return (String)this.items[nFirstItemIndex];
    }
    //--------------------------------------------------------------------------    
}
