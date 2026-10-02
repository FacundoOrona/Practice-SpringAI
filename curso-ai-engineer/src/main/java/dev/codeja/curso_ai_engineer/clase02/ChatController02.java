package dev.codeja.curso_ai_engineer.clase02;

import dev.codeja.curso_ai_engineer.clase01.ChatRequest;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController02 {

    private static final List<String> memoria = new ArrayList<>();
    private final ChatClient chatClient;

    public ChatController02(OllamaChatModel chatModel, MessageChatMemoryAdvisor messageChatMemoryAdvisor) {

//        ChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();

//        ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                        .chatMemoryRepository(chatMemoryRepository)
//                        .maxMessages(10)
//                        .build();

//        MessageChatMemoryAdvisor messageWindowChatMemory = MessageChatMemoryAdvisor
//                .builder(chatMemory)
//                .build();

        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(messageChatMemoryAdvisor)
                .build();
    }

    @PostMapping
    public String chat(@RequestBody ChatRequest request) {

        String prompt = request.message();

        memoria.add(prompt);

        String response = chatClient.prompt()
                // system : instruccion del rol
                .system(String.format("""
                        Eres una persona argentina ubicada en la zona porteña
                        """))
                // user : mensaje que le enviamos
                .user(request.message())
                // Los ID deben ser unicos para cada conversacion
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "123"))
                .call()
                .content();

        return response;
    }

}