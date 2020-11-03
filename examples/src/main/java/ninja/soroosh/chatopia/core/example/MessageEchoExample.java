package ninja.soroosh.chatopia.core.example;

import ninja.soroosh.chatopia.core.annotation.ChatController;
import ninja.soroosh.chatopia.core.annotation.OnCommand;
import ninja.soroosh.chatopia.core.annotation.OnEvent;
import ninja.soroosh.chatopia.core.runner.*;
import ninja.soroosh.chatopia.core.runner.responses.Response;
import ninja.soroosh.chatopia.core.session.Session;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@ChatController
public class MessageEchoExample {
    @OnCommand(value = "echo", help = "This command echo")
    public Response onEchoCommand(String message, Context context) {
        return Response.asText("echo " + message);
    }

    @OnCommand(value = "echo *", help = "This command echo")
    public Response onEchoStarCommand(String message, Context context) {
        return Response.asText(message.substring(5));
    }

    @OnCommand(value = "hi", help = "start a chat")
    public Response onHiCommand(String message, Context context) {
        final Session session = context.getSession().get();
        final Optional<String> previousCount = session.get("count");
        final String currentCount = previousCount.map(strCount -> String.valueOf(Integer.parseInt(strCount) + 1))
                .orElse("1");
        session.set("count", currentCount);

        return Response.asText("Hi, How are you?\nyour session Id is: "
                + context.getSessionId() +
                "\nyou are on channel: " + context.getChannel() +
                "\nyou have called me: " + currentCount + " times");
    }

    @OnCommand(value = "options", help = "A showcase to list options")
    public Response onOptionsCommand(String message, Context context) {
        return Response
                .asText("Here are the options")
                .withOptions(
                        List.of(
                                new CallbackDataOption("option1", "options"),
                                new CallbackURLOption("option2", "https://google.com")
                        )
                );
    }

    @OnCommand(value = "photo", help = "A showcase to send a photo")
    public Response onPhotoCommand(String message, Context context) {
        return Response.asPhoto(this.getClass().getClassLoader().getResourceAsStream("bird.jpg"));
    }

    @OnCommand(value = "photo *", help = "Load photo as message in telegram")
    public Response onPhCommand(String message, Context context) throws MalformedURLException {
        final var url = message.substring(6);
        return Response
                .asPhoto(url)
                .withCaption("chetoram?");
    }

    @OnCommand(value = "video", help = "A showcase to send a video")
    public Response onVideoCommand(String message, Context context) {
        return Response.asVideo(this.getClass().getClassLoader().getResourceAsStream("buongiorno.mp4"));
    }

    @OnEvent("NEW_CHAT_MEMBER")
    public Response onNewMember(Event<UserEventPayload> event, Context context) {
        return Response.asText("Khosh oomadi lanati %s!".formatted(event.getPayload().getFirstName()));
    }
}
