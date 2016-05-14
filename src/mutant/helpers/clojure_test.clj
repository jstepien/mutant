(ns mutant.helpers.clojure-test
  (:require clojure.test))

(defn test-fn
  ([]
   (test-fn #".*"))
  ([regex]
   (let [{:keys [fail error]} (clojure.test/run-all-tests regex)]
     (zero? (+ fail error)))))
