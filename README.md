# test-helpers

A Leiningen plugin to help with running unit tests.

## Usage

To install on user-level:

Put `[bigml/test-helpers "0.1.1"]` into the `:plugins` vector of your `:user`
profile.

To install on a project-level:

Put `[bigml/test-helpers "0.1.1"]` into the `:plugins` vector of your project.clj.

Provides two lein tasks:

### iteratest

Repeatedly runs the test in a given namespace or list of namespaces
until failure occurs or a given maximum number of iterations.

    $ lein iteratest 10 wintermute.test.predict.iforest-batch-predictions

    $ lein iteratest 42 phi.test.text.util phi.test.text.common

### test-children

Given a namespace prefix, either runs all unit tests in namespaces
matching the prefix, or interactively select which matching namespaces
to run.

    $ lein test-children phi.test.text

    $ lein test-children wintermute.test.dataset :interactive
