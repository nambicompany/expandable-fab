---
layout: home
title: ExpandableFab
---

## Why use ExpandableFab?
You've read the elevator pitch in our tagline, but why should developers actually use the ExpandableFab library?


### Fills a void
The ExpandableFab fills the gap between action items in the Options/Overflow Menu (used for global actions) and those in your Navigation Drawer (used for destinations).

Now developers have a simple way of highlighting to users the primary actions available to be taken on the current screen.


### Easy to use
In one of its simplest use cases, the ExpandableFab can be set up by defining just 4 elements: `ExpandableFabLayout`, `Overlay`, `ExpandableFab` and `FabOption` (two if you don't need an `Overlay` or `FabOption`). The XML layout snippet below produces the gif shown:

```xml
<com.nambimobile.widgets.efab.ExpandableFabLayout
  android:layout_width="match_parent"
  android:layout_height="match_parent">
	
  <com.nambimobile.widgets.efab.Overlay
	android:layout_width="match_parent"
	android:layout_height="match_parent"/>
		
  <com.nambimobile.widgets.efab.ExpandableFab
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:layout_gravity="bottom|end"
	android:layout_marginBottom="@dimen/ui_margin_medium"
	android:layout_marginEnd="@dimen/ui_margin_medium"
	android:layout_marginRight="@dimen/ui_margin_medium"/>
        
  <com.nambimobile.widgets.efab.FabOption
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	app:fab_icon="@drawable/ic_email_white_24dp"
	app:label_text="Option 1"
	android:onClick="option1Clicked"/>
		
</com.nambimobile.widgets.efab.ExpandableFabLayout>
```
![ExpandableFab widget simple use case]({{ site.gallery_relative_path | relative_url }}/simple_use_case.gif)

All properties of the ExpandableFab and its custom Views have default values, so animations, colors, margins and other properties don't need to be customized unless you want them to be. This allows developers to get up and running easily and quickly, with very little set up.


### Customizable
If you don't like the default values, you can easily modify colors, text, size, placement, animations and more of every custom View within the ExpandableFab widget. Each custom View also derives from subclasses of Android `View`, so they have all the additional inherited properties you'd expect that are also open for customization (there are a few exceptions to this that can be read below under 'Warnings').

And since the widget was also designed as a proper Compound View, you can access or set all of its properties (and those of its custom Views) programmatically *and* via XML layout files - whatever you’re most comfortable with. Just make sure to include the `xmlns:app="http://schemas.android.com/apk/res-auto` namespace in each layout file that uses Views from the ExpandableFab widget in order to have access to custom properties.

Check out the full [KDoc]({{ site.kdoc_path | relative_url }}) or [JavaDoc]({{ site.javadoc_path | relative_url }}) to see what's available for use and customization in your particular release.


### Orientation Aware
Improve user experience by taking advantage of the ExpandableFab's ability to display different options between portrait and landscape orientations. Consider the following XML layout snippet:

```xml
<!-- This is NOT a root view, but should be a child of whatever root view you choose (CoordinatorLayout, ConstraintLayout, etc) -->
<com.nambimobile.widgets.efab.ExpandableFabLayout
  android:layout_width="match_parent"
  android:layout_height="match_parent">
  
  <!-- The next 8 Views will only display in portrait orientation -->          
  <com.nambimobile.widgets.efab.Overlay
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:overlay_orientation="portrait"/>
  <com.nambimobile.widgets.efab.ExpandableFab
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_marginBottom="@dimen/ui_margin_medium"
    android:layout_marginEnd="@dimen/ui_margin_medium"
    android:layout_marginRight="@dimen/ui_margin_medium"
    app:efab_orientation="portrait"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="portrait"
    app:label_text="Portrait Option 1"
    android:onClick="portraitOption1"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="portrait"
    app:label_text="Portrait Option 2"
    android:onClick="portraitOption2"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="portrait"
    app:label_text="Portrait Option 3"
    android:onClick="portraitOption3"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="portrait"
    app:label_text="Portrait Option 4"
    android:onClick="portraitOption4"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="portrait"
    app:label_text="Portrait Option 5"
    android:onClick="portraitOption5"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="portrait"
    app:label_text="Portrait Option 6"
    android:onClick="portraitOption6"/>

  <!-- The next 3 Views will only display in landscape orientation -->
  <com.nambimobile.widgets.efab.Overlay
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:overlay_orientation="landscape"/>
  <com.nambimobile.widgets.efab.ExpandableFab
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_marginBottom="@dimen/ui_margin_medium"
    android:layout_marginEnd="@dimen/ui_margin_medium"
    android:layout_marginRight="@dimen/ui_margin_medium"
    app:efab_orientation="landscape"/>
  <com.nambimobile.widgets.efab.FabOption
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:fab_orientation="landscape"
    app:label_text="Landscape Option 1"
    android:onClick="landscapeOption1"/>
            
</com.nambimobile.widgets.efab.ExpandableFabLayout>
```

Here we defined an ExpandableFab widget in portrait orientation that, when clicked, will display 6 options for the user to choose.

However, knowing that many user devices likely won't have enough space to show that many options in landscape orientation, we have also defined a second ExpandableFab widget that will only display a single option when clicked in landscape. 

Now we can have our full slate of user actions shown when our ExpandableFab is clicked in portrait orientation, while limiting the ExpandableFab in landscape to only showing a single action for users that, when clicked, opens say an AlertDialog showing the remaining actions in a scrollable list - a much more efficient use of the limited screen real estate in landscape orientations.

These two ExpandableFabs are wholly separate and can have their properties accessed or set entirely apart from one another. There's also no limit to how many ExpandableFab widgets are shown on a single screen, or in any single orientation.

And of course, by default, developers don't need to take advantage of this feature. If only one set of Views are declared, the library will automatically use that same ExpandableFab widget in both portrait and landscape orientations.


## Warnings
As stated previously, every custom View that makes up the ExpandableFab widget is derived from subclasses of Android `View`, so they contain all the additional inherited properties a developer might expect. Feel free to edit these additional inherited properties as desired, but note, for any inherited property not explicitly defined within the library's published API (see the [KDoc]({{ site.kdoc_path | relative_url }}) or [JavaDoc]({{ site.javadoc_path | relative_url }})) there comes a chance that said inherited property might conflict with / be overridden by the library during regular use.

We suggest trying to use the library's published API as much as possible, only deviating to use inherited properties when functionality is desired that can't be achieved by the library directly (if you come across such a case, please bring it to our attention by filing a feature request with the library's [ticketing system]({{ site.repository_url }}/issues) so we can consider adding the functionality to the library directly).

This guidance is especially true when using the `Label` class of the ExpandableFab library. Properties of the officially published API like `labelText`, `labelTextColor`, `labelTextSize`, `labelBackgroundColor` and `labelElevation` should be used **in favor of** inherited methods/properties like `set/getText`, `setTextColor`, `set/getTextSize`, `setBackgroundColor` and `set/getElevation`.

The ExpandableFab widget was designed to be easy to use and highly customizable; so, in general, usage of inherited properties in order to achieve desired functionality or aesthetics shouldn't pose an issue. However, it's good practice to stick with the published API where possible.