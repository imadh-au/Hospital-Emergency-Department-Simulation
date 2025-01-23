package project;

public class Physicians {
    private String name;
    private String role; // Intern, Junior, Senior, Registrar, Consultant
    private int maxPatients;
    private int currentPatients;
    private Patient[] assignedPatients;

    public Physicians(String name, String role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Physician name cannot be null or empty.");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Physician role cannot be null or empty.");
        }

        this.name = name;
        this.role = role;
        currentPatients = 0;

        // Determine maxPatients based on the role
        switch (role) {
            case "Intern":
                maxPatients = 2;
                break;
            case "Junior":
                maxPatients = 3;
                break;
            case "Senior":
                maxPatients = 4;
                break;
            case "Registrar":
            case "Consultant":
                maxPatients = 15;
                break;
            default:
                throw new IllegalArgumentException("Invalid role specified for the physician.");
        }

        assignedPatients = new Patient[maxPatients];
    }

    public boolean canTreat() {
        return currentPatients < maxPatients;
    }
    
    public int getmax() {return maxPatients;}
    
    public int getCurrentPatients() {return currentPatients;}
    public String getName() {return name;}
    public String getRole() {return role;}

    public void incrementCurrentPatients() {
        if (!canTreat()) {
            throw new IllegalStateException("Cannot increment beyond maximum patients.");
        }
        currentPatients++;
        //return true;
    }

    public void finishTreatment(Patient p) {
        if (p == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        boolean patientFound = false;
        for (int i = 0; i < currentPatients; ++i) {
            if (assignedPatients[i].equals(p)) {
                patientFound = true;
                // Shift the remaining patients to fill the gap
                for (int j = i; j < currentPatients - 1; ++j) {
                    assignedPatients[j] = assignedPatients[j + 1];
                }
                assignedPatients[--currentPatients] = null;
                break;
            }
        }

        if (!patientFound) {
            throw new IllegalStateException("Patient not found in the assigned list for physician " + name + ".");
        }
    }
}
