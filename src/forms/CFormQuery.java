package forms;

import data.*;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CFormQuery extends javax.swing.JFrame {
      
    private CFormMain oMain;
    protected CFormQueryRouter oRouter_Query;
    
    private COrderedArray<CToken> OrderedTokensOcc;
    private COrderedArray<CToken> OrderedTokensName;
    private String[] TokenisedInput;

    private CSort<CToken> oSort;
    private CStack<String> UndoStack;
    private CStack<String> RedoStack;

    public CFormQuery(CFormMain p_oMain, CFormQueryRouter p_oRouter) 
    {
        initComponents();
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        oMain = p_oMain;
        oRouter_Query = p_oRouter;
        
        OrderedTokensOcc = new COrderedArray<>(8, new CComparisonCases(GroupByType.OCCURANCES));
        oSort = new CSort<>();
        UndoStack = new CStack<>();
        RedoStack = new CStack<>();

    }
    
    //Create tokens of input in an unordered array
    //(we use this for displaying input with IDs as it was typed by user)
    public void TokeniseInput() { 

        if(txtEditor.getText() != null)
            TokenisedInput = txtEditor.getText().trim().split("[^\\w\\d]+");
                
    }
    
    //Create alphabetical ordered array to group words/numbers of input
    public COrderedArray<CToken> OrderedTokensAlph() {
        
        COrderedArray<CToken> TokensForGroupping = new COrderedArray<>(TokenisedInput.length, new CComparisonCases(GroupByType.ALPHABETICAL_TOKENS));

        for(String Token: TokenisedInput) {
            
            TokensForGroupping.Add(new CToken(Token));  //Adding the tokens from input into the array using Add() method
            
        }
        System.out.println("The tokens ordered Alphabetically: \n" + TokensForGroupping.toString());
        
        return TokensForGroupping; 
    }
    
    //Method where the alph.ordered array is inserted as parameter, and using
    //groupping based method, we create the final array ordered by occurances 
    public void GroupTokens(COrderedArray<CToken> oGroupTokens) { //Method where the alph.ordered array is inserted as parameter

        String sCurrentItem = null;
        int nCount = 0;
        String sCurrentGroup = null;
        boolean bEndOf = false;
        boolean bIsFinishingGroup, bIsCreatingGroup;
        int nIndex = 0;
        
        while(!bEndOf)
        {
            bEndOf = ( nIndex >= oGroupTokens.getItemCount() );
            if (bEndOf)
            {
                bIsFinishingGroup = true;
                bIsCreatingGroup = false;
            }
            else
            {
                sCurrentItem = oGroupTokens.getItem(nIndex).getName();
                // We check the current item against the current group.
                // If we have moved to a different item, then it is the start of a new group
                bIsFinishingGroup = !sCurrentItem.equals(sCurrentGroup);
                bIsCreatingGroup  = bIsFinishingGroup;
            }
            
            
            if (bIsFinishingGroup)
            {
                if (sCurrentGroup != null)
                { // The processing of the previous group has finished. Show it in the form.
                    OrderedTokensOcc.Add(new CToken(sCurrentGroup, nCount));
                }
                
                if (bIsCreatingGroup)
                {   // The processing of a new group is starting.
                    sCurrentGroup = sCurrentItem;
                    // The processing of the current group occurs here
                    System.out.println("New group [" + sCurrentGroup + "]");
                    // The aggregated value is initialized (here count)
                    nCount = 1;
                }
            }
            else
            {   
                System.out.println(" |__ Still at group [" + sCurrentGroup + "]");
                // The processing of the current group occurs here (increasing the count)
                nCount += 1;
            }
            
            nIndex ++;
        }
        
    }
    
    public void BubbleSort(COrderedArray<CToken> p_oArr){ 
        
        oSort.BubbleSort(p_oArr);
    }
    
    
    public void SortForSearchByName() {
        
        OrderedTokensName = new COrderedArray<>(OrderedTokensOcc.getItemCount(), new CComparisonCases(GroupByType.ALPHABETICAL_TOKENS));
        
        for(int i=0; i < OrderedTokensOcc.getItemCount(); i++) {
            
            OrderedTokensName.Add(OrderedTokensOcc.getItem(i));
            
        }
        
        BubbleSort(OrderedTokensName);
        
    }
    
    //Do the following "steps" for the ordered array to be made(ordered by occurance)
    public void CreateTokens() {
            
        if(TokenisedInput == null)
            TokeniseInput();

        if(!CheckIfEmpty()) {
            DisplayInputTokens();
            GroupTokens(OrderedTokensAlph());
            SortForSearchByName();
            DisplayOccSortedTokens();
        }
        
    }
    
    public void Find() { 
        
        int index = 0;
        
        index = OrderedTokensName.FastSearch(new CToken(txtSearchCriterion.getText()));

        if(index < 0) {
            
            txtFoundIndex.setText("-");
            txtTokenID.setText("-");
            txtTokenText.setText("Token not found!");
            txtTokenOccurences.setText("-");         
        }
        else {
            
            txtFoundIndex.setText(String.valueOf(index));
            txtTokenID.setText(String.valueOf(OrderedTokensName.getItem(index).getID()));
            txtTokenText.setText(String.valueOf(OrderedTokensName.getItem(index).getName()));
            txtTokenOccurences.setText(String.valueOf(OrderedTokensName.getItem(index).getOccurances()));
        }
      
    }
    
    //Clear everything to re-take input from user if "Send Message" is pressed 
    //again
    public void Clear() {
        
        txtViewWordCounts.setText("");
        txtViewWords.setText("");
        OrderedTokensOcc.Clear();
        TokenisedInput = null;
        txtViewUndo.setText("");
        txtViewRedo.setText("");
        UndoStack.Clear();
        RedoStack.Clear();
        
    }
    
    //Check if text area has input
    public boolean CheckIfEmpty() {
        
        String sCheck = "";
        
        for(String sTemp: TokenisedInput) {
            
            sCheck += sTemp;
            
        }
        
        return sCheck.isEmpty();
    }

    public void SaveForUndo(){
        
        UndoStack.Push(txtEditor.getText());
        UpdateUndoRedoView();
    }
    
    public void Undo() {
        
        if(!UndoStack.IsEmpty()) { //Stop the action as the undo will take more empty
                                   //values and so will Redo, thus there will be a point
                                   //that we will be Undoing and Redoing with blank ("") values
                                   //which make no sense for a real implementetion of such actions
                                
                RedoStack.Push(txtEditor.getText());    //Save the previous steps/words of undo to redo
                txtEditor.setText(UndoStack.Pop());         //Undo/Pop  
            
            UpdateUndoRedoView();
        }
        
    }
    
    public void Redo() {
        
        if(!RedoStack.IsEmpty()) {
            
                UndoStack.Push(txtEditor.getText());    //Save previous steps/words of redo to undo
                txtEditor.setText(RedoStack.Pop());         //Redo/Pop
 
            UpdateUndoRedoView();
        }
        
    }
    
    public void UpdateUndoRedoView() {

        txtViewUndo.setText(UndoStack.Peek());
        txtViewRedo.setText(RedoStack.Peek());  
    }
    
    //TokenisedInput[] contains String type elements, so copy them into an unordered
    //array to keep the original order of input of each word/number and implement
    //the toString() method of CUnorderedArray class for displaying
    public void DisplayInputTokens() {
        
        if(TokenisedInput == null)
            TokeniseInput();
      
        CUnorderedArray<CToken> oInput = new CUnorderedArray<>(TokenisedInput.length);
        
        for(String Token: TokenisedInput) {
            
            oInput.Append(new CToken(Token));  //Adding the tokens from input into the array using Add() method
            
        }
        
        txtViewWords.setText(ReturnInputTokensString(oInput));
        
    }
    
    public String ReturnInputTokensString(CUnorderedArray<CToken> p_oInput) {
    
    String sResult = "";
        
        for(int i=0; i < p_oInput.getItemCount(); i++) {
            
            sResult += p_oInput.getItem(i).getName() 
                    + "-ID: " + p_oInput.getItem(i).getID(); 
            
            if(i < p_oInput.getItemCount()) {
                
                sResult += "\n-------------\n";
                
            }
           
        }
        
        return sResult;
    
    }
    
    public String ReturnSortedTokensString(COrderedArray<CToken> p_oOrderedTokensOcc) {
        
        String sResult = "";
        
        for(int i=0; i < p_oOrderedTokensOcc.getItemCount(); i++) {
            
            sResult += "Word: " + p_oOrderedTokensOcc.getItem(i).getName()  
                    +" - ID: " + p_oOrderedTokensOcc.getItem(i).getID()
                    + " - Occurances: " + p_oOrderedTokensOcc.getItem(i).getOccurances(); 
            
            if(i < p_oOrderedTokensOcc.getItemCount()) {
                
                sResult += "\n--------------------------------\n";
                
            }
            
        }
        
        return sResult;
        
    }
    
    public void DisplayOccSortedTokens() {
        
        txtViewWordCounts.setText(ReturnSortedTokensString( OrderedTokensOcc)); 
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtEditor = new javax.swing.JTextArea();
        tabView = new javax.swing.JTabbedPane();
        pnlWordsArray = new javax.swing.JPanel();
        txtViewWordArray = new javax.swing.JScrollPane();
        txtViewWords = new javax.swing.JTextArea();
        pnlTokensArray = new javax.swing.JPanel();
        txtViewTokens = new javax.swing.JScrollPane();
        txtViewWordCounts = new javax.swing.JTextArea();
        lblTokenID = new javax.swing.JLabel();
        txtTokenID = new javax.swing.JTextField();
        lblTokenText = new javax.swing.JLabel();
        txtTokenText = new javax.swing.JTextField();
        lblTokenOccurences = new javax.swing.JLabel();
        txtTokenOccurences = new javax.swing.JTextField();
        lblSearchCriterion = new javax.swing.JLabel();
        txtSearchCriterion = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        lblFoundIndex = new javax.swing.JLabel();
        txtFoundIndex = new javax.swing.JTextField();
        pnlUndoStack = new javax.swing.JPanel();
        txtViewUndoStack = new javax.swing.JScrollPane();
        txtViewUndo = new javax.swing.JTextArea();
        pnlRedoStack = new javax.swing.JPanel();
        txtViewRedoStack = new javax.swing.JScrollPane();
        txtViewRedo = new javax.swing.JTextArea();
        lblDataStructureViews1 = new javax.swing.JLabel();
        btnUndo = new javax.swing.JButton();
        btnRedo = new javax.swing.JButton();
        btnTokenize = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        txtTopicTitle = new javax.swing.JTextField();
        lblTopicTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtEditor.setColumns(20);
        txtEditor.setRows(5);
        txtEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEditorKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtEditor);

        txtViewWords.setColumns(20);
        txtViewWords.setRows(5);
        txtViewWordArray.setViewportView(txtViewWords);

        javax.swing.GroupLayout pnlWordsArrayLayout = new javax.swing.GroupLayout(pnlWordsArray);
        pnlWordsArray.setLayout(pnlWordsArrayLayout);
        pnlWordsArrayLayout.setHorizontalGroup(
            pnlWordsArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWordsArrayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtViewWordArray, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlWordsArrayLayout.setVerticalGroup(
            pnlWordsArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWordsArrayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtViewWordArray, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabView.addTab("Words Array", pnlWordsArray);

        txtViewWordCounts.setColumns(20);
        txtViewWordCounts.setRows(5);
        txtViewTokens.setViewportView(txtViewWordCounts);

        lblTokenID.setText("Token ID");

        lblTokenText.setText("Token Text");

        lblTokenOccurences.setText("Occurence Count");

        lblSearchCriterion.setText("Token Text:");

        btnFind.setText("Search");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        lblFoundIndex.setText("Found at Index:");

        javax.swing.GroupLayout pnlTokensArrayLayout = new javax.swing.GroupLayout(pnlTokensArray);
        pnlTokensArray.setLayout(pnlTokensArrayLayout);
        pnlTokensArrayLayout.setHorizontalGroup(
            pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTokensArrayLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtViewTokens)
                    .addGroup(pnlTokensArrayLayout.createSequentialGroup()
                        .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlTokensArrayLayout.createSequentialGroup()
                                .addComponent(lblTokenID)
                                .addGap(38, 38, 38))
                            .addGroup(pnlTokensArrayLayout.createSequentialGroup()
                                .addComponent(txtTokenID)
                                .addGap(12, 12, 12)))
                        .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTokenText)
                            .addComponent(txtTokenText, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTokenOccurences)
                            .addComponent(txtTokenOccurences, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 238, Short.MAX_VALUE))
                    .addGroup(pnlTokensArrayLayout.createSequentialGroup()
                        .addComponent(lblSearchCriterion, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchCriterion)
                        .addGap(18, 18, 18)
                        .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(lblFoundIndex)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFoundIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlTokensArrayLayout.setVerticalGroup(
            pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTokensArrayLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFoundIndex)
                    .addComponent(lblFoundIndex)
                    .addComponent(btnFind)
                    .addComponent(txtSearchCriterion)
                    .addComponent(lblSearchCriterion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtViewTokens, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTokenID)
                    .addComponent(lblTokenText)
                    .addComponent(lblTokenOccurences))
                .addGap(4, 4, 4)
                .addGroup(pnlTokensArrayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTokenID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTokenText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTokenOccurences, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tabView.addTab("Tokens Array", pnlTokensArray);

        txtViewUndo.setColumns(20);
        txtViewUndo.setRows(5);
        txtViewUndoStack.setViewportView(txtViewUndo);

        javax.swing.GroupLayout pnlUndoStackLayout = new javax.swing.GroupLayout(pnlUndoStack);
        pnlUndoStack.setLayout(pnlUndoStackLayout);
        pnlUndoStackLayout.setHorizontalGroup(
            pnlUndoStackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUndoStackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtViewUndoStack, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlUndoStackLayout.setVerticalGroup(
            pnlUndoStackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUndoStackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtViewUndoStack, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabView.addTab("Undo Stack", pnlUndoStack);

        txtViewRedo.setColumns(20);
        txtViewRedo.setRows(5);
        txtViewRedoStack.setViewportView(txtViewRedo);

        javax.swing.GroupLayout pnlRedoStackLayout = new javax.swing.GroupLayout(pnlRedoStack);
        pnlRedoStack.setLayout(pnlRedoStackLayout);
        pnlRedoStackLayout.setHorizontalGroup(
            pnlRedoStackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRedoStackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtViewRedoStack, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRedoStackLayout.setVerticalGroup(
            pnlRedoStackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRedoStackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtViewRedoStack, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabView.addTab("Redo Stack", pnlRedoStack);

        lblDataStructureViews1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDataStructureViews1.setText("Data Structure Views");

        btnUndo.setText("Undo");
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        btnRedo.setText("Redo");
        btnRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedoActionPerformed(evt);
            }
        });

        btnTokenize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow.png"))); // NOI18N
        btnTokenize.setText("Send Message");
        btnTokenize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTokenizeActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        lblTitle.setText("Chatbot Query");

        lblTopicTitle.setText("Topic Title:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblDataStructureViews1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tabView)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTopicTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTopicTitle))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTokenize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUndo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTopicTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTopicTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTokenize, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUndo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRedo)))
                .addGap(18, 18, 18)
                .addComponent(lblDataStructureViews1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabView, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed

        this.Find();
    }//GEN-LAST:event_btnFindActionPerformed

    public void ReceiveMessage() {
        
        this.Clear();
  
        if(!txtTopicTitle.getText().equals("")){
            
            this.CreateTokens();
            System.out.println("Kanei tokens");
            SaveForUndo();//Have to save when pressing send message as if no space
                                         //or DEL button is pressed when writing the last word of the message
                                         //will be lost and not saved for Undo function
        }
        else{
            
            JOptionPane.showMessageDialog(null, "You have to include a title for you message!");
        }
    }
    private void SendMessage(){
        
        try{
            
            if(!txtTopicTitle.getText().equals("")){
            
                if(oRouter_Query != null){

                    if(oRouter_Query.isDemo){

                        int confirmed = JOptionPane.showConfirmDialog(null, "Would you like to quit Query Router Demo"
                                                                               + "\nand start a new one with this file?"
                                                                               , "Quit Demo", JOptionPane.YES_NO_OPTION);

                        if (confirmed == JOptionPane.YES_OPTION){ //if yes->quit demo

                            //Dispose the already open Query Router Form 
                            oRouter_Query.dispose();

                            //Open the router form but do not close this form in case the user
                            //wants to send more messages
                            CFormQueryRouter oOpenNewRouter = new CFormQueryRouter(oMain, false);
                            oMain.oRouter_Main = oOpenNewRouter;

                            //Point the Query Router form
                            //link to the new form
                            this.oRouter_Query = oOpenNewRouter;

                            oOpenNewRouter.QueryQueue.Enqueue(new CQuery(txtTopicTitle.getText(), txtEditor.getText()));

                            oOpenNewRouter.setVisible(true); //->open the new real query form
                        }     
                    }
                    else{

                            this.oRouter_Query.QueryQueue.Enqueue(new CQuery(txtTopicTitle.getText(), txtEditor.getText())); //Enqueue the extra query

                            this.oRouter_Query.DisplayQueue();
                            this.oRouter_Query.RefreshBacklogs();
                    }
                }
                else{

                    CFormQueryRouter oOpenNewRouter = new CFormQueryRouter(oMain, false);
                    oMain.oRouter_Main = oOpenNewRouter;

                    //Point the Query Router form
                    //link to the new form
                    this.oRouter_Query = oOpenNewRouter;


                    oOpenNewRouter.QueryQueue.Enqueue(new CQuery(txtTopicTitle.getText(), txtEditor.getText())); //Enqueue the new Query

                    oOpenNewRouter.setVisible(true); //->open the new real query form
                }
            }
        }
        catch(HeadlessException e){
            
            JOptionPane.showMessageDialog(null, "Something went wrong!");
        }    
    }
    
    private void btnTokenizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTokenizeActionPerformed

        this.ReceiveMessage();
        this.SendMessage();
    }//GEN-LAST:event_btnTokenizeActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        
        this.Undo();
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        
        this.Redo();
    }//GEN-LAST:event_btnRedoActionPerformed

    private void txtEditorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEditorKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_SPACE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            
            this.SaveForUndo();   
        } 
    }//GEN-LAST:event_txtEditorKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnTokenize;
    private javax.swing.JButton btnUndo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDataStructureViews1;
    private javax.swing.JLabel lblFoundIndex;
    private javax.swing.JLabel lblSearchCriterion;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTokenID;
    private javax.swing.JLabel lblTokenOccurences;
    private javax.swing.JLabel lblTokenText;
    private javax.swing.JLabel lblTopicTitle;
    private javax.swing.JPanel pnlRedoStack;
    private javax.swing.JPanel pnlTokensArray;
    private javax.swing.JPanel pnlUndoStack;
    private javax.swing.JPanel pnlWordsArray;
    private javax.swing.JTabbedPane tabView;
    private javax.swing.JTextArea txtEditor;
    private javax.swing.JTextField txtFoundIndex;
    private javax.swing.JTextField txtSearchCriterion;
    private javax.swing.JTextField txtTokenID;
    private javax.swing.JTextField txtTokenOccurences;
    private javax.swing.JTextField txtTokenText;
    private javax.swing.JTextField txtTopicTitle;
    private javax.swing.JTextArea txtViewRedo;
    private javax.swing.JScrollPane txtViewRedoStack;
    private javax.swing.JScrollPane txtViewTokens;
    private javax.swing.JTextArea txtViewUndo;
    private javax.swing.JScrollPane txtViewUndoStack;
    private javax.swing.JScrollPane txtViewWordArray;
    private javax.swing.JTextArea txtViewWordCounts;
    private javax.swing.JTextArea txtViewWords;
    // End of variables declaration//GEN-END:variables
}
