
# alert-config-builder

[ ![Download](https://api.bintray.com/packages/hmrc/releases/alert-config-builder/images/download.svg) ](https://bintray.com/hmrc/releases/alert-config-builder/_latestVersion)

`alert-config-builder` is a Scala utility which, given an alert specification for a number of services, generates and emits JSON alert configuration documents for those services, suitable for indexing in Elasticsearch.

The artifact produced by this project is used in the `alert-config` project. The 2 repositories are kept separate due to the fact that the `alert-config` project is user editable yet we don't want to make the functionality exposed here editable.

# Dependencies

Depends on the `app-config` Git repository for the environment for which the alert config is being generated e.g. `app-config-qa`.

# Parameters

`app-config-path` - A Java system property which identifies the location of the app-config repository to use in the generation of the alert-config. This can be either a relative or absolute path. If not provided this will default to `../app-config`.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
