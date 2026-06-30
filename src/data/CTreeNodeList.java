package data;

public class CTreeNodeList extends CArray<CTreeNode>
{
    //--------------------------------------------------------------------------
    public CTreeNodeList(int p_nPageSize)
    {
        super(p_nPageSize);
    }
    //--------------------------------------------------------------------------
    public void Append(CTreeNode p_oNode)
    {
        this.appendItem(p_oNode);
    }
    //--------------------------------------------------------------------------
    public void Remove(CTreeNode p_oNode)
    {
        for(int i = 0; i < this.itemCount; i++)
        {
            CTreeNode oNode = this.getItem(i);
            if (oNode == p_oNode)
            {
                this.Delete(i);
                break;
            }
        }
    }
    //--------------------------------------------------------------------------
}
