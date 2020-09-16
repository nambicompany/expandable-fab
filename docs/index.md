---
layout: home
title: ExpandableFab
---

## What is the ExpandableFab, and why should I use it?
<p align="center"><img src="{{ site.gallery_relative_path | relative_url }}/highly_customizable.gif"></p>

You’ve read the elevator pitch in our tagline, but you may be thinking: why should developers actually use the ExpandableFab library?

Read our Medium article below explaining the intent behind the widget, and how it can help you when building a modern, Material Design adhering Android app:

["A modern take on the expandable Floating Action Button for Android"](https://uxdesign.cc/a-modern-take-on-the-expandable-floating-action-button-for-android-aka-speed-dial-4146e63c658c)


## How to use the ExpandableFab
Now that you're convinced that the ExpandableFab is right for your project, consider reading our in-depth, easy-to-follow walkthrough on how you as a designer/developer can easily include and use it:

["How to use the expandable Floating Action Button"](https://uxdesign.cc/how-to-use-the-expandable-floating-action-button-9c6fdedc4169)


## Warnings
Every custom View that makes up the ExpandableFab widget is derived from subclasses of Android `View`, so they contain all the additional inherited properties a developer might expect. Feel free to edit these additional inherited properties as desired, but note, for any inherited property not explicitly defined within the library's published API (see the [KDoc]({{ site.kdoc_path | relative_url }}) or [JavaDoc]({{ site.javadoc_path | relative_url }})) there comes a chance that said inherited property might conflict with / be overridden by the library during regular use.

We suggest trying to use the library's published API as much as possible, only deviating to use inherited properties when functionality is desired that can't be achieved by the library directly. If you come across such a case, please bring it to our attention by filing a feature request with the library's [ticketing system]({{ site.repository_url }}/issues) so we can consider adding the functionality to the library directly.

This guidance is especially true when using the `Label` class of the ExpandableFab library. Properties of the officially published API like `labelText`, `labelTextColor`, `labelTextSize`, `labelBackgroundColor` and `labelElevation` should be used **in favor of** inherited methods/properties like `set/getText`, `setTextColor`, `set/getTextSize`, `setBackgroundColor` and `set/getElevation`.

The ExpandableFab widget was designed to be easy to use and highly customizable; so, in general, usage of inherited properties in order to achieve desired functionality or aesthetics shouldn't pose an issue. However, it's good practice to stick with the published API where possible.