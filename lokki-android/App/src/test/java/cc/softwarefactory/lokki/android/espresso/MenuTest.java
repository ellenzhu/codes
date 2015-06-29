package cc.softwarefactory.lokki.android.espresso;

import cc.softwarefactory.lokki.android.R;
import cc.softwarefactory.lokki.android.espresso.utilities.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class MenuTest extends LoggedInBaseTest {
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    private void openNavigationDrawer() {
        getActivity();
        TestUtils.toggleNavigationDrawer();
    }
    
    // TEST
    
    public void testUsernameShownInMenu() {
        openNavigationDrawer();
        onView(withText("test@test.com")).check(matches(isDisplayed()));
    }

    public void testNavDrawerToggleButtonIsPresent() {
        getActivity();
        onView(allOf(withContentDescription("Lokki"), withParent(withId(R.id.toolbar_layout)), not(withText("Lokki"))))
                .check(matches(isDisplayed()));
        onView(allOf(withContentDescription("Lokki"), withParent(withId(R.id.toolbar_layout)), not(withText("Lokki"))))
                .check(isLeftOf(allOf(withText("Lokki"), withParent(withId(R.id.toolbar_layout)), not(hasContentDescription()))));
    }

    public void testNavDrawerToggleButtonOpensMenu() {
        getActivity();
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(allOf(withContentDescription("Lokki"), withParent(withId(R.id.toolbar_layout)), not(withText("Lokki"))))
                .perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(not(isClosed())));
    }

}
