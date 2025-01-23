package project;

public class Bed {
	    private String type;
	    private boolean available;
	    private Patient allocatedPatient;

	    public Bed(String type) {
	        if (type == null || type.isEmpty()) {
	            throw new IllegalArgumentException("Bed type cannot be null or empty.");
	        }
	        this.type = type;
	        available = true;
	        allocatedPatient = null;
	    }

	    public String getType() {return type;}

	    public boolean isBedAvailable() {
	        return available;
	    }

	    public void allocateBed(Patient patient) {
	        if (!available) {
	            throw new IllegalStateException("Bed is already allocated.");
	        }
	        if (patient == null) {
	            throw new IllegalArgumentException("Cannot allocate bed to a null patient.");
	        }
	        
	        allocatedPatient = patient;
	        available = false;
	        //System.out.println("Bed of type " + type + " allocated to patient ID " + patient.getId());
	    }

	    public void disallocateBed() {
	        if (available) {
	            throw new IllegalStateException("Bed is already available and cannot be disallocated.");
	        }
	        System.out.println("Bed of type " + type + " disallocated from patient ID " + allocatedPatient.getId());
	        this.allocatedPatient = null;
	        this.available = true;
	    }

	    public Patient getAllocatedPatient() {
	        if (available) {
	            throw new IllegalStateException("No patient is currently allocated to this bed.");
	        }
	        return allocatedPatient;
	    }
}
