package cs1302.p2;

import cs1302.adt.StringList;
import cs1302.adt.FancyStringList;
import cs1302.adt.Node;

/**
 * {@code BaseStringList} is the abstract class for base String which
 * implements the interface {@link StringList}.
 */
public abstract class BaseStringList implements FancyStringList {

    protected int size;

    /**
     * Constructor for {@code BaseStringList} objects.
     */
    public BaseStringList() {
        size = 0;
    } // BaseStringList

    /**
     * {@code append} appends an item into the {@code size} index.
     *
     * @param item is the item being appended.
     */
    @Override
    public boolean append(String item) {
        boolean appended = add(size, item);
        return appended;
    } // append

    /**
     * {@code isEmpty} checks to see whether the list is empty or not.
     *
     * @return true if the string list has no items
     */
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        } // if else
    } // isEmpty

    /**
     * {@code makeString} makes a string representation of the string list.
     *
     * @param start is the first string in the string list representation.
     * @param sep is the string that separates each string element in the list.
     * @param end is the last string in the string list representation.
     */
    @Override
    public String makeString(String start, String sep, String end) {
        String stringMade = "";
        stringMade += start;

        if (isEmpty()) {
            stringMade += end;
        } else {
            // for loop that prints each element of the string list separated by sep
            for (int i = 0; i < size - 1; i++) {
                stringMade += this.get(i) + sep;
            } // for
            stringMade += this.get(size - 1) + end;
        } // if
        return stringMade;
    } // makeString

    /**
     * {@code prepend} prepends an item to the string list at index 0.
     *
     * @param item is the item that is prepended.
     */
    @Override
    public boolean prepend(String item) {
        boolean prepended = add(0, item);
        return prepended;
    } // prepend

    /**
     * {@code size} is the size of the string list.
     *
     * @return size is the current size of the list.
     */
    @Override
    public int size() {
        return size;
    } // size

    /**
     * {@code toString} prints the makeString method with "[", ", ", "]"
     * for its respective parameters.
     *
     * @return makeString("[", ", ", "]")
     */
    @Override
    public String toString() {
        return makeString("[", ", ", "]");
    } // toString

    /**
     * {@code add} adds a list of {@code items} to the string list at
     * the specified index position.
     *
     * @param index is the index position where hte item is to be inserted.
     * @param items is the StringList that is inserted.
     * @return true when there are no exceptions thrown
     */
    @Override
    public boolean add(int index, StringList items) {
        // Inserting list of items at the index using for loop
        int i = 0;
        int sum = index + items.size();
        for (int j = index; j < sum; i++) {
            this.add(j, items.get(i));
            j++;
        } // for

        return !items.isEmpty();
    } // add

    /**
     * {@code append} appends a list of items into the {@code size} index.
     *
     * @param items is the list of items being appended.
     */
    @Override
    public boolean append(StringList items) {
        // Inserting list of items at the end of the list using for loop
        for (int i = 0; i < items.size(); i++) {
            this.append(items.get(i));
        } // for

        return true;
    } // append

    /**
     * {@code contains} looks for a {@code target} string in the StringList
     * starting from the {@code start} index.
     *
     * @return true when the {@code target} is found in the list.
     */
    @Override
    public boolean contains(int start, String target) {
        if (!(start >= 0)) {
            return false;
        } else {
            // Using a for loop starting from the start index
            // to look for the target String
            boolean isFound = false;
            for (int i = start; i < this.size(); i++) {
                if (target.equals(this.get(i))) {
                    isFound = true;
                } // if
            } // for
            return isFound;
        }
    } // contains

    /**
     * {@code indexOf} loops from start to the end of the list
     * to find the index of the {@code target} string.
     *
     * @return theIndex is the index of the target string.
     */
    @Override
    public int indexOf(int start, String target) {
        int theIndex = -1;
        for (int i = start; i < this.size(); i++) {
            if (target.equals(this.get(i))) {
                theIndex = i;
                break; // breaks when it finds the index where the string equals the target
            } // if
        } //for

        return theIndex;
    } // indexOf

    /**
     * {@code prepend} prepends a list of items to the string list at index 0.
     *
     * @param items is the list of items that is prepended.
     */
    @Override
    public boolean prepend(StringList items) {
        // Inserting list of items at the beginning of the list using a for loop
        for (int i = items.size() - 1; i >= 0; i--) {
            this.prepend(items.get(i));
        } // for

        return true;
    } // prepend

} // BaseStringList
