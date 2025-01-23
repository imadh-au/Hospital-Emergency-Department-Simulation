package project;

public class Patient {
	 	private static int nextId = 1; // Static variable for auto-incrementing ID
	    private int id;
	    private Time arrivalTime;
	    private int triageCategory;
	    private Time treatmentTime;
	    private Time dischargeTime;
	    private String physicianName;
	    private double pddt; // Post-Discharge Decision Time
	    private String nextstep;
	    private String DischargeDecision;
	    private double interarrivalTime;

	public Patient(Time arrivalTime, String condition) {
	     if (arrivalTime == null) {
	         throw new IllegalArgumentException("Arrival time cannot be null.");
	     }

	     id = nextId++; // Auto-increment ID
	     this.arrivalTime = arrivalTime;
	     triageCategory = determineTriageCategory(condition);
	     treatmentTime = calculateTreatmentTime();
	     dischargeTime = null;
	     physicianName = null;
	     pddt = calculatePDDT();
	     nextstep=null;
	    }

    public Patient() {
    	id = 0;
        arrivalTime = new Time();
        treatmentTime = new Time();
        triageCategory = 0;
        physicianName = null;
        pddt = 0;
    }
    private int determineTriageCategory(String condition) {
        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition cannot be null or empty.");
        }

        switch (condition.toLowerCase()) {
            case "multitrauma":
            case "cardiac/vascular":
                return 1; // Highest priority

            case "blood/immune":
            case "neurological":
                return 2;

            case "gastrointestinal":
                return 3; // Example: Based on Weibull distribution (scale=180, shape=0.914)

            case "injury":
            case "paediatric":
                return 4;

            case "nonemergent review":
            case "eye":
                return 5; // Lowest priority

            default:
                throw new IllegalArgumentException("Unknown condition: " + condition);
        }
    }
    
    public Time calculateTreatmentTime() {
    	int mins;
        switch (triageCategory) {
            case 1:
                mins = (int)StatisticalUtils.generatePearsonVI(1,355,1.64,5.72); // Example parameters
                break;
            case 2:
                mins = (int)StatisticalUtils.generatePearsonVI(2,400,1.5,5.5);
                break;
            case 3:
                mins = (int)StatisticalUtils.generatePearsonVI(3,370,1.75,6.0);
                break;
            case 4:
                mins = (int)StatisticalUtils.generatePearsonVI(4,360,1.7,5.8);
                break;
            case 5:
                mins = (int)StatisticalUtils.generatePearsonVI(5,380,1.55,5.65);
                break;
            default:
                throw new IllegalStateException("Unexpected triage category: " + triageCategory);
        }
        treatmentTime=arrivalTime.addMin(mins);
        treatmentTime.sethours(treatmentTime.getmins());
        treatmentTime.setmins(treatmentTime.gethours());
        return treatmentTime;
    }

    public void calculateInterArrivalTime() {
        double minutes;
        switch (triageCategory) {
            case 1:
                minutes =  StatisticalUtils.generateWeibull(1,180, 0.914); // Example parameters
                break;
            case 2:
                minutes = StatisticalUtils.generateWeibull(2,200, 0.914);
                break;
            case 3:
                minutes = StatisticalUtils.generateWeibull(3,240, 0.914);
                break;
            case 4:
                minutes = StatisticalUtils.generateWeibull(3,280, 0.914);
                break;
            case 5:
                minutes = StatisticalUtils.generateWeibull(5,320, 0.914);
                break;
            default:
                throw new IllegalStateException("Unexpected triage category: " + triageCategory);
                
        }
        interarrivalTime=minutes;
        int minute=(int)minutes;
        arrivalTime.addMin(minute);
    }
    
    
    public double getinterarrivaltime() {return interarrivalTime;}
    public String getnextstep() {return nextstep;}
    public String getTreatmentTime() { 
    	String result=treatmentTime.display();
    	return result;
    }
    
    private double calculatePDDT() {
        return StatisticalUtils.generateExponential(triageCategory,156);
    }

    public void setDischargeTime() {
        if (arrivalTime == null || treatmentTime == null) {
            throw new IllegalStateException("Arrival time or treatment time cannot be null when setting discharge time.");
        }
        dischargeTime = arrivalTime.add(treatmentTime);
        
    }

    public void assignPhysician(Physicians physician) {
        if (physician == null) {
            throw new IllegalArgumentException("Physician cannot be null.");
        }

        // Check if the physician can treat the patient based on triage category

        // Check if the physician has capacity
        if (!physician.canTreat()) {
            throw new IllegalStateException("Physician " + physician.getName() + " cannot handle more patients.");
        }
        // Assign physician and update their current patient count
        this.physicianName = physician.getName();
        physician.incrementCurrentPatients();
    }

    public String getPhysicianName() {return physicianName;}
    public int getId() {return id;}
    public int getTriage() {return triageCategory;}
    public String getArrivalTime() {
    	String result=arrivalTime.display();
    	return result;
    }
   // public void getTreatmentTime() {treatmentTime.display();}

    public String getDischargeTime() {
    	setDischargeTime();
        if (dischargeTime == null) {
            throw new IllegalStateException("Discharge time has not been set.");
        }
        String result=dischargeTime.display();
    	return result;
        
    }

    public double getPDDT() {return pddt;}
    
    public String PatientData() {
    	return "Patient Id: "+id+"\n Arrival Time: "+arrivalTime+"\n Triage: "+triageCategory+"\nTreatment Time: "+treatmentTime+"Discharge Time: "+dischargeTime+"\nPDDT: "+pddt;
    }
    
    public void setnextstep(String step) {
    	nextstep=step;
    }
    public void DischargeDecision(String DischargeDecision) {
    	this.DischargeDecision=DischargeDecision;
    }
    public String getDischargeDecision() {return DischargeDecision;}
    
    StringBuilder result=new StringBuilder();
    
    public void proceedToNextStage() {
        if (id <= 0) {
            throw new IllegalStateException("Patient ID is not valid.");
        }
        if(nextstep!=null) {
        	result.append("Next Step: ").append(nextstep).append("\n");
        }
        result.append("Patient ID ").append(id).append(" proceeding to the next treatment stage.\n");
    }
    public String getresult() {
    	return result.toString();
    }
}
