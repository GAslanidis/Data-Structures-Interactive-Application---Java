package forms;

/**
 *
 * @author georgiosaslanidis
 */
import data.*;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CDialogueCreateCustomJSON extends javax.swing.JDialog {
    
    CFormMain oMain;
    CFormQueryRouter oRouter;
    
    //Used to create file from the start
    //or to make changes to an existing one
    private CDictionary<String, String> Dict;  
    
    //To save the initial "form" of the dictionary 
    //in case user wants to cancel his process
    private CDictionary<String, String> SafeDict; 
    
    CFormTopics oFormTopics;

    public CDialogueCreateCustomJSON(java.awt.Frame parent, boolean modal, CFormMain p_oMain, CFormTopics p_oForm, CFormQueryRouter p_oRouter) {
        super(parent, modal);
        initComponents();
        
        oMain = p_oMain;
        oFormTopics = p_oForm;
        oRouter = p_oRouter;
        
        this.setTitle("Using JSON Files for Dictionaries");
        
        this.Dict = new CDictionary<>(new CComparisonCases(GroupByType.COMPARABLE_DICT), true);
         
        this.txtDictView.setText(Dict.toString());
    }

    // -------------------------------------------------------------------------
    private void searchEntryByUserEnteredKey()
    {
        String sKey           = this.txtKey.getText();
        CKeyValueEntry oEntry = this.Dict.FindEntry(sKey);
        
        if (oEntry == null)
            this.txtValue.setText("***NOT FOUND***");
        else
            this.txtValue.setText((String)oEntry.Value);        
    }
    // -------------------------------------------------------------------------
    private void deleteEntryByUserEnteredKey()
    {
        String sKey = this.txtKey.getText();
        this.Dict.RemoveEntry(sKey);

        // Update the data structure view
        this.txtDictView.setText(Dict.toString());
    }  
    // -------------------------------------------------------------------------
    private void OnlySaveFile(){
        
        JFileChooser oChooser = new JFileChooser();
        File oUserFolder = new File(System.getProperty("user.dir"));
        oChooser.setCurrentDirectory(oUserFolder);  // Start from current folder
        oChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only choose folders
        oChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        oChooser.setDialogTitle("Enter the name for your JSON file");    // Your custom title

        // If  the user has made a selection
        if (oChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            String sFileName = oChooser.getSelectedFile().getAbsolutePath();
            if(!sFileName.endsWith(".json"))
            sFileName += ".json";
            CJSONFileDictStringKeyStringValue oJSONFile = new CJSONFileDictStringKeyStringValue(sFileName);
            oJSONFile.SaveDict(this.Dict);
            
        }
    }
    
    private String SaveAndUseFile(){
        
        JFileChooser oChooser = new JFileChooser();
        File oUserFolder = new File(System.getProperty("user.dir"));
        oChooser.setCurrentDirectory(oUserFolder);  // Start from current folder
        oChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only choose folders
        oChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        oChooser.setDialogTitle("Enter the name for your JSON file");    // Your custom title

        String sFileName = "";
        // If  the user has made a selection
        if (oChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            sFileName = oChooser.getSelectedFile().getAbsolutePath();
            if(!sFileName.endsWith(".json"))
            sFileName += ".json";
            CJSONFileDictStringKeyStringValue oJSONFile = new CJSONFileDictStringKeyStringValue(sFileName);
            oJSONFile.SaveDict(this.Dict);
            
        }
        
        return sFileName;
    }
    
    public void CancelMessage() {
        
        int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit? Any progress will be lost!", "Cancel Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (confirmed == JOptionPane.YES_OPTION) {
                
                this.Dict = this.SafeDict;
                
                this.dispose();
                this.setVisible(false);
            }
    }
    // -------------------------------------------------------------------------
    public void Save() {
        
        int confirmed = JOptionPane.showConfirmDialog(null, "Would you like to exit and use this file?", "Save", JOptionPane.YES_NO_OPTION);
            
            if (confirmed == JOptionPane.YES_OPTION) {
                
                oFormTopics.dispose();
                oFormTopics.setVisible(false);
                
                CFormTopics oForm = new CFormTopics(oMain, oRouter, SaveAndUseFile());
                oForm.setVisible(true);
                
                this.dispose();
                this.setVisible(false);
            }
            else{
                
                this.OnlySaveFile();
                
                this.dispose();
                this.setVisible(false);
            }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtKey = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        btnAddEntry = new javax.swing.JButton();
        btnFindExistingEntry = new javax.swing.JButton();
        btnRemoveEntry = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        lblDictView = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDictView = new javax.swing.JTextArea();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txtKey.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtKey.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeyKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Key");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Value");

        txtValue.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btnAddEntry.setText("Add/Set Entry");
        btnAddEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEntryActionPerformed(evt);
            }
        });

        btnFindExistingEntry.setText("Find Entry");
        btnFindExistingEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindExistingEntryActionPerformed(evt);
            }
        });

        btnRemoveEntry.setText("Remove Entry");
        btnRemoveEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveEntryActionPerformed(evt);
            }
        });

        btnLoad.setText("Load From File");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnSave.setText("Save To File");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        lblDictView.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDictView.setText("Create Custom JSON File");

        txtDictView.setColumns(20);
        txtDictView.setFont(new java.awt.Font("Courier New", 0, 16)); // NOI18N
        txtDictView.setRows(5);
        jScrollPane1.setViewportView(txtDictView);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtKey))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnRemoveEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnFindExistingEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnAddEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(84, 84, 84))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancel))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDictView)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDictView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(txtValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFindExistingEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRemoveEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKeyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeyKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        this.searchEntryByUserEnteredKey();
        else if ((evt.getKeyCode() == KeyEvent.VK_DELETE) && ((evt.getModifiers() & KeyEvent.CTRL_MASK) != 0))
        this.deleteEntryByUserEnteredKey();
    }//GEN-LAST:event_txtKeyKeyPressed

    private void btnAddEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEntryActionPerformed
        String sKey    = this.txtKey.getText();
        String sValue  = this.txtValue.getText();
        this.Dict.setValue(sKey, sValue );

        // Update the data structure view
        this.txtDictView.setText(Dict.toString());
    }//GEN-LAST:event_btnAddEntryActionPerformed

    private void btnFindExistingEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindExistingEntryActionPerformed
        this.searchEntryByUserEnteredKey();
    }//GEN-LAST:event_btnFindExistingEntryActionPerformed

    private void btnRemoveEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveEntryActionPerformed
        this.deleteEntryByUserEnteredKey();
    }//GEN-LAST:event_btnRemoveEntryActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // Example of using JFileChooser for a file open dialog
        JFileChooser oChooser = new JFileChooser();
        File oUserFolder = new File(System.getProperty("user.dir"));
        oChooser.setCurrentDirectory(oUserFolder);  // Start from current folder
        oChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only choose folders
        oChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        oChooser.setDialogTitle("Please select a JSON file");    // Your custom title

        // If  the user has made a selection
        if (oChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            String sFileName = oChooser.getSelectedFile().getAbsolutePath();
            CJSONFileDictStringKeyStringValue oJSONFile = new CJSONFileDictStringKeyStringValue(sFileName);
            oJSONFile.LoadDict(this.Dict);
            
            this.SafeDict = this.Dict;
            
            this.txtDictView.setText(Dict.toString());
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
        this.Save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
        this.CancelMessage();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddEntry;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnFindExistingEntry;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnRemoveEntry;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDictView;
    private javax.swing.JTextArea txtDictView;
    private javax.swing.JTextField txtKey;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}
