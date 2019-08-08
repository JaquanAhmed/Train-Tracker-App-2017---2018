package ukk1451070.ac.kingston.httpkunet.transporttracker;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Search;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

public class SearchTest {

    private String places;
    private String places2;
    private Search mActivity = null;

    @Rule
    public ActivityTestRule<Search> mActivityRule = new ActivityTestRule<>(Search.class);

    @Before
    public void setUp() throws Exception {
        places = "L";
        places2 = "W";
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void Switch()
    {
        onView(withId(R.id.places))
                .perform(typeText(places));
        onView(withId(R.id.places2))
                .perform(typeText(places2));
        onView(withId(R.id.button_switch))
                .perform(click());
        onView(withId(R.id.places))
                .check(matches(withText(places2)));
        onView(withId(R.id.places2))
                .check(matches(withText(places)));
    }

    @Test
    public void GooglePlacesAPI() throws InterruptedException {
        onView(withId(R.id.places))
                .perform(typeText("londo"), closeSoftKeyboard());
        Thread.sleep(5000);
        onView(withText("London Bridge"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.places2))
                .perform(typeText("wwa"), closeSoftKeyboard());
        Thread.sleep(5000);
        onView(withText("Woolwich Arsenal"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.lat))
                .check(matches(withText("51.5047001")));
        onView(withId(R.id.lon))
                .check(matches(withText("-0.08597629999999999")));
        onView(withId(R.id.lat2))
                .check(matches(withText("51.49001579999999")));
        onView(withId(R.id.lon2))
                .check(matches(withText("0.0689901")));
    }
}