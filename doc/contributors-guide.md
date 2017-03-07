# Mutant: Contributor's guide

Hi, thanks for opening the contributor's guide.
The goal of this document is to discuss how Mutant works internally.
After reading this document you should be able to extend Mutant and contribute
changes to its source code.
If that's not the case, please send a patch fixing it!

## lein-mutate: Leiningen plugin

_in which lein-mutate is presented as a shallow layer atop mutant proper_

## The library itself

_some copy-pasta from the readme below_

### API

The public API consists of two functions and one helper.

#### `(mutant.core/run source-directory test-directory test-fn)`

Subjects code in `source-directory` to mutation testing. After each
mutation it reloads relevant namespaces found in `source-directory`
and `test-directory` and invokes `test-fn`. `test-fn` should throw
or return a falsey value upon failure.

Returns a lazy seq of maps of results consisting of following keys:

  - `:total` is the total number of generated mutants so far, and
  - `:survivors` is a sequence of mutants which didn't cause `test-fn`
    to report failure. Each mutant is a map with following keys:
    - `:ns` is the mutant's namespace,
    - `:original` is the original form, and
    - `:mutant` is the mutated form.

#### `(mutant.core/pprint results)`

Pretty-prints `results` returned by `mutant.core/run`.

#### `(mutant.helpers.clojure-test/test-fn)`

A simple wrapper around `clojure.test/run-all-tests`, which makes it return
false in case of any failures or errors. An optional argument is a regular
expression passed to `clojure.test/run-all-tests`.

## Mutation testing mutant

_and now the tricky bit_
