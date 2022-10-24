package cs1302.p2;

import cs1302.adt.FancyStringList;
import cs1302.adt.StringList;
import cs1302.adt.Node;

/**
 * {@code LinkedStringList} is a child class of {@link BaseStringList}
 * that has the specific implementation of methods for linked lists.
 */
public class LinkedStringList extends BaseStringList {

    private Node head;

    /**
     * Constructor to be able to instantiate {@code LinkedStringList} objects.
     */
    public LinkedStringList() {
        head = new Node("");
        size = 0;
    } // LinkedStringList

    /**
     * Copy constructor of {@code LinkedStringList} objects so it can create
     * deep copies of previous LinkedStringList objects.
     *
     * @param other is the other StringList that is being copied.
     */
    public LinkedStringList(StringList other) {
        // If other is empty, then it will just make an empty list with size 0
        if (other.isEmpty()) {
            this.head = new Node(""); // makes an empty list
            this.size = 0;
        } else if (other.size() == 1) {
            this.head = new Node(other.get(0)); // only copies the head
            this.size++;
        } else {
            this.head = new Node(other.get(0)); // copies head and the rest of the nodes
            this.size++;
            for (int i = 1; i < other.size(); i++) {
                this.append(other.get(i));
            } // for
        } // if-else
    } // LinkedStringList

    /**
     * {@code add} adds an {@code item} to the string list at
     * a specified index position.
     *
     * @param index is the index position where the item is to be inserted.
     * @param item is the String item that is inserted.
     * @return true when there are no exception thrown.
     */
    @Override
    public boolean add(int index, String item) {
        // Checking for exceptions
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        } // if

        if (item == null) {
            throw new NullPointerException();
        } // if

        if (item.equals("")) {
            throw new IllegalArgumentException();
        } // if

        if (index == 0) {
            head = new Node(item, head);
        } else {
            Node newNode = new Node(item);
            Node tempNode = head;
            for (int i = 0; i < index - 1; i++) {
                tempNode = tempNode.getNext();
            } // for
            // Setting newNode's next Node to the node with index + 1
            newNode.setNext(tempNode.getNext());
            // Setting the previous node's next to newNode
            tempNode.setNext(newNode);
        } //if
        size++;
        return true;
    } // add

    /**
     * {@code clear} removes all items in the string list.
     */
    public void clear() {
        head = null;
        size = 0;
    } // clear

    /**
     * {@code get} gets an item from the string list.
     *
     * @param index is the index of the item that is returned from the list.
     * @throws IndexOutOfBoundsException when the provided index is out of range.
     */
    @Override
    public String get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } // if

        Node targetNode = head;
        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNext();
        } // for

        return targetNode.getItem();
    } // get

    /**
     * {@code remove} removes an item from a specified index position in
     * the string list. The item connected after the removed item will be
     * reconnected to the item preceding the removed item.
     *
     * @param index is the index of the string that is removed.
     * @throws IndexOutOfBoundsException when the provided index is out of range.
     */
    @Override
    public String remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } // if

        if (index == 0) {
            // removing first node (head)
            Node firstNode = head;
            String removedItem = firstNode.getItem(); // storing the item to string to return later
            head = head.getNext(); // setting head to the seceding node
            firstNode.setNext(null); // Deleting next reference from the first node
            size--; // Decrementing size

            return removedItem;
        } else {
            Node precedingNode = head;
            Node removeNode = head;
            // Accessing to the intended removed node
            for (int i = 0; i < index; i++) {
                removeNode = removeNode.getNext();
            } // for

            String removedItem = removeNode.getItem(); // This will be returned later

            // Accessing to the Node preceding the intended removedNode
            for (int i = 0; i < index - 1; i++) {
                precedingNode = precedingNode.getNext();
            } // for

            // Connecting the preceding node to the node that comes after the intended removed node
            precedingNode.setNext(removeNode.getNext());
            // Setting removeNode's next to null so it disappears (since nothing is referencing it)
            removeNode.setNext(null);
            size--; // Decrementing the size
            return removedItem;
        } // if

    } // remove

    /**
     * {@code slice} slices the StringList from the {@code start} index
     * (inclusive) to the {@code stop} index (exclusive).
     *
     * @param start is the inclusive starting point of the sliced array.
     * @param stop is the exclusive ending point of the sliced array.
     * @return the sliced StringList from {@code start} (inclusive) to {@code stop} (exclusive).
     * @throws IndexOutOfBoundsException when the provided index is out of range.
     */
    @Override
    public StringList slice(int start, int stop) {
        // Checking for IndexOutOfBoundsException
        if (start < 0 || stop > size() || start > stop) {
            throw new IndexOutOfBoundsException();
        } // if
        // Declaring new reference to new LinkedStringObject where the sliced list will be stored
        LinkedStringList slicedList = new LinkedStringList();
        /*
         * If the size after slice is still the whole array, then just each item to
         * a new reference and return that reference.
         * Else it will remove the extraneous elements after the slice and return the
         * edited reference.
         */
        if (start == 0 && stop == size()) {
            for (int i = size - 1; i >= 0; i--) {
                slicedList.prepend(this.get(i));
            } // for
        } else if (start == 0) {
            // Removing Nodes at stop index and after
            //Accessing the node right before the stop index
            Node precedingStopNode = head;
            for (int i = 0; i < stop - 1; i++) {
                precedingStopNode = precedingStopNode.getNext();
            } // for
            // Breaking the link of the precedingStopNode
            precedingStopNode.setNext(null);
            size = stop - start;
            // Copying all nodes to slicedList's object reference
            for (int i = size - 1; i >= 0; i--) {
                slicedList.prepend(this.get(i));
            } // for
        } else {
            // Removing nodes at the stop index and after first
            //Accessing the node right before the stop index
            Node precedingStopNode = head;
            for (int i = 0; i < stop - 1; i++) {
                precedingStopNode = precedingStopNode.getNext();
            } // for
            // Breaking the link of the precedingStopNode
            precedingStopNode.setNext(null);
            // Removing the nodes before the start index
            Node precedingStartNode = head;
            // Accessing the node right before the node with the start index
            for (int i = 0; i < start - 1; i++) {
                precedingStartNode = precedingStartNode.getNext();
            } // for
            head = precedingStartNode.getNext(); // Setting head to the start index node
            precedingStartNode.setNext(null); // Setting the preceding node's next to null
            size = stop - start;
            // Copying all nodes to slicedList's object reference
            for (int i = size - 1; i >= 0; i--) {
                slicedList.prepend(this.get(i));
            } // for
        } // if
        return slicedList;
    } // slice

    /**
     * {@code reverse} reverses the order of the strings in the list.
     *
     * @return reversedList is the new {@code FancyStringList} object with the
     * reversed objects.
     */
    @Override
    public FancyStringList reverse() {
        LinkedStringList reversedList = new LinkedStringList();

        Node currentNode = this.head;
        // Setting last node of current list to the first item
        // of the new list
        for (int i = 0; i < this.size(); i++) {
            reversedList.prepend(this.get(i));
            currentNode = currentNode.getNext();
        } // for

        return reversedList;
    } // reverse

    /**
     * {@code slice} returns the new {@code FancyStringList} when where the list
     * starts (inclusive) to where it stops (exclusive) and adds intervals based on
     * the {@code step} size.
     *
     * @param start is where the sliced list starts (inclusive).
     * @param stop is where the sliced list stops (exclusive).
     * @param step is step size amount between each String element.
     * @return slicedList is the new sliced list.
     * @throws IndexOutOfBoundsException when the parameters don't meet the criteria.
     */
    @Override
    public FancyStringList slice(int start, int stop, int step) {

        StringList slicedList = new LinkedStringList();
        slicedList = this.slice(start, stop); // Calling first slice method from start to stop

        LinkedStringList slicedListWithStep = new LinkedStringList();

        for (int i = 0; i < stop - start; i++) {
            if (i % step == 0) {
                // if the index mod step is 0, then append it to the new list
                slicedListWithStep.append(slicedList.get(i));
            } // if
        } // for

        return slicedListWithStep;
    } // slice

} // LinkedStringList
