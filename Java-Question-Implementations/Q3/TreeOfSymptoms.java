import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

class Symptom extends SymptomBase {

	public Symptom(String symptom, int severity) {
		super(symptom, severity);
	}

	@Override
	public int compareTo(SymptomBase o) {
		Integer severity = getSeverity();
		Integer comparedSeverity = o.getSeverity();
		return severity.compareTo(comparedSeverity);
	}
}

public class TreeOfSymptoms extends TreeOfSymptomsBase {


	public TreeOfSymptoms(SymptomBase root) {
		super(root);
	}

	@Override
	public ArrayList<SymptomBase> inOrderTraversal() {

		//Makes an array list to store the items in order of inorder traversal
		ArrayList<SymptomBase> inorderList = new ArrayList<SymptomBase>();

		//Assign variable for current node, getRoot
		SymptomBase currentNode = getRoot();

		//Creates a stack, this will represent the inorder progress through the branches, popping out the
		//symptoms into the arrayList as you go up through them
		Stack<SymptomBase> symptomStack = new Stack<SymptomBase>();

		//While the current root is not null and the stack isn't empty (hasn't popped out the last symptom in the traversal)
		while (currentNode != null || symptomStack.size() > 0) {
			//Get the current node and pushes down as far left as it can go and adds each element to the stack so the
			//last node in the branch at the top of the stack.
			while (currentNode != null) {
				symptomStack.push(currentNode);
				currentNode = currentNode.getLeft();
			}
			//Pops the top of the stack and assigns it to being the currently looked at node
			currentNode = symptomStack.pop();

			//Adds that node to the inOrder list
			inorderList.add(currentNode);

			//if that node has a right child, move to it and then repeat pushing down
			//as far left as that child's left branch goes (back at the top of the loop)
			currentNode = currentNode.getRight();
		}

		return inorderList;
	}

	@Override
	public ArrayList<SymptomBase> postOrderTraversal() {
		ArrayList<SymptomBase> postorderList = new ArrayList<SymptomBase>();

		SymptomBase currentNode = getRoot();

		//Skipping postOrderTraversal

		return postorderList;
	}

	@Override
	public void restructureTree(int severity) {

		//NOTE: it's probably better to use the post-order traversal for the array here to insert the symptoms more
		// aligning to their original structuring, but I haven't implemented it.

		//Gets the in order traversal of all the items from the original tree
		ArrayList<SymptomBase> symptomList = inOrderTraversal();

		//Gets the index of the greatest severity symptom that is closest to the threshold
		int closestSeverityIndex = findClosestToThreshold(symptomList,severity);

		//Sets new root to the correct symptom
		setRoot(symptomList.get(closestSeverityIndex));

		//For all the elements in the inorder list, will slot them in the left and right halves of the root accordingly
		for (SymptomBase symptom : symptomList) {
			symptom.setLeft(null);
			symptom.setRight(null);
		}

		//Removes the root from the inorder list so that it isn't added as a duplicate later
		symptomList.remove(closestSeverityIndex);

		//Goes through all symptoms and places them left or right of the root according to severity
		for (SymptomBase symptom : symptomList) {
			SymptomBase previousIteratedNode = null;
			SymptomBase iteratedNode = getRoot();
			boolean isLarger = false;

			while (iteratedNode != null) {
				if (symptom.compareTo(iteratedNode) < 0) {
					previousIteratedNode = iteratedNode;
					iteratedNode = iteratedNode.getLeft();
					isLarger = false;
				} else {
					previousIteratedNode = iteratedNode;
					iteratedNode = iteratedNode.getRight();
					isLarger = true;
				}
			}
			if (isLarger) {
				previousIteratedNode.setRight(symptom);
			} else {
				previousIteratedNode.setLeft(symptom);
			}
		}
	}

	//Gets the index of the highest severity symptom with severity closest to the threshold
	private int findClosestToThreshold(ArrayList<SymptomBase> symptomList, int threshold) {
		int closestIndex = 0;

		//Absolute distance between the current item and the threshold
		int difference = Math.abs(symptomList.get(0).getSeverity() - threshold);

		//Goes through the list of symptoms and compares their differences in relation to the threshold with the most
		//suitable symptom's index being set to the closestIndex variable.
		for (int i = 0 ; i < symptomList.size() ; i++) {
			int newDifference = Math.abs(symptomList.get(i).getSeverity() - threshold);
			if (newDifference <= difference) {
				if (symptomList.get(i).compareTo(symptomList.get(closestIndex)) >= 0) {
					closestIndex = i;
					difference = newDifference;
				}
			}
		}
		return closestIndex;

	}
	/* Add any extra functions below */

}
