(ns mutant.internals-test
  (:require [clojure.test :refer :all]
            [mutant.internals :as mi]
            [rewrite-clj.zip :as z]
            clojure.tools.namespace.dependency))

(deftest t-run-ns
  (let [forms '[(defn not-much? [x] (or (= 0 x) (= 1 x)))]
        zippers (for [form forms]
                  (z/of-string (pr-str form) {:track-position? true}))
        graph (clojure.tools.namespace.dependency/graph)]
    (testing "no mutants"
      (is (= (repeat 5 {})
             (mi/run-ns 'mutant.t zippers graph
                        #(let [not-much? (resolve 'mutant.t/not-much?)]
                           (and (not-much? 1) (not (not-much? 2))))))))
    (testing "survivors"
      (is (= (cons {}
                   (for [body '[()
                                ((and (= 0 x) (= 1 x)))
                                ((or (not= 0 x) (= 1 x)))
                                ((or (= 0 x) (not= 1 x)))]]
                     {:survivor
                      {:mutant (pr-str (list* 'defn 'not-much? '[x] body))
                       :original (pr-str (first forms))
                       :ns 'mutant.t}}))
             (mi/run-ns 'mutant.t zippers graph (constantly true)))))))
