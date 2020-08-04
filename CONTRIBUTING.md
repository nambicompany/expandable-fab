# Contributing
Contributions to ExpandableFab are highly encouraged! Before beginning work, please put in a [ticket](https://github.com/nambicompany/expandable-fab/issues) labeled with `proposal` to describe your proposal and get approval from a repo admin. If applicable, please link to related existing tickets (bugs, feature requests, etc) that your proposal will address. Once given approval, we simply ask that you adhere to the following guidelines as much as possible or risk having your pull request denied:


## General
* Code in Kotlin and adhere to Google's [Kotlin style guide for Android](https://developer.android.com/kotlin/style-guide) as much as possible
* Follow the existing coding practices seen in the library
* If granted collaborator status: develop on a branch based off `master` and submit pull requests to be merged to `master`
* If not granted collaborator status: fork the repo, develop on a branch based off `master`, then submit a pull request to `master` of the original repo (this project)
* Keep commits small and distinct (containing a single feature or 'piece' of work) with meaningful messages


## API Changes
* Design additions to the API to be clear and intuitive, exposing the least necessary operations to clients
    * New classes should specify a `@since` block tag in its class description and none on its properties/methods.
    * Additions/modifications to existing classes should specify a `@since` block tag in the documentation for each addition.
    * See the existing code base for how to use the `@since` block tag, or read more from [Oracle](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html#@since) or [JetBrains](https://kotlinlang.org/docs/reference/kotlin-doc.html#since).
* Update documentation where applicable, or include new thorough documentation for API additions
    * Whenever the API/Documentation is changed in anyway, users should run our gradle task for updating the JavaDoc/KDoc on our library website. Don't worry, this process is mostly automated. Just run the `generateDocsAndUpdateWebsite` task from the library's [generate-docs-and-update-website.gradle](/library/generate-docs-and-update-website.gradle) file.
* Ensure any feature or property added to any of the Views within the ExpandableFab widget can be modified by clients programmatically *and* via XML


## Testing
* Test your changes, preferably on physical devices spanning multiple Android versions


## Finishing Touches
* Update the [ChangeLog](/CHANGELOG.md), following guidelines set at [Keep a Changelog](https://keepachangelog.com/en/1.0.0/#how)
* Update the library's version in its [build.gradle](/library/build.gradle) according to the [Semantic Versioning](https://semver.org/) specification
* Create an annotated git tag with a tag name of 'v[versionName]' (where [versionName] is equal to the library's new version after your update in the previous step)
    * Example: `git tag -a v1.0.3 -m "Fixed FabOption labeling bug introduced in v1.0.2"`
* Push to your feature branch, then create a pull request to `master`!

## Releasing to Sonatype Nexus / Maven Central (only for those with creds)
* Run the publish Gradle task, ensuring that gpg-agent is running locally and the necessary properties are set in global/project gradle.properties (see [publish-artifacts-to-maven-repo.gradle](/library/publish-artifacts-to-maven-repo.gradle))
* Once Gradle task finishes successfully: 
    * Login to the Nexus Repository Manager (https://oss.sonatype.org/)
    * Navigate to `Staging Repositories`
    * `Close` then `Release` the repository for publication to Sonatype Nexus' Release Repo
        * Will take around 2 hours for Sonatype Nexus' Release Repo to sync with Maven Central