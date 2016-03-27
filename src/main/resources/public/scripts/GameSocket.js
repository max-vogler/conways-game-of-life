function GameSocket() {
    this.onGameUpdated = function (){};
    var _this = this;

    this.websocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/api/v1/connect");
    this.websocket.binaryType = 'arraybuffer';
    this.websocket.onmessage = function (buffer) {
        _this.onGameUpdated(buffer);
    };

    this.send = function (d) { this.websocket.send(d) };

}