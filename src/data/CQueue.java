package data;

public class CQueue<T> extends CLinkedList<T>
{
    //--------------------------------------------------------------------------
    public CQueue()
    {
        super();
    }
    //--------------------------------------------------------------------------
    public void Enqueue(T p_sItem)
    {
        this.appendItem(p_sItem);
    }
    //--------------------------------------------------------------------------
    public T Dequeue()  
    {
        T sFirstItem = null;
        if (this.itemCount > 0)
        {
            sFirstItem = (T)this.FirstItem.Value;
            this.deleteFirstItem();
        }
        return sFirstItem;
    }      
    //--------------------------------------------------------------------------
    public T Peek()
    {
        T sFirstItem = null;
        if (this.itemCount > 0)
        {
            sFirstItem = (T)this.FirstItem.Value;
        }
        
        return sFirstItem;
    }
    //--------------------------------------------------------------------------
    public void Delete(int p_nIndex)
    {
        this.delete(p_nIndex);
    }    
    //--------------------------------------------------------------------------
    public boolean IsEmpty()
    {
        return (this.itemCount == 0);
    }     
    //--------------------------------------------------------------------------
    
}
