package data;

 public class CTreeNode 
{
    // Basic fields for the implementation of a Tree Node
    private   CTreeNode root;           // The root node of the tree
    protected CTreeNode parent;         // The parent node of this tree node
    protected CTreeNodeList children = new CTreeNodeList(32);  // All child nodes of this tree node

    // Information fields
    public String Name = null;         // The name of the tree node
    public Object Value = null;        // Some value/item that the tree node may store.
    
    
    //--------------------------------------------------------------------------    
    // Path Property \\
    // ................................................
    public String getPath() 
    {
        if (this.parent == null)
          return "";
        else
          return parent.getPath() + "/" + Name;
    }
    //--------------------------------------------------------------------------    
    
    
    
    //--------------------------------------------------------------------------
    // Root Property
    // ................................................
    public CTreeNode getRoot() 
    {
        return this.root;
    }
    //--------------------------------------------------------------------------

    
    //--------------------------------------------------------------------------
    // Child Indexed Property 
    // ................................................
    public CTreeNode getChild(int p_nIndex) 
    {
        return this.children.getItem(p_nIndex);
    }
    //--------------------------------------------------------------------------
    // ChildCount Property
    // ................................................
    public int getChildCount()
    {
        return this.children.itemCount;
    }
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------    
    // Parent Property \\
    // ................................................
    public CTreeNode getParent() 
    {
        return parent;
    }
    // ................................................
    public void setParent(CTreeNode p_oParent) 
    {
        if (this.parent != null)
        {   // Removes this node object from the children of its current parent
            this.parent.children.Remove(this);
            this.root = null;
        }
        
        // Parent node of this node object is set/replaced
        this.parent = p_oParent;

        if (this.parent != null)
        {    // Adds this node object in the children of the new parent
            this.parent.children.Append(this);
            this.root = this.parent.root;
        }
    }
    //--------------------------------------------------------------------------
    
    
    
    
    
    //--------------------------------------------------------------------------    
    // Creates a TreeNode that is not added to a tree
    public CTreeNode()
    {
    }
    //--------------------------------------------------------------------------    
    // To set up new branch for text file base
    public CTreeNode(String p_sName, Object p_lSize, CTreeNode p_oParent)
    {
        this.Name = p_sName;
        this.Value = p_lSize;
        this.setParent(p_oParent);
    }
    //--------------------------------------------------------------------------   
    // Creates a root/non-root TreeNode that is not added to a tree
    public CTreeNode(boolean p_bIsRoot)
    {
        if (p_bIsRoot)
        {
            this.root = this;
            this.Name = "/";
        }
    }
    //-------------------------------------------------------------------------- 
    // Creates a root/non-root TreeNode that is not added to a tree, passing its name by parameter
    public CTreeNode(boolean p_bIsRoot, String p_sName)
    {
        if (!p_bIsRoot)
        {
            this.Name = p_sName;
        }
        else{
            
            this.root = this;
            this.Name = "/";
        }
    }
    //--------------------------------------------------------------------------   
    // Creates a TreeNode and adds it under a given parent node.
    public CTreeNode(CTreeNode p_oParent) 
    {
        this.setParent(p_oParent);
    }    
    //--------------------------------------------------------------------------        
    public boolean IsRoot()
    {
        return (root == this);
    }
    //--------------------------------------------------------------------------        
    public boolean IsLeaf()
    {
        return (this.children.itemCount == 0);
    }
    //--------------------------------------------------------------------------            
    public int AddChildNode(CTreeNode p_oChildNode)
    {
        // This node object becomes the parent of p_oChildNode
        p_oChildNode.setParent(this);
        return (this.children.itemCount - 1);
    }
    //--------------------------------------------------------------------------            
    public CTreeNode AddChild(String p_sName)
    {
        CTreeNode oNewChildNode = new CTreeNode();
        oNewChildNode.Name = p_sName;
        oNewChildNode.setParent(this);
        return oNewChildNode;
    }
    //--------------------------------------------------------------------------            
    public void RemoveChildNode(CTreeNode p_oChildNode)
    {
        // Setting null will active the code in the setter that 
        // removes p_oChildNode from the children of this node object.
        p_oChildNode.setParent(null);
    }         
    //--------------------------------------------------------------------------            
    public void RemoveChild(String p_sName)
    {   // Searching for a node by its name, goign through all children.
        for(int i=0; i < this.children.itemCount; i++)
        {
            CTreeNode oNode = this.children.getItem(i);
            if (oNode.Name.equals(p_sName))
            {   // We disconnect this node from its parent that leads to removal.
                oNode.setParent(null);
                break;
            }
        }
    }    
    //--------------------------------------------------------------------------
    // Postorder DFS traversal that deletes the node and all the nodes beneath it
    public void Delete()
    {
        // Depth First: We recurse on each one of the children. This will go to 
        // up to the maximum depth before moving to the next child.
        for(int i = 0; i < this.children.itemCount; i++)
        {
            CTreeNode oNode = this.children.getItem(i);
            oNode.Delete();
        }
        
        // Preorder: Removing this node from its parent
        this.parent.RemoveChildNode(this);
        System.out.println("Removed node " + this.Name);
    }
    //--------------------------------------------------------------------------            
    @Override
    public String toString()
    {
        if (this.IsRoot())
            return "/";
        else
            return this.getPath();
    }    
    //--------------------------------------------------------------------------
    
}
