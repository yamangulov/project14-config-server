package org.satel.eip.project14.config.server.exception;

@Deprecated
public class NotValidRefreshStatusException extends Exception{
    public NotValidRefreshStatusException(String name, int status, int delay) {
        super(String.format("Ошибка при запросе на обновление конфигурации клиента %s, клиент вернул статус %s\n " +
                        "Запрос на обновление конфигурации будет повторен через %s ms",
                name, status, delay));
    }
}
