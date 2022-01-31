package org.satel.eip.project14.config.server.exception;

@Deprecated
public class NotValidConfigValueException extends Exception {
    public NotValidConfigValueException(String name, int delay) {
        super(String.format("Ошибка при получении конфигурации клиента %s, клиент не вернул версию конфигурации" +
                " в ответном сообщении\n Обновление конфигурации будет повторено через %s ms", name, delay));
    }
}
