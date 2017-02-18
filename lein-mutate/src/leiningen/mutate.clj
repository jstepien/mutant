(ns leiningen.mutate
  (:require [leiningen.core.eval :as eval]
            [leiningen.core.project :as project]))

(def ^:private mutant-artifact
  '[mutant "0.2.0"])

#_(require 'clojure.pprint)

(defn mutate
  "Run a mutation analysis on your project."
  [project & args]
  (let [mutant-profile {:dependencies [mutant-artifact]}
        project (->> [:leiningen/test :test mutant-profile]
                     (project/merge-profiles project))]
    (->> '((->> (:test-paths project)
                (mapcat #(find/find-namespaces-in-dir (java.io.File. %)))
                (run! require))
           (letfn [(survivor? [prev curr]
                     (not= (-> prev :survivors first)
                           (-> curr :survivors first)))
                   (report [prev curr]
                     (print (if (survivor? prev curr) \x \.))
                     (flush)
                     curr)
                   (final-report [result]
                     (print "\n\n")
                     (mutant/pprint result)
                     (flush))]
             (binding [*out* *err*]
               (->> (mutant/run (first (:source-paths project))
                                (first (:test-paths project))
                                clojure-test/test-fn)
                    (reduce report)
                    final-report))
             (shutdown-agents)))
         (list* 'let ['project `(quote ~(select-keys project [:test-paths
                                                              :source-paths]))])
         (list 'do
               '(require '[mutant.core :as mutant]
                         '[mutant.helpers.clojure-test :as clojure-test]
                         '[clojure.java.shell :refer [sh]]
                         '[clojure.tools.namespace.find :as find]))
         #_clojure.pprint/pprint
         (eval/eval-in-project project))))
