# Mutant

A [mutation testing][wiki] library for Clojure.

> *tl;dr:* Mutant breaks your code and lets you know if your test suite is
> still green.

Mutant tries to break your code. It goes through your namespaces and
introduces changes to your sources. After each mutation it attempts to run
your test suite. If all tests still pass despite the mutation Mutant reports
the mutation as a survivor.

[wiki]: https://en.wikipedia.org/wiki/Mutation_testing

## Usage

1. Add `mutant` to the dependencies in your `project.clj`

``` [mutant "0.0.0"] ```

2. Start a repl (e.g. with `lein repl`). Ensure that your current tests pass.

3. Make sure your test namespaces are loaded.

4. Now run Mutant:

```clojure
user=> (require 'mutant.core 'mutant.helpers.clojure-test)

user=> (def results
         (mutant.core/run "src" "test" mutant.helpers.clojure-test/test-fn))

user=> (spit "results" (with-out-str (mutant.core/pprint (last results))))
```

5. Print the report or save it in a file and highlight it with [colordiff][cd].

![coloured diff](https://stepien.cc/~jan/mutant/pretty.png)

[cd]: http://www.colordiff.org/

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

## Maturity status

Wildly experimental 🔥

## Related work

  - Markus Schirp's [Mutant](https://github.com/mbj/mutant/) is a mutation
    testing tool for Ruby.

## License

    Copyright 2016 Jan Stępień <jan@stepien.cc>

    Licensed under the Apache License, Version 2.0 (the "License"); you may
    not use this file except in compliance with the License. You may obtain
    a copy of the License at https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
