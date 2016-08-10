;; Copyright (c) 2016 BigML, Inc
;; Licensed under the Apache License, Version 2.0
;; http://www.apache.org/licenses/LICENSE-2.0

(ns leiningen.test-children
  "Run unit tests in namespaces with a given prefix"
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [bultitude.core :as bultitude]
            [leiningen.core.main :as lein]
            [leiningen.core.project :as lein-project]
            [leiningen.test :as lein-test]))

(defn- parse-selection [resp]
  (when (seq resp)
    (let [tokens (map read-string (string/split resp #"\s+"))]
      (reduce (fn [acc t]
                (cond (and (integer? t) (pos? t))
                      (conj acc t)
                      (and (string? t) (re-matches #"\d+-\d+" t))
                      (let [[start end] (string/split t #"-")]
                        (into acc (range start (inc end))))
                      :else acc))
              [] tokens))))

(defn- choose-namespaces [nses]
  (let [nses (vec (sort nses))
        rows (mapv #(str %1 "\t" %2) (range) nses)
        prompt "\nEnter namespaces to test (ex. 1 2 3 9 10 or 1-3 9 10): "
        table (string/join "\n" (conj rows prompt))
        resp (do (print table) (flush) (read-line))
        indices (filterv #(< % (count nses)) (parse-selection resp))]
    (map #(str (nses %)) indices)))

(defn test-children
  "Given a namespace prefix, either runs all unit tests in namespaces
  matching t he prefix, or interactively select which matching
  namespaces to run.

  Usage: lein test-children prefix [interactive?]"
  ([project prefix]
   (test-children project prefix false))
  ([project prefix interactive?]
   (let [nses (bultitude/namespaces-on-classpath
               :prefix prefix
               :classpath (map io/file (distinct (:test-paths project))))
         _ (lein/info  "Found" (count nses) "namespaces with prefix"
                       prefix)
         nses (or (and interactive? (choose-namespaces nses))
                  (map str nses))
         nses (sort nses)]
     (when (seq nses)
       (lein/info "Testing " nses)
       (apply lein-test/test project nses)))))
