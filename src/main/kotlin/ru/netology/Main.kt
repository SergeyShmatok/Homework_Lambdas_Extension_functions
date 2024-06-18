package ru.netology


fun main() {


    println(ChatService.sendMessage(Person(1), Person(2), Message(text = "Сообщение: 1")))
    println(ChatService.sendMessage(Person(2), Person(1), Message(text = "Сообщение: 2")))
    println(ChatService.sendMessage(Person(1), Person(2), Message(text = "Сообщение: 3")))


    println(ChatService.sendMessage(Person(3), Person(1), Message(text = "Сообщение: 4")))
    println(ChatService.sendMessage(Person(3), Person(2), Message(text = "Сообщение: 5")))

    println()

    ChatService.chats.forEach { println(it) }

    println()

    println(ChatService.getMessagesById(1, 1, 3))

    println()

    ChatService.deleteMessage(3, 5)

    println()

    ChatService.chats.forEach { println(it) }

    println()

}

