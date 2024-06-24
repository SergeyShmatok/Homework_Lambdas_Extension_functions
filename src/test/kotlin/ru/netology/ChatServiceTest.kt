package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.netology.ChatService.chats
import ru.netology.ChatService.sendMessage

class ChatServiceTest {


    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

//-----------------------------------------


    @Test
    fun sendMessage_isThereAlreadyChat_true_1() {
        val chat = Chat(id = 1, "", 1, 2)
        chats += chat
        val result = sendMessage(Person(1), Person(2), Message(1))

        assertEquals(0, result)

    }

    @Test
    fun sendMessage_isThereAlreadyChat_true_2() {
        val chat = Chat(id = 1, "", 1, 2)
        chats += chat
        val result = sendMessage(Person(2), Person(1), Message(1))

        assertEquals(0, result)

    }


    @Test
    fun sendMessage_isThereAlreadyChat_false() {

        val result = sendMessage(Person(1), Person(2), Message(1))

        assertEquals(1, result)
    }


//-----------------------------------------


    @Test
    fun editMessage() {
        val message = Message(1, " ")
        val chat = Chat(1)
        chat.messages += message
        chats += chat

        ChatService.editMessage(1, 1, "qwe")

        assertEquals("qwe", chats.last().messages.last().text)

    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessage_exception_1() {
        val message = Message(2)
        val chat = Chat(1)
        chat.messages += message
        chats += chat
        ChatService.editMessage(1, 1, "qwe")

    }

    @Test(expected = ChatNotFoundException::class)
    fun editMessage_exception_2() {

        ChatService.editMessage(1, 1, "qwe")

    }


    @Test(expected = ChatNotFoundException::class)
    fun editMessage_exception_3() {
        val chat = Chat(1)
        chats += chat

        ChatService.editMessage(2, 1, "qwe")

    }




//-----------------------------------------

    @Test
    fun deleteChat() {
        val chat = Chat(id = 1)
        chats += chat
        ChatService.deleteChat(1)
        assertTrue(chats.none())
    }


    @Test(expected = ChatNotFoundException::class)
    fun deleteChat_exception_1() {
        val chat = Chat(id = 2)
        chats += chat

        ChatService.deleteChat(1)

    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChat_exception_2() {

        ChatService.deleteChat(1)

    }

//---------------------------------------

    @Test
    fun deleteMessage() {
        val message = Message(1)
        val chat = Chat(1)
        chat.messages += message
        chats += chat

        ChatService.deleteMessage(1, 1)

        assertTrue(chats.last().messages.last().deleted)
    }


    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage_exception_1() {
        val message = Message(2)
        val chat = Chat(1)
        chat.messages += message
        chats += chat

        ChatService.deleteMessage(1, 1)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteMessage_exception_2() {

        ChatService.deleteMessage(1, 1)

    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteMessage_exception_3() {
        val chat = Chat(1)
        chats += chat

        ChatService.deleteMessage(2, 1)

    }

//-----------------------------------------

    @Test
    fun getChatsCustom() {
        val chat1 = Chat()
        val chat2 = Chat()

        chats += listOf(chat1, chat2)

       assertTrue(ChatService.getChatsCustom().size == 2)

    }

    @Test(expected = ChatNotFoundException::class)
    fun getChats_exception() {

        ChatService.getChatsCustom()

    }



    @Test
    fun getUnreadChatsCount() {

        val chat1 = Chat(1)
        val chat2 = Chat(2)

        val message1 = Message(1)
        val message2 = Message(1, unread = false)

        chat1.messages += message1
        chat2.messages += message2

        chats += listOf(chat1, chat2)

        assertTrue(ChatService.getUnreadChatsCount().size == 1)

    }



    @Test(expected = ChatNotFoundException::class)
    fun getUnreadChatsCount_exception() {

        ChatService.getUnreadChatsCount()

    }
//-----------------------------------------

    @Test
    fun getMessagesCustom() {

        val chat1 = Chat(1)
        val chat2 = Chat(2)

        val message1 = Message(1)
        val message2 = Message(2)
        val message3 = Message(3, deleted = true)

        chat1.messages += message1
        chat2.messages += listOf(message2, message3)

        chats += listOf(chat1, chat2)

        assertTrue(ChatService.getMessagesCustom(2).size == 1)
    }


    @Test(expected = ChatNotFoundException::class)
    fun getMessagesCustom_exception_1() {

        ChatService.getMessagesCustom(1)

    }



    @Test(expected = MessageNotFoundException::class)
    fun getMessagesCustom_exception_2() {

        val chat1 = Chat(1)

        val message1 = Message(1, deleted = true)

        chat1.messages += message1

        chats += chat1

        ChatService.getMessagesCustom(1)

    }





    @Test
    fun getAllMessages() {

        val chat1 = Chat(1)
        val chat2 = Chat(2)


        val message1 = Message(1, unread = false)
        val message2 = Message(2)
        val message3 = Message(3)

        chat1.messages += message1
        chat2.messages += listOf(message2, message3)

        chats += listOf(chat1, chat2)

        assertTrue(ChatService.getAllMessages().size == 2)


    }


    @Test(expected = ChatNotFoundException::class)
    fun getAllMessages_exception() {

        ChatService.getAllMessages()

    }


    @Test
    fun getMessagesById() {

        val chat1 = Chat(1)
        val chat2 = Chat(2)

        val message1 = Message(1, creatorID = 1)
        val message2 = Message(2, creatorID = 2)
        val message3 = Message(3, creatorID = 1)

        chat1.messages += listOf(message1, message2, message3)

        chats += listOf(chat1, chat2)

        assertTrue(ChatService.getMessagesById(1,1, 3).size == 2)

    }


    @Test(expected = ChatNotFoundException::class)
    fun getMessagesById_exception_1() {

        ChatService.getMessagesById(2, 214, 3)

    }


    @Test(expected = MessageNotFoundException::class)
    fun getMessagesById_exception_2() {
        val chat1 = Chat(1)
        val message1 = Message(1, creatorID = 1)
        chat1.messages += message1
        chats += chat1

        ChatService.getMessagesById(1, 2, 3)

    }



}
