package data;

public class CNodeQueue extends CArray<CTreeNode>
{
    //--------------------------------------------------------------------------
    public CNodeQueue(int p_nPageSize) 
    {
        super(p_nPageSize);
    }
    //--------------------------------------------------------------------------
    public boolean IsEmpty()
    {
        return (this.itemCount == 0);
    }
    //--------------------------------------------------------------------------
    public boolean Enqueue(CTreeNode p_oNode)
    {
        this.appendItem(p_oNode);
        return true;
    }
    //--------------------------------------------------------------------------
    public CTreeNode Dequeue()  
    {
        CTreeNode oNode = null;
        if (this.itemCount > 0)
        {
            oNode = (CTreeNode)this.items[0];
            this.deleteFirstItem();
        }
       return oNode;
    }      
    //--------------------------------------------------------------------------
    public CTreeNode Peek(int p_nIndex)
    {
        int nFirstItemIndex =  0;
        return (CTreeNode)this.items[nFirstItemIndex];
    }
    //--------------------------------------------------------------------------    
}
