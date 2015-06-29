package cc.softwarefactory.lokki.android.espresso;

import cc.softwarefactory.lokki.android.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class WelcomeScreensTest extends LokkiBaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testWelcomeTextIsOnScreen() {
        onView(withText(R.string.welcome_title)).check(matches(isDisplayed()));
    }

    public void testContinueButtonTakesToTermsScreen() {
        onView(withText(R.string.continue_with_terms)).perform(click());
        onView(withText(R.string.terms_title)).check(matches(isDisplayed()));
    }

    public void testAgreeOnTermsTakesToRegistrationScreen() {
        onView(withText(R.string.continue_with_terms)).perform(click());
        onView(withText(R.string.i_agree)).perform(click());
        onView(withText(R.string.sign_up_explanation)).check(matches(isDisplayed()));
    }


}