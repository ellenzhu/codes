package cc.softwarefactory.lokki.android.espresso;

import com.squareup.okhttp.mockwebserver.MockResponse;

import org.json.JSONException;

import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.espresso.utilities.MockJsonUtils;
import cc.softwarefactory.lokki.android.espresso.utilities.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


public class ContactsScreenTest extends LoggedInBaseTest {


    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    private void enterContactsScreen() {
        getActivity();
        TestUtils.toggleNavigationDrawer();
        onView(withText(R.string.contacts)).perform(click());
    }

    public void testNoContactsShownWhenNoContacts() {
        enterContactsScreen();
        onView(withId(R.id.contact_email)).check(doesNotExist());
    }

    public void testContactsTableHeaderTextsShown() {
        enterContactsScreen();
        onView(withText(R.string.i_can_see)).check(matches(isDisplayed()));
        onView(withText(R.string.can_see_me)).check(matches(isDisplayed()));
    }

    public void testOneContactShownWhenOneContact() throws JSONException, InterruptedException {
        String contactEmail = "family.member@example.com";
        getMockDispatcher().setDashboardResponse(new MockResponse().setBody(MockJsonUtils.getDashboardJsonWithContacts(contactEmail)));

        enterContactsScreen();

        onView(allOf(withId(R.id.contact_email), withText(contactEmail))).check(matches(isDisplayed()));
    }

    public void testAllCheckboxesShownWhenOneContact() throws JSONException, InterruptedException {
        String contactEmail = "family.member@example.com";
        getMockDispatcher().setDashboardResponse(new MockResponse().setBody(MockJsonUtils.getDashboardJsonWithContacts(contactEmail)));

        enterContactsScreen();

        onView(allOf(withId(R.id.can_see_me), hasSibling(withText(contactEmail)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.i_can_see), hasSibling(withText(contactEmail)))).check(matches(isDisplayed()));
    }

    public void testTwoContactsShownWhenTwoContacts() throws JSONException, InterruptedException {
        String firstContactEmail = "family.member@example.com";
        String secondContactEmail = "work.buddy@example.com";
        getMockDispatcher().setDashboardResponse(new MockResponse().setBody(MockJsonUtils.getDashboardJsonWithContacts(firstContactEmail, secondContactEmail)));

        enterContactsScreen();

        onView(allOf(withId(R.id.contact_email), withText(firstContactEmail))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.contact_email), withText(secondContactEmail))).check(matches(isDisplayed()));
    }

    public void testAllCheckboxesShownWhenTwoContacts() throws JSONException, InterruptedException {
        String firstContactEmail = "family.member@example.com";
        String secondContactEmail = "work.buddy@example.com";
        getMockDispatcher().setDashboardResponse(new MockResponse().setBody(MockJsonUtils.getDashboardJsonWithContacts(firstContactEmail, secondContactEmail)));

        enterContactsScreen();

        onView(allOf(withId(R.id.can_see_me), hasSibling(withText(firstContactEmail)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.i_can_see), hasSibling(withText(firstContactEmail)))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.can_see_me), hasSibling(withText(secondContactEmail)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.i_can_see), hasSibling(withText(secondContactEmail)))).check(matches(isDisplayed()));
    }

}
