;; Copyright (c) 2016 BigML, Inc
;; Licensed under the Apache License, Version 2.0
;; http://www.apache.org/licenses/LICENSE-2.0

(ns leiningen.test-repeat
  "Repeat a test multiple times"
  (:require [leiningen.core.main :as lein]
            [leiningen.core.eval :as lein-eval]
            [leiningen.core.project :as lein-project]))

(defn test-repeat
  "Repeat a test until failure or a maximum number of iterations"
  [project max-iter & tests]
  (let [max-iter (read-string max-iter)
        project (lein-project/merge-profiles project [:leiningen/test :test])]
    (if (and (integer? max-iter) (pos? max-iter))
      (lein-eval/eval-in-project
       project `(let [test-ns# '~(map symbol tests)
                      ts# (mapcat #(repeat ~max-iter %) test-ns#)
                      report# (-> ~'clojure.test/report
                                  (defmethod :begin-test-ns [m#])
                                  (defmethod :summary [m#]))]
                  (when (seq test-ns#)
                    (apply require :reload test-ns#))
                  (binding [clojure.test/report report#]
                    (loop [[ns# & more#] ts#
                           i# 1]
                      (if-not ns#
                        (do (println "\nReached max iterations.")
                            (System/exit 0))
                        (let [summary# (~'clojure.test/run-tests ns#)
                              fail# (:fail summary#)
                              error# (:error summary#)]

                          (if (or (pos? fail#) (pos? error#))
                            (do (println "\nStopped with failure.")
                                (System/exit 1))
                            (do (print "\r" ns# ": " i# "/" ~max-iter)
                                (when (not= ns# (first more#)) (print "\n"))
                                (flush)
                                (recur
                                 more#
                                 (if (= i# ~max-iter) 1 (inc i#))))))))))
       '(require 'clojure.test))
      (lein/info "Maximum iterations must be a positive integer. Got"
                 max-iter))))
