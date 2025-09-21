// Отправляет команду движения на сервер (U=вверх, D=вниз, L=влево, R=вправо)
function move(direction) {
    if (!connected || !stompClient) {
        addLogEntry('Нет соединения'); // Ошибка, если не подключены
        return;
    }
    if (!playerName) {
        addLogEntry('Нет имени пользователя'); // Ошибка, если имя не задано
        return;
    }
    // Проверяем, что направление валидно
    if (!['U', 'D', 'L', 'R'].includes(direction)) {
        addLogEntry(`Ошибка: Некорректное направление: ${direction}`);
        return;
    }
    try {
        // Отправляем команду движения на сервер
        stompClient.publish({
            destination: '/app/player/move', // Куда отправляем команду
            body: JSON.stringify({ direction }) // Формируем JSON, например, { "direction": "U" }
        });
        addLogEntry(`Команда: ${direction}`); // Логируем отправленную команду
    } catch (e) {
        addLogEntry(`Ошибка при отправке команды: ${e.message}`);
    }
}