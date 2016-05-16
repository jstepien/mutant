(ns mutant.core-test.src.nums)

(defn naught? [x]
  (= 0 x))

(defn non-neg? [x]
  (<= 0 x))

(defn stricly-pos? [x]
  (> x 0))
