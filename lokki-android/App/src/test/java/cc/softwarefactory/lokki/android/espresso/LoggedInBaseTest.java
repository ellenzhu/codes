package cc.softwarefactory.lokki.android.espresso;

import cc.softwarefactory.lokki.android.espresso.utilities.TestUtils;

public abstract class LoggedInBaseTest extends LokkiBaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestUtils.setUserRegistrationData(getInstrumentation().getTargetContext());
    }

}
