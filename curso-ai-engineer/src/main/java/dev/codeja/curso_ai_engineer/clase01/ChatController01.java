package dev.codeja.curso_ai_engineer.clase01;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/claseuno/chat")
public class ChatController01 {

    private final ChatClient chatClient;

    public ChatController01(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @PostMapping
    public String chat(@RequestBody ChatRequest request) {

        String response = chatClient.prompt()
                // system : instruccion del rol
                .system(String.format("""
                        Eres una persona argentina ubicada en la zona porteña
                        
                        Aqui tiene la conversacion del usuario
                        """))
                // user : mensaje que le enviamos
                .user(request.message())
                .call()
                .content();

        return response;
    }

}

/*
PS C:\Users\CYMAX> ollama
PS C:\Users\CYMAX> ollama pull llama3.2
pulling manifest
pulling dde5aa3fc5ff: 100% ▕█████████████████████████████████████████████████████████ ▏ 2.0 GB/2.0 GB  771 KB/s      0s
verifying sha256 digest
writing manifest
success
PS C:\Users\CYMAX> ollama run llama3.2
>>> hola
Hola! ¿En qué puedo ayudarte hoy?

>>>
Use Ctrl + d or /bye to exit.
>>>
PS C:\Users\CYMAX> ollama list
NAME               ID              SIZE      MODIFIED
llama3.2:latest    a80c4f17acd5    2.0 GB    About a minute ago
PS C:\Users\CYMAX> ollama run llama3.2
>>> /bye
PS C:\Users\CYMAX>
 */
