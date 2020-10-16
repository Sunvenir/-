// function SocketObj(){
// 	this.socket = null;
//     this.openSocket = function(uid,onMessge) {
//         if(typeof(WebSocket) == "undefined") {
//             console.log("您的浏览器不支持WebSocket");
//         }else{
//             console.log("您的浏览器支持WebSocket");
//             var socketUrl="ws://139.9.216.155:8888/socket/"+uid;
//             // var socketUrl="ws://localhost:9000/socket/"+uid;
//             socketUrl=socketUrl.replace("https","ws").replace("http","ws");
//             console.log(socketUrl);
//             if(this.socket!=null){
//                 this.socket.close();
//                 this.socket=null;
//             }
//             this.socket = new WebSocket(socketUrl);
//             this.socket.onopen = function() {
//                 console.log("websocket已打开");
//                 //socket.send("这是来自客户端的消息" + location.href + new Date());
//             };
//             this.socket.onmessage = function(msg) {
//                 onMessge(msg)
//             };
//             this.socket.onclose = function() {
//                 console.log("websocket已关闭");
//             };
//             this.socket.onerror = function() {
//                 console.log("websocket发生了错误");
//             }
//         }
//     }
//     this.sendMessage = function(to,message) {
//         if(typeof(WebSocket) == "undefined") {
//             console.log("您的浏览器不支持WebSocket");
//         }else {
//             // console.log("您的浏览器支持WebSocket");
//             // console.log('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
//             this.socket.send('{"toUserId":"'+to+'","contentText":"'+message+'"}');
//         }
//     }
// }
// var Socket = new SocketObj()
// export default Socket