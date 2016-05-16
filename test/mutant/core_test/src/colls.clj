(ns mutant.core-test.src.colls)

(defn all? [xs]
  (reduce #(and %1 %2) (for [x xs] (boolean x))))
