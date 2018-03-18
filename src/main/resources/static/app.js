function connect() {
	var socket = new SockJS('/websocket');
	stompClient = Stomp.over(socket);
	
	stompClient.connect({}, function (frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/chat',
				function (chatMessage) {
					showChatMessage(JSON.parse(chatMessage.body));			
				});
		stompClient.send("/app/send", {}, JSON.stringify({'messageType': 'JOIN'}));
	});

}

function showChatMessage(chatMessage){
	console.log("message: "+chatMessage);
	//$("#chat").append("<p>" + chatMessage.message + "</p>");
	
	if(chatMessage.messageType === 'JOIN') {
		$("#chat").append('<p>'+chatMessage.name+' has joined</p>');
	}else if(chatMessage.messageType === 'LEAVE'){
		$("#chat").append('<p>'+chatMessage.name+' has left</p>');
	}else{
	$("#chat").append('<div class="card mb-2"><div class="card-body"><h5 class="card-title">'+chatMessage.name+'</h5><p class="card-text">'+ chatMessage.message +'</p></div></div>');
	}
}

function sendMessage(){
	stompClient.send("/app/send", {}, JSON.stringify({'message': $("#message").val(), 'messageType': 'CHAT'}));
	$("#message").val('');
}

$(document).ready(function () {
	connect();
	$("#chat-form").submit(function(e){
        e.preventDefault();
    });


    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});