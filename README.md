# test-helpers

A Leiningen plugin to help with running unit tests.

## Usage

To install on user-level:

Put `[test-helpers "0.1.0"]` into the `:plugins` vector of your `:user`
profile.

To install on a project-level:

Put `[test-helpers "0.1.0"]` into the `:plugins` vector of your project.clj.

Provides two lein tasks:

### test-repeat

Runs the test in a given namespace or list of namespaces multiple times

    $ lein test-repeat 10 wintermute.test.predict.iforest-batch-predictions

    $ lein test-repeat 42 phi.test.text.util phi.test.text.common

### test-children

Given a namespace prefix, either runs all unit tests in namespaces
matching the prefix, or interactively select which matching namespaces
to run.

    $ lein test-children phi.test.text

    $ lein test-children wintermute.test.dataset :interactive
