# FHIR Diff

FHIR standard comparison web application.

## Usage

To compare two FHIR standards you will need to download FHIR definitions in
JSON format(latest one can be found [here](https://www.hl7.org/fhir/definitions.json.zip))
unpack it and upload two `profiles-resources.json` files - one for old standard
and another one for new standard to application via web interface.

## Prerequisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.0.0 or above installed.
Also you will need node/npm for front-end dependencies.

## Running

Run following command to install npm dependencies:

    lein npm install

To start a web server for the application, run:

    lein ring server

## License

Copyright (c) 2017 Okasu

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
