package forms;

import data.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class CFormTextFileBase extends javax.swing.JFrame {
 
    private CFormMain oMain;
    protected CFormQueryRouter oRouter_TextFile;
    //Uncomment to use with dictionary
    //CDictionary<String,Long> LeafDict;
    private CLeafArray LeafArray;

        private CTree FileTree;
    
    public CFormTextFileBase(CFormMain p_oMain, CFormQueryRouter p_oRouter) 
    {
        initComponents();
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        this.oMain = p_oMain;
        this.oRouter_TextFile = p_oRouter;
        
        //Uncomment to use with dictionary
        //LeafDict = new CDictionary<>(new CComparisonCases(GroupByType.COMPARABLE_DICT));
        
        this.LeafArray = new CLeafArray(new CComparison());
    }
    
//------------------------------------------------------------------------------ 
    //TO USE CAN CLICKING FILE TO OPEN LOCAL FILES
    
    private void OpenPath(){
        
        JFileChooser oChooser = new JFileChooser();
        oChooser.setCurrentDirectory(new File("C:\\Users"));  // Start from a specific folder
        oChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only choose folders
        oChooser.setDialogTitle("Please select a path");    // Your custom title
        
        // If  the user has made a selection 
        if (oChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)  
        {
            File oSelectedFolder = oChooser.getSelectedFile();
            this.txtFolder.setText(oSelectedFolder.getPath());
        }
    }
    
    //--------------------------------------------------------------------------      
    
    private void walkPath()
    {
        this.LeafArray.Clear();
        File oStartFolder = new File(this.txtFolder.getText());
        
        this.txtTreeView.setText(""); // Clear the display control
        InitiateTree(oStartFolder, 1, this.txtFolder.getText());        
    }
    
        
    //--------------------------------------------------------------------------
    
    private void InitiateTree(File oFile, int p_nDepth, String p_sPath){
        
        CTreeNode oRoot = new CTreeNode(true, p_sPath);
        FileTree = new CTree(oRoot);
        
        this.recurseListFiles(FileTree, oRoot, oFile, p_nDepth );
    }
    
    //--------------------------------------------------------------------------
     // This implements a pre-order Depth-First Traversal of the file system tree
    // and assigns each file to a branch of the FileTree
   // while at the same time if a file is not Directory, thus is Leaf,
  // appends it to an array containing all the files are leafs and .txt type
    
    private void recurseListFiles(CTree p_oFileTree, CTreeNode p_oParentNode, File oFile, int p_nDepth)
    {
        int nDepth = p_nDepth;
        
        File[] oFileEntries = oFile.listFiles();
        if (oFileEntries != null)
            for (File oFileEntry: oFileEntries) 
            {
                
                String sNodeName = oFileEntry.getName();
                Boolean bIsDirectory = oFileEntry.isDirectory();
                Long lSize = oFileEntry.length();

                CTreeNode oBranch = new CTreeNode(sNodeName, lSize, p_oParentNode);
                
                if (bIsDirectory){
                    this.recurseListFiles(p_oFileTree, oBranch, oFileEntry, nDepth++);
                }
                else{
                    
                    if(oBranch.Name.toLowerCase().endsWith(".txt")){
                        
                        //Uncomment to use with dictionary
                        //this.LoadLeafToDict(this.LeafDict, oBranch.Name, oBranch.Value);
                        
                        this.LeafArray.AppendTxtFile(oFileEntry);
                    }
                    
                        
                    this.recurseListFiles(p_oFileTree, FileTree.Root, oFileEntry, p_nDepth+1);
                }
            }
    }
//------------------------------------------------------------------------------ 
    //Visualizes the tree to the text area
    
    private void VisualizeTree(CTree p_oTree){
        
        this.txtTreeView.setText(p_oTree.toString());
    }
    
//------------------------------------------------------------------------------     
    //Sorts the array containg the files using merge sort
    
    private void SortLeafArray(CLeafArray p_oLeafArray){
        
        p_oLeafArray.MergeSort(p_oLeafArray, 0, p_oLeafArray.getItemCount() - 1);
    }
    
    //--------------------------------------------------------------------------
     //Visualize the list that contains the txt files (leafs of the FileTree)
    
    private void VisualizeTxtList(CLeafArray p_oLeafArray){
        
        this.txtLeafsView.setText(p_oLeafArray.toString());
    }
//------------------------------------------------------------------------------
  //To use if we need to implement with dictionary
  /*  
    private void LoadLeafToDict(CDictionary<String,Long> p_oLeafDict, String p_sName, Object p_lSize){
        
        p_oLeafDict.AddEntry(new CKeyValueEntry(p_sName, p_lSize));
    }  
    
    private void VisualizeLeafDict(CDictionary p_oLeafDict){
        
        String sResult = "{\r\n    ";
        for(int i=0; i < p_oLeafDict.getItemCount(); i++)
        {
            if(i>0)
            {
                sResult += ",\r\n    ";
            }
            CKeyValueEntry oEntry = p_oLeafDict.getEntry(i);
            
            sResult += "\"" + oEntry.Key.toString() + "\" :" + "\"" + oEntry.Value.toString() + " bytes\"";
        }
        sResult += "\r\n}";
        
        txtLeafsView.setText(sResult);
    }
    */
//------------------------------------------------------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFolderView = new javax.swing.JLabel();
        btnBuild = new javax.swing.JButton();
        lblFileInformationList = new javax.swing.JLabel();
        txtFolder = new javax.swing.JTextField();
        lblFolder = new javax.swing.JLabel();
        btnFolderDialog = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTreeView = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLeafsView = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblFolderView.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFolderView.setText("File System Tree View");

        btnBuild.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/tree-elements.png"))); // NOI18N
        btnBuild.setText("Build");
        btnBuild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuildActionPerformed(evt);
            }
        });

        lblFileInformationList.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFileInformationList.setText("File Information List View");

        txtFolder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFolderKeyPressed(evt);
            }
        });

        lblFolder.setText("Start Folder:");

        btnFolderDialog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open-folder.png"))); // NOI18N
        btnFolderDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderDialogActionPerformed(evt);
            }
        });

        txtTreeView.setColumns(20);
        txtTreeView.setRows(5);
        jScrollPane2.setViewportView(txtTreeView);

        txtLeafsView.setColumns(20);
        txtLeafsView.setRows(5);
        jScrollPane3.setViewportView(txtLeafsView);

        jButton1.setText("Send Message");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFolderDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnBuild, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(lblFolderView))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(191, 191, 191)
                                .addComponent(lblFileInformationList))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(153, 153, 153)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFolder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFolderDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFolder)
                    .addComponent(btnBuild, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFileInformationList)
                    .addComponent(lblFolderView, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFolderDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderDialogActionPerformed

        this.OpenPath();
    }//GEN-LAST:event_btnFolderDialogActionPerformed

    private void txtFolderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolderKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            this.walkPath();
            this.VisualizeTree(FileTree);
            
            //Uncomment to use with dictionary
            //VisualizeLeafDict(LeafDict);
            
            this.SortLeafArray(this.LeafArray);
            this.VisualizeTxtList(LeafArray);
        }
    }//GEN-LAST:event_txtFolderKeyPressed

    private void btnBuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuildActionPerformed
        
        this.walkPath();
        this.VisualizeTree(FileTree);
        
        //Uncomment to use with dictionary
        //VisualizeLeafDict(LeafDict);

        this.SortLeafArray(this.LeafArray);
        this.VisualizeTxtList(LeafArray);
    }//GEN-LAST:event_btnBuildActionPerformed

    private void SendMessage(){
        
        CQueue<CQuery> TempQueue = new CQueue<>();
        String sContent = "";
        
        try{
            
            //Read and save the content of each file 
            for(int i=0; i < LeafArray.getItemCount(); i++){

                Scanner myReader = new Scanner(LeafArray.getItem(i));
                
                while (myReader.hasNextLine()) {
                    
                    sContent = myReader.nextLine(); //Save the topic's content 
                }
                

                myReader.close();
                
                String sTitle = LeafArray.getItem(i).getName();             //Get the title of the topic
                TempQueue.Enqueue(new CQuery(sTitle, sContent)); //Enqueue a new query with the
                                                                                 //title and content we just saved
            }
            
            if(this.oRouter_TextFile != null){
                
                if(this.oRouter_TextFile.isDemo){ //if the query opened is demp
                    
                    int confirmed = JOptionPane.showConfirmDialog(null, "Would you like to quit Query Router Demo"
                                                                           + "\nand start a new one with this file?"
                                                                           , "Quit Demo", JOptionPane.YES_NO_OPTION);
                    
                    if (confirmed == JOptionPane.YES_OPTION){ //if yes->quit demo
 
                        //Dispose the already open Query Router Form 
                        this.oRouter_TextFile.dispose();
                        
                        //Open the router form but do not close this form in case the user
                        //wants to send more messages
                        CFormQueryRouter oOpenNewRouter = new CFormQueryRouter(oMain, false);
                        oMain.oRouter_Main = oOpenNewRouter;
                        
                        //Point the Query Router form
                        //link to the new form
                        this.oRouter_TextFile = oOpenNewRouter;
                        
                        for(int i=0; i < TempQueue.getItemCount(); i++)
                           oOpenNewRouter.QueryQueue.Enqueue(TempQueue.Dequeue()); //Enqueue the new Query
                        
                        oOpenNewRouter.setVisible(true); //->open the new real query form
                    }
                
                }
                else{
                    
                    //Just adds more items in the queue of the real version of the CFormQueryRouter
                    for(int i=0; i < TempQueue.getItemCount(); i++)
                        this.oRouter_TextFile.QueryQueue.Enqueue(TempQueue.Dequeue()); //Enqueue the new Query
                    
                    this.oRouter_TextFile.DisplayQueue();
                    this.oRouter_TextFile.RefreshBacklogs();
                }
            }
            else{
                
            CFormQueryRouter oOpenNewRouter = new CFormQueryRouter(oMain, false);
            oMain.oRouter_Main = oOpenNewRouter;
            
            //Point the Query Router form
            //link to the new form
            this.oRouter_TextFile = oOpenNewRouter;
            
            for(int i=0; i < TempQueue.getItemCount(); i++)
                oOpenNewRouter.QueryQueue.Enqueue(TempQueue.Dequeue()); //Enqueue the new Query
            
            oOpenNewRouter.setVisible(true); //->open the new real query form
            }
          }
        
        catch (FileNotFoundException e) {
            
            JOptionPane.showMessageDialog(null, "Something went wrong!");
          }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        this.SendMessage();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuild;
    private javax.swing.JButton btnFolderDialog;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblFileInformationList;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblFolderView;
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextArea txtLeafsView;
    private javax.swing.JTextArea txtTreeView;
    // End of variables declaration//GEN-END:variables
}
