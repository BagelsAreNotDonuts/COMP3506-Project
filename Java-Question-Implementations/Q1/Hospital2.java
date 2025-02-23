import java.util.Iterator;
import java.util.Objects;

public class Hospital2 extends HospitalBase {

    priorityLinkedList2 patientTimeslots;
    public Hospital2() {
        patientTimeslots = new priorityLinkedList2();
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        //Filters the input based on if its' within the specified timeframe then adds patient
        String[] time = patient.getTime().split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        if ( 59 < minute || minute < 0 ) {
            return false;
        }
        if (hour < 8 || hour >= 18 || (patient.getTime().compareTo("12:00") >= 0
                && patient.getTime().compareTo("13:00") < 0)) {
            return false;
        } else {
            return patientTimeslots.insert(new Node(patient));
        }
    }

    @Override
    public Iterator<PatientBase> iterator() {
        return new Iterator<PatientBase>() {
            Node currentNode = patientTimeslots.head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public PatientBase next() {
                if (hasNext()) {
                    PatientBase data = currentNode.data;
                    currentNode = currentNode.next;
                    return data;
                }
                return null;
            }
        };

    }

    public priorityLinkedList2 getPatientTimeslots() {
        return this.patientTimeslots;
    }

    /* Add any extra functions below */


}


class priorityLinkedList2 {
    Node head;
    Node tail;

    public priorityLinkedList2 () {

    }

    public void changeHead(Node patientNode) {
        head = patientNode;
    }

    public void changeTail(Node patientNode) {
        tail = patientNode;
    }

    public boolean insert(Node insertedNode) {

        //Empty list
        if (head == null) {
            changeHead(insertedNode);
            changeTail(insertedNode);
            return true;
        }
        //Inserted node is smaller than head
        if (insertedNode.data.compareTo(head.data) < 0) {
            if (head == tail) {
                changeTail(head);
            }
            insertedNode.next = head;
            changeHead(insertedNode);
            return true;
        }

        //Inserted node is bigger or equal to the tail. Duplicates are added after.
        if (insertedNode.data.compareTo(tail.data) >= 0) {
            tail.next = insertedNode;
            changeTail(insertedNode);
            return true;
        }

        Node currentNode = head;
        while (currentNode != null) {
            //Inserted patient has bigger or equal time compared to current smaller than next. Duplicates are added after.
            if (insertedNode.data.compareTo(currentNode.data) >= 0 && insertedNode.data.compareTo(currentNode.next.data) < 0) {
                insertedNode.next = currentNode.next;
                currentNode.next = insertedNode;
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;

    }

    public boolean printList() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        return false;
    }


}


