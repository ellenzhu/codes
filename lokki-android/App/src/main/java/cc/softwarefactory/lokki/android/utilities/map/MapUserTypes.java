package cc.softwarefactory.lokki.android.utilities.map;

public enum MapUserTypes {
    User(0),
    Others(1),
    All(2);
    // 0 = user, 1 = others, 2 = all.

    private int value;

    MapUserTypes(int value) {
        this.value = value;
    }
}
