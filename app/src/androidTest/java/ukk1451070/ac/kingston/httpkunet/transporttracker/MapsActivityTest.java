package ukk1451070.ac.kingston.httpkunet.transporttracker;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.MapsActivity;
import ukk1451070.ac.kingston.httpkunet.transporttracker.Journey.Search;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasCategories;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasHost;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.matchers.JUnitMatchers.hasItem;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    private int partNo;
    private int endNo;
    ArrayList<LatLng> train = new ArrayList<>();
    ArrayList<LatLng> foot = new ArrayList<>();
    ArrayList<LatLng> tube = new ArrayList<>();
    ArrayList<LatLng> bus = new ArrayList<>();
    ArrayList<LatLng> other = new ArrayList<>();
    ArrayList<LatLng> full = new ArrayList<>();


    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(MapsActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        partNo = 1;
        endNo = 3;
    }

    @Test
    public void PreviousButton () {
        Intent intent = new Intent();
        intent.putExtra("partNo", partNo);
        intent.putExtra("endNo", endNo);
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.buttonPP))
                .check(matches(not(isDisplayed())))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void VisabilityButton (){
        partNo = partNo + 1;
        Intent intent = new Intent();
        intent.putExtra("partNo", partNo);
        intent.putExtra("endNo", endNo);
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.buttonNP))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.buttonPP))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void NextButton (){
        partNo = partNo + 2;
        Intent intent = new Intent();
        intent.putExtra("partNo", partNo);
        intent.putExtra("endNo", endNo);
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.buttonNP))
                .check(matches(not(isDisplayed())))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void RefreshButtonVisible (){
        train.add(new LatLng(51.490, 0.069));
        train.add(new LatLng(51.5044, 0.0857));
        Intent intent = new Intent();
        intent.putExtra("partNo", partNo);
        intent.putExtra("endNo", endNo);
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.refresh))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void RefreshButtonNotVisible (){
        Intent intent = new Intent();
        intent.putExtra("partNo", partNo);
        intent.putExtra("endNo", endNo);
        intent.putExtra("full", full);
        intent.putExtra("train", train);
        intent.putExtra("tube", tube);
        intent.putExtra("foot", foot);
        intent.putExtra("bus", bus);
        intent.putExtra("other", other);
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.refresh))
                .check(matches(not(isDisplayed())))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}