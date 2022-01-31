package org.satel.eip.project14.config.server.exception;

public class NotValidConfigStatusException extends Exception{
    public NotValidConfigStatusException(String name, int status, int delay) {
        super(String.format("Ошибка при получении конфигурации клиента %s, клиент вернул статус %s\n " +
                        "Обновление конфигурации будет повторено через %s ms",
                name, status, delay));
    }
}
