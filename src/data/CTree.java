package data;

import java.util.Arrays;
import javax.swing.DefaultListModel;

public class CTree
{
    public CTreeNode Root;
    
    private CTreeNodeList nodeList; // Needed for traversal operations

    //--------------------------------------------------------------------------
    public CTree(CTreeNode p_oRoot)
    {
        this.Root = p_oRoot;
    }
    
    //--------------------------------------------------------------------------
   //UPDATING THE LISTS RESPONSIBLE FOR DISPLAYING TOPICS IN THE UI
    
    //TO DO WHEN CLICKING "ONE-LETTER" BUTTONS OR LOADING ALL POSSIBLE SECOND LEVEL NODES   
    private void collectSecondAndThirdLevelNodes(CTreeNode p_oNode, DefaultListModel<String> p_oSecondLevelNodes, DefaultListModel<String>  p_oThirdLevelNodes) {
                
        for (int i = 0; i < p_oNode.getChildCount(); i++) {
            
            CTreeNode child = p_oNode.getChild(i); //A -> AB

            if (child.getChildCount() > 0) {    //AB.childCount>0
                    
                System.out.println("ADDING TO 2: " + child.Name);
                p_oSecondLevelNodes.addElement(child.Name);

                for (int j = 0; j < child.getChildCount(); j++) { //AB -> whole topic

                    System.out.println("ADDING TO 3: " + child.getChild(j).Name);
                    p_oThirdLevelNodes.addElement(child.getChild(j).Name);
                    
                    collectSecondAndThirdLevelNodes(child.getChild(j), p_oSecondLevelNodes, p_oThirdLevelNodes);
                }
            }
        }
    }
    
    //.................................................................................
     //TO USE ONLY WHEN CLICKING ON lstBranchesL2 LIST TO UPDATE THE lstBranchesL3 list
    
    private void collectThirdLevelNodes(CTreeNode p_oNode, String p_sStartingLetters, DefaultListModel<String> p_oSecondLevelNodes, DefaultListModel<String>  p_oThirdLevelNodes) {
                
        for (int i = 0; i < p_oNode.getChildCount(); i++) {
            
            CTreeNode child = p_oNode.getChild(i); //A -> AB

            if (child.getChildCount() > 0 && child.Name.startsWith(p_sStartingLetters)) {    //AB.childCount>0

                for (int j = 0; j < child.getChildCount(); j++) { //AB (point to)-> whole topic(s)

                    System.out.println("ADDING TO 3: " + child.getChild(j).Name);
                    p_oThirdLevelNodes.addElement(child.getChild(j).Name);
                    
                    collectSecondAndThirdLevelNodes(child.getChild(j), p_oSecondLevelNodes, p_oThirdLevelNodes);
                }
            }
        }
    }

    //.........................................................................................................
     //METHOD TO CALL IN THE TOPICS FORM TO SHOWCASE RESULTS ON TOPICS
     //Follow instructions for safe implementation based on your purpose
    
    public void getSecondAndThridLevelNodes(boolean withLetter, CTreeNode p_nRoot, String p_sStartingLetter, DefaultListModel<String> p_oSecondLevelNodes, DefaultListModel<String> p_oThirdLevelNodes){

        p_oThirdLevelNodes.clear();
       
        for (int i = 0; i < p_nRoot.getChildCount(); i++) {
            
            CTreeNode firstLevelNode = p_nRoot.getChild(i);
            
            //withLetter = True
            //When pressing button with a particular letter (Show based on 1st Node Selection)
            if(withLetter && p_sStartingLetter.length() == 1) {
                
                if (firstLevelNode.Name.equals(p_sStartingLetter)) {

                    collectSecondAndThirdLevelNodes(firstLevelNode, p_oSecondLevelNodes, p_oThirdLevelNodes);
                }
            }
            //withLetter = True
            //When choosing item from lstBranchesL2 (Show based on 2nd Level Node Selection)
            else if(withLetter && p_sStartingLetter.length() > 1) {
                
                System.out.println(firstLevelNode.Name + "=" + p_sStartingLetter.charAt(0));
                if (firstLevelNode.Name.equals(Character.toString(p_sStartingLetter.charAt(0)))){
                
                    collectThirdLevelNodes(firstLevelNode, p_sStartingLetter, p_oSecondLevelNodes, p_oThirdLevelNodes);
                }
            }
            //withLetter = false, p_sStartingLetter can be equal to anything as we will not use it
            //preferably use p_sStartingLetter = "-" for clarity in your code
            //Used only when we want load all possible 2nd level and 3rd level nodes
            //doesn't check for a particular FirstLetter or FirstTwoLetters
            //so the code just moves on to save all 2nd level and 3rd level nodes
            else{
     
                collectSecondAndThirdLevelNodes(firstLevelNode, p_oSecondLevelNodes, p_oThirdLevelNodes);
            }
        }
    }
    
    //--------------------------------------------------------------------------
    // Preorder DFS traversal that adds the node to the list of nodes
    protected void recursePreorderAppendToNodeList(CTreeNode p_oCurrentNode, int p_nDepth)
    {   
        // Preorder = We process the current node before its children
        this.nodeList.Append(p_oCurrentNode);
        debugNodeAccess(this.nodeList.itemCount, p_oCurrentNode, p_nDepth);

        // Depth First: We recurse on each one of the children. This will go to 
        // up to the maximum depth before moving to the next child.
        for(int i = 0; i < p_oCurrentNode.children.getItemCount(); i++)
        {
            CTreeNode oChildNode = p_oCurrentNode.children.getItem(i);
            recursePreorderAppendToNodeList(oChildNode, p_nDepth + 1);
        }        
    }
    //--------------------------------------------------------------------------
    // Postorder DFS traversal that adds the node to the list of nodes
    protected void recursePostorderAppendToNodeList(CTreeNode p_oCurrentNode, int p_nDepth)
    {   
        // Depth First: We recurse on each one of the children. This will go to 
        // up to the maximum depth before moving to the next child.
        for(int i = 0; i < p_oCurrentNode.children.getItemCount(); i++)
        {
            CTreeNode oChildNode = p_oCurrentNode.children.getItem(i);
            recursePostorderAppendToNodeList(oChildNode, p_nDepth + 1);
        }

        // Postorder = We process the current node after returning form children recursion
        this.nodeList.Append(p_oCurrentNode);
        debugNodeAccess(this.nodeList.itemCount, p_oCurrentNode, p_nDepth);
    }    
    //--------------------------------------------------------------------------
    public CTreeNodeList TraverseDepthFirst(boolean p_bIsPreorder)
    {
        System.out.println("------ Depth-First Traversal ------");
        this.nodeList = new CTreeNodeList(256);
        if (p_bIsPreorder)
            recursePreorderAppendToNodeList(this.Root, 0);
        else
            recursePostorderAppendToNodeList(this.Root, 0);
        
        return this.nodeList;    
    }
    //--------------------------------------------------------------------------
    public CTreeNodeList TraverseBreadthFirst()
    {
        System.out.println("------ Breadth-First Traversal ------");
        CTreeNodeList oNodeList = new CTreeNodeList(256);
        CNodeQueue oQueue = new CNodeQueue(128);
        oQueue.Enqueue(this.Root);
        
        CTreeNode oNode = oQueue.Dequeue();
        while(oNode != null)
        {
            oNodeList.Append(oNode);
            this.debugNodeAccess(oNodeList.itemCount, oNode, -1);
            
            for(int i = 0; i < oNode.children.itemCount; i++)
                oQueue.Enqueue(oNode.children.getItem(i));
            
            oNode = oQueue.Dequeue();
        }
        
        return oNodeList;
    }
    //--------------------------------------------------------------------------
    public CTreeNode recurseFollowPath(CStringQueue p_oPathNodeNames, CTreeNode p_oCurrentNode, int p_nDepth)
    {
        CTreeNode oResult = null;
        // We will try to visit the next node in the queue
        String sNodeName = p_oPathNodeNames.Dequeue();
        
        // Search the children with the name of the next node
        for(int i=0; i<p_oCurrentNode.children.itemCount; i++)
        {   CTreeNode oNode = p_oCurrentNode.children.getItem(i);
            System.out.print("D" + String.format("%d", p_nDepth) + this.getIdentation(p_nDepth) 
                + " (" + oNode.Name + " == " + sNodeName + ")");
                        
            if (oNode.Name.equals(sNodeName) )
            {
                System.out.println(" -> true");
                // If this is the last node in the queue, then it is our destination.
                // At this point we do not recurse any more, and we return this node as the result.
                if (p_oPathNodeNames.IsEmpty())
                    oResult = oNode;
                else
                    // There are still nodes in the path, recurse on the current node.
                    oResult = recurseFollowPath(p_oPathNodeNames, oNode, p_nDepth + 1);
                break;
            }
            else
                System.out.println("");
        };
        
        return oResult;
    }
    //--------------------------------------------------------------------------
    public CTreeNode Follow(String p_sPath)
    {
        System.out.println("------ Following Path ------");
        // We split the path to an array of node names that is separated with '/'
        String[] sNodeNames = p_sPath.split("/");
        
        // Visiting a list of locations is a FIFO scheme. Hence we need a queue
        // where we enqueue node names
        CStringQueue oPathNodeNames = new CStringQueue(32);
        for(String sNodeName: sNodeNames)
            oPathNodeNames.Enqueue(sNodeName);
        
        // In an empty tree the result will be the root node
        CTreeNode oResult = this.Root;
        if (!oPathNodeNames.IsEmpty())
        {
            oPathNodeNames.Dequeue(); // Remove the nodename "" that stands for the root node
            oResult = recurseFollowPath(oPathNodeNames, this.Root, 1); // Start recursion from the root
        }
        return oResult;
    }    
    //--------------------------------------------------------------------------
    private void debugNodeAccess(int p_nOrder, CTreeNode p_oNode, int p_nDepth)
    {
        if (p_nDepth >= 0)
            System.out.println(   String.format(" %3d", p_nOrder)
                                + " D" + String.format("%d", p_nDepth) + " " + this.getIdentation(p_nDepth) 
                                + p_oNode.Name
                               );   
        else
            System.out.println(   String.format(" %3d", p_nOrder) + " "
                                + p_oNode.toString()
                               );               
    }    
    //--------------------------------------------------------------------------    
    // Returns an identation with space characters according to a given depth
    protected String getIdentation(int p_nDepth)
    {
        String sIdentation = "";
        if ((p_nDepth -1) >=0)
        {
            char[] sCharArray = new char[(p_nDepth-1)*4];
            Arrays.fill(sCharArray, ' ');
            sIdentation = new String(sCharArray);  
        }
        
        return sIdentation;       
    }    
    //--------------------------------------------------------------------------
    // Preorder DFS traversal that adds the node representation to a string
    protected String recurseNodeDescription(CTreeNode p_oCurrentNode, int p_nDepth)
    {
        String sResult = "";
        if (!p_oCurrentNode.IsRoot())
            sResult = getIdentation(p_nDepth) + "|__ ";
        
        // Preorder: We get the name before any recursion on the list of children 
        sResult += p_oCurrentNode.Name;
         
        // Depth First: We recurse on each one of the children. This will go to 
        // up to the maximum depth before moving to the next child.
        for(int i = 0; i < p_oCurrentNode.children.getItemCount(); i++)
        {
            CTreeNode oChildNode = p_oCurrentNode.children.getItem(i);
            sResult += "\r\n" + recurseNodeDescription(oChildNode, p_nDepth + 1);
        } 
        
        return sResult;
    }
    //--------------------------------------------------------------------------            
    @Override
    public String toString()
    {
        // Depth First Traversal: Starts recursion from the root
        return recurseNodeDescription(this.Root, 0);        
    }
    //--------------------------------------------------------------------------
}
