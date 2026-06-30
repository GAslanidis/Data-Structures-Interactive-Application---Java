package data;


//--------------------------------------------------------------------------    
public class CLinkedListItem
{
    //--------------------------------------------------------------------------
    private CLinkedListItem nextItem = null;
    // ................................................
    public CLinkedListItem getNextItem() {
        return nextItem;
    }
    // ................................................
    public void setNextItem(CLinkedListItem p_oNextItem) {
        this.nextItem = p_oNextItem;
    }
    //--------------------------------------------------------------------------
    
    // This is a  value "boxed" inside an object of CLinkedListItem
    public Object Value = null;
}
//--------------------------------------------------------------------------    
