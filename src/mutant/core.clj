(ns mutant.core
  (:require [mutant.internals :as mi]
            [jansi-clj [core :as jansi] auto]
            [clj-diffmatchpatch :as dmp]))

(defn run [source-directory test-directory test-fn]
  (let [dep-graph (mi/dependency-graph [source-directory test-directory])]
    (->> (mi/namespaces source-directory)
         (mapcat (fn [[file ns]]
                   (mi/run-ns ns (mi/forms file) dep-graph test-fn)))
         (reductions (fn [{:keys [total survivors]} {:keys [survivor]}]
                       {:survivors (if survivor
                                     (cons survivor survivors)
                                     survivors)
                        :total (inc total)})
                     {:survivors ()
                      :total 0})
         (rest))))

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
