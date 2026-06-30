 package forms;

import data.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CFormTopics extends javax.swing.JFrame {

    private CFormMain oMain;
    protected CFormQueryRouter oRouter_Topics;
    
    private CDictionary<String, String> Dict;

    private DefaultListModel<String> oListModelKeysL3 = new DefaultListModel<>();
    private DefaultListModel<String> oListModelKeysL2 = new DefaultListModel<>();
    
    private CTree Tree;
    
    private JFileChooser oChooser;
    private File oUserFolder;
    private String sFileName;

    private Thread oThread;
    private Timer timer;
    
    private String PrevSelectedL3;
    private String PrevSelectedL2;
    
    public CFormTopics(CFormMain p_oMain, CFormQueryRouter p_oRouter, String p_sFileName) 
    {
        initComponents();
        
        this.oMain = p_oMain;
        this.oRouter_Topics = p_oRouter;
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.timer = new Timer();
        
        this.PrevSelectedL3 = "";
        this.PrevSelectedL2 = "";
        
        this.Dict = new CDictionary<>(new CComparisonCases(GroupByType.COMPARABLE_DICT), true);
            
        this.oChooser = new JFileChooser();
        this.oUserFolder = new File(System.getProperty("user.dir"));
        
        this.sFileName = p_sFileName;
        this.OpenFile(sFileName);
        this.LoadTree();    
        this.LoadAllTopics();
        
        this.StartThread();   
    }
 //-----------------------------------------------------------------------------
//TO OPEN JSON FILE AND INITIATE THE TREE AND LISTS
    
    //Opens(reads) JSON file
    private void OpenFile(String p_sFileName) {
        
        this.Dict.Clear();
        
        CJSONFileDictStringKeyStringValue oJSONFile = new CJSONFileDictStringKeyStringValue(p_sFileName);
        oJSONFile.LoadDict(this.Dict);

        //Get all the keys into this DefaultListModel<String>
        //to use it when loading the Tree
        this.Dict.GetKeysInto(oListModelKeysL3); 
        
    }
    //.............................................................................
    //Loads the tree. Use only after OpenFile() to build the tree based on contents
    private void LoadTree(){
        
        CTreeNode oRoot = new CTreeNode(true);
        this.Tree = new CTree(oRoot);
        
        for(int i=0; i < this.oListModelKeysL3.size(); i++){
            
            String sKey = this.oListModelKeysL3.getElementAt(i); //Do this after success with array
            String sFirstLetterOfKeys = sKey.substring(0,1);
            String sFirstTwoLettersOfKeys = sKey.substring(0,2);
            
            CTreeNode oBranch = this.Tree.Follow("/" + sFirstLetterOfKeys);
            if(oBranch == null)
                oBranch = this.Tree.Root.AddChild(sFirstLetterOfKeys);
            
            
            String sPath2ndLevel = "/" + sFirstLetterOfKeys + "/" + sFirstTwoLettersOfKeys;
            CTreeNode oSubBranch = this.Tree.Follow(sPath2ndLevel);
            if(oSubBranch == null)
                oSubBranch = oBranch.AddChild(sFirstTwoLettersOfKeys);
            
            oSubBranch.AddChild(sKey);
        }
    }
    
    //..........................................................................
    //Loads all posible 2nd level and 3rd level nodes when opening a new file
    //from folder
    public void LoadFromFile(){
        
        this.oChooser.setCurrentDirectory(this.oUserFolder);  // Start from current folder
        this.oChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only choose folders
        this.oChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        this.oChooser.setDialogTitle("Please select a JSON file");    // Your custom title

        // If  the user has made a selection
        if (this.oChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            this.sFileName = oChooser.getSelectedFile().getAbsolutePath();
            this.OpenFile(sFileName);
            this.LoadTree();
            this.LoadAllTopics();
        } 
    }
    
    //..........................................................................
    //Loads all posible 2nd level and 3rd level nodes when opening a new file
    //to the Lists and then sets the JLists
    private void LoadAllTopics(){
        
        //Clear the list to keep only the new ones
        this.oListModelKeysL2.clear(); 
            
        this.Tree.getSecondAndThridLevelNodes(false, this.Tree.Root, "-", this.oListModelKeysL2, this.oListModelKeysL3);
        
        //For checking progress
        for(int i=0; i < this.oListModelKeysL2.getSize(); i++)
            System.out.println("LIST2: " + this.oListModelKeysL2.getElementAt(i)); 
        
        //For checking progress
        for(int i=0; i < this.oListModelKeysL3.getSize(); i++)
            System.out.println("LIST3: " + this.oListModelKeysL3.getElementAt(i));
        
        this.lblCountL2Children.setText("Subcategories: " + this.oListModelKeysL2.size());
        this.lblCountLeaves.setText("Topics of sub-category: " + this.oListModelKeysL3.size());
                
        this.lstBranchesL2.setModel(this.oListModelKeysL2);
        this.lstBranchesL3.setModel(this.oListModelKeysL3);
    }
 //-----------------------------------------------------------------------------
//TO SEARCH FOR KEY ENTERED BY USER
    
    private void searchEntryByUserEnteredKey(String p_sKey)
    {
        CKeyValueEntry oEntry = this.Dict.FindEntry(p_sKey);
        
        if (oEntry == null)
            this.txtTopic.setText("***NOT FOUND***");
        else
            this.txtTopic.setText((String)oEntry.Value);        
    }
 //------------------------------------------------------------------------------
//RETURN DICTIONARY ITEM    
    private CKeyValueEntry returnEntryItem(String p_sKey)
    {
        CKeyValueEntry oEntry = this.Dict.FindEntry(p_sKey);
       
        return  oEntry;  
    }
  //----------------------------------------------------------------------------
 //USED WHEN CLICKING BUTTONS TO SHOW THE TWO LETTER CHOICES IN lstBranchL2
//Updates both lstBranchL2 and lstBranchL3
    private void setModelL2(String p_sPath){

        try{
            
            this.oListModelKeysL2.clear();  //Ready the list for an update
            
            this.Tree.getSecondAndThridLevelNodes(true, this.Tree.Root, p_sPath, this.oListModelKeysL2, oListModelKeysL3);
            
            this.lstBranchesL2.setModel(this.oListModelKeysL2);
            this.lstBranchesL3.setModel(this.oListModelKeysL3);
            
            this.lblCountL2Children.setText("Subcategories: " + this.oListModelKeysL2.getSize());
            this.lblCountLeaves.setText("Topics of sub-category: " + this.oListModelKeysL3.getSize());
        } 
        catch(Exception e){
            
            this.lblCountL2Children.setText("Subcategories: 0");
            this.lblCountLeaves.setText("Topics of sub-category: 0");
        }
    }
    
  //----------------------------------------------------------------------------
 //Checks if any selection is made in lstBranchesL3 and prints selected topic 
//and prints its contents in the txtTopics
    
    //......................................................
    //Checks if any selection is made in lstBranchesL3
   //If selection is not empty then search for entered Key
    TimerTask task1 = new TimerTask(){
    @Override
        public void run(){
            try{ 
                
                if(!lstBranchesL3.isSelectionEmpty() && !lstBranchesL3.getSelectedValue().equals(PrevSelectedL3)) { 

                    PrevSelectedL3 = lstBranchesL3.getSelectedValue();
                    String sKeyToFind = lstBranchesL3.getSelectedValue(); //Gets the value of the selected item from the JList
                    searchEntryByUserEnteredKey(sKeyToFind);

                }
            }
            catch(Exception e){
                
                JOptionPane.showMessageDialog(null, "Something went wrong when selecting branch!");
            }
 
        }
    };
    
 //-----------------------------------------------------------------------------
//USED WHEN CLICKING ON lstBranchesL2 TO SHOW THE TOPICS AT lstBranchesL3
    
    //Responsible for updating lstBranchesL3
    private void setModelL3(String p_sPath){

        oListModelKeysL3.clear();
         
        try{
            
            //The function will make changes only to oListModelKeysL3
            //and lstBranchesL3 
            this.Tree.getSecondAndThridLevelNodes(true, this.Tree.Root, p_sPath, oListModelKeysL2, oListModelKeysL3);

            lblCountLeaves.setText("Topics of sub-category: " + oListModelKeysL3.getSize());
        }
        catch(Exception e){
            
            lblCountLeaves.setText("Topics of sub-category: 0");
        }
    }
    
    //..........................................................
    //Checks if something is pressed on the JList lstBranchesL2
   //and calls setModelL3() to update lstBranchesL3 
    TimerTask task2 = new TimerTask(){ 
    @Override
        public void run(){
            try{

                if(!lstBranchesL2.isSelectionEmpty() && !PrevSelectedL2.equals(lstBranchesL2.getSelectedValue())) { 

                    PrevSelectedL2 = lstBranchesL2.getSelectedValue();
                    String FirstTwoLetters = lstBranchesL2.getSelectedValue(); //Save selected value
                    
                    //Update lstBranchesL3 and show topics based on selection
                    setModelL3(FirstTwoLetters); 
                }
            }
            catch(Exception e){
                
                JOptionPane.showMessageDialog(null, "Something went wrong when selecting branch!");
            }
 
        }
    };
    
    //Implements the tasks above every 200mil seconds
    private void StartThread(){
        
        this.oThread = new Thread(){
        
            @Override
            public void run(){

                try{
 
                    timer.scheduleAtFixedRate(task1, 1, 200);
                    timer.scheduleAtFixedRate(task2, 1, 200);
                }
                catch(Exception e){
                    
                    JOptionPane.showMessageDialog(null, "Something went wrong while starting the thread!");
                }

            }
        }; 
        this.oThread.start();
     }
    
 //-----------------------------------------------------------------------------
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitleToFind = new javax.swing.JLabel();
        txtTitleToFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        lblTopic = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTopic = new javax.swing.JTextArea();
        btnL1_G1 = new javax.swing.JButton();
        btnL1_G2 = new javax.swing.JButton();
        btnL1_G3 = new javax.swing.JButton();
        btnL1_G4 = new javax.swing.JButton();
        btnL1_G5 = new javax.swing.JButton();
        btnL1_G6 = new javax.swing.JButton();
        btnL1_G7 = new javax.swing.JButton();
        btnL1_G8 = new javax.swing.JButton();
        btnL1_G9 = new javax.swing.JButton();
        btnL1_G10 = new javax.swing.JButton();
        btnL1_G11 = new javax.swing.JButton();
        btnL1_G12 = new javax.swing.JButton();
        btnL1_G13 = new javax.swing.JButton();
        btnL1_G14 = new javax.swing.JButton();
        btnL1_G15 = new javax.swing.JButton();
        btnL1_G16 = new javax.swing.JButton();
        btnL1_G17 = new javax.swing.JButton();
        btnL1_G18 = new javax.swing.JButton();
        btnL1_G19 = new javax.swing.JButton();
        btnL1_G20 = new javax.swing.JButton();
        btnL1_G21 = new javax.swing.JButton();
        btnL1_G22 = new javax.swing.JButton();
        btnL1_G23 = new javax.swing.JButton();
        btnL1_G24 = new javax.swing.JButton();
        btnL1_G25 = new javax.swing.JButton();
        btnL1_G26 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstBranchesL2 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstBranchesL3 = new javax.swing.JList<>();
        lblCountL2Children = new javax.swing.JLabel();
        lblCountLeaves = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        btnLoadFromFiles = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitleToFind.setText("Topic Title:");

        btnFind.setText("Search");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        lblTopic.setText("Topic Contents");

        txtTopic.setColumns(20);
        txtTopic.setRows(5);
        jScrollPane1.setViewportView(txtTopic);

        btnL1_G1.setText("A");
        btnL1_G1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G1ActionPerformed(evt);
            }
        });

        btnL1_G2.setText("B");
        btnL1_G2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G2ActionPerformed(evt);
            }
        });

        btnL1_G3.setText("C");
        btnL1_G3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G3ActionPerformed(evt);
            }
        });

        btnL1_G4.setText("D");
        btnL1_G4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G4ActionPerformed(evt);
            }
        });

        btnL1_G5.setText("E");
        btnL1_G5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G5ActionPerformed(evt);
            }
        });

        btnL1_G6.setText("F");
        btnL1_G6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G6ActionPerformed(evt);
            }
        });

        btnL1_G7.setText("G");
        btnL1_G7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G7ActionPerformed(evt);
            }
        });

        btnL1_G8.setText("H");
        btnL1_G8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G8ActionPerformed(evt);
            }
        });

        btnL1_G9.setText("I");
        btnL1_G9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G9ActionPerformed(evt);
            }
        });

        btnL1_G10.setText("J");
        btnL1_G10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G10ActionPerformed(evt);
            }
        });

        btnL1_G11.setText("K");
        btnL1_G11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G11ActionPerformed(evt);
            }
        });

        btnL1_G12.setText("L");
        btnL1_G12.setAutoscrolls(true);
        btnL1_G12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G12ActionPerformed(evt);
            }
        });

        btnL1_G13.setText("M");
        btnL1_G13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G13ActionPerformed(evt);
            }
        });

        btnL1_G14.setText("N");
        btnL1_G14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G14ActionPerformed(evt);
            }
        });

        btnL1_G15.setText("O");
        btnL1_G15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G15ActionPerformed(evt);
            }
        });

        btnL1_G16.setText("P");
        btnL1_G16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G16ActionPerformed(evt);
            }
        });

        btnL1_G17.setText("Q");
        btnL1_G17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G17ActionPerformed(evt);
            }
        });

        btnL1_G18.setText("R");
        btnL1_G18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G18ActionPerformed(evt);
            }
        });

        btnL1_G19.setText("S");
        btnL1_G19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G19ActionPerformed(evt);
            }
        });

        btnL1_G20.setText("T");
        btnL1_G20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G20ActionPerformed(evt);
            }
        });

        btnL1_G21.setText("U");
        btnL1_G21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G21ActionPerformed(evt);
            }
        });

        btnL1_G22.setText("V");
        btnL1_G22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G22ActionPerformed(evt);
            }
        });

        btnL1_G23.setText("W");
        btnL1_G23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G23ActionPerformed(evt);
            }
        });

        btnL1_G24.setText("X");
        btnL1_G24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G24ActionPerformed(evt);
            }
        });

        btnL1_G25.setText("Y");
        btnL1_G25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G25ActionPerformed(evt);
            }
        });

        btnL1_G26.setText("Z");
        btnL1_G26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnL1_G26ActionPerformed(evt);
            }
        });

        lstBranchesL2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "AA", "AB" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstBranchesL2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstBranchesL2);

        lstBranchesL3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(lstBranchesL3);

        lblCountL2Children.setText("Subcategories: ?");

        lblCountLeaves.setText("Topics of sub-category: ?");

        lblTitle.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        lblTitle.setText("Knowledge Topics Dataset");

        btnLoadFromFiles.setText("Load From Files");
        btnLoadFromFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadFromFilesActionPerformed(evt);
            }
        });

        jButton2.setText("Create Custom JSON");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnReset.setText("Reset Selection");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnSend.setText("Send Message");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTopic)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btnL1_G1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnL1_G4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL1_G13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnL1_G14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnL1_G16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnL1_G17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G24, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G25, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnL1_G26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jScrollPane3))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(lblCountL2Children)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblCountLeaves))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(lblTitleToFind)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTitleToFind, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTitle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLoadFromFiles)
                            .addComponent(jButton2)
                            .addComponent(btnReset)
                            .addComponent(btnSend))
                        .addGap(0, 9, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTitleToFind)
                    .addComponent(btnFind)
                    .addComponent(lblTitleToFind))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTopic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnL1_G1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnL1_G2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnL1_G3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnL1_G4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnL1_G14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnL1_G15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnL1_G16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnL1_G17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G24, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G25, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnL1_G26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLoadFromFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCountLeaves)
                    .addComponent(lblCountL2Children))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        
        searchEntryByUserEnteredKey(this.txtTitleToFind.getText());  
    }//GEN-LAST:event_btnFindActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    
        CDialogueCreateCustomJSON oNewUser = new CDialogueCreateCustomJSON(this, true, oMain, this, oRouter_Topics);
        oNewUser.setVisible(true);  
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnLoadFromFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadFromFilesActionPerformed
        
        this.LoadFromFile();
    }//GEN-LAST:event_btnLoadFromFilesActionPerformed

    private void btnL1_G1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G1ActionPerformed

        this.setModelL2("A");
    }//GEN-LAST:event_btnL1_G1ActionPerformed

    private void btnL1_G2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G2ActionPerformed

        this.setModelL2("B");
    }//GEN-LAST:event_btnL1_G2ActionPerformed

    private void btnL1_G3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G3ActionPerformed
        
        this.setModelL2("C");
    }//GEN-LAST:event_btnL1_G3ActionPerformed

    private void btnL1_G4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G4ActionPerformed
        
        this.setModelL2("D");
    }//GEN-LAST:event_btnL1_G4ActionPerformed

    private void btnL1_G5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G5ActionPerformed
       
        this.setModelL2("E");
    }//GEN-LAST:event_btnL1_G5ActionPerformed

    private void btnL1_G6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G6ActionPerformed
       
        this.setModelL2("F");
    }//GEN-LAST:event_btnL1_G6ActionPerformed

    private void btnL1_G7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G7ActionPerformed
        
        this.setModelL2("G");
    }//GEN-LAST:event_btnL1_G7ActionPerformed

    private void btnL1_G8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G8ActionPerformed
        
        this.setModelL2("H");
    }//GEN-LAST:event_btnL1_G8ActionPerformed

    private void btnL1_G9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G9ActionPerformed
        
        this.setModelL2("I");
    }//GEN-LAST:event_btnL1_G9ActionPerformed

    private void btnL1_G10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G10ActionPerformed
        
        this.setModelL2("J");
    }//GEN-LAST:event_btnL1_G10ActionPerformed

    private void btnL1_G11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G11ActionPerformed
        
        this.setModelL2("K");
    }//GEN-LAST:event_btnL1_G11ActionPerformed

    private void btnL1_G12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G12ActionPerformed
        
        this.setModelL2("L");
    }//GEN-LAST:event_btnL1_G12ActionPerformed

    private void btnL1_G13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G13ActionPerformed
       
        this.setModelL2("M");
    }//GEN-LAST:event_btnL1_G13ActionPerformed

    private void btnL1_G14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G14ActionPerformed
        
        this.setModelL2("N");
    }//GEN-LAST:event_btnL1_G14ActionPerformed

    private void btnL1_G15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G15ActionPerformed
        
        this.setModelL2("O");
    }//GEN-LAST:event_btnL1_G15ActionPerformed

    private void btnL1_G16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G16ActionPerformed
       
        this.setModelL2("P");
    }//GEN-LAST:event_btnL1_G16ActionPerformed

    private void btnL1_G17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G17ActionPerformed
        
        this.setModelL2("Q");
    }//GEN-LAST:event_btnL1_G17ActionPerformed

    private void btnL1_G18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G18ActionPerformed
        
        this.setModelL2("R");
    }//GEN-LAST:event_btnL1_G18ActionPerformed

    private void btnL1_G19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G19ActionPerformed
        
        this.setModelL2("S");
    }//GEN-LAST:event_btnL1_G19ActionPerformed

    private void btnL1_G20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G20ActionPerformed
        
        this.setModelL2("T");
    }//GEN-LAST:event_btnL1_G20ActionPerformed

    private void btnL1_G21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G21ActionPerformed
        
        this.setModelL2("U");
    }//GEN-LAST:event_btnL1_G21ActionPerformed

    private void btnL1_G22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G22ActionPerformed
        
        this.setModelL2("V");
    }//GEN-LAST:event_btnL1_G22ActionPerformed

    private void btnL1_G23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G23ActionPerformed
        
        this.setModelL2("W");
    }//GEN-LAST:event_btnL1_G23ActionPerformed

    private void btnL1_G24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G24ActionPerformed
        
        this.setModelL2("X");
    }//GEN-LAST:event_btnL1_G24ActionPerformed

    private void btnL1_G25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G25ActionPerformed
        
        this.setModelL2("Y");
    }//GEN-LAST:event_btnL1_G25ActionPerformed

    private void btnL1_G26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnL1_G26ActionPerformed
        
        this.setModelL2("Z");
    }//GEN-LAST:event_btnL1_G26ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed

        this.LoadAllTopics();
    }//GEN-LAST:event_btnResetActionPerformed

    private void SendMessage(){
        
        //I could just create a CQuery item and then Enqueue() it
        //in the Query Router's que, but, I was planning on giving
        //the possibility to send multiple texts from one form to the other
        
        //Get the selected Title from L3
        CKeyValueEntry TempEntry = this.returnEntryItem(this.lstBranchesL3.getSelectedValue());

        //if a Query Router form is already opened
        if(this.oRouter_Topics != null){

                if(this.oRouter_Topics.isDemo){ //if the query opened is demo
                    
                    int confirmed = JOptionPane.showConfirmDialog(null, "Would you like to quit Query Router Demo"
                                                                           + "\nand start a new one with this file?"
                                                                           , "Quit Demo", JOptionPane.YES_NO_OPTION);
                    
                    if (confirmed == JOptionPane.YES_OPTION){ //if yes->quit demo
 
                        //Dispose the already open Query Router Form 
                        this.oRouter_Topics.dispose();
                        
                        //Open the router form but do not close this form in case the user
                        //wants to send more messages
                        CFormQueryRouter oOpenNewRouter = new CFormQueryRouter(oMain, false);
                        oMain.oRouter_Main = oOpenNewRouter;
                        
                        //Point the Query Router form
                        //link to the new form
                        this.oRouter_Topics = oOpenNewRouter;
                        
                        oOpenNewRouter.QueryQueue.Enqueue(new CQuery((String)TempEntry.Key, (String)TempEntry.Value)); //Enqueue the new Query
                        
                        oOpenNewRouter.setVisible(true); //->open the new real query form

                    }
                }
                else{
                    
                    //Just adds more items in the queue of the real verion of the CFormQueryRouter
                    this.oRouter_Topics.QueryQueue.Enqueue(new CQuery((String)TempEntry.Key, (String)TempEntry.Value)); //Enqueue the new Query
                    this.oRouter_Topics.DisplayQueue();
                    this.oRouter_Topics.RefreshBacklogs();
                }
        }
        else{
            
            CFormQueryRouter oOpenNewRouter = new CFormQueryRouter(oMain, false);
            oMain.oRouter_Main = oOpenNewRouter;
            //Point the Query Router form
            //link to the new form
            this.oRouter_Topics = oOpenNewRouter;
            oOpenNewRouter.QueryQueue.Enqueue(new CQuery((String)TempEntry.Key, (String)TempEntry.Value)); //Enqueue the new Query
            
            oOpenNewRouter.setVisible(true); //->open the new real query form
        }
    }
    
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        
        this.SendMessage();
    }//GEN-LAST:event_btnSendActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnL1_G1;
    private javax.swing.JButton btnL1_G10;
    private javax.swing.JButton btnL1_G11;
    private javax.swing.JButton btnL1_G12;
    private javax.swing.JButton btnL1_G13;
    private javax.swing.JButton btnL1_G14;
    private javax.swing.JButton btnL1_G15;
    private javax.swing.JButton btnL1_G16;
    private javax.swing.JButton btnL1_G17;
    private javax.swing.JButton btnL1_G18;
    private javax.swing.JButton btnL1_G19;
    private javax.swing.JButton btnL1_G2;
    private javax.swing.JButton btnL1_G20;
    private javax.swing.JButton btnL1_G21;
    private javax.swing.JButton btnL1_G22;
    private javax.swing.JButton btnL1_G23;
    private javax.swing.JButton btnL1_G24;
    private javax.swing.JButton btnL1_G25;
    private javax.swing.JButton btnL1_G26;
    private javax.swing.JButton btnL1_G3;
    private javax.swing.JButton btnL1_G4;
    private javax.swing.JButton btnL1_G5;
    private javax.swing.JButton btnL1_G6;
    private javax.swing.JButton btnL1_G7;
    private javax.swing.JButton btnL1_G8;
    private javax.swing.JButton btnL1_G9;
    private javax.swing.JButton btnLoadFromFiles;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCountL2Children;
    private javax.swing.JLabel lblCountLeaves;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitleToFind;
    private javax.swing.JLabel lblTopic;
    private javax.swing.JList<String> lstBranchesL2;
    private javax.swing.JList<String> lstBranchesL3;
    private javax.swing.JTextField txtTitleToFind;
    private javax.swing.JTextArea txtTopic;
    // End of variables declaration//GEN-END:variables
}
