import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;


class TripRecordFrame extends JFrame implements MouseListener, ActionListener, DropTargetListener, WindowListener
{
    TableModel tableModel;
    JTable table;
    JScrollPane tableScroll;
    
    DropTarget dropTarget;
    
    JMenuBar menuBar;
    JMenu fileMenu, editMenu;
    JMenuItem deleteItem, saveAsItem;
    JButton deleteButton, editButton;
    
    JFileChooser fileChooser;
    File file;
    
    int[] indices;
    
    JMenuItem editPopupItem;
    JMenuItem delPopupItem;
    
    //=================================
        
    TripRecordFrame()
    {
        setupTableModel();
        setJMenuBar(setupJMenuBar());
        setupJButtons();
        setupFrame();
        fileChooser = new JFileChooser();
    }
    
    //=================================
    
    void setupTableModel()
    {
        tableModel = new TableModel();
        table = new JTable(tableModel);
        table.addMouseListener(this);
      
        table.setMinimumSize(new Dimension(400, 250));
        table.setColumnModel(getColumnModel()); // calls method below
        tableModel.addTableModelListener(table);
        
        tableScroll = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(400, 250));
        
        table.setRowSorter(new TableRowSorter(tableModel));
        
        add(tableScroll, BorderLayout.CENTER);
        
        dropTarget = new DropTarget(tableScroll, this);
    }
      
    //=================================
                             
    TableColumnModel getColumnModel()
    {
        DefaultTableColumnModel colModel;
        
        colModel = new DefaultTableColumnModel();
        
        colModel.addColumn(getColumn(0, "Date"));
        colModel.addColumn(getColumn(1, "Name"));
        colModel.addColumn(getColumn(2, "Service Code"));
        colModel.addColumn(getColumn(3, "Initial Mileage"));
        colModel.addColumn(getColumn(4, "Mileage On Return"));
        colModel.addColumn(getColumn(5, "Billing Rate"));
        colModel.addColumn(getColumn(6, "Comments"));
        
        return colModel;
    }
    
    // helper for method above
    TableColumn getColumn(int index, String name)
    {
        TableColumn col = new TableColumn(index);
        col.setPreferredWidth(80);
        col.setMinWidth(80);
        col.setHeaderValue(name);
        
        return col;
    }
                             
    //=================================
    
    // each item will be created with the setupMenuItem method
    // some methods will be data members for the purpose of setEnabledfalse for delete and doClick for saveAsItem
    JMenuBar setupJMenuBar()
    {
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File", true);
        fileMenu.setMnemonic('F');
        fileMenu.add(setupMenuItem("Load", "Load from an existing file", KeyEvent.VK_L));
        fileMenu.add(setupMenuItem("Save", "Save list to last used file", KeyEvent.VK_S));
        saveAsItem = setupMenuItem("SaveAs", "Save list to a file", KeyEvent.VK_A);
        fileMenu.add(saveAsItem);
        menuBar.add(fileMenu);
                     
        editMenu = new JMenu("Edit", true);
        editMenu.setMnemonic('E');
        editMenu.add(setupMenuItem("Add", "Add new record", KeyEvent.VK_N));
        deleteItem = setupMenuItem("Delete", "Delete record", KeyEvent.VK_D);
        deleteItem.setEnabled(false);
        editMenu.add(deleteItem);
        editMenu.add(setupMenuItem("Clear", "CLear all records", KeyEvent.VK_C));
        menuBar.add(editMenu);
        
        return menuBar;
    }
    
    // helper for method above
    JMenuItem setupMenuItem(String label, String toolTipText, int mnemonic)
    {
        JMenuItem item;
            
        item = new JMenuItem(label, mnemonic);
        item.setToolTipText(toolTipText);
        item.setAccelerator(KeyStroke.getKeyStroke(mnemonic, KeyEvent.ALT_MASK));
        item.setActionCommand(label);
        item.addActionListener(this);
        
        return item;
    }
    
    //=================================
    
    //each button will be created with the setupButton method
    // deleteButton is a data member for the purpose of setEnabledFalse
    void setupJButtons()
    {
        JPanel panel = new JPanel(new GridLayout(9, 1));
        
        panel.add(setupButton("Load", "Load from an existing file"));
        panel.add(setupButton("Save", "Save list to last used file"));
        panel.add(setupButton("SaveAs", "Save list to a file"));
        panel.add(setupButton("Add", "Add new record"));
        
        editButton = setupButton("Edit", "Edit record");
        editButton.setEnabled(false);
        panel.add(editButton);
        
        deleteButton = setupButton("Delete", "Delete record");
        deleteButton.setEnabled(false);
        panel.add(deleteButton);
        
        panel.add(setupButton("Sort", "Sort Records"));
        panel.add(setupButton("Add Random", "For debugging"));
        panel.add(setupButton("Exit", "Exit program"));
        
        add(panel, BorderLayout.EAST);
    }
    
    // helper for method above
    JButton setupButton(String label, String toolTipText)
    {
        JButton button;
                             
        button = new JButton(label);
        button.setToolTipText(toolTipText);
        button.setActionCommand(label);
        button.addActionListener(this);
                         
        return button;
    }
    
    //=================================
    
    // the magic method
    void setupFrame()
    {
        Toolkit    tk;
        Dimension   d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        
        setSize(d.width / 2, d.height / 2);
        setLocation(d.width/3, d.height/3);

        // closing handled in windowClosing()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setTitle("Trip Record Table");
        setVisible(true);
    }
    
    //=================================
    //=================================
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde)
    {
    }
    
    @Override
    public void dragExit(DropTargetEvent dte)
    {
    }
    
    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {
    }
    
    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        java.util.List<File> fileList;
        Transferable transferableData = dtde.getTransferable();
        
        try
        {
            if(transferableData.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                
                fileList = (java.util.List<File>)(transferableData.getTransferData(DataFlavor.javaFileListFlavor));
                
                // reject multiple files
                if(fileList.size() > 1)
                    return;
                
                // checking for unsaved changes
                if(tableModel.getIsChanged())
                {
                    int option = JOptionPane.showConfirmDialog(this, "Continue?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
                
                    if (option == JOptionPane.NO_OPTION)
                        return;
                }
                
                tableModel.clear();
                
                fileChooser.setSelectedFile(fileList.get(0));
                
                file = fileList.get(0);
                tableModel.load(file);
            }
            
            else
                System.out.println("File list flavor not supported");
        }
        catch(UnsupportedFlavorException ufe)
        {
            System.out.println("unsupported flavor found");
        }
        catch(IOException ioe)
        {
            System.out.println("IOException found getting transferable data");
        }
    }
    
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde)
    {
    }
    
    
    //=================================
    //=================================
    
  
    @Override
    public void mouseClicked(MouseEvent e)
    {
        // right click JPopup for edit / delete
        if(e.getButton() == 3)
        {
            JPopupMenu rClickMenu = new JPopupMenu("Edit / Delete");
            
            editPopupItem = new JMenuItem("Edit");
            editPopupItem.setActionCommand("Edit");
            editPopupItem.addActionListener(this);
            rClickMenu.add(editPopupItem);
            
            delPopupItem = new JMenuItem("Delete");
            delPopupItem.setActionCommand("Delete");
            delPopupItem.addActionListener(this);
            rClickMenu.add(delPopupItem);
            
            
            int rowSelection = table.rowAtPoint(e.getPoint());
            table.setRowSelectionInterval(rowSelection, rowSelection);
            
            rClickMenu.show(this, e.getX(), e.getY());
        }
        
        // double click to edit
        if(e.getClickCount() == 2)
        {
            new Dialog(tableModel, tableModel.getRecord(indices[0]), indices[0]);
        }
        
        // indices to edit / delete
        indices = new int[table.getSelectedRows().length];
        indices = table.getSelectedRows();
        
        deleteItem.setEnabled(indices.length != 0);
        deleteButton.setEnabled(indices.length != 0);
        editButton.setEnabled(indices.length == 1);
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        indices = new int[table.getSelectedRows().length];
        indices = table.getSelectedRows();
        
        deleteItem.setEnabled(indices.length != 0);
        deleteButton.setEnabled(indices.length != 0);
        editButton.setEnabled(indices.length == 1);
    }
    
    
    //=================================
    //=================================
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Load"))
        {
            if(fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                return;
            
            // checking for unsaved changes
            if(tableModel.getIsChanged())
            {
                int option = JOptionPane.showConfirmDialog(this, "Continue?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
            
                if (option == JOptionPane.NO_OPTION)
                    return;
            }
            
            tableModel.clear();
            
            file = fileChooser.getSelectedFile();
            
            tableModel.load(file);
        }
        
        
        if(e.getActionCommand().equals("Save"))
        {
            if(file.exists())
                tableModel.save(file);
            else
                saveAsItem.doClick();
        }
        
        
        if(e.getActionCommand().equals("SaveAs"))
        {
            if(fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
                return;
            
            file = fileChooser.getSelectedFile();
            
            tableModel.save(file);
        }
        
    
        if(e.getActionCommand().equals("Add"))
        {
            new Dialog(tableModel);
        }
        
        
        if(e.getActionCommand().equals("Edit"))
        {
            new Dialog(tableModel, tableModel.getRecord(indices[0]), indices[0]);
        }
        
        
        if(e.getActionCommand().equals("Delete"))
        {
            for(int n = indices.length - 1; n >= 0; n--)
                tableModel.delete(indices[n]);
        }
        
        
        if(e.getActionCommand().equals("Clear"))
        {
            // checking for unsaved changes
            if(tableModel.getIsChanged())
            {
                int option = JOptionPane.showConfirmDialog(this, "Continue?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
            
                if (option == JOptionPane.NO_OPTION)
                    return;
            }
            
            tableModel.clear();
        }
        
        
        if(e.getActionCommand().equals("Sort"))
        {
            System.out.println("Sorting");
        }
        
        
        if(e.getActionCommand().equals("Add Random"))
        {
            tableModel.addRecord(TripRecord.getRandom());
        }
        
        
        if(e.getActionCommand().equals("Exit"))
        {
            // checking for unsaved changes
            if(tableModel.getIsChanged())
            {
                int option = JOptionPane.showConfirmDialog(this, "Continue?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
            
                if (option == JOptionPane.NO_OPTION)
                    return;
            }
            
            System.exit(0);
        }
        
        deleteItem.setEnabled(false);
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
    }
    
    
    //=================================
    //=================================
    
    
    @Override
    public void windowActivated(WindowEvent e)
    {
    }
    
    
    @Override
    public void windowClosed(WindowEvent e)
    {
    }
  

    @Override
    public void windowClosing(WindowEvent e)
    {
        // checking for unsaved changes
        if(tableModel.getIsChanged())
        {
            int option = JOptionPane.showConfirmDialog(this, "Continue?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
        
            if (option == JOptionPane.YES_OPTION)
                System.exit(0);
        }
        
        else
            System.exit(0);
    }
   
    
    @Override
    public void windowDeactivated(WindowEvent e)
    {
    }
   
    
    @Override
    public void windowDeiconified(WindowEvent e)
    {
    }
  
    
    @Override
    public void windowIconified(WindowEvent e)
    {
    }
    
    
    @Override
    public void windowOpened(WindowEvent e)
    {
    }
}


