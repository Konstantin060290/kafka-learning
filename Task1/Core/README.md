Порядок запуска проекта Task1/Core:

1. Разворачиваем кластер kafka с помощью команды 'sudo EXTERNAL_IP=localhost docker compose up -d'.
Если kafka будет разворачиваться на отдельном сервере, то вместо localhost указываем ip адрес сервера.
2. Проверяем с помощью команды docker ps, что поднялось 3 ноды kafka, и 1 контейнер с Ui и их статус up.
3. Проверяем логи всех нод kafka с помощью docker logs <container_name>, не должно быть ошибок
4. Проверяем доступность UI и брокеров: http://localhost:8080/ui/clusters/kraft/brokers
5. Создаем топик с помощью команды указанной в файле ./Infrastructure/topic.txt
6. Собираем проект mvn clean package
7. При необходимости изменяем application.properties модулей Producer, Consumer, BatchConsumer
8. Запускаем java -jar Producer-1.0-SNAPSHOT.jar
9. Запускаем java -jar SimpleConsumer-1.0-SNAPSHOT.jar
10. Отправляем post запрос в развернутый сервис Producer http://localhost:8000/api/produce для формирования сообщения
11. Проверяем, пришло ли сообщение в соответствующий топик, например через Kafka-ui
12. Смотрим в консоль SimpleConsumer как выполняется прием сообщения, не должно быть ошибок
13. Запускаем второй инстанс SimpleConsumer на другом порте, поменяв в application.properties server.port
14. Повторяем пункты по 9-11 и смотрим как ведется обработка сообщений двумя инстансами SimpleConsumer
15. Останавливаем оба инстанса SimpleConsumer
16. Запускаем java -jar BatchConsumer-1.0-SNAPSHOT.jar
17. Отправляем более 10 сообщений с помощью Producer. Смотрим, как выполняется обработка сообщений в BatchConsumer
18. Запускаем второй инстанс BatchConsumer на другом порте, поменяв в application.properties server.port
19. Повторяем отправку сообщений, смотрим, как ведется обработка сообщений двумя BatchConsumer