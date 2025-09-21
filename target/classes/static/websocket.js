import { initVisibilityTracking } from './visibilityTracker.js';

export function initWebSocket({
  onPositionUpdate,
  onVisibilityUpdate, // ✅ добавлено
  onLog,
  scene
}) {
  let stompClient = null;
  let subscriptions = {};
  let userName = null;

  function log(msg) {
    if (onLog) onLog(msg);
  }

  const ws = new WebSocket("ws://localhost:8080/ws-game");
  stompClient = Stomp.over(ws);
  stompClient.debug = str => log("STOMP: " + str);

  stompClient.connect({}, frame => {
    log("Connected to WebSocket");
    userName = frame.headers['user-name'];

    // Подписка на события игрока
    const eventDest = `/user/${userName}/queue/events`;
    const eventSub = stompClient.subscribe(eventDest, msg => {
      try {
        const payload = JSON.parse(msg.body);

        if (payload.playerPosition && payload.playerPosition.x !== undefined) {
          onPositionUpdate?.({ x: payload.playerPosition.x, y: payload.playerPosition.y });
        } else if (payload.x !== undefined && payload.y !== undefined) {
          onPositionUpdate?.({ x: payload.x, y: payload.y });
        } else {
          log(`Unknown payload: ${msg.body}`);
        }
      } catch (e) {
        log(`Failed to parse: ${msg.body}`);
      }
    });

    subscriptions[eventDest] = eventSub;
    log(`Subscribed to ${eventDest}`);

    // Подписка на видимость через внешний модуль
    const { visibilityDest, visibilitySub } = initVisibilityTracking({
      stompClient,
      log,
      scene,
      userName,
      onVisibilityUpdate // ✅ передано
    });

    subscriptions[visibilityDest] = visibilitySub;
    log(`Subscribed to ${visibilityDest}`);

    initPlayer();
  }, error => log("Connection error: " + error));

  function initPlayer() {
    if (stompClient) stompClient.send("/app/player/init", {}, {});
  }

  function movePlayer(direction) {
    if (stompClient) stompClient.send("/app/player/move", {}, JSON.stringify({ direction }));
  }

  function showSubscriptions() {
    Object.keys(subscriptions).forEach(k => log(`Subscription: ${k}`));
  }

  return { initPlayer, movePlayer, showSubscriptions };
}
