let stompClient = null;

let chatId = null;
let userId = null;
let firstBlock = document.getElementById('firstBlock');
let secondBlock = document.getElementById('chat-page');
let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

const host = 'http://localhost:8083';

function createChat() {
    let firstUserId = document.getElementById('firstUserId').value;
    let secondUserId = document.getElementById('secondUserId').value;
    let request = new XMLHttpRequest();
    request.open("POST", host + "/api/v1/chats/create/users/" + firstUserId + "/" + secondUserId, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.addEventListener("readystatechange", () => {
        if (request.readyState === 4 && request.status === 200) {
            let json = JSON.parse(request.responseText);
            chatId = json.id;
            userId = firstUserId;
            connect();
        }
    });

    request.send();
}

function connect() {
    if (chatId == null) {
        chatId = document.getElementById('chatId').value;
    }
    if (userId == null) {
        userId = document.getElementById('userId').value;
    }
    console.log(chatId);
    let socket = new SockJS(host + '/SPSA');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        firstBlock.style.display = "none";
        secondBlock.style.display = "block";
        stompClient.subscribe('/spsa/chat/' + chatId, resp => {
            onMessageReceived(JSON.parse(resp.body));
        });
    });
}

function sendMessage() {
    let message = document.getElementById('message').value;
    console.log(chatId, userId);
    stompClient.send('/send/chats/' + chatId + '/user/' + userId, {},
        JSON.stringify(
            {
                'message': message
            })
    );
}

function deleteMessage(id) {
    let message = document.getElementById('message').value;
    console.log(chatId, userId);
    stompClient.send('/delete/' + id + '/chats/' + chatId + '/user/' + userId, {});
}

function updateMessage(id) {
    let message = document.getElementById('message').value;
    console.log(chatId, userId);
    stompClient.send('/update/' + id + '/chats/' + chatId + '/user/' + userId, {},
        JSON.stringify(
            {
                'message': message
            })
    );
}

function onMessageReceived(payload) {
    if (payload.delete) {
        document.getElementById("" + payload.mainMessageDto.id).remove();
    }
    if (payload.update) {
        document.getElementById("p-" + payload.mainMessageDto.id).innerText = payload.mainMessageDto.message;
    } else {
        let messageElement = document.createElement('li');

        messageElement.classList.add('chat-message');
        messageElement.setAttribute("id", payload.id);

        let avatarElement = document.createElement('i');
        let avatarText = document.createTextNode(payload.sender.name[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(payload.sender.name);

        messageElement.appendChild(avatarElement);

        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(payload.sender.name);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        let textElement = document.createElement('p');
        textElement.setAttribute("id", "p-" + payload.id);
        let messageText = document.createTextNode(payload.message);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        if (payload.sender.id == userId) {
            messageElement.setAttribute('onclick', 'messageClick(' + payload.id + ')');
            let container = document.createElement('div');
            container.setAttribute('id', 'buttons-' + payload.id);
            container.style.display = "none";
            let updateButton = document.createElement('button');
            // updateButton.style.display = "none";
            updateButton.setAttribute('class', 'btn btn-success btn-block accent');
            updateButton.setAttribute('type', 'submit');
            updateButton.setAttribute('onclick', 'updateMessage(' + payload.id + ')');
            updateButton.setAttribute('id', 'updateBtn' + payload.id);

            let deleteButton = document.createElement('button');
            deleteButton.setAttribute('class', 'btn btn-success btn-block accent');
            // deleteButton.style.display = "none";
            deleteButton.setAttribute('type', 'submit');
            deleteButton.setAttribute('onclick', 'deleteMessage(' + payload.id + ')');
            deleteButton.setAttribute('id', 'deleteBtn' + payload.id);

            container.appendChild(updateButton);
            container.appendChild(deleteButton);
            messageElement.appendChild(container);
        }

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;

        if (payload.sender.id == userId) {
            document.getElementById('deleteBtn' + payload.id).innerText = "Delete";
            document.getElementById('updateBtn' + payload.id).innerText = "Update";
        }
    }
}

function messageClick(id) {
    let updateMessage = document.getElementById("buttons-" + id);
    if (updateMessage.style.display === "block") {
        updateMessage.style.display = "none";
    } else if (updateMessage.style.display === "none") {
        updateMessage.style.display = "block";
    }
}


function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    let index = Math.abs(hash % colors.length);
    return colors[index];
}