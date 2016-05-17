(ns mutant.internals-test
  (:require [clojure.test :refer :all]
            [mutant.internals :as mi]
            clojure.tools.namespace.dependency))

(deftest t-run-ns
  (let [forms [(pr-str '(defn not-much? [x] (or (= 0 x) (= 1 x))))]
        graph (clojure.tools.namespace.dependency/graph)]
    (testing "no mutants"
      (is (= {:survivors [], :total 5}
             (mi/run-ns 'mutant.t forms graph
                        #(let [not-much? (resolve 'mutant.t/not-much?)]
                           (and (not-much? 1) (not (not-much? 2))))))))
    (testing "survivors"
      (is (= {:survivors (set (for [body '[((and (= 0 x) (= 1 x)))
                                           ((or (not= 0 x) (= 1 x)))
                                           ((or (= 0 x) (not= 1 x)))
                                           ()]]
                                {:mutant (pr-str (list* 'defn 'not-much? '[x] body))
                                 :original (first forms)
                                 :ns 'mutant.t}))
              :total 5}
             (update (mi/run-ns 'mutant.t forms graph (constantly true))
                     :survivors
                     set))))))
