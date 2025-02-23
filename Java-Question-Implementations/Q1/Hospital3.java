import java.util.Iterator;
import java.util.Objects;

public class Hospital3 extends HospitalBase {

    unsortedLinkedList patientTimeslots;

    public Hospital3() {
        patientTimeslots = new unsortedLinkedList();
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
            patientTimeslots.push(new Node(patient));
            return true;
        }
    }

    @Override
    public Iterator<PatientBase> iterator() {
        //When an iterator is made, performs a merge sort so that the list is ordered when iterating
        patientTimeslots.mergeSort(patientTimeslots.head);
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

    /* Add any extra functions below */
}

class unsortedLinkedList {
    Node head;
    Node tail;

    public unsortedLinkedList () {

    }

    public void changeHead(Node patientNode) {
        head = patientNode;
    }

    public void changeTail(Node patientNode) {
        tail = patientNode;
    }

    public boolean push(Node insertedNode) {

        //Empty list
        if (head == null) {
            changeHead(insertedNode);
            changeTail(insertedNode);
        } else {
            tail.next = insertedNode;
            changeTail(insertedNode);
        }
        return true;

    }

    public int getLength(Node currentHead) {
        //Gets the length of the linked list
        int length = 0;
        Node currentNode = currentHead;
        while(currentNode != null) {
            currentNode = currentNode.next;
            length++;
        }
        return length;
    }

    //Gets the middle node of the linked list
    public Node getMiddleNode(Node head) {
        if (head == null) {
            return head;
        }
        Node currentNode = head;
        int length = getLength(head);
        int halfLength;
        if (length % 2 == 0) {
            //Since the length is even, and it already starts at head, we -1 from
            // the required steps forward to get the middle.
            halfLength = (length/2)-1;
        } else {
            halfLength = length/2;
        }
        //Steps halfway across the linked list
        while (halfLength != 0) {
            currentNode = currentNode.next;
            halfLength--;
        }
        return currentNode;
    }

    public Node mergeSort(Node currentHead) {
        //If head is null or there is only one element, so it is empty, or it is now already sorted.
        if (currentHead == null || currentHead.next == null) {
            return currentHead;
        }

        //Gets the middle node of the linked list, which is the end of the left half.
        Node middleNode = getMiddleNode(currentHead);

        //Retains a reference to node that starts the right half of the list
        Node middleNodeNext = middleNode.next;

        //Breaks the list in half
        middleNode.next = null;

        //Assigns the starts of the left and right halves.
        Node leftHalf = mergeSort(currentHead);
        Node rightHalf = mergeSort(middleNodeNext);

        //Merges the two halves together in order
        return merge(leftHalf, rightHalf);

    }

    //Merges the two linked lists together
    public Node merge(Node a, Node b) {
        Node result;
        //Base cases
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        //Recursively merge both sides together in an ordered fashion.
        if (a.data.compareTo(b.data) <= 0) {
            //So if a is smaller than b, a will be set to result, then the next element in a is compared to that same
            //element in b, and if b is smaller, it will be added next, going recursively to the end of each half
            result = a;
            result.next = merge(a.next, b);
        }
        else {
            result = b;
            result.next = merge(a, b.next);
        }
        return result;

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
