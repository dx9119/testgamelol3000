// Добавляет сообщение в лог событий
function addLogEntry(message) {
    const log = document.getElementById('eventLog'); // Находим область лога
    if (!log) {
        console.error('Ошибка: Контейнер лога событий не найден');
        return;
    }
    const entry = document.createElement('div'); // Создаем новую запись
    entry.textContent = `[${new Date().toLocaleTimeString()}] ${message}`; // Добавляем время и текст
    log.appendChild(entry); // Добавляем запись в лог
    log.scrollTop = log.scrollHeight; // Прокручиваем лог вниз
}