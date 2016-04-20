# Conway's Game of Life
![Screenshot](screenshot.png)

An experimental, multiplayer, client-server implementation of Conway's Game of Life.

### Server
The Java backend calculates the Game of Life cells using all available CPU cores, storing the state in a combination of immutable and mutable fixed-length bit strings.

### Client
The HTML5 frontend receives the current state using WebSockets and displays a 3D user interface using WebGL. The same WebSockets are used to transfer commands to the Server.


## Build or [Download](https://github.com/mr-max/conways-game-of-life/releases/download/v1.0/life-1.0.jar)
```
bower install
./gradlew jar
cd build/libs/
```

## Run
```
java -jar life-1.0.jar
```
This starts the game with a 100x100 grid. Access it on `http://localhost:8080`

To start the Game with a custom Grid size (e.g. 200x200) call:
```
java -jar life-1.0.jar 200
```

# License

> The MIT License (MIT)
> 
> Copyright (c) 2016 [Max Vogler](https://www.maxvogler.de/)
> 
> Permission is hereby granted, free of charge, to any person obtaining a copy
> of this software and associated documentation files (the "Software"), to deal
> in the Software without restriction, including without limitation the rights
> to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
> copies of the Software, and to permit persons to whom the Software is
> furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be included in all
> copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
> IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
> FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
> AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
> LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
> OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
> SOFTWARE.
