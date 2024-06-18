package ru.netology


data class Chat(
    val id: Int? = 0,
    val title: String? = " ",
    val creatorID: Int? = 0, // id создателя чата
    val interlocutorId: Int? = 0, // id собеседника
    var messages: MutableList<Message> = mutableListOf()
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


    var chats = mutableListOf<Chat>()

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
        val generalChat = chats.filter(predicate)
        chats.forEach {
            if (it.id == generalChat.last().id) it.messages += Message(
                id = ++nextMessageId,
                commentChatID = generalChat.last().id, text = message.text, creatorID = creator.id
            )
        }

        return 0

    }

    fun editMessage(chatId: Int, messageId: Int, text: String) {   // редактирует указанное сообщение, отредактированное сообщение становится прочитанным.
        val solution = chats.forEach { chat -> if (chat.id == chatId) chat.messages.forEach {
                if (it.id == messageId && !it.deleted) it.text = text
                it.unread = false
            }
        }

        if (ifThereAreNoMessages(chatId, messageId)) throw MessageNotFoundException("- Сообщение не найдено -") else return solution

    }

    fun deleteChat(chatId: Int) { // удаление чата и связанной с ним информации
        val (deletedChat, remainingCollection) = chats.partition { it.id == chatId }
        fun solution() {
            chats = remainingCollection.toMutableList()
        }

        if (ifThereAreNoChats(chatId)) throw ChatNotFoundException("- Чат не найден -") else solution()

    }


    fun deleteMessage(chatId: Int, messageId: Int) { // меняет параметр deleted, указанного сообщения на true

        val sortList1 = chats.filter {it.id == chatId}

        if (sortList1.isEmpty()) throw ChatNotFoundException ("- Чат не найден -")

        fun solution () = sortList1.last().messages.forEach { if (it.id == messageId) it.deleted = true
            it.unread = false
        }

        if (ifThereAreNoMessages(chatId, messageId)) throw MessageNotFoundException("- Сообщение не найдено -") else solution()

    }


    fun getChatsCustom(): List<Chat> {
        val sortList = mutableListOf<Chat>()

        chats.forEach { sortList += it }

        if (sortList.isEmpty()) throw ChatNotFoundException("Чаты не обнаружены") else return sortList // выдаёт список чатов
    }


    fun getUnreadChatsCount(): List<Chat> {  // выдаёт список чатов с непрочитанными сообщениями

        val sortList = chats.filter { chat -> chat.messages.any { it.unread } }


        if (sortList.isEmpty()) throw ChatNotFoundException("Чаты не обнаружены") else return sortList

    }


    fun getMessagesCustom(chatId: Int): List<Message> { // выдаёт список сообщений из чата по id (удалённые не отдаст)

        val sortList1 = chats.filter {it.id == chatId}

        if (sortList1.isEmpty()) throw ChatNotFoundException ("- Чат не найден -")

        val sortList2 = sortList1.last().messages.filter { !it.deleted }

        sortList2.forEach { it.unread = false }

        if (sortList2.isEmpty()) throw MessageNotFoundException("- Сообщения не найдены -") else return sortList2

    }

    fun getAllMessages(): List<Message> {  // выдаёт все 'unread' (последние) сообщения из чатов

        val sortList = mutableListOf<Message>()

        val solution = chats.forEach { chat -> chat.messages.forEach { if (it.unread) sortList += it } }

        sortList.forEach { it.unread = false }

        if (sortList.isEmpty()) throw ChatNotFoundException("Чаты не обнаружены") else return sortList

    }


    fun getMessagesById(chatId: Int, personId: Int, countMessages: Int): List<Message> { // выдаёт указанное количество сообщений по id пользователя

        val sortList1 = chats.filter {it.id == chatId}

        if (sortList1.isEmpty()) throw ChatNotFoundException ("- Чат не найден -")

        val sortList2 = sortList1.last().messages.filter { it.creatorID == personId }.takeLast(countMessages)

        sortList2.forEach{ it.unread = false }

        if (sortList2.isEmpty()) throw MessageNotFoundException ("- Сообщение не найдено -") else return sortList2


    }

        // Все методы получения сообщений меняют параметр 'unread' сообщений на false

    fun clear() {    // Очистка сервиса
        chats.clear()
        nextChatId = 0
        nextMessageId = 0
    }



    fun ifThereAreNoChats(chatId: Int) = chats.none { it.id == chatId } // метод (для исключений) определения наличия чата с указанным id в списке чатов


    fun ifThereAreNoMessages(chatId: Int, messageId: Int): Boolean { // то же самое, только с сообщениями
        val sortList = chats.filter { it.id == chatId }

        fun solution() = sortList.last().messages.none { it.id == messageId && !it.deleted }

        return if (sortList.isEmpty()) true else solution()
    }


}

class ChatNotFoundException(message: String) : RuntimeException(message)

class MessageNotFoundException(message: String) : RuntimeException(message)
