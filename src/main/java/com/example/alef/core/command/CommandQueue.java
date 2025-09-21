package com.example.alef.core.command;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Сервисный компонент, представляющий потокобезопасную очередь команд.
 * Используется для хранения и обработки игровых команд в рамках игрового цикла.
 */
@Service
public class CommandQueue {

    /**
     * Очередь команд, ожидающих обработки.
     * ConcurrentLinkedQueue обеспечивает неблокирующую, потокобезопасную структуру FIFO.
     * Подходит для сценариев с множественными продюсерами и одним/несколькими потребителями.
     */
    private final ConcurrentLinkedQueue<GameCommand> commands = new ConcurrentLinkedQueue<>();

    /**
     * Возвращает ссылку на очередь команд.
     * Важно: потребители должны сами обрабатывать логику извлечения и удаления команд.
     * Метод предоставляет прямой доступ, чтобы избежать лишней обёртки и сохранить производительность.
     */
    public ConcurrentLinkedQueue<GameCommand> getCommands() {
        return commands;
    }
}
