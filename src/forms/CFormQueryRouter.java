package forms;

import data.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CFormQueryRouter extends javax.swing.JFrame {

    CFormMain oMain;
    
    String[] FName = {"George", "Alex", "Dimitris", "John", "James", "Tyler", "Antony", "Steve", "Stathis", "Terry"};
    String[] LName = {"Kasselakis", "Aslanidis", "Papadopoulos", "Karakatsianopoulos", "Smith", "Cameron", "Karpas", "Nicks", "Kiedis", "Frusciante"};
    Random rand;
    
    Timer timer;
    boolean pauseSim;
    Thread oThread;
    
    CQueue<CQuery> QueryQueue;
    CQueue<CQuery> BotBacklogQ1;
    CQueue<CQuery> BotBacklogQ2;
    CQueue<CQuery> BotBacklogQ3;
    CQueue<CQuery> BotBacklogQ4;
    
    public boolean isDemo;
    
    public CFormQueryRouter(CFormMain p_oMain, boolean p_bisDemo) 
    {
        initComponents();
        
        oMain = p_oMain;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        isDemo = p_bisDemo;
        
        if(isDemo){
            
            lblTitle.setText("Query Router Simulator Demo");
        }
        else
            lblTitle.setText("Query Router Simulator");
        
        pauseSim = false;

        rand = new Random();
        timer = new Timer();
        
        QueryQueue = new CQueue<>();
        BotBacklogQ1 = new CQueue();
        BotBacklogQ2 = new CQueue();
        BotBacklogQ3 = new CQueue();
        BotBacklogQ4 = new CQueue();
        
        this.Vaccum(); //Clear all text areas and queues, keep it tidy!
    }

//-------------------------------Create Random Queries--------------------------
    //CREATE FAKE PHONE NUMS FOR OUR QUEUE ITEMS
    
    private String generateRandomPhoneNumber() {
        
        String sPhone = "69";
        
        for(int i=0; i < 8; i++){
            
            sPhone += rand.nextInt(10);
        }
        return sPhone;
    }
//..................................................
    //GENERATE RAND NAMES FOR OUR QUEUE ITEMS
    
    private String generateRandomFullName(){
        
        String sFName = FName[rand.nextInt(FName.length)] + " " + LName[rand.nextInt(LName.length)];
        
        return sFName;
    }
//..................................................
    //CREATE THE FAKE QUERIES
    
    private void CreateDemoQueue(){
        
        for(int i=0; i < 1000; i++){
            
            QueryQueue.Enqueue(new CQuery(generateRandomFullName(), generateRandomPhoneNumber()));
        }
    }
    
//..................................................  
    //TO DISPLAY THE FAKE QUERIES
    
    protected void DisplayQueue(){
        
        txtIncomingQ.setText(QueryQueue.toString());
    }
    
//..................................................
    //CREATES AND DISPLAYS THE FAKE QUERIES
    
    private void SimulateDEMOQueries(){
        
        CreateDemoQueue();
        DisplayQueue();
    }
    
//..................................................
    //CREATES AND DISPLAYS REAL QUERIES
    
    private void SimulateREALQueries(CQueue<CQuery> p_oQueue){
        
        for(int i=0; i < p_oQueue.getItemCount(); i++){

            QueryQueue.Enqueue(p_oQueue.Dequeue());
        }  
        DisplayQueue();
    }
    
//--------------------------------Backlogs and Bots-----------------------------
    //ASSIGN TO ALL BOTS AND REFRESH THEIR VIEWS
    //USED ONLY IN "TASK" EVERY 10 SECONDS
    
    protected void RefreshBacklogs(){
        
        AssignQueries();
        DisplayBacklogsQ();
    }
    
//.....................................................  
    //RUN THESE EVERY 10 SECONDS (ASSIGNMENTS-DISPLAYS)
    
    TimerTask task = new TimerTask(){
    @Override
    public void run(){
        try{
            
            while(pauseSim == true) {

                Thread.sleep(200);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Something went wrong while pausing system!");
        }
        
            RefreshBacklogs();  //assiagn new queries to bots and display them
            DisplayQueue();     //refresh the list of remaining queries
        }
    };

//.......................................................
    //START THE SIM (CREATE FAKE QUERIES - ASSIGN TO BOTS
    
    private void StartSimulation(){
        
        this.oThread = new Thread(){
        
            public void run(){

                try{

                    if(!QueryQueue.IsEmpty())
                        timer.scheduleAtFixedRate(task, 1000, 10000); //start after 100millisecond(start for visual perposes)
                                                                                //repeat every 10 seconds the task
                }
                catch(Exception e){
                    
                    JOptionPane.showMessageDialog(null, "Something went wrong while simulating queue!");
                }

            }
        }; 
        this.oThread.start();
     }
//..................................................
    //TO PSEUDO SET THE BUTTON STATE 
    //AND PAUSE/CONTINUE THE THREAD
    
    public void ButtonChangeState(){
        
        if(pauseSim == false){
            
            pauseSim = true; 
            
            tglbtnPause.setText("Resume");
        }
        else if(pauseSim = true){
            
            pauseSim = false;

            tglbtnPause.setText("Pause");
        }
    }
 
//..................................................
    //ASSIGN TO ALL BOTS
    
    private void AssignQueries(){
        
        if(BotBacklogQ1.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ1.Enqueue(QueryQueue.Dequeue());
        }
        
        if(BotBacklogQ2.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ2.Enqueue(QueryQueue.Dequeue());
        }
        
        if(BotBacklogQ3.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ3.Enqueue(QueryQueue.Dequeue());
        }
        
        if(BotBacklogQ4.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ4.Enqueue(QueryQueue.Dequeue());
        }
    }
    
//..................................................
    //BOT INDIVIDUALL ASSIGNMENTS
    
    private void AssignQueryBot1(){
        
        if(BotBacklogQ1.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ1.Enqueue(QueryQueue.Dequeue());
        }
    }
    
    private void AssignQueryBot2(){
        
        if(BotBacklogQ2.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ2.Enqueue(QueryQueue.Dequeue());
        }
    }
    
    private void AssignQueryBot3(){
        
        if(BotBacklogQ3.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ3.Enqueue(QueryQueue.Dequeue());
        }
    }
    
    private void AssignQueryBot4(){
        
        if(BotBacklogQ4.getItemCount() < 4 && !QueryQueue.IsEmpty()){
            
            BotBacklogQ4.Enqueue(QueryQueue.Dequeue());
        }
    }
    
//..................................................
    //DISPLAY ALL BACKLOGS
    
    private void DisplayBacklogsQ(){
        
        DisplayBacklogQ1();
        DisplayBacklogQ2();
        DisplayBacklogQ3();
        DisplayBacklogQ4();
    }
    
//..................................................
    //BOT INDIVIDUAL DISPLAYS
    
    private void DisplayBacklogQ1(){
        
        if(!BotBacklogQ1.IsEmpty())
            txtBacklogQ1.setText(BotBacklogQ1.toString());
        else
            txtBacklogQ1.setText("");
    }
    
    private void DisplayBacklogQ2(){
        
        if(!BotBacklogQ2.IsEmpty())
            txtBacklogQ2.setText(BotBacklogQ2.toString());
        else
            txtBacklogQ2.setText("");
    }
    
    private void DisplayBacklogQ3(){
        
        if(!BotBacklogQ3.IsEmpty())
            txtBacklogQ3.setText(BotBacklogQ3.toString());
        else
            txtBacklogQ3.setText("");
    }
    
    private void DisplayBacklogQ4(){
        
        if(!BotBacklogQ4.IsEmpty())
            txtBacklogQ4.setText(BotBacklogQ4.toString());
        else
            txtBacklogQ4.setText("");
    }
    
//..................................................
    //BOT INDIVIDUAL ANSWERS
    
    private void Bot1Answer(){
        
        txtNow1.setText("ID: "+BotBacklogQ1.Peek().getID()+" - "+BotBacklogQ1.Peek().getTime());

        txtContent1.setText(BotBacklogQ1.Peek().getContent());
        txtTitle1.setText(BotBacklogQ1.Peek().getTitle());
        
        BotBacklogQ1.Dequeue();
        DisplayBacklogQ1();
        AssignQueryBot1();
    }
    
    private void Bot2Answer(){
        
        txtNow2.setText("ID: "+BotBacklogQ2.Peek().getID()+" - "+BotBacklogQ2.Peek().getTime());
        
        txtContent2.setText(BotBacklogQ2.Peek().getContent());
        txtTitle2.setText(BotBacklogQ2.Peek().getTitle());
        
        BotBacklogQ2.Dequeue();
        DisplayBacklogQ2();
        AssignQueryBot2();
    }
    
    private void Bot3Answer(){
        
        txtNow3.setText("ID: "+BotBacklogQ3.Peek().getID()+" - "+BotBacklogQ3.Peek().getTime());
        
        txtContent3.setText(BotBacklogQ3.Peek().getContent());
        txtTitle3.setText(BotBacklogQ3.Peek().getTitle());
        
        BotBacklogQ3.Dequeue();
        DisplayBacklogQ3();
        AssignQueryBot3();
    }
    
    private void Bot4Answer(){
        
        txtNow4.setText("ID: "+BotBacklogQ4.Peek().getID()+" - "+BotBacklogQ4.Peek().getTime());
        
        txtContent4.setText(BotBacklogQ4.Peek().getContent());
        txtTitle4.setText(BotBacklogQ4.Peek().getTitle());
        
        BotBacklogQ4.Dequeue();
        DisplayBacklogQ4();
        AssignQueryBot4();
    }
//------------------------------Clear Everything--------------------------------
    //Clear Queues
    private void ClearQueues(){
        
    QueryQueue.Clear();
    BotBacklogQ1.Clear();
    BotBacklogQ2.Clear();
    BotBacklogQ3.Clear();
    BotBacklogQ4.Clear();
    }
    
    //Clear Text Areas
    private void ClearTextAreas(){
        
        txtIncomingQ.setText("");
        
        txtBacklogQ1.setText("");
        txtBacklogQ2.setText("");
        txtBacklogQ3.setText("");
        txtBacklogQ4.setText("");
        
        txtNow1.setText("");
        txtNow2.setText("");
        txtNow3.setText("");
        txtNow4.setText("");
        
        txtTitle1.setText("");
        txtTitle2.setText("");
        txtTitle3.setText("");
        txtTitle4.setText("");
        
        txtContent1.setText("");
        txtContent2.setText("");
        txtContent3.setText("");
        txtContent4.setText("");
    }
    
    //GET THE VACCUM
    private void Vaccum(){
        
        this.ClearQueues();
        this.ClearTextAreas();
    }
 
//--------------------------I BELIEVE THATS ENOUGH------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSimulateIncoming = new javax.swing.JButton();
        lblIncomingQ = new javax.swing.JLabel();
        lblBacklogQ1 = new javax.swing.JLabel();
        lblBacklogQ2 = new javax.swing.JLabel();
        lblBacklogQ3 = new javax.swing.JLabel();
        lblBacklogQ4 = new javax.swing.JLabel();
        btnAnswer2 = new javax.swing.JButton();
        btnAnswer1 = new javax.swing.JButton();
        btnAnswer3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtIncomingQ = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtBacklogQ1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtBacklogQ2 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtBacklogQ3 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtBacklogQ4 = new javax.swing.JTextArea();
        btnAnswer4 = new javax.swing.JButton();
        lblProcessed1 = new javax.swing.JLabel();
        txtTitle1 = new javax.swing.JTextField();
        lblProcessed2 = new javax.swing.JLabel();
        txtTitle2 = new javax.swing.JTextField();
        lblProcessed3 = new javax.swing.JLabel();
        txtTitle3 = new javax.swing.JTextField();
        lblProcessed4 = new javax.swing.JLabel();
        txtTitle4 = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();
        tglbtnPause = new javax.swing.JToggleButton();
        lblNow2 = new javax.swing.JLabel();
        lblNow4 = new javax.swing.JLabel();
        lblNow1 = new javax.swing.JLabel();
        lblNow3 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtNow1 = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtNow2 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtNow4 = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtNow3 = new javax.swing.JTextArea();
        lblStart = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtContent3 = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtContent1 = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtContent2 = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtContent4 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        btnSimulateIncoming.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow.png"))); // NOI18N
        btnSimulateIncoming.setText("Simulate User Queries");
        btnSimulateIncoming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimulateIncomingActionPerformed(evt);
            }
        });

        lblIncomingQ.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIncomingQ.setText("Incoming Queries");

        lblBacklogQ1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBacklogQ1.setText("Chatbot 1 Backlog");

        lblBacklogQ2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBacklogQ2.setText("Chatbot 2 Backlog");

        lblBacklogQ3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBacklogQ3.setText("Chatbot 3 Backlog");

        lblBacklogQ4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBacklogQ4.setText("Chatbot 4 Backlog");

        btnAnswer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bot.png"))); // NOI18N
        btnAnswer2.setText("2");
        btnAnswer2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnswer2ActionPerformed(evt);
            }
        });

        btnAnswer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bot.png"))); // NOI18N
        btnAnswer1.setText("1");
        btnAnswer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnswer1ActionPerformed(evt);
            }
        });

        btnAnswer3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bot.png"))); // NOI18N
        btnAnswer3.setText("3");
        btnAnswer3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnswer3ActionPerformed(evt);
            }
        });

        txtIncomingQ.setColumns(20);
        txtIncomingQ.setRows(5);
        jScrollPane1.setViewportView(txtIncomingQ);

        txtBacklogQ1.setColumns(20);
        txtBacklogQ1.setRows(5);
        jScrollPane2.setViewportView(txtBacklogQ1);

        txtBacklogQ2.setColumns(20);
        txtBacklogQ2.setRows(5);
        jScrollPane3.setViewportView(txtBacklogQ2);

        txtBacklogQ3.setColumns(20);
        txtBacklogQ3.setRows(5);
        jScrollPane4.setViewportView(txtBacklogQ3);

        txtBacklogQ4.setColumns(20);
        txtBacklogQ4.setRows(5);
        jScrollPane5.setViewportView(txtBacklogQ4);

        btnAnswer4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bot.png"))); // NOI18N
        btnAnswer4.setText("4");
        btnAnswer4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnswer4ActionPerformed(evt);
            }
        });

        lblProcessed1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProcessed1.setText("Query answered by 1");

        lblProcessed2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProcessed2.setText("Query answered by 2");

        lblProcessed3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProcessed3.setText("Query answered by 3");

        lblProcessed4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProcessed4.setText("Query answered by 4");

        lblTitle.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        lblTitle.setText("Query Routing Simulator");

        tglbtnPause.setText("Pause");
        tglbtnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnPauseActionPerformed(evt);
            }
        });

        lblNow2.setText("Now Answering");

        lblNow4.setText("Now Answering");

        lblNow1.setText("Now Answering");

        lblNow3.setText("Now Answering");

        txtNow1.setColumns(20);
        txtNow1.setRows(5);
        jScrollPane6.setViewportView(txtNow1);

        txtNow2.setColumns(20);
        txtNow2.setRows(5);
        jScrollPane7.setViewportView(txtNow2);

        txtNow4.setColumns(20);
        txtNow4.setRows(5);
        jScrollPane8.setViewportView(txtNow4);

        txtNow3.setColumns(20);
        txtNow3.setRows(5);
        jScrollPane10.setViewportView(txtNow3);

        lblStart.setText("Press Simulate to Start!");

        txtContent3.setColumns(20);
        txtContent3.setRows(5);
        jScrollPane9.setViewportView(txtContent3);

        txtContent1.setColumns(20);
        txtContent1.setRows(5);
        jScrollPane11.setViewportView(txtContent1);

        txtContent2.setColumns(20);
        txtContent2.setRows(5);
        jScrollPane13.setViewportView(txtContent2);

        txtContent4.setColumns(20);
        txtContent4.setRows(5);
        jScrollPane12.setViewportView(txtContent4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(lblBacklogQ1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(lblNow1)))
                                .addGap(154, 154, 154)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNow2)
                                    .addComponent(lblBacklogQ2)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblBacklogQ3)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(583, 583, 583)
                        .addComponent(lblNow3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblBacklogQ4)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNow4))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addComponent(lblIncomingQ)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSimulateIncoming, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tglbtnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStart)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(430, 430, 430)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(289, 289, 289)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnAnswer2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblProcessed2))))
                            .addComponent(txtTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnAnswer1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblProcessed1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane12))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtTitle3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(137, 137, 137)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTitle4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(btnAnswer4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblProcessed4))))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnAnswer3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblProcessed3)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblTitle)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lblStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSimulateIncoming, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tglbtnPause))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(lblIncomingQ))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBacklogQ1)
                            .addComponent(lblBacklogQ2)
                            .addComponent(lblBacklogQ3)
                            .addComponent(lblBacklogQ4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNow2)
                            .addComponent(lblNow4)
                            .addComponent(lblNow3)
                            .addComponent(lblNow1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAnswer3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAnswer2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAnswer1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblProcessed1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblProcessed2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(txtTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblProcessed3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTitle3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAnswer4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblProcessed4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTitle4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(jScrollPane12)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(jScrollPane13))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    protected void OperateDemo(){
        
        this.Vaccum();
        this.SimulateDEMOQueries();
        this.StartSimulation();
    }
    
    protected void OperateReal(CQueue<CQuery> p_oQueue){
        
        this.SimulateREALQueries(p_oQueue);
        this.StartSimulation();
    }
    private void btnSimulateIncomingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimulateIncomingActionPerformed

    lblStart.setVisible(false);
    
    if(isDemo)
        this.OperateDemo();
    else
        this.OperateReal(QueryQueue);
    }//GEN-LAST:event_btnSimulateIncomingActionPerformed

    private void btnAnswer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnswer1ActionPerformed
        
        Bot1Answer();
    }//GEN-LAST:event_btnAnswer1ActionPerformed

    private void btnAnswer2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnswer2ActionPerformed
        
        Bot2Answer();
    }//GEN-LAST:event_btnAnswer2ActionPerformed

    private void btnAnswer3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnswer3ActionPerformed
        
        Bot3Answer();
    }//GEN-LAST:event_btnAnswer3ActionPerformed

    private void btnAnswer4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnswer4ActionPerformed
        
        Bot4Answer();
    }//GEN-LAST:event_btnAnswer4ActionPerformed

    private void tglbtnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnPauseActionPerformed

        this.ButtonChangeState();  
    }//GEN-LAST:event_tglbtnPauseActionPerformed

    //If window is closing update the links to the other forms
    //and let them know that the window is now closed
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
        oMain.UpdateLinks();
        
        System.out.println("\nExiting Router\n");
    }//GEN-LAST:event_formWindowClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnswer1;
    private javax.swing.JButton btnAnswer2;
    private javax.swing.JButton btnAnswer3;
    private javax.swing.JButton btnAnswer4;
    private javax.swing.JButton btnSimulateIncoming;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblBacklogQ1;
    private javax.swing.JLabel lblBacklogQ2;
    private javax.swing.JLabel lblBacklogQ3;
    private javax.swing.JLabel lblBacklogQ4;
    private javax.swing.JLabel lblIncomingQ;
    private javax.swing.JLabel lblNow1;
    private javax.swing.JLabel lblNow2;
    private javax.swing.JLabel lblNow3;
    private javax.swing.JLabel lblNow4;
    private javax.swing.JLabel lblProcessed1;
    private javax.swing.JLabel lblProcessed2;
    private javax.swing.JLabel lblProcessed3;
    private javax.swing.JLabel lblProcessed4;
    private javax.swing.JLabel lblStart;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JToggleButton tglbtnPause;
    private javax.swing.JTextArea txtBacklogQ1;
    private javax.swing.JTextArea txtBacklogQ2;
    private javax.swing.JTextArea txtBacklogQ3;
    private javax.swing.JTextArea txtBacklogQ4;
    private javax.swing.JTextArea txtContent1;
    private javax.swing.JTextArea txtContent2;
    private javax.swing.JTextArea txtContent3;
    private javax.swing.JTextArea txtContent4;
    private javax.swing.JTextArea txtIncomingQ;
    private javax.swing.JTextArea txtNow1;
    private javax.swing.JTextArea txtNow2;
    private javax.swing.JTextArea txtNow3;
    private javax.swing.JTextArea txtNow4;
    private javax.swing.JTextField txtTitle1;
    private javax.swing.JTextField txtTitle2;
    private javax.swing.JTextField txtTitle3;
    private javax.swing.JTextField txtTitle4;
    // End of variables declaration//GEN-END:variables
}
