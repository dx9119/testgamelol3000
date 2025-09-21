export function createUI({ onInit, onMove, onShowSubs }) {
  const container = document.getElementById('ui-container');
  const logEl = document.getElementById('log');

  let isPaused = false;
  const maxLines = 200;

  // Создаём кнопку с текстом и обработчиком
  function createButton(text, cb) {
    const btn = document.createElement('button');
    btn.textContent = text;
    btn.onclick = cb;
    container.appendChild(btn);
  }

  // Добавляем кнопки управления
  createButton("Init Player", onInit);
  createButton("Move UP", () => onMove('U'));
  createButton("Move DOWN", () => onMove('D'));
  createButton("Move LEFT", () => onMove('L'));
  createButton("Move RIGHT", () => onMove('R'));
  createButton("Show Subscriptions", onShowSubs);

  // Кнопка паузы логов
  createButton("Pause Logs", () => {
    isPaused = !isPaused;
    log(`Логирование ${isPaused ? 'приостановлено' : 'возобновлено'}`);
  });

  // Функция логирования с форматированием
  function log(msg) {
    if (isPaused) return;

    const timestamp = new Date().toLocaleTimeString();
    const formatted = `[${timestamp}] ${msg}`;

    // Добавляем новую строку
    logEl.textContent += formatted + "\n";

    // Ограничиваем количество строк
    const lines = logEl.textContent.split("\n");
    if (lines.length > maxLines) {
      logEl.textContent = lines.slice(-maxLines).join("\n");
    }

    // Прокручиваем вниз
    logEl.scrollTop = logEl.scrollHeight;
  }

  return { log };
}
