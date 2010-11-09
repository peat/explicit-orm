# Explicit ORM

This is a very simple ORM built in Scala and targeting MongoDB.  There is also a partially completed JSON web service included; don't expect that part to work.

Explicit ORM was written as a demonstration for the Portland Scala User Group (PDXScala.org)

The philosophy is to be explicit about how data is mapped from Scala objects into MongoDB, and also into JSON -- every class has a corresponding object that directly maps values from an instance into the data store.  

There is no magic.

_Note: This project was a demonstration, and is not actively developed ... unless, of course, you'd like to contribute something!_

## The Basics

The core of the code is in `src/main/scala/db` and consists of three files:

* _Database.scala_ contains an object with hardcoded connection values for a MongoDB.
* _Factory.scala_ contains a trait that provides the fundamental mapping functions to a mapping object.
* _Persistable.scala_ contains a trait that provides metadata storage to instances of mapped records.

## Batteries Included

There are example classes and objects, found in `src/main/scala/models`

* _Material.scala_ contains a `Material` class (containing record data) and a `Material` object (containing the mapping logic) for a simple material with a name, part number, and a list of associated `Component` records.
* _Component.scala_ contains a similar arrangement for the `Component` class and object.

The two are related in the sense that a `Material` has many `Component`s.

If you're of the testing persuasion, there is a relatively full set of ScalaTest suites in `src/test/scala` for the `db` and `model` packages.  They show some basic demonstrations of how to instantiate, persist, and retrieve records. 

## Running

Explicit ORM is built with SBT, and uses MongoDB for persistence.  

You will need to manually start your MongoDB server in order to run the tests.  The tests will create/destroy/mangle a collection named "test" -- if you'd like to change this, you can alter the value in `src/main/scala/db/Database.scala`

`sbt test` to run ScalaTest for the project.

## License

Explicit ORM is released under a BSD license, as follows:

Copyright (c) 2010, Peat Bakke

All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of Explicit ORM nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

