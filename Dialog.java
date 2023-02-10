import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.text.*;
import java.util.*;

class Dialog extends JDialog implements ActionListener
{
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    
    JTextField dateTF;
    JTextField nameTF;
    JComboBox serviceCodeCB;
    JTextField initialMileageTF;
    JTextField mileageOnReturnTF;
    JTextField billingRateTF;
    JTextField commentsTF;
    JButton cancelButton;
    JButton submitButton;
    
    DataManager manager;
    
    TripRecord record;
    int index;
    
    //=================================
    
    // add
    Dialog(DataManager manager)
    {
        this.manager = manager;
        record = new TripRecord();
        index = -1;
        
        setupJComponents();
        setupLayout();
        setupJDialog("Add Record");
    }
    
    //=================================
    
    // edit
    Dialog(DataManager manager, TripRecord record, int index)
    {
        this.manager = manager;
        this.record = record;
        this.index = index;
        
        setupJComponents();
        setupLayout();
        fillFields();
        setupJDialog("Replace Record");
    }
    
    //=================================
    
    void setupJDialog(String title)
    {
        Toolkit    tk;
        Dimension   d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(d.width/3, d.height/3);
        setLocation(d.width/3, d.height/3);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle(title);
        
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setVisible(true);
    }
    
    //=================================
    
    // initialize and prepare JComponents
    void setupJComponents()
    {
        dateTF = new JTextField(10);
        dateTF.setInputVerifier(new DateVerifier());
        
        nameTF = new JTextField(10);
        
        String[] serviceCodeList = {"A0428", "A0429", "A0427", "A0434"};
        serviceCodeCB = new JComboBox(serviceCodeList);
        
        initialMileageTF = new JTextField(10);
        initialMileageTF.setInputVerifier(new IntVerifier());
        
        mileageOnReturnTF = new JTextField(10);
        mileageOnReturnTF.setInputVerifier(new IntVerifier());
        
        billingRateTF = new JTextField(10);
        billingRateTF.setInputVerifier(new DoubleVerifier());
        
        commentsTF = new JTextField(10);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        
        submitButton = new JButton("Submit");
        submitButton.setActionCommand("Submit");
        submitButton.addActionListener(this);
    }
        
    
    void setupLayout()
    {
        JPanel panel = new JPanel();
        
        JLabel dateLbl = new JLabel("Date:");
        JLabel nameLbl = new JLabel("Name:");
        JLabel serviceCodeLbl = new JLabel("Service Code:");
        JLabel initialMileageLbl = new JLabel("Initial Mileage:");
        JLabel mileageOnReturnLbl = new JLabel("Mileage On Return:");
        JLabel billingRateLbl = new JLabel("Billing Rate:");
        JLabel commentsLbl = new JLabel("Comments:");
        
        //=====
        
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        panel.setLayout(layout);
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        
        hGroup.addGroup(layout.createParallelGroup()
            .addComponent(dateLbl)
            .addComponent(nameLbl)
            .addComponent(serviceCodeLbl)
            .addComponent(initialMileageLbl)
            .addComponent(mileageOnReturnLbl)
            .addComponent(billingRateLbl)
            .addComponent(commentsLbl)
            .addComponent(cancelButton));
                        
        hGroup.addGroup(layout.createParallelGroup()
            .addComponent(dateTF)
            .addComponent(nameTF)
            .addComponent(serviceCodeCB)
            .addComponent(initialMileageTF)
            .addComponent(mileageOnReturnTF)
            .addComponent(billingRateTF)
            .addComponent(commentsTF)
            .addComponent(submitButton));
     
        layout.setHorizontalGroup(hGroup);
        
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        vGroup.addGroup(layout.createParallelGroup().addComponent(dateLbl).addComponent(dateTF));
        vGroup.addGroup(layout.createParallelGroup().addComponent(nameLbl).addComponent(nameTF));
        vGroup.addGroup(layout.createParallelGroup().addComponent(serviceCodeLbl).addComponent(serviceCodeCB));
        vGroup.addGroup(layout.createParallelGroup().addComponent(initialMileageLbl).addComponent(initialMileageTF));
        vGroup.addGroup(layout.createParallelGroup().addComponent(mileageOnReturnLbl).addComponent(mileageOnReturnTF));
        vGroup.addGroup(layout.createParallelGroup().addComponent(billingRateLbl).addComponent(billingRateTF));
        vGroup.addGroup(layout.createParallelGroup().addComponent(commentsLbl).addComponent(commentsTF));
        vGroup.addGroup(layout.createParallelGroup().addComponent(cancelButton).addComponent(submitButton));
                        
        layout.setVerticalGroup(vGroup);
        
        add(panel);
    }
    
    //=================================
    
    // only for editing a record
    // fills jdialog fields with record to be edited
    void fillFields()
    {
        dateTF.setText(dateFormat.format(record.date));
        nameTF.setText(record.name);
        serviceCodeCB.setSelectedItem((Object)record.serviceCode);
        initialMileageTF.setText("" + record.initialMileage);
        mileageOnReturnTF.setText("" + record.mileageOnReturn);
        billingRateTF.setText("" + record.billingRate);
        commentsTF.setText(record.comments);
    }
    
    //=================================
    
    // fills record with data from jdialog fields when user clicks submit
    void fillRecord()
    {
        record.date = new Date();
        ParsePosition pos = new ParsePosition(0);
        dateFormat.setLenient(false);
        record.date = dateFormat.parse(dateTF.getText(), pos);
        
        record.name = nameTF.getText();
        record.serviceCode = (String)serviceCodeCB.getSelectedItem();
        record.mileageOnReturn = Integer.parseInt(mileageOnReturnTF.getText());
        record.initialMileage = Integer.parseInt(initialMileageTF.getText());
        record.billingRate = Double.parseDouble(billingRateTF.getText());
        record.comments = commentsTF.getText();
    }
    
    //=================================
    
    // checks if text fields are empty when user clicks submit
    boolean isEmpty()
    {
        if(dateTF.getText().length() == 0)
        {
            dateTF.requestFocus();
            return true;
        }
        
        if(nameTF.getText().length() == 0)
        {
            nameTF.requestFocus();
            return true;
        }
        
        if(initialMileageTF.getText().length() == 0)
        {
            initialMileageTF.requestFocus();
            return true;
        }
        
        if(mileageOnReturnTF.getText().length() == 0)
        {
            mileageOnReturnTF.requestFocus();
            return true;
        }
        
        if(billingRateTF.getText().length() == 0)
        {
            billingRateTF.requestFocus();
            return true;
        }
        
        return false;
    }
    
    //=================================
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // cancel button
        if(e.getActionCommand().equals("Cancel"))
        {
            dispose();
            TableModel.setIsChanged(false);
        }
        
        // submit button
        if(e.getActionCommand().equals("Submit"))
        {
            // checking if any required fields are empty
            if(isEmpty())
                return;
            
            // mileage check
            if(Integer.parseInt(initialMileageTF.getText()) > Integer.parseInt(mileageOnReturnTF.getText()))
            {
                initialMileageTF.requestFocus();
                return;
            }
            
            // add
            if(index == -1)
            {
                fillRecord();
                manager.addRecord(record);
                dispose();
            }
            
            // edit
            else
            {
                fillRecord();
                manager.editRecord(record, index);
                dispose();
            }
            
            TableModel.setIsChanged(true);
        }
    }
}
