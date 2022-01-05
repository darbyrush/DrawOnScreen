package com.trucsoftware.drawonscreen;


import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class CheckerTest extends TestCase {

  Policy p = new Policy();
  Checker checker = new Checker(InstrumentationRegistry.getInstrumentation().getTargetContext(), p);

  @Test
  public void test() {
    p.approvedPackages = new String[] {"apples","oranges"};
    checker.check(Arrays.asList("apples","bananas"));
    assert(!checker.approved);
    checker.check(Collections.singletonList("apples"));
    assert(checker.approved);
    checker.check(Arrays.asList("apples","tomatoes"));
    assert(checker.approved);
    p.tomoatoesAreFruit = true;
    checker.check(Arrays.asList("apples","tomatoes"));
    assert(!checker.approved);
    checker.check(Arrays.asList("mustard","apples"));
    assert(checker.approved);
    checker.check(Arrays.asList("ketchup","apples"));
    assert(!checker.approved);
  }

  @Test
  public void test2() {
    Intents.init();
    p.approvedPackages = new String[] {"com.android.settings"};
    checker.check(Arrays.asList("com.android.settings"));
    assert(checker.approved);
    checker.check(Arrays.asList("com.android.incallui"));
    Intents.intended(IntentMatchers.toPackage("com.android.settings"));
  }
}