package exceptions;

public class TowerException extends Exception {
    public static String OVERFLOW(int index, boolean isCup) {
        return String.format("Inserting the new item (index: %d | isCup: %b) will result in an overflow of the tower.", index, isCup);
    }

    public static String IMPOSSIBLE_SWAP(int index1, boolean isCup1, int index2, boolean isCup2) {
        return String.format("Cannot swap the given items ((index: %d | isCup: %b) with (index: %d | isCup: %b)) because the resulting order will overflow the tower.", index1, isCup1, index2, isCup2);
    }

    public static String SWAP_INSIDE_LIDDED_CUP(int index, boolean isCup, int containerIndex) {
        return String.format("Cannot swap because one of the items (index: %d | isCup: %b) is enclosed by a lidded cup (index: %d).", index, isCup, containerIndex);
    }

    public static String INVALID_PUSH_FEARFUL(int index) {
        return String.format("Cannot push fearful lid (index: %d) because its cup does not belong to the tower.", index);
    }

    public static String REMOVING_NOT_REMOVABLE_ITEM(int index, boolean isCup, String type) {
        return String.format("Cannot remove an item that is not removable (index: %d | isCup: %b | type: %s).", index, isCup, type);
    }

    public static String IMPOSSIBLE_COVER(int index) {
        return String.format("Cannot cover the cup (index: %d) because it will cause an overflow.", index);
    }

    public TowerException(String message) {
        super(message);
    }
}

