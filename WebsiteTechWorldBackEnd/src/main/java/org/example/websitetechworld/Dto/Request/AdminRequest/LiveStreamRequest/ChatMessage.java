package org.example.websitetechworld.Dto.Request.AdminRequest.LiveStreamRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessage {

    private String user;
    private String content;
}
