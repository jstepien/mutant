# lein-mutate

A Leiningen plugin to run mutation analysis on your Clojure projects.

## Usage

Put the `lein-mutate` artifact into the `:plugins` vector of either your `:user`
profile or your project.clj. Make sure your test suite is green. Then run:

    $ lein mutate

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
