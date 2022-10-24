package cs1302.p2;

import cs1302.adt.StringList;
import cs1302.adt.FancyStringList;
import cs1302.adt.Node;

/**
 * {@code ArrayStringList} is a child class of {@link BaseStringList}
 * that has the specific implementation of methods for arrays.
 */
public class ArrayStringList extends BaseStringList {

    private String[] items;

    /**
     * Constructor to be able to instantiate {@code ArrayStringList} objects.
     */
    public ArrayStringList() {
        items = new String[100]; // Making new array with 100 size
        // Setting all elements to empty string
        for (int i = 0; i < items.length; i++) {
            items[i] = "";
        } // for
        size = 0;
    } // ArrayStringList

    /**
     * Copy constructor of {@code ArrayStringList} objects so it can create
     * deep copies of previous ArrayStringList objects.
     *
     * @param other is the other StringList that is being copied.
     */
    public ArrayStringList(StringList other) {
        this.items = new String[100];
        // Setting all elements to elements in other
        for (int i = 0; i < other.size(); i++) {
            arrayLengthCheck();
            this.items[i] = other.get(i);
            this.size++;
        } // for
    } // ArrayStringList


    /**
     * {@code arrayLengthCheck} checks to see if the array is full or not.
     * If the array is full, then it will add 50% more space to the array.
     */
    private void arrayLengthCheck() {
        if (size == items.length) {
            String[] tempArray = new String[items.length];
            // Temporarily storing elements in items to tempArray
            for (int i = 0; i < items.length; i++) {
                tempArray[i] = items[i];
            } // for

            items = new String[(int) items.length * 2]; // Adds 100% more space to items

            // for loop to assign the established elements to the new items array
            for (int i = 0; i < tempArray.length; i++) {
                items[i] = tempArray[i];
            } // for
        } // if
    } // arrayLengthCheck

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

        // Checking if there is enough space in array
        arrayLengthCheck();

        // Incrementing the size of the string list
        if (index > size) {
            size = index;
        } else {
            size++;
        } // if

        for (int i = items.length - 1; i > index; i--) {
            items[i] = items[i - 1];
        } // for
        items[index] = item;

        return true;
    } // add

    /**
     * {@code clear} removes all items in the string list.
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = "";
        } // for
        size = 0;
    } // clear

    /**
     * {@code get} gets an item from the string list.
     *
     * @param index is the index of the item that is returned from the list.
     */
    @Override
    public String get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } // if
        return items[index];
    } // get

    /**
     * {@code remove} removes an item from a specified index position in
     * the string list. Any items that are after the removed string will be
     * shifted to the left.
     *
     * @param index is the index of the string that is removed.
     * @throws IndexOutOfBoundsException when the provided index is out of range.
     */
    @Override
    public String remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } // if

        String removedString = items[index];
        items[index] = ""; // Removing the item
        // Pulling all items after the removed item 1 space to the left
        String temp = items[index + 1];
        items[index] = temp;
        for (int i = index + 1; i < size; i++) {
            temp = items[i + 1];
            items[i] = temp;
        } // for
        size--; // Decrements size of string list
        return removedString;
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

        ArrayStringList slicedList = new ArrayStringList();

        // Checking for IndexOutOfBoundsException
        if (start < 0 || stop > size() || start > stop) {
            throw new IndexOutOfBoundsException();
        } // if

        String[] slicedArray = new String[stop - start];
        // Setting sliced elements into new slicedArray
        int j = 0;
        for (int i = start; i < stop; i++) {
            slicedArray[j] = items[i];
            j++;
        } // for

        // Putting sliced elements back in items array
        size = stop - start;
        for (int i = 0; i < size; i++) {
            items[i] = slicedArray[i];
        } // for

        // Setting any extra elements in items to empty string
        for (int i = size; i < items.length; i++) {
            items[i] = "";
        } // for

        // Copying items elements into slicedList reference objects
        for (int i = 0; i < size; i++) {
            slicedList.append(this.get(i));
        } // for

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
        // Making new ArrayStringList object to return
        ArrayStringList reversedList = new ArrayStringList();

        // Setting last item of current list to first item of reversed list
        // and iterating all the way to when i = 0
        int j = 0;
        for (int i = this.size() - 1; i >= 0; i--, j++) {
            reversedList.arrayLengthCheck();
            reversedList.items[j] = this.items[i];
            reversedList.size++;
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
        if (start < 0 || stop > size() || start > stop || step < 1) {
            throw new IndexOutOfBoundsException();
        } // if

        StringList slicedList = new ArrayStringList();
        slicedList = this.slice(start, stop); // calling first slice method from start to stop

        ArrayStringList slicedListWithStep = new ArrayStringList();

        for (int i = 0; i < stop - start; i++) {
            // List gets appended when current index is divixiple by step
            if (i % step == 0) {
                slicedListWithStep.append(slicedList.get(i));
            } // if
        } // for

        return slicedListWithStep;
    } // slice
} // ArrayStringList
