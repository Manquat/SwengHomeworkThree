/*
 * Copyright 2014 EPFL. All rights reserved.
 */

package ch.epfl.sweng.testing;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;
import android.widget.TextView;

/** Custom Hamcrest matchers for properties such as text color. */
public final class ViewMatchers {
    public static Matcher<View> withTextColor(final int color) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("has text color %d", color));
            }

            @Override
            public boolean matchesSafely(View item) {
                if (!(item instanceof TextView)) {
                    return false;
                }

                return ((TextView) item).getTextColors().getDefaultColor() == color;
            }
        };
    }

    public static Matcher<View> withPaintFlag(final int flag) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("has paint flag %d", flag));
            }

            @Override
            public boolean matchesSafely(View item) {
                if (!(item instanceof TextView)) {
                    return false;
                }
                return (((TextView) item).getPaintFlags() & flag) != 0;
            }
        };
    }
}
