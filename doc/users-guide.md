# Mutant: User's guide

Hey there, great to have you here!
Thanks for taking the time to read this file.
The goal of this document is to:

  - introduce you to basic concepts of mutation testing,
  - help you run Mutant on your Clojure projects and understand results you'll
    see, and
  - discuss current limitations of Mutant.

Running Mutant is as simple as executing the one-liner you've seen in
[README.md][readme].
In this document we're going dig a bit deeper into the topic.

## Theoretical introduction

Writing correct software is difficult.
To make sure that programmes we write are correct we often accompany them with
tests.
When you use Leiningen to create a new Clojure project skeleton you get your first
test namespace for free.
It has a single test inside, which is broken and you have to fix it.
It's a good first step.
It encourages us to test our code from the very beginning.

```clojure
    (ns project.core-test
      (:require [clojure.test :refer :all]
                [project.core :refer :all]))

    (deftest a-test
      (testing "FIXME, I fail."
        (is (= 0 1))))
```

There are some really powerful testing tools at our disposal in the Clojure
ecosystem.
test.check is one of them.
This property-based testing library allows us to make general claims about our
code and have them subjected to an awful lot of automatically generated test
cases.
Let's look at a simple example.
We can use test.check to verify that sorting a vector once yields the same
result as sorting it twice in a row.

```clojure
    (require '[clojure.test.check
               [generators :as gen]
               [properties :as prop]])

    (def prop-sort-idempotency
      (prop/for-all [coll (gen/vector gen/int)]
        (= (sort (sort coll))
           (sort coll))))
```

The problem with tests is that we have to _write_ them.
If writing correct software is difficult, how can we expect that writing correct
tests will be any easier?
Who is _testing our tests_?
How we can trust them to keep bugs and regressions at bay?

Mutation testing is a method of evaluating the quality of our test suites.
In essence, it tests our tests and shows us where are they lacking.
Before we move on to discussing how it works, let's talk about its theoretical
foundations.

### Two hypotheses

Mutation testing is based on two hypotheses: the competent programmer
hypothesis and the coupling effect hypothesis.
Let's discuss them.

According to the competent programmer hypothesis, programmes written by a
_competent programmer_—like you and me—differ from _correct_ programmes only by
a small number of faults.
What do we expect from our test suite then?
We want our tests to find those few faults we accidentally left in our
implementation.

How can we judge the quality of our test suite?
We can manually introduce faults into our programme and see whether the test
suite finds and reports those bugs.
But we don't really want to do it by hand.
It would be great if we could automate it.
Imagine a tool which automatically injects faults resembling real bugs and lets
us know if our test suite finds those synthetic problems.

But there are so many ways to introduce bugs!
There are so many ways in which we could break our software.
Moreover, if you take into account more complex faults, which are combinations
of simple ones, you end up with an enormous number of bugs to introduce.
This is when the second hypothesis comes into play.

According to the coupling effect hypothesis, a test suite detecting simple
faults will also detect complex ones caused by combinations of those
simple bugs.
Complex faults are thus _coupled_ to simple ones through a test suite which
will detect both of them.
This leads us to a striking observation.

We don't need to introduce complex combinations of synthetic bugs into our
programmes.
We can focus only on injecting simple ones and introduce them one at a
time.
As long as the coupling hypothesis holds and our test suite detects simple
faults, we can trust our tests to detect complex faults as well.

This brings us to mutation testing, a technique of evaluating the quality of our
programmes' test suites.
Mutation testing injects simple faults a _competent programmer_ would introduce.
It subjects those faults to our test suite, one after another.
If our tests catch those bugs we can rest assured that our tests will
discover even more complex faults _coupled_ to the simple ones.
If faults aren't detected we know that our test suite is lacking.

Mutation testing has been a subject of research since 1970s, resulting in
hundreds of academic papers.
[Existing research][jia] gives backing to both the competent programmer and the
coupling effect hypothesis.
It confirms that mutation testing is a valid approach of evaluating the quality
of our tests and helping us improve them.

[jia]: http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.221.4005

### Vocabulary

Before we wrap up the theoretical introduction, let's define our dictionary.
Here are some terms you see often in mutation testing literature.

  - A *mutant* is a faulty version of our programme. It has a synthetic
    bug introduced by our mutation testing tool.
  - We use our tests to *kill* the faulty version of our programme. If our tests
    detect the bug in the faulty version, it's *dead*.
  - A *survivor* is a faulty version of our programme which wasn't detected by
    our tests.

*Mutants* which don't get *killed* by our tests become *survivors*.
The goal of our test suite is to leave no *survivors*.

## Mutation testing

_to be continued_

[readme]: https://github.com/jstepien/mutant/blob/master/README.md
