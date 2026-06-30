package forms;

/**
 *
 * @author georgiosaslanidis
 */
public class CFormMain extends javax.swing.JFrame {

    protected CFormQueryRouter oRouter_Main;
    protected CFormQuery oQuery;
    protected CFormQueryRouter oQueryRouter;
    protected CFormTopics oTopics;
    protected CFormTextFileBase oTextFileBase;
    
    public CFormMain() {
        initComponents();
        
        this.oRouter_Main = null;
    }

    protected void UpdateLinks(){
     
    if(this.oQuery != null)
        this.oQuery.oRouter_Query = null;
    
    if(this.oTopics != null)
        this.oTopics.oRouter_Topics = null;
    
    if(this.oTextFileBase != null)
        this.oTextFileBase.oRouter_TextFile = null;
    
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnQueryRouter = new javax.swing.JButton();
        btnTopics = new javax.swing.JButton();
        btnTextFileBase = new javax.swing.JButton();
        btnQuery1 = new javax.swing.JButton();
        btnCFormPythonParser = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnQueryRouter.setText("Query Router");
        btnQueryRouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQueryRouterActionPerformed(evt);
            }
        });

        btnTopics.setText("Topics");
        btnTopics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopicsActionPerformed(evt);
            }
        });

        btnTextFileBase.setText("Text File Base");
        btnTextFileBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTextFileBaseActionPerformed(evt);
            }
        });

        btnQuery1.setText("Query");
        btnQuery1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuery1ActionPerformed(evt);
            }
        });

        btnCFormPythonParser.setText("Python Parser");
        btnCFormPythonParser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCFormPythonParserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnQuery1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                    .addComponent(btnQueryRouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCFormPythonParser, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTextFileBase, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(btnTopics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTopics, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTextFileBase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnQueryRouter, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnQuery1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCFormPythonParser, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQueryRouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQueryRouterActionPerformed

        this.oQueryRouter = new CFormQueryRouter(this, true);
       this.oRouter_Main = oQueryRouter;
       this.oQueryRouter.setVisible(true);
    }//GEN-LAST:event_btnQueryRouterActionPerformed

    private void btnTopicsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopicsActionPerformed
        
        this.oTopics = new CFormTopics(this, oRouter_Main, "/Users/georgiosaslanidis/Desktop/ACT/Code/20220440/SemesterProject/Topics.json");
        this.oTopics.setVisible(true);
    }//GEN-LAST:event_btnTopicsActionPerformed

    private void btnTextFileBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTextFileBaseActionPerformed
        
        this.oTextFileBase = new CFormTextFileBase(this, oRouter_Main);
        this.oTextFileBase.setVisible(true);
    }//GEN-LAST:event_btnTextFileBaseActionPerformed

    private void btnQuery1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuery1ActionPerformed
        
        this.oQuery = new CFormQuery(this, oRouter_Main);
        this.oQuery.setVisible(true);
    }//GEN-LAST:event_btnQuery1ActionPerformed

    private void btnCFormPythonParserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCFormPythonParserActionPerformed
        
        CFormPythonParser oParser = new CFormPythonParser();
        oParser.setVisible(true);
    }//GEN-LAST:event_btnCFormPythonParserActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCFormPythonParser;
    private javax.swing.JButton btnQuery1;
    private javax.swing.JButton btnQueryRouter;
    private javax.swing.JButton btnTextFileBase;
    private javax.swing.JButton btnTopics;
    // End of variables declaration//GEN-END:variables
}
