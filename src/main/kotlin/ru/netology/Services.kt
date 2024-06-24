package ru.netology


data class Chat(
    val id: Int? = 0,
    val title: String? = " ",
    val creatorID: Int? = 0, // id создателя чата
    val interlocutorId: Int? = 0, // id собеседника
    var messages: Sequence<Message> = sequenceOf()
)

data class Message(
    val id: Int? = 0, // собственный id сообщения
    var text: String? = "text",
    val commentChatID: Int? = 0, // id чата в котором отправили это сообщение (равен id этого чата)
    var deleted: Boolean = false, // параметр, определяющий удалено ли сообщение
    var unread: Boolean = true, // параметр, определяющий прочитано ли сообщение
    val creatorID: Int? = 0, // id создателя сообщения (равен его id)
)

data class Person(
    val id: Int? = 0, // id пользователя
)


object ChatService {


    var chats = sequenceOf<Chat>()

    private var nextChatId: Int = 0
    private var nextMessageId: Int = 0


    fun sendMessage(
        creator: Person,
        interlocutor: Person,
        message: Message
    ): Int? {  // Отправка сообщения от одного существующего пользователя другому
        // (оно же и создание). Отправка первого сообщения создаёт чат.
        val predicate = { chat: Chat ->
            chat.creatorID == creator.id && chat.interlocutorId == interlocutor.id
                    || chat.creatorID == interlocutor.id && chat.interlocutorId == creator.id
        }

        val isThereAlreadyChat = chats.any(predicate) // Проверка, что чат между этими участниками уже есть

        if (!isThereAlreadyChat) {
            chats += Chat(id = ++nextChatId, creatorID = creator.id, interlocutorId = interlocutor.id)
            chats.last().messages += Message(
                id = ++nextMessageId, commentChatID = chats.last().id,
                text = message.text, creatorID = creator.id
            )
            return chats.last().id // После успешного выполнения возвращает идентификатор созданного чата
        }
        val generalChat = chats.first(predicate)
        chats.first { it.id == generalChat.id }.messages += Message(
            id = ++nextMessageId, commentChatID = generalChat.id,
            text = message.text,
            creatorID = creator.id
        )
        return 0

    }

    // редактирует указанное сообщение, отредактированное сообщение становится прочитанным.
    fun editMessage(chatId: Int, messageId: Int, text: String) = chats.singleOrNull{ it.id == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("- Чат не найден -")}.singleOrNull{it.id == messageId && !it.deleted}
            .let { it ?: throw MessageNotFoundException("Сообщение не найдено")}
            .let { it.text = text; it.unread = false }



    fun deleteChat(chatId: Int) { // удаление чата и связанной с ним информации
        val (deletedChat, remainingCollection) = chats.partition { it.id == chatId }
        fun solution() {
            chats = remainingCollection.asSequence()
        }

        if (chats.none{it.id == chatId}) throw ChatNotFoundException("- Чат не найден -") else solution()

    }

    // меняет параметр deleted, указанного сообщения на true
    fun deleteMessage(chatId: Int, messageId: Int)= chats.singleOrNull { it.id == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("- Чат не найден -")}.singleOrNull{it.id == messageId}
            .let { it ?: throw MessageNotFoundException("Сообщение не наёдено")}
            .let { it.deleted = true; it.unread = false }




    fun getChatsCustom(): List<Chat> {

        val solution = chats.toList()

        if (solution.isEmpty()) throw ChatNotFoundException("Чаты не обнаружены") else return solution // выдаёт список чатов
    }


    fun getUnreadChatsCount(): List<Chat> {  // выдаёт список чатов с непрочитанными сообщениями

        val sortList = chats.filter { chat -> chat.messages.any { it.unread } }.toList()

        if (sortList.isEmpty()) throw ChatNotFoundException("Чаты не обнаружены") else return sortList

    }


    fun getMessagesCustom(chatId: Int): List<Message> { // выдаёт список сообщений из чата по id (удалённые не отдаст)

        val sortList = chats.singleOrNull{ it.id == chatId}
            .let { it?.messages ?: throw ChatNotFoundException("- Чат не найден -")}
            .filter { !it.deleted}
            .ifEmpty { throw MessageNotFoundException("- Сообщений нет -")}
            .toList()

        sortList.forEach { it.unread = false }

        return sortList
    }


    fun getAllMessages(): List<Message> {  // выдаёт все 'unread' (последние) сообщения из чатов

        val sortList = mutableListOf<Message>()

        chats.forEach { chat -> chat.messages.filter {(it.unread)}.forEach {sortList += it; it.unread = false} }


        if (sortList.isEmpty()) throw ChatNotFoundException("Сообщения не обнаружены") else return sortList

    }


    fun getMessagesById(chatId: Int, personId: Int, countMessages: Int): List<Message> { // выдаёт указанное количество сообщений по id пользователя

        val sortList = chats.singleOrNull{ it.id == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("- Чат не найден -")}
            .filter { it.creatorID == personId }
            .ifEmpty {throw MessageNotFoundException("- Сообщений нет -")}
            .toList()
            .takeLast(countMessages)

        sortList.forEach { it.unread = false }

        return sortList

    }

    // Все методы получения сообщений меняют параметр 'unread' сообщений на false

    fun clear() {    // Очистка сервиса
        chats = emptySequence()
        nextChatId = 0
        nextMessageId = 0
    }


}

class ChatNotFoundException(message: String) : RuntimeException(message)

class MessageNotFoundException(message: String) : RuntimeException(message)


