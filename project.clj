(defproject bigml/test-helpers "0.1.1"
  :description "A Leiningen plugin to help with running unit tests"
  :url "https://github.com/bigmlcom/test-helpers"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :deploy-repositories {"releases" {:url "s3://bigml-development-wmlibs/repo/"
                                    :username :env/aws_access_key_id
                                    :passphrase :env/aws_secret_access_key
                                    :sign-releases false}}
  :plugins [[s3-wagon-private "1.3.0"]]
  :eval-in-leiningen true)
