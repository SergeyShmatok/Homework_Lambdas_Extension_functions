package ru.netology


fun main() {

    println(ChatService.sendMessage(Person(1), Person(2), Message(text = "Сообщение: 1")))
    println(ChatService.sendMessage(Person(2), Person(1), Message(text = "Сообщение: 2")))
    println(ChatService.sendMessage(Person(1), Person(2), Message(text = "Сообщение: 3")))

    println(ChatService.sendMessage(Person(3), Person(1), Message(text = "Сообщение: 4")))
    println(ChatService.sendMessage(Person(3), Person(2), Message(text = "Сообщение: 5")))


    ChatService.getAllMessages().forEach { println(it) }

    println()

    ChatService.chats.forEach { it.messages.forEach { println(it) } }


    ChatService.editMessage(3, 5, " -EDITED-")

    println()

    ChatService.chats.forEach { it.messages.forEach { println(it) } }

    println()

    ChatService.deleteChat(2)

    ChatService.chats.forEach { it.messages.forEach { println(it) } }

    println()

    ChatService.deleteMessage(3,5)

    println()

    ChatService.chats.forEach { it.messages.forEach { println(it) } }

    println()

    println(ChatService.getChatsCustom())

    println()

    println(ChatService.getMessagesCustom(1))

    println()

    println(ChatService.getMessagesById(1, 1, 2))

    println()

    println(ChatService.getMessagesById(3, 3, 6))


}