/*  Name:  Gabriel Couto 
     Course: CNT 4714 – Spring 2022 
     Assignment title: Project 1 – Event-driven Enterprise Simulation 
     Date: Sunday January 30, 2022 
*/ 

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List; 

public class FrontWindow {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtItemID;
	private Text txtItemDetials;
	private Text txtOrderTotal;
	private Text txtItemQuan;
	public Button btnFinishOrder;
	public Button btnProcess;
	public Button btnConfirm;
	public Button btnViewOrder;
	public Button btnNewOrder;
	public Button btnExit;
	public static String [][] inventory;
	public static int InvLength;
	public static double Subtot;
	public static double Tot;
	public static String [] histo;
	public static int Itter = 0;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("inventory.txt"));
	    String line = null;
	    inventory = new String [100][100];
	    histo = new String [100];
	    int i = 0, j = 0;
	    
	    while ((line = br.readLine()) != null) {
	    	String[] values = line.split(",");
	    	j = 0;
	    	
	        for (String str : values) {
	        	inventory[i][j] = str;
	        	j++;
	        }
	        
	        i++;
	    }
	    
	    InvLength = i;
	    
	    /*
	    for(i = 0; i <39; i++)
	    	for(j = 0; j <4; j++)
	    		System.out.println(inventory[i][j]);
	    */
	    
	    br.close();
	    
	    File temp = new File("transactions");
	    
	    if(temp.exists() == false) {
	    	File yourFile = new File("transactions.txt");
	    	yourFile.createNewFile();
	    }
	    
		try {
			FrontWindow window = new FrontWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		btnViewOrder.setEnabled(false);
		btnConfirm.setEnabled(false);
		btnFinishOrder.setEnabled(false);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(688, 303);
		shell.setText("Nile Dot Com");
		
		btnProcess = formToolkit.createButton(shell, "Process Item", SWT.NONE);
		btnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ItemID = txtItemID.getText();
				String ItemQuantity = txtItemQuan.getText();
				double ItemQ = Double.parseDouble(ItemQuantity);
				int i, j;
				double Deal, Total;
				
				if(ItemQ < 5)
					Deal = 0;
				else if(ItemQ < 10)
					Deal = 10;
				else if(ItemQ < 15)
					Deal = 15;
				else
					Deal = 20;
					
				
				for(i = 0; i < InvLength; i++) {
					if (inventory[i][0].equals(ItemID)) {
						
						if (inventory[i][2].equals(" false")) {
							JFrame jFrame = new JFrame();
							JOptionPane.showMessageDialog(jFrame, "Sorry this item is out of stock please try another item");
							return;
						}
						
						String Temp = inventory[i][3].strip();
						double ItemPrice = Double.parseDouble(Temp);
						
						if (Deal == 0)
							Tot = (ItemPrice * ItemQ);
						else
							Tot = ((ItemPrice * ItemQ) * ((100 - Deal) / 100));
							
						txtItemDetials.setText(inventory[i][1] + " $" + inventory[i][3] + " " + Deal + "% $" + Tot);
						
						btnProcess.setEnabled(false);
						btnConfirm.setEnabled(true);
						
						return;
					}
				}
				
				JFrame jFrame = new JFrame();
				JOptionPane.showMessageDialog(jFrame, "Item ID " + ItemID + " was not found in file");
				txtItemID.setText("");
				txtItemQuan.setText("");
			}
		});
		btnProcess.setBounds(10, 229, 75, 25);
		
		btnConfirm = formToolkit.createButton(shell, "Confirm Item", SWT.NONE);
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Subtot += Tot;
				txtOrderTotal.setText("$"+ Subtot);
				
				histo[Itter] = txtItemDetials.getText();
				Itter++;
				
				JFrame jFrame = new JFrame();
				JOptionPane.showMessageDialog(jFrame, ("Item #" + Itter + "has been accepted and added to you cart"));
				
				txtItemID.setText("");
				txtItemQuan.setText("");
				
				btnProcess.setEnabled(true);
				btnConfirm.setEnabled(false);
				btnViewOrder.setEnabled(true);
				btnFinishOrder.setEnabled(true);				
			}
		});
		btnConfirm.setBounds(112, 229, 75, 25);
		
		btnViewOrder = formToolkit.createButton(shell, "View Order", SWT.NONE);
		btnViewOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFrame jFrame = new JFrame();
				JOptionPane.showMessageDialog(jFrame, (histo));
			}
		});
		btnViewOrder.setBounds(229, 229, 75, 25);
		
		btnFinishOrder = formToolkit.createButton(shell, "Finish Order", SWT.NONE);
		btnFinishOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat formaty = new SimpleDateFormat("ddMMyyyyHHmm");
				Date date = new Date();  
				JFrame jFrame = new JFrame();
				BufferedWriter bw = null;
				int i;
				
				List<String> values = new ArrayList<String>();
			      for(String data: histo) {
			         if(data != null) { 
			            values.add(data);
			         }
			      }
			         
				JOptionPane.showMessageDialog(jFrame, new JTextArea("Date " + formatter.format(date) + " EST\n" + "\n" + "Number of line items" + Itter + "\n" + "\n" + values.toString() + 
				"\n" + "\n" + "Order Subtotal: $" + Subtot + "\n" + "\n" + 
				"Tax rate: 6%\n" + "\n" + "Tax Amount: $" + (Subtot * (0.06)) + "\n" + "\n" + "Order Total: $" + ((Subtot * (0.06)) + Subtot) + "\n" + "\n" + "Thank you for shopping with us!"));
				
				try {
					BufferedWriter f_write = new BufferedWriter(new FileWriter("transactions.txt"));
					
					for(i = 0; i < Itter; i++) {
						f_write.write(formaty.format(date) + " " + histo[i] + " " +formatter.format(date) + "\r");
					}
					
					f_write.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				btnProcess.setEnabled(false);
				btnConfirm.setEnabled(false);
				btnFinishOrder.setEnabled(false);
			}
		});
		btnFinishOrder.setBounds(352, 229, 75, 25);
		
		btnNewOrder = formToolkit.createButton(shell, "New Order", SWT.NONE);
		btnNewOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int i = 0; i < Itter; i++)
					histo[i] = null;
				
				txtItemID.setText("");
				txtItemQuan.setText("");
				txtItemDetials.setText("");
				txtOrderTotal.setText("");
				
				btnProcess.setEnabled(true);
				btnConfirm.setEnabled(false);
				btnViewOrder.setEnabled(false);
				btnFinishOrder.setEnabled(false);
				
				Itter = 0;
			}
		});
		btnNewOrder.setBounds(468, 229, 75, 25);
		
		Button btnExit = formToolkit.createButton(shell, "Exit", SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(576, 229, 75, 25);
		
		Label lblItemID = formToolkit.createLabel(shell, "Enter Item ID", SWT.NONE);
		lblItemID.setBounds(112, 55, 130, 15);
		
		txtItemID = formToolkit.createText(shell, "New Text", SWT.NONE);
		txtItemID.setText("");
		txtItemID.setBounds(248, 52, 371, 21);
		
		txtItemQuan = formToolkit.createText(shell, "New Text", SWT.NONE);
		txtItemQuan.setText("");
		txtItemQuan.setBounds(248, 90, 371, 21);
		
		txtItemDetials = formToolkit.createText(shell, "New Text", SWT.NONE);
		txtItemDetials.setText("");
		txtItemDetials.setBounds(248, 128, 371, 21);
		
		txtOrderTotal = formToolkit.createText(shell, "New Text", SWT.NONE);
		txtOrderTotal.setText("");
		txtOrderTotal.setBounds(248, 165, 371, 21);
		
		Label lblItemQuantity = formToolkit.createLabel(shell, "Enter Quanity of Item", SWT.NONE);
		lblItemQuantity.setBounds(112, 96, 130, 15);
		
		Label lblItemDetails = formToolkit.createLabel(shell, "Details for Item", SWT.NONE);
		lblItemDetails.setBounds(112, 134, 130, 15);
		
		Label lblOrderTotal = formToolkit.createLabel(shell, "Order Subtotal", SWT.NONE);
		lblOrderTotal.setBounds(112, 171, 130, 15);

	}
}
