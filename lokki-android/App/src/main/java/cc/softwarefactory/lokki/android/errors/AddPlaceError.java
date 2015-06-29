package cc.softwarefactory.lokki.android.errors;

import cc.softwarefactory.lokki.android.R;

public enum AddPlaceError {
    PLACE_LIMIT("place_limit_reached", R.string.place_limit_reached),
    NAME_IN_USE("place_name_already_in_use", R.string.place_name_already_in_use);

    private String name;
    private int errorMessage;

    AddPlaceError(String name, int errorMessage) {
        this.name = name;
        this.errorMessage = errorMessage;
    }

    public String getName() {
        return name;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    public static AddPlaceError getEnum(String serverMessage) {
        for (AddPlaceError ape : AddPlaceError.values()) {
            if (ape.getName().equals(serverMessage)) {
                return ape;
            }
        }
        return null;
    }

}
