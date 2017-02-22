(ns mutant.internals
  (:require [rewrite-clj.zip :as z]
            [mutant.mutations :refer [mutate]]
            [clojure.tools.namespace
             [find :as find]
             [file :as file]
             [parse :as parse]
             [dependency :as dep]]))

(defn- paths-in-zipper [zipper]
  (let [directions [z/down z/right]
        rec (fn rec [prefix node]
              (mapcat (fn [dir]
                        (if-let [sub-node (dir node)]
                          (cons (conj prefix dir)
                                (rec (conj prefix dir) sub-node))))
                      directions))]
    (cons [] (rec [z/down] (z/down zipper)))))

(defn- mutants [zipper paths]
  (->> (for [path paths]
         (let [node (reduce (fn [node dir] (dir node)) zipper path)
               rev-path (map {z/down z/up, z/right z/left} (reverse path))]
           (remove nil?
                   (for [mutant (mutate node)]
                     (reduce (fn [node dir] (dir node)) mutant rev-path)))))
       (apply concat)))

(defn- file [^String name]
  (java.io.File. name))

(defn namespaces
  "Returns a map from files to namespaces in a given directory."
  [directory-name]
  (->> (find/find-clojure-sources-in-dir (file directory-name))
       (map (juxt identity (comp second file/read-file-ns-decl)))
       (into {})))

(defn dependency-graph [directory-names]
  (let [decls (find/find-ns-decls (map file directory-names))]
    (->> decls
         (map (juxt second parse/deps-from-ns-decl))
         (reduce (fn [graph [ns deps]]
                   (reduce #(dep/depend %1 ns %2)
                           graph
                           deps))
                 (dep/graph)))))

(defn forms
  "Return a collection of zippers for top-level sexprs found in a given file."
  [file]
  (->> (z/of-file file {:track-position? true})
       (iterate z/right)
       (remove (comp #{'ns} z/sexpr z/down))
       (take-while boolean)))

(defn- dependants [graph ns]
  (letfn [(rec [sym]
            (if-let [deps (seq (dep/immediate-dependents graph sym))]
              (reduce into [] (conj (mapv rec deps) deps))))]
    (reverse (distinct (rec ns)))))

(defn run-ns
  [ns zippers dep-graph test-fn]
  (let [cur-ns (symbol (str *ns*))
        deps (dependants dep-graph ns)
        forms (map z/string zippers)
        zipped (zipmap forms zippers)]
    (for [candidate forms
          mutant (mutants (zipped candidate)
                          (paths-in-zipper (zipped candidate)))]
      (try
        (in-ns ns)
        (doseq [form forms]
          (if (= form candidate)
            (eval (z/sexpr mutant))
            (eval (read-string form))))
        (doseq [dep deps]
          (require dep :reload))
        (if (test-fn)
          {:survivor {:mutant (z/string mutant)
                      :original candidate
                      :ns ns}}
          {})
        (catch Throwable ex
          {})
        (finally
          (require ns :reload)
          (doseq [dep deps]
            (require dep :reload))
          (in-ns cur-ns))))))
