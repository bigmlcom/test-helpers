;; Copyright (c) 2016 BigML, Inc
;; All rights reserved.

;; Author: Chee Sing Lee <lee@bigml.com>
;; Start date: Mon Aug 08, 2016 15:22

(ns leiningen.test-repeat
  "Repeat a test multiple times"
  (:require [leiningen.core.main :as lein]
            [leiningen.core.eval :as lein-eval]
            [leiningen.core.project :as lein-project]))

(defn test-repeat
  "Repeat a test multiple times"
  [project n & tests]
  (let [n (read-string n)
        project (lein-project/merge-profiles project [:leiningen/test :test])]
    (if (and (integer? n) (pos? n))
      (lein-eval/eval-in-project
       project `(let [test-ns# '~(map symbol tests)
                      ts# (mapcat #(repeat ~n %) test-ns#)]
                  (when (seq test-ns#)
                    (apply require :reload test-ns#))
                  (apply ~'clojure.test/run-tests ts#))
       '(require 'clojure.test))
      (lein/info "n must be a positive integer. Got" n))))
