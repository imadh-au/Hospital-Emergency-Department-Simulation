package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.time.LocalTime;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Random;

public class EmergencyDepartment {
    private Bed[] beds;
    private Patient[] patients;
    private Physicians[] physicians;
    private int totalBeds;
    private int corridorPositions;
    private int reclinerChairs;
    private int patientCount;
    private int physicianCount;
    private PriorityQueue<Patient> waitingRoom;

    public EmergencyDepartment(int bedCount, int corridorPos, int reclinerChairsCount) {
        if (bedCount <= 0 || corridorPos < 0 || reclinerChairsCount < 0) {
            throw new IllegalArgumentException("Invalid parameters for EmergencyDepartment initialization.");
        }

        this.totalBeds = bedCount;
        this.corridorPositions = corridorPos;
        this.reclinerChairs = reclinerChairsCount;

        this.beds = new Bed[bedCount];
        this.patients = new Patient[100]; // Maximum of 100 patients
        this.physicians = new Physicians[100]; // Maximum of 100 physicians
        this.patientCount = 0;
        this.physicianCount = 0;

        this.waitingRoom = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getTriage(), p2.getTriage()));

        try {
            // Initialize beds with different types
            for (int i = 0; i < totalBeds; ++i) {
                if (i < 5)
                    beds[i] = new Bed("Resuscitation");
                else if (i < 15)
                    beds[i] = new Bed("Acute");
                else
                    beds[i] = new Bed("Subacute");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize beds: " + e.getMessage(), e);
        }
    }
    
    public int physicianCount() {
        return physicianCount;  // Returns the number of physicians
    }
    

    public int getWaitingRoomSize() {
        return waitingRoom.size();  // Returns the size of the waiting room (queue)
    }

    public EmergencyDepartment() {
        this(24, 13, 3);
    }

    public void addPhysician(Physicians physician) {
        if (physician == null) {
            throw new IllegalArgumentException("Physician cannot be null.");
        }
        if (physicianCount >= physicians.length) {
            throw new IllegalStateException("Cannot add more physicians. Maximum capacity reached.");
        }
        physicians[physicianCount++] = physician;
    }

    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        if (patientCount >= patients.length) {
            throw new IllegalStateException("Cannot add more patients. Maximum capacity reached.");
        }
        patients[patientCount++] = patient;
        waitingRoom.offer(patient);
    }

    public int getPatientCount() {return patientCount;}

    public int getPhysicianCount() {return physicianCount;}

    public boolean canAllocateBed(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        for (int i = 0; i < totalBeds; i++) {
            if (beds[i].isBedAvailable()) {
                if (beds[i].getType().equals("Resuscitation") && patient.getTriage() == 1) {
                    return true;
                }
                if ((beds[i].getType().equals("Acute") || beds[i].getType().equals("Minor Procedure")) &&
                        patient.getTriage() >= 2 && patient.getTriage() <= 5) {
                    return true;
                }
                if (beds[i].getType().equals("Subacute") && patient.getTriage() >= 3 && patient.getTriage() <= 5) {
                    return true;
                }
            }
        }
        return false;
    }
    
    StringBuilder resultbed = new StringBuilder();
    public void allocateBed(Patient patient) {
    	 if (patient == null) {
    	        throw new IllegalArgumentException("Patient cannot be null.");
    	    }

    	    try {
    	        for (int i = 0; i < totalBeds; i++) {
    	            if (beds[i].isBedAvailable() && canAllocateBed(patient)) {
    	                beds[i].allocateBed(patient);
    	                totalBeds--;
    	                resultbed.append("Patient ID ").append(patient.getId())
    	                      .append(" allocated to ").append(beds[i].getType())
    	                      .append(" bed.\n");
    	                return;
    	            }
    	        }

    	        if (corridorPositions > 0) {
    	            corridorPositions--;
    	            resultbed.append("Patient ID ").append(patient.getId())
    	                  .append(" allocated to corridor position.\n");
    	        } else if (reclinerChairs > 0) {
    	            reclinerChairs--;
    	            resultbed.append("Patient ID ").append(patient.getId())
    	                  .append(" allocated to recliner chair.\n");
    	        } else {
    	            resultbed.append("Patient ID ").append(patient.getId())
    	                  .append(" remains waiting.\n");
    	        }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to allocate a bed for patient ID " + patient.getId() + ": " + e.getMessage(), e);
        }
    }
    public String bedresult() {
    	return resultbed.toString();
    }
    public void processWaitingRoom() {
        while (!waitingRoom.isEmpty()) {
            Patient nextPatient = waitingRoom.poll();
            allocateBed(nextPatient);
            //System.out.println("Initial physician consult for Patient ID " + nextPatient.getId());
        }
    }
    
    StringBuilder resultdischarge = new StringBuilder();
    
    public void dischargePatient() {
    	Patient patient=new Patient();
        String[] Decisions= {"Transfer in Alternate Hospital","Home","Observation Unit","Inpatient bed"}; 
        Random rand=new Random();
        int temp;
        boolean found = false;
        try {
            // Remove patient from the list
            for (int i = 0; i < patientCount; i++) {
                if (patients[i] == null) {
                    throw new IllegalArgumentException("Patient cannot be null.");
                }

                patient = patients[i];
                found = true;
                temp = rand.nextInt(Decisions.length);
                patients[i].DischargeDecision(Decisions[temp]);

                // Write patient data to file
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\HP\\Desktop\\3rd Semester\\OOP\\Projects\\patients_data.txt", true))) {
                    writer.write("ID: " + patient.getId());
                    writer.newLine();
                    writer.write("Arrival Time: " + patient.getArrivalTime());
                    writer.newLine();
                    writer.write("Triage Category: " + patient.getTriage());
                    writer.newLine();
                    writer.write("Treatment Time: " + patient.getTreatmentTime());
                    writer.newLine();
                    writer.write("Discharge Time: " + patient.getDischargeTime());
                    writer.newLine();
                    writer.write("Physician Name: " + patient.getPhysicianName());
                    writer.newLine();
                    writer.write("Post-Discharge Decision Time: " + patient.getPDDT());
                    writer.newLine();
                    writer.write("Next Step: " + patient.getnextstep());
                    writer.newLine();
                    writer.write("Discharge Decision: " + patient.getDischargeDecision());
                    writer.newLine();
                    writer.write("Interarrival Time: " + patient.getinterarrivaltime());
                    writer.newLine();
                    writer.write("----------------------------------------");
                    writer.newLine();
                }
                
                if (temp == 3) {
                    // Shift patients in the array
                    for (int j = i; j < patientCount - 1; j++) {
                        patients[j] = patients[j + 1];
                    }
                    patients[--patientCount] = null; // Decrease count and clear the last slot

                    // Adjust the loop index to prevent skipping
                    i--;

                    // Disallocate bed if applicable
                    for (int k = 0; k < totalBeds; k++) {
                        if (beds[k].isBedAvailable() && beds[k].getAllocatedPatient().equals(patient)) {
                            beds[k].disallocateBed();
                            break;
                        }
                    }

                    resultdischarge.append("Patient ID ").append(patient.getId())
                    .append(" Discharge Reason: ").append(patient.getDischargeDecision())
                    .append("\n");
                } else {
                	resultdischarge.append("Patient ID ").append(patient.getId())
                     .append(" Discharge Reason: ").append(patient.getDischargeDecision())
                     .append("\n");
                }
                
                
            }

            if (!found) {
            	resultdischarge.append("Patient not found in the system.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultdischarge.append("Error: Failed to write patient data: ").append(e.getMessage()).append("\n");
            throw new IllegalStateException("Failed to write patient data: " + e.getMessage(), e);
        } catch (Exception e) {
        	resultdischarge.append("Error: Failed to discharge patient: ").append(e.getMessage()).append("\n");
            throw new IllegalStateException("Failed to discharge patient ID " + (patient != null ? patient.getId() : "unknown") + ": " + e.getMessage(), e);
        }


    }
    public String getdischargelog() {
    	return resultdischarge.toString();
    }

    public String displayStatus() {
        String result;
        try {
            result = "Total Beds: " + totalBeds + "\nCorridor Positions Available: " + corridorPositions + "\nRecliner Chairs Available: " + reclinerChairs + "\nTotal Physicians: " + physicianCount + "\nTotal Patients: " + patientCount;
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to display status: " + e.getMessage(), e);
        }
    }

    public void loadAndAllocatePatients(String filePath) {
    	LocalTime currentTime = LocalTime.now();

        // Extract hours and minutes
        int hours = currentTime.getHour();   // Get the hour (0-23)
        int minutes = currentTime.getMinute();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String disease;
            while ((disease = br.readLine()) != null) {
                // Create a new Patient for each disease
            	
                Patient patient = new Patient(new Time(minutes,hours), disease);
                patient.calculateInterArrivalTime();
                
                addPatient(patient);
               // System.out.println(patient.getinterarrivaltime());
            }
            System.out.println("Patients loaded successfully.");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load diseases from file: " + e.getMessage(), e);
        }
    }
    
    public void loadPhysiciansFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse the line in the format "Dr. John Smith - Junior"
                String[] parts = line.split(" - ");
                if (parts.length != 2) {
                    System.out.println("Invalid format in physician file: " + line);
                    continue;
                }

                String name = parts[0].trim();
                String role = parts[1].trim();

                // Create a new physician and add to the array
                Physicians physician = new Physicians(name, role);
                addPhysician(physician);
            }
            System.out.println("Physicians loaded successfully.");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load physicians from file: " + e.getMessage(), e);
        }
    }
    
    public String displayLoadedPhysicians() {
        StringBuilder result = new StringBuilder("Loaded Physicians:\n");
        
        if (physicians == null || physicians.length == 0) {
            result.append("No physicians available.");
        } else {
            for (int i = 0; i < physicians.length; i++) {
                if (physicians[i] != null) {
                    result.append("Physician Name: ").append(physicians[i].getName())
                          .append(", Role: ").append(physicians[i].getRole()).append("\n");
                }
            }
        }
        
        return result.toString();
    }
    
    StringBuilder assignpatienttophysicianlog = new StringBuilder();
    public void assignPatientsToPhysicians() {
        int physicianCount = physicians.length;
        if (physicianCount == 0) {
            throw new IllegalStateException("No physicians available to assign.");
        }

        Random rand = new Random();
        int physicianIndex = 0;

        for(int i=0;i<100;i++) {  // This should check if the queue is not empty
            Patient patient = patients[i];
            Physicians assignedPhysician = physicians[physicianIndex];

            String physicianName = assignedPhysician.getName();
            String physicianRole = assignedPhysician.getRole();
            
            // Dynamically check the physician's multitasking capabilities
            boolean canTreat = false;
            if (assignedPhysician.getRole().equals("Intern")) {
                // Intern can see 1-2 patients simultaneously
                canTreat = assignedPhysician.canTreat();
            } else if (assignedPhysician.getRole().equals("Junior")) {
                // Junior can see 2-3 patients simultaneously
                canTreat = assignedPhysician.canTreat();
            } else if (assignedPhysician.getRole().equals("Senior")) {
                // Senior can see 2-4 patients simultaneously
                canTreat = assignedPhysician.canTreat();
            } else if (assignedPhysician.getRole().equals("Registrar") || assignedPhysician.getRole().equals("Consultant")) {
                // Registrar/Consultant can handle up to 5 patients simultaneously
                canTreat = assignedPhysician.canTreat();
            }
            if ((patient.getTriage() == 1 || patient.getTriage() == 2) &&
                    !(assignedPhysician.getRole().equals("Registrar") || assignedPhysician.getRole().equals("Consultant"))) {
                    canTreat=false;
                }

            // If the current physician cannot treat, select another physician randomly
            while (!canTreat) {
                physicianIndex = rand.nextInt(80);  // Retry with a new random index
                assignedPhysician = physicians[physicianIndex];
                
                physicianName = assignedPhysician.getName();
                physicianRole = assignedPhysician.getRole();

                // Recheck multitasking capabilities
                if (assignedPhysician.getRole().equals("Intern")) {
                    canTreat = assignedPhysician.canTreat() && assignedPhysician.getCurrentPatients() < 2;
                } else if (assignedPhysician.getRole().equals("Junior")) {
                    canTreat = assignedPhysician.canTreat() && assignedPhysician.getCurrentPatients() < 3;
                } else if (assignedPhysician.getRole().equals("Senior")) {
                    canTreat = assignedPhysician.canTreat() && assignedPhysician.getCurrentPatients() < 4;
                } else if (assignedPhysician.getRole().equals("Registrar") || assignedPhysician.getRole().equals("Consultant")) {
                    canTreat = assignedPhysician.canTreat() && assignedPhysician.getCurrentPatients() < 5;
                }
                if ((patient.getTriage() == 1 || patient.getTriage() == 2) &&
                        !(assignedPhysician.getRole().equals("Registrar") || assignedPhysician.getRole().equals("Consultant"))) {
                        canTreat=false;
                    }
            }

            // Once a valid physician is found, assign the patient
            
            
            patient.assignPhysician(assignedPhysician);
            if(assignedPhysician.getCurrentPatients()!=assignedPhysician.getmax()) {
                assignedPhysician.incrementCurrentPatients();
            }
            
            assignpatienttophysicianlog.append("Assigned patient ID: ").append(patient.getId())
            .append(" to physician ").append(physicianName)
            .append(" (").append(physicianRole).append(")\n");

            // Move to the next physician for the next patient
            physicianIndex = (physicianIndex + 1) % physicianCount;
        }
    }
    public String getassignlog() {
    	return assignpatienttophysicianlog.toString();
    }
    
    StringBuilder nextstagelog = new StringBuilder();
    public void proceedtoNextStage() {
    	String[] Steps= {"Nursing Procudure","Treatment/Procedures","Imaging&Laborarotary Bodies","Senior Physician consult"};
    	
    	Random rand = new Random();
    	 for (int i = 0; i < patientCount; i++) {
    		 int x= rand.nextInt(patientCount);
    		 if(x<=i||x>=i-20) {
    			 int y=rand.nextInt(Steps.length);
    			 patients[i].setnextstep(Steps[y]);
    		 }
    		 patients[i].proceedToNextStage();
    		 nextstagelog.append(patients[i].getresult());
    	 } 
    }
    
    public String getnextstagelog() {
    	return nextstagelog.toString();
    }


    
}
