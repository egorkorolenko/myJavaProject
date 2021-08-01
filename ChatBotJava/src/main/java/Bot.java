import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "MyFirstTelegramWithUniqueNameBot";
    }

    @Override
    public String getBotToken() {
        return "1871574234:AAF3aN1OUMeBsMAxmdAdw1E_xwLnJQAxYQE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Model model = new Model();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMess(message, "Чем могу помочь?");
                    break;
                case "/setting":
                    sendMess(message, "Что будем настраивать?");
                    break;
                default:
                    try {
                        sendMess(message,Weather.getWeather(message.getText(),model));
                    } catch (IOException e) {
                        sendMess(message,"Город не найден");
                    }
            }
        }
    }

    public void sendMess(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboard);
        replyKeyboard.setSelective(true);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();

        keyboardButtons.add(new KeyboardButton("/help"));
        keyboardButtons.add(new KeyboardButton("/settings"));

        keyboardRowList.add(keyboardButtons);
        replyKeyboard.setKeyboard(keyboardRowList);
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
