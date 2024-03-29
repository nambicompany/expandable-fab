# ChangeLog
All notable changes to the ExpandableFab library (*not* its website or example application) will be documented in this file. Versions below correspond to git tags and releases.

The format is based on the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) spec,
and this library adheres to [Semantic Versioning](https://semver.org/).

Note: Dates are formatted as YEAR-MONTH-DAY.

#

## [v1.2.1] - 2021-10-21
### Fixed
- Due to the library targeting SDK 30 as of v1.2.0 instead of 28, `Label` widgets were unintentionally calling `setPadding` on their GradientDrawable background (a method that was only added in SDK 29), instead of calling it on the TextView itself (a method that has been present since SDK 1). This led to crashes on devices running SDK 28 and below. The setPadding call was simply moved outside the scope of the GradientDrawable in order to fix the bug, and now the widgets run on all supported SDK versions again.

## [v1.2.0] - 2021-10-20
### Added
- Clients can now set global animation durations for all children widgets of the `ExpandableFabLayout` right on the layout itself. This can be done programmatically or via XML layout attributes. So instead of having to update the animation durations on every `Overlay`, `ExpandableFab`, `FabOption`, and `Label` within an ExpandableFabLayout (which can become tedious), a developer can now set the value once for each widget type right on the parent. Note that these global duration fields will *overwrite* any duration set on individual widgets, so be mindful when using them. See the documentation of `ExpandableFabLayout` for more details.
- Font (typeface) can now be set on all `Labels` programmatically and via XML layout attributes. Each Label can use a different font if desired. See the documentation of `Label` for more details.

### Fixed
- Icons using vector drawables are now loaded using `AppCompatResources` instead of `ContextCompat` in order to prevent a rare crash that occurred on certain devices when those vector drawables were referencing other resources (e.g. a vector drawable `ic_smile.xml` that referenced `@color/white` as its fill color, for instance).

## [v1.1.1] - 2021-06-30
### Fixed
- FabOptions that are dynamically removed while the ExpandableFab is still open are now properly removed from the ExpandableFabLayout (and consequently removed from the UI).
- FabOption removal logic updated to actually remove the correct FabOption (indexing issue).

## [v1.1.0] - 2021-06-29
### Added
- FabOptions can now be dynamically removed at runtime by calling remove(FabOption) or remove(int) (the latter is named removeAt(int) in Kotlin) on the list of FabOptions contained within an `OrientationConfiguration`. You can easily obtain an `OrientationConfiguration` from an instance of ExpandableFabLayout by calling `expandableFabLayout.portraitConfiguration` or `expandableFabLayout.landscapeConfiguration` or `expandableFabLayout.getCurrentConfiguration`.

### Fixed
- The alpha channel (degree of transparency) is now respected when setting Label background colors (`label.labelBackgroundColor`). Colors should be set as an integer in the form 0xAARRGGBB (check [documentation for `label.labelBackgroundColor`](https://nambicompany.github.io/expandable-fab/kdoc/) for more details).

## [v1.0.2] - 2020-10-26
### Fixed
- ExpandableFab views can now accept 0 milliseconds for opening and closing animation durations - just like FabOptions and Labels. For all views, 0 for animation duration means "do not play an animation". This has been clarified in the documentation.

## [v1.0.1] - 2020-09-06
### Fixed
- ExpandableFab and FabOptions now use the colorAccent defined in the app's theme within styles.xml (or colorSecondary if colorAccent is not defined). Previously, the library did not walk the theme for colorAccent and just used the value defined in colors.xml.

## [v1.0.0] - 2020-09-03
### Moved to Stable Release
- First stable release. API for existing functionality is now locked. No changes from v0.9.2, except the completion of beta testing.

## [v0.9.2] - 2020-08-05
### Changed
- Clients can now add *any* view as a direct child of ExpandableFabLayout. This allows compatibility with a range of new Material Design views like Snackbar and BottomAppBar.

## [v0.9.1] - 2020-08-04
### Fixed
- Removed quotes from the POM metadata file and removed the `-release` classifier from generated AARs. These issues were breaking the ability for Maven repositories like JCenter and Maven Central to serve the `v0.9.0` artifact. This fix allows users to now import the library as a dependency using Maven, Gradle, etc starting from `v0.9.1`.

## [v0.9.0] - 2020-08-04
### Added
- Initial commit of ExpandableFab library.
- Widget custom Views: `ExpandableFabLayout`, `Overlay`, `ExpandableFab`, `FabOption` and `Label`.
- Default animations for all widget custom Views.
- Full documentation for the published API.
- Insightful error messages to clients.