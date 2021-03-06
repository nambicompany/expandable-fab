# ChangeLog
All notable changes to the ExpandableFab library (*not* its website or example application) will be documented in this file. Versions below correspond to git tags and releases.

The format is based on the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) spec,
and this library adheres to [Semantic Versioning](https://semver.org/).

Note: Dates are formatted as YEAR-MONTH-DAY.

#

## [v1.1.1] - 2021-06-30
### Fixed
- FabOptions that are dynamically removed while the ExpandableFab is still open are now properly removed from the ExpandableFabLayout (and consequently removed from the UI).
- FabOption removal logic updated to actually remove the correct FabOption (indexing issue).

## [v1.1.0] - 2021-06-29
### Fixed
- The alpha channel (degree of transparency) is now respected when setting Label background colors (`label.labelBackgroundColor`). Colors should be set as an integer in the form 0xAARRGGBB (check [documentation for `label.labelBackgroundColor`](https://nambicompany.github.io/expandable-fab/kdoc/) for more details).

### Added
- FabOptions can now be dynamically removed at runtime by calling remove(FabOption) or remove(int) (the latter is named removeAt(int) in Kotlin) on the list of FabOptions contained within an `OrientationConfiguration`. You can easily obtain an `OrientationConfiguration` from an instance of ExpandableFabLayout by calling `expandableFabLayout.portraitConfiguration` or `expandableFabLayout.landscapeConfiguration` or `expandableFabLayout.getCurrentConfiguration`.

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