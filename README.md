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

[![lein-mutate on Clojars](https://img.shields.io/clojars/v/lein-mutate.svg)](https://clojars.org/lein-mutate)

Add the `lein-mutate` to in the `:plugins` vector of either your `:user` profile
or your project.clj. Make sure your test suite is green. Then run:

    $ lein trampoline mutate

Learn more in the [user's guide][ug].

## Documentation

The documentation lives in the `doc` directory.
It's currently divided into two parts:

  - [the user's guide][ug], and
  - [the contributor's guide][cg].

Take a look, let me know what you think, and if anything is unclear feel free to
submit patches!

[ug]: https://github.com/jstepien/mutant/blob/master/doc/users-guide.md
[cg]: https://github.com/jstepien/mutant/blob/master/doc/contributors-guide.md

## Maturity status

Wildly experimental 🔥

## Related work

  - Markus Schirp's [Mutant](https://github.com/mbj/mutant/) is a mutation
    testing tool for Ruby.
  - Henry Coles' [Pitest](https://github.com/hcoles/pitest) is a state of the
    art mutation testing system for the JVM.

## License

    Copyright 2016–now Jan Stępień <jan@stepien.cc>

    Licensed under the Apache License, Version 2.0 (the "License"); you may
    not use this file except in compliance with the License. You may obtain
    a copy of the License at https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
