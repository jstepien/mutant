(ns mutant.core-test
  (:require [clojure.test :refer :all]
            [mutant.core :as m]
            [mutant.helpers.clojure-test :refer [test-fn]]
            mutant.core-test.test.test))

(deftest t-run
  (is (= {:survivors [], :total 10}
         (m/run "test/mutant/core_test/src"
                "test/mutant/core_test/test"
                #(test-fn #"mutant.core-test.test.test"))))
  (is (= {:survivors [{:original "(defn all? [xs]\n  (reduce #(and %1 %2) (for [x xs] (boolean x))))"
                       :mutant "(defn all? [xs]\n  (reduce #(or %1 %2) (for [x xs] (boolean x))))"
                       :ns 'mutant.core-test.src.colls}
                      {:original "(defn non-neg? [x]\n  (<= 0 x))"
                       :mutant "(defn non-neg? [x]\n  (< 0 x))"
                       :ns 'mutant.core-test.src.nums}]

          :total 10}
         (m/run "test/mutant/core_test/src"
                "test/mutant/core_test/test"
                #(test-fn #"mutant.core-test.test.incomplete-test")))))
