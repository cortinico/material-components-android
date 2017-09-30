/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.support.design.animation;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.design.testapp.animation.R;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MotionSpecTest {

  @Rule
  public final ActivityTestRule<AppCompatActivity> activityTestRule =
      new ActivityTestRule<>(AppCompatActivity.class);

  @Test
  public void inflateMotionSpec() {
    MotionSpec.inflate(
        activityTestRule.getActivity(), R.animator.valid_set_of_object_animator_motion_spec);
    MotionSpec.inflate(
        activityTestRule.getActivity(), R.animator.valid_object_animator_motion_spec);
    MotionSpec.inflate(activityTestRule.getActivity(), R.animator.valid_empty_set_motion_spec);
  }

  @Test
  public void setOfObjectAnimatorMotionSpecHasAlphaAndTranslationTimings() {
    MotionSpec spec =
        MotionSpec.inflate(
            activityTestRule.getActivity(), R.animator.valid_set_of_object_animator_motion_spec);
    assertNotNull(spec.getTiming("alpha"));
    assertNotNull(spec.getTiming("translation"));
  }

  @Test
  public void validateSetOfObjectAnimatorAlphaMotionTiming() {
    MotionSpec spec =
        MotionSpec.inflate(
            activityTestRule.getActivity(), R.animator.valid_set_of_object_animator_motion_spec);
    MotionTiming alpha = spec.getTiming("alpha");

    assertEquals(3, alpha.getDelay());
    assertEquals(5, alpha.getDuration());
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      assertThat(alpha.getInterpolator(), instanceOf(PathInterpolator.class));
    } else {
      assertThat(alpha.getInterpolator(), instanceOf(FastOutLinearInInterpolator.class));
    }
    assertEquals(7, alpha.getRepeatCount());
    assertEquals(ValueAnimator.RESTART, alpha.getRepeatMode());
  }

  @Test
  public void validateSetOfObjectAnimatorTranslationMotionTiming() {
    MotionSpec spec =
        MotionSpec.inflate(
            activityTestRule.getActivity(), R.animator.valid_set_of_object_animator_motion_spec);
    MotionTiming translation = spec.getTiming("translation");

    assertEquals(11, translation.getDelay());
    assertEquals(13, translation.getDuration());
    assertThat(translation.getInterpolator(), instanceOf(LinearInterpolator.class));
    assertEquals(17, translation.getRepeatCount());
    assertEquals(ValueAnimator.REVERSE, translation.getRepeatMode());
  }

  @Test(expected = Resources.NotFoundException.class)
  public void inflateInvalidSetOfSetMotionSpec() {
    MotionSpec.inflate(activityTestRule.getActivity(), R.animator.invalid_set_of_set_motion_spec);
  }

  @Test(expected = Resources.NotFoundException.class)
  public void inflateInvalidSetOfValueAnimatorMotionSpec() {
    MotionSpec.inflate(
        activityTestRule.getActivity(), R.animator.invalid_set_of_value_animator_motion_spec);
  }
}