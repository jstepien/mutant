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

(deftest t-run-ns-cljc
  (let [form-strs ["(defn cljc-five [] #?(:clj 5))"
                   "(defn five? [x] (contains? #{5 #?(:clj nil)} x))"]
        zippers (for [string form-strs]
                  (z/of-string string {:track-position? true}))
        graph (clojure.tools.namespace.dependency/graph)]
    (is (= #{"(defn cljc-five [])"
             "(defn cljc-five [] #?(:foo 5))"
             "(defn five? [x] (contains? #{5 #?(:foo nil)} x))"}
           (->> (mi/run-ns 'mutant.t zippers graph
                           #(let [five? (resolve 'mutant.t/five?)
                                  cljc-five (resolve 'mutant.t/cljc-five)]
                              ;(prn (cljc-five))
                              (five? (cljc-five))))
                (keep :survivor)
                (map :mutant)
                (set))))))
