(ns mutant.mutations
  (:require [rewrite-clj.zip :as z]))

(defn- swapping-mutation [from to]
  (fn [node]
    (condp = (z/sexpr node)
      to   [(z/replace node from)]
      from [(z/replace node to)]
      nil)))

(def ^:private and-or
  (swapping-mutation 'and 'or))

(def ^:private not-boolean
  (swapping-mutation 'boolean 'not))

(def ^:private empty?-seq
  (swapping-mutation 'empty? 'seq))

(def ^:private gt-gte
  (swapping-mutation '<= '<))

(def ^:private lt-lte
  (swapping-mutation '>= '>))

(def ^:private eq-noteq
  (swapping-mutation '= 'not=))

(defn- for->doseq [node]
  (if (= 'for (z/sexpr node))
    [(z/replace node 'doseq)]))

(defn- rm-args [node]
  (let [sexpr (z/sexpr node)]
    (if (seq? sexpr)
      (let [[defn name args & more] sexpr]
        (if (and (#{'defn 'defn-} defn)
                 (vector? args))
          (for [arg args]
            (-> node z/down z/right z/right
                (z/edit (partial filterv (complement #{arg})))
                (z/up))))))))

(defn- rm-fn-body [node]
  (let [sexpr (z/sexpr node)]
    (if (seq? sexpr)
      (let [[defn name args & more] sexpr]
        (if (and (#{'defn 'defn-} defn)
                 (vector? args))
          (loop [child (->> (z/down node)
                           (iterate z/right)
                           (take-while identity)
                           last)]
            (if-not (= (z/sexpr (z/up child)) [defn name args])
              (recur (z/up (z/remove child)))
              [(z/up child)])))))))

(def ^:private mutations
  [and-or
   gt-gte
   lt-lte
   rm-args
   rm-fn-body
   eq-noteq
   empty?-seq
   for->doseq
   not-boolean])

(defn mutate [zipper]
  (->> mutations
       (mapcat (fn [m]
                 (try
                   (m zipper)
                   (catch UnsupportedOperationException ex
                     nil))))
       (remove nil?)))
