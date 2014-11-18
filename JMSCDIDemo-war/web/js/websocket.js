var websocketSession;

function f_onmessage(evt) {
    console.log("receiving message:",evt.data);
    websocketMessages = document.getElementById('websocketMessages');
    websocketMessages.innerHTML = websocketMessages.innerHTML + evt.data + '<br/>';
}

function open() {
    if (!websocketSession) {
        console.log("opening websocket");
        websocketSession = new WebSocket('ws://' + document.location.host + document.location.pathname +'websocket');
        websocketSession.onmessage = f_onmessage;
    }
}

function close() {
    if (websocketSession) {
        console.log("closing websocket");
        websocketSession.close();
    }
}

function sendMessage(msg) {
    console.log("sending message:",msg);
    websocketSession.send(msg);
}