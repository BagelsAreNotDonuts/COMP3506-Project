public class Patient extends PatientBase {

    public Patient(String name, String time) {
        super(name, time);
    }

    @Override
    public int compareTo(PatientBase o) {
        return this.getTime().compareTo(o.getTime());
    }

    /* Add any extra functions below */
}
