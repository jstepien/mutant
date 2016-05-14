(ns mutant.core
  (:require [mutant.internals :as mi]
            [jansi-clj [core :as jansi] auto]
            [clj-diffmatchpatch :as dmp]))

(defn run [source-directory test-directory test-fn]
  (let [dep-graph (mi/dependency-graph [source-directory test-directory])
        nss (mi/namespaces source-directory)]
    (reduce (fn [acc [file ns]]
              (let [result (mi/run-ns ns (mi/forms file) dep-graph test-fn)
                    {:keys [survivors total]} result]
                (-> acc
                    (update :survivors into survivors)
                    (update :total + total))))
            {:survivors [], :total 0}
            nss)))

(defn pprint [{:keys [survivors total]}]
  (printf "%s out of %s mutants\n\n"
          (if (seq survivors)
            (jansi/red (count survivors) " survivors")
            "No survivors")
          total)
  (doseq [{:keys [mutant original ns]} survivors]
    (printf "(ns %s)\n" ns)
    (doseq [[op chunk] (dmp/wdiff original mutant)]
      (print (case op
               :equal  chunk
               :insert (jansi/green "{+" chunk "+}")
               :delete (jansi/red   "[-" chunk "-]"))))
    (print "\n\n")))
