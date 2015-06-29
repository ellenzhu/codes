package cc.softwarefactory.lokki.android.espresso;

import android.content.Context;
import android.support.test.espresso.action.ViewActions;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;

import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.datasources.contacts.ContactDataSource;
import cc.softwarefactory.lokki.android.espresso.utilities.MockJsonUtils;
import cc.softwarefactory.lokki.android.espresso.utilities.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class AddContactsScreenTest extends LoggedInBaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setMockContacts();
        enterContactsScreen();
    }



    private void setMockContacts() throws JSONException {
        ContactDataSource mockContactDataSource = Mockito.mock(ContactDataSource.class);
        JSONObject testJSONObject = new JSONObject(MockJsonUtils.getContactsJson());
        when(mockContactDataSource.getContactsJson(any(Context.class))).thenReturn(testJSONObject);
        getActivity().setContactUtils(mockContactDataSource);
    }

    private void enterContactsScreen() {
        TestUtils.toggleNavigationDrawer();
        onView(withText("Contacts")).perform(click());
    }

    private void enterAddContactsScreen() {
        onView(withId(R.id.add_people)).perform(click());
    }

    private void openAddContactDialog() {
        onView(withId(R.id.allow_people)).perform(click());
    }

    private void addContactFromContactListScreen(String clickableText, String email) {
        onView(withText(clickableText)).perform(click());

        String message = getResources().getString(R.string.add_contact_dialog_save, email);
        onView(withText(message)).check(matches(isDisplayed()));

        onView(withText(R.string.ok)).perform(click());

        onView(withText(clickableText)).check(doesNotExist());
    }

    // TEST

    public void testContactListScreenIsDisplayed() {
        onView(withText(R.string.can_see_me)).check(matches(isDisplayed()));
        onView(withText(R.string.i_can_see)).check(matches(isDisplayed()));
    }

    public void testOpenAddContactsScreen() {
        enterAddContactsScreen();
        openAddContactDialog();
        onView(withText(R.string.add_contact)).check(matches(isDisplayed()));
        onView(withHint(R.string.contact_email_address)).check(matches(isDisplayed()));
        onView(withText(R.string.ok)).check(matches(isDisplayed()));
        onView(withText(R.string.cancel)).check(matches(isDisplayed()));
    }

    public void testSeeAnyContactOnAddScreen() {
        onView(withId(R.id.add_people)).perform(click());
        onView(withText("Family Member")).check(matches(isDisplayed()));
    }

    public void testAddingSingleContact() {
        enterAddContactsScreen();

        String contactName = "Family Member";
        String contactEmail = "family.member@example.com";
        addContactFromContactListScreen(contactName, contactEmail);

    }

    public void testCancelingSingleContactAdding() {
        enterAddContactsScreen();

        String contactEmail = "family.member@example.com";

        onView(withText(contactEmail)).perform(click());
        onView(withText(R.string.cancel)).perform(click());

        onView(withText(contactEmail)).check(matches(isDisplayed()));
    }

    public void testAddingTwoContactsUsingName() {
        enterAddContactsScreen();

        String firstContactName = "Test Friend";
        String firstContactEmail = "test.friend@example.com";
        String secondContactName = "Family Member";
        String secondContactEmail = "family.member@example.com";

        addContactFromContactListScreen(firstContactName, firstContactEmail);
        addContactFromContactListScreen(secondContactName, secondContactEmail);
    }

    public void testAddingTwoContactsUsingEmail() {
        enterAddContactsScreen();

        String firstContactEmail = "test.friend@example.com";
        String secondContactEmail = "family.member@example.com";

        addContactFromContactListScreen(firstContactEmail, firstContactEmail);
        addContactFromContactListScreen(secondContactEmail, secondContactEmail);
    }

    public void testAddingEmptyContact() {
        enterAddContactsScreen();
        openAddContactDialog();
        onView(withText(R.string.ok)).perform(click());
        onView(withHint(R.string.contact_email_address)).check(matches(isDisplayed()));
    }

    public void testAddingCustomContact() {
        enterAddContactsScreen();
        openAddContactDialog();
        onView(withHint(R.string.contact_email_address)).perform(typeText("test@example.com"));
        onView(withText(R.string.ok)).perform(click());
    }

    public void testBackButtonToContactsScreen() {
        onView(withId(R.id.add_people)).perform(click());
        onView(isRoot()).perform(ViewActions.pressBack());

        onView(withText(R.string.can_see_me)).check(matches(isDisplayed()));
        onView(withText(R.string.i_can_see)).check(matches(isDisplayed()));
    }
}
