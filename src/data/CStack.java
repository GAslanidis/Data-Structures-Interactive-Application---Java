package data;

public class CStack<T> extends CLinkedList<T>
{
    //--------------------------------------------------------------------------
    public void Push(T p_sItem)
    {
        this.prependItem(p_sItem);  //Instead of using append and adding the item to the end
                                              //I use prepend so it will put the item
                                              //in the first position of the list
                                              //and thus it will require only O(1) steps
                                              //to Pop items by using deleteFirstItem and not
                                              //deleteLastItem as usually, thus maintaining
                                              //the LIFO theory.
    }
    //--------------------------------------------------------------------------
    public T Pop()  
    {
       T sFirstItem = null;
        if (this.itemCount > 0)
        {
            sFirstItem = (T)this.FirstItem.Value;
            this.deleteFirstItem();                 //detele First and not Last item
        }
        return sFirstItem;
    }      
    //--------------------------------------------------------------------------
    public T Peek()
    {
       T sFirstItem = null;
        if (this.itemCount > 0)
        {
            sFirstItem = (T)this.FirstItem.Value;   //As the first item in the list 
                                                    //is also the last added, we need
                                                    //to peek the first item
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
