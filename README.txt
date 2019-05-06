Tyler Tech Software Development Challenge 2018

[Author]
    Name: Kyle Goodale
    Web: kylegoodale.com

[Building]
    Note:   This project requires Java 9 or later.
            This project also requires Maven.
            Instructions for installing Maven can be found here:
            https://books.sonatype.com/mvnex-book/reference/installation-sect-maven-install.html

    With Maven installed run `mvn install` in the root directory of this project to download all dependencies and compile it.

    The project should now be built and the resulting jar should be available in the 'target' subdirectory

[Running]
    After building the application navigate to the 'target' subdirectory.

    The application takes a single argument which should be the path to the input file. The input file is expected to be
    named input.X.json where X is any integer

    From there you can run the application using:
        `java -jar tylertechchallenge2018-1.0-jar-with-dependencies.jar ./inputs/input.X.json`

    The application expects there to be an existing outputs directors in a subdirectory from it,
    and will output the result of the order as a JSON file at  ./outputs/KYLE_GOODALE.X.json



[License]

    Copyright 2018 Kyle Goodale

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
