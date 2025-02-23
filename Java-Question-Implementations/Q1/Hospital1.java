import java.util.Iterator;
import java.util.Objects;

public class Hospital1 extends HospitalBase {

    //Member variable
    priorityLinkedList patientTimeslots;

    public Hospital1() {
        patientTimeslots = new priorityLinkedList();
    }

    @Override
    public boolean addPatient(PatientBase patient) {

        //Filters the input based on if its' in 20 minute intervals and within the specified timeframe then adds patient
        String[] time = patient.getTime().split(":");
        int minutes = Integer.parseInt(time[1]);
        int hour = Integer.parseInt(time[0]);
        if (minutes % 20 != 0 || 59 < minutes || minutes < 0) {
            return false;
        } else if (hour < 8 || hour >= 18 || (patient.getTime().compareTo("12:00") >= 0
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

    //Function that checks a passed patient to see if its' within the data structure

    /* Add any extra functions below */

}

class priorityLinkedList {
    Node head;
    Node tail;

    public priorityLinkedList() {

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

        //Inserted node is bigger than the tail
        if (insertedNode.data.compareTo(tail.data) > 0) {
            tail.next = insertedNode;
            changeTail(insertedNode);
            return true;
        }

        Node currentNode = head;
        while (currentNode != null) {
            //Inserted patient has bigger time than current smaller than next.
            if (insertedNode.data.compareTo(currentNode.data) > 0 && insertedNode.data.compareTo(currentNode.next.data) < 0) {
                insertedNode.next = currentNode.next;
                currentNode.next = insertedNode;
                return true;
                //Duplicate timeslot not allowed
            } else if (insertedNode.data.compareTo(currentNode.data) == 0 ){
                return false;
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

//A single node in a linked list. Contains the data of a patient and the pointer to the node that is after it.
class Node {
    PatientBase data;
    Node next;

    public Node(PatientBase data) {
        this.data = data;
    }
}


