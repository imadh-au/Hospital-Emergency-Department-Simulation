package project;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RealGui {
	EmergencyDepartment ed = new EmergencyDepartment();
	protected Shell shell;
	private Text logArea;


	/**
	 * Launch the application.
	 * @param args
	 */
	Display display = Display.getDefault();
	public static void main(String[] args) {
		try {
			RealGui window = new RealGui();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
	
	

	/**
	 * Open the window.
	 */
	public void open() {
		
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Display display = new Display();
	     //Shell shells = new Shell(display);
	     //shell.setText("SWT Clock");
	     //shell.setSize(300, 150);

	     // Create and start the clock
	
	}
	private Clock clock = new Clock();
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
        shell = new Shell();
        shell.setSize(840, 514);
        shell.setText("Emergency Department Management System");
        shell.setLayout(null);
        
        // Add Patient Button
        Button btnAddPatient = new Button(shell, SWT.NONE);
        btnAddPatient.setText("Add Patient");
        btnAddPatient.setBounds(10, 44, 157, 57);
        btnAddPatient.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	 ed.loadAndAllocatePatients("C:\\Users\\HP\\Desktop\\3rd Semester\\OOP\\Projects\\Patient.txt");
            	 
            }
        });

        // Add Physician Button
        Button btnAddPhysician = new Button(shell, SWT.NONE);
        btnAddPhysician.setBounds(10, 126, 157, 57);
        btnAddPhysician.setText("Add Physician");
        btnAddPhysician.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	ed.loadPhysiciansFromFile("C:\\Users\\HP\\Desktop\\3rd Semester\\OOP\\Projects\\Doctors.txt");
            	logArea.append(ed.displayLoadedPhysicians());      
            }
        });
        
        logArea = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
        logArea.setBounds(240, 44, 514, 320);
        logArea.setEditable(false);
        
        Button btnNewButton = new Button(shell, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		try {
        		ed.processWaitingRoom();
        		logArea.append(ed.bedresult());
                ed.assignPatientsToPhysicians();
                logArea.append(ed.bedresult());
                ed.proceedtoNextStage();
                logArea.append(ed.getnextstagelog());
                ed.dischargePatient();
                logArea.append(ed.getdischargelog());
                
                logArea.append("END OF THE SIMULATION");
        		}catch(Exception ec) {
        			showErrorMessage("Error",ec.getMessage());
        		}
        	}
        });
        btnNewButton.setBounds(10, 225, 157, 57);
        btnNewButton.setText("Run Simulation");
        
        
	     
	     Label clockLabel = new Label(shell, SWT.NONE);
	     clockLabel.setBounds(704, 423, 120, 47);
	     clockLabel.setText("New Label");
	     clock.setlabel(clockLabel);
	     clock.start();

	     // Open the shell
	     shell.open();
	     
	     System.out.println(clockLabel.getText());

	     // Main event loop
	     while (!shell.isDisposed()) {
	         if (!display.readAndDispatch()) {
	             display.sleep();
	         }
	     }

	     // Clean up resources
	     clock.interrupt();
	     display.dispose();

        
    }
    
    
    private void showErrorMessage(String title, String message) {
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        messageBox.setText(title);
        messageBox.setMessage(message);
        messageBox.open();
    }

    class Clock extends Thread {
        private Label clockLabel;

        public Clock() {
            
        }
        public void setlabel(Label l) {
        	clockLabel=l;
        	clockLabel.setFont(display.getSystemFont());
        	clockLabel.setText("00:00:00"); // Placeholder for initial text}
        }
   
        @Override
        public void run() {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            try {
                while (!isInterrupted() && !shell.isDisposed()) {
                    String currentTime = timeFormat.format(new Date());
                    display.asyncExec(() -> {
                        if (!shell.isDisposed() && !clockLabel.isDisposed()) {
                            clockLabel.setText(currentTime);
                            shell.layout(); // Refresh layout
                        }
                    });
                    Thread.sleep(1000); // Update every second
                }
            } catch (InterruptedException e) {
                // Thread interrupted; exit gracefully
            }
        }
    }
}