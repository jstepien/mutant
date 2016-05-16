(ns mutant.core-test.test.incomplete-test
  (:require [clojure.test :refer :all]
            [mutant.core-test.src
             [nums :refer :all]
             [colls :refer :all]]))

(deftest t []
  (is (all? [1 () []]))

  (is (naught? 0))

  (is (stricly-pos? 1))
  (is (not (stricly-pos? 0)))

  (is (not (non-neg? -1))))
