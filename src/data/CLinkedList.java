package data;


public class CLinkedList<T>
{
    public CLinkedListItem FirstItem = null; 
    public CLinkedListItem LastItem = null;
    
    //--------------------------------------------------------------------------
    protected int itemCount = 0; // We make this field protected so it can be used by descendants.
    // ................................................
    public int getItemCount()  {
        return this.itemCount;
    }
    //--------------------------------------------------------------------------

    
    //--------------------------------------------------------------------------
    public T getItem(int index) {
        // Indirect access to the item at index, via sequential traversal. Costs O(n)
        CLinkedListItem oFoundItem = this.locateItem(index);
        if (oFoundItem != null)
           return (T)oFoundItem.Value;
        else
            return null;
    }
    // ................................................
    public void setItem(int index, T p_oItemValue) {
        // Indirect access to the item at index, via sequential traversal. Costs O(n)
        CLinkedListItem oFoundItem = this.locateItem(index);
        if (oFoundItem != null)
            oFoundItem.Value = p_oItemValue;
    }    
    //--------------------------------------------------------------------------

    
    
    
    //--------------------------------------------------------------------------
    public CLinkedList()
    {
    }
    //--------------------------------------------------------------------------
    public void Clear()
    {
        // Remove the two references that hold the linked-list chain in the memory
        // After this the garbage collector will dispose the chaing of unusud objects
        this.FirstItem  = null;
        this.LastItem   = null;
        this.itemCount  = 0;
    }
    //--------------------------------------------------------------------------
    protected void appendItem(T p_oItemValue)
    {
        // Create a new link in the chain
        CLinkedListItem oNewLastItem = new CLinkedListItem();
        // Box the value inside the new link
        oNewLastItem.Value = p_oItemValue;        
        
        
        if (itemCount == 0)
            // The first item appended to the linked-list
            this.FirstItem = oNewLastItem;
        else
            // Set the link of the last item to add the item after that (append).
            this.LastItem.setNextItem(oNewLastItem);
        
        // In any case the item becomes the last in the lined-list, and the count increases.
        this.LastItem = oNewLastItem;
        itemCount++;
    }
    //--------------------------------------------------------------------------    
    protected void prependItem(T p_oItemValue)
    {
        // Create a new link in the chain
        CLinkedListItem oNewFirstItem = new CLinkedListItem();
        // Box the value inside the new link
        oNewFirstItem.Value = p_oItemValue;  
        
        
        if (itemCount == 0)
            // The first item prepended to the linked-list would be also the last
            this.LastItem = oNewFirstItem;
        else
            // Set the link to point to the old first item.
            oNewFirstItem.setNextItem(this.FirstItem);
        
        // In any case the item becomes the first in the lined-list, and the count increases.
        this.FirstItem = oNewFirstItem;
        itemCount++;        
    }
    //--------------------------------------------------------------------------
    protected void insertItem(int p_nIndex, T p_oItemValue)
    {
        CLinkedListItem oPreviousItem = null;
        if (p_nIndex == 0)
            this.prependItem(p_oItemValue);
        else if (p_nIndex >= this.itemCount) 
            this.appendItem(p_oItemValue);
        else
        {
            // Create a new link in the chain
            CLinkedListItem oNewItem = new CLinkedListItem();
            // Box the value inside the new link
            oNewItem.Value = p_oItemValue; 

            // For inserting B between A -> C, we need to find the item
            // that will become the previous item of B, that is A
            oPreviousItem = locateItem(p_nIndex - 1);
   
            // Inserting B between A -> C is done with the following steps
            oNewItem.setNextItem(oPreviousItem.getNextItem());  // B -> C
            oPreviousItem.setNextItem(oNewItem);                // A -> B ( -> C )
                
            itemCount++;
        }
    }    
    //--------------------------------------------------------------------------
    protected void deleteFirstItem()
    {
        if (this.FirstItem != null)
        {
            // Keep the item after the first item (the one that will become the first) 
            CLinkedListItem oNewFirstItem = this.FirstItem.getNextItem();
            // Remove the link from the first item that is beeing deleted.
            this.FirstItem.setNextItem(null);
            
            // Set the kept item as the first item and decrease count
            this.FirstItem = oNewFirstItem;
            itemCount--;

            // In case we deleted the last item we set the reference to null
            if (itemCount == 0)
                this.LastItem = null;
        }
    }
    //--------------------------------------------------------------------------
    protected void deleteLastItem()
    {
        this.delete(this.itemCount - 1);
    }
    //--------------------------------------------------------------------------
    protected void delete(int p_nIndex)  
    {
        // For the first item runs a simple logic with O(1) cost
        if (p_nIndex == 0)
            this.deleteFirstItem();
        else if((p_nIndex > 0) && (p_nIndex < this.itemCount))
        {   // Initialize the two items that we need to find
            
            CLinkedListItem oItemToDelete = this.FirstItem;
            CLinkedListItem oPreviousItem = this.FirstItem;
            
            // Traverse the linked-list to find the position. This has an O(n) cost.
            int nIndex = 0;
            while (nIndex < p_nIndex)
            {   // Keep the previous item of the one that will be deleted
                oPreviousItem = oItemToDelete; 
                oItemToDelete = oItemToDelete.getNextItem();
                nIndex ++;
            }
            
            // Deleting B in the chain A -> B -> C is done by 
            // skipping skipping the item that is deleted, pointing to its next item
            // in these steps:
            CLinkedListItem oNextItem = oItemToDelete.getNextItem(); // C
            oPreviousItem.setNextItem(oNextItem);             // A -> C 
            // Remove the link from the item that is beeing deleted.
            oItemToDelete.setNextItem(null);

            // In case the last item was deleted, its previous is now the last one
            if (oItemToDelete == this.LastItem)
                this.LastItem = oPreviousItem;
            
            itemCount--;
        }
    } 
    //--------------------------------------------------------------------------
    // This linked-list traversal visits items in a sequential manner.
    private CLinkedListItem locateItem(int p_nIndex)
    {
        int nIndex = 0;
        CLinkedListItem oCurrentItem = this.FirstItem;
        while (nIndex < p_nIndex)
        {
            oCurrentItem = oCurrentItem.getNextItem();
            nIndex ++;
        }
        return oCurrentItem;
    }     
    //--------------------------------------------------------------------------
    // This method will help us display the data structure in the UI
    @Override
    public String toString()
    {
        int nIndex = 0;
        String sResult = "";
        CLinkedListItem oCurrentItem = this.FirstItem;
        while (oCurrentItem != null)
        {
            if(nIndex>0)
                sResult += "\r\n";

            sResult += "Query " +Integer.toString(nIndex) + ": " + oCurrentItem.Value.toString();

            oCurrentItem = oCurrentItem.getNextItem();
            nIndex ++;
        }
        return sResult;
    }
    //--------------------------------------------------------------------------    
}
