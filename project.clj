(defproject mutant "0.2.0-SNAPSHOT"
  :description "Mutation testing for Clojure"
  :url "https://github.com/jstepien/mutant"
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [clj-diffmatchpatch "0.0.9.3" :exclusions [org.clojure/clojure]]
                 [org.clojure/tools.namespace "0.2.11"]
                 [jansi-clj "0.1.0" :exclusions [org.clojure/clojure]]
                 [rewrite-clj "0.6.0"]])
