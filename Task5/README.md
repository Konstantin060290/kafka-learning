Инструкции по запуску проекта через Docker Compose:

1. Скопируйте полностью структуру модуля Infrastructure на машину, на которой будете разворачивать 
инфраструктуру.
2. C помощью команды 'sudo EXTERNAL_IP=localhost docker compose up -d' разверните инфраструктуру.
Если инфраструктура будет разворачиваться на отдельном сервере, то вместо localhost укажите ip адрес сервера.
3. Сконфигурируйте коннектор к базе данных с помощью команды:
curl -X PUT -H 'Content-Type: application/json' --data @connector-truncate.json http://localhost:8083/connectors/pg-connector/config
4. Проверьте статус коннектора в debezium ui: http://localhost:8080/ Статус коннектора должен быть Running.
5. Заполните таблицы users, orders, базы данных task5 данными с помощью скрипта:
   INSERT INTO public.users(
   id, name, email, created_at)
   VALUES (1, 'Alex', 'Alex@email.com', TIMESTAMP '2023-05-15 14:30:00'),
   (2, 'Dian', 'Dian@email.com', TIMESTAMP '2023-05-15 14:40:00'),
   (3, 'Xenia', 'Xenia@email.com', TIMESTAMP '2023-05-15 14:50:00');

    INSERT INTO public.orders(
    id, user_id, product_name, quantity, order_date)
    VALUES (1, 1, 'TestProduct1', 2, TIMESTAMP '2023-05-12 14:30:00'),
    (2, 2, 'TestProduct2', 3, TIMESTAMP '2024-05-12 14:30:00');
6. Убедитесь что в kafka создались топики task5.public.users, task5.public.orders и в них есть соответствующие скрипту сообщения
в формате json. Проверку можно выполнить с помощью kafka-ui: http://localhost:8085/ui/clusters/kraft/all-topics
7. Соберите проект  mvn clean install
8. Внесите необходимые изменения в application.properties, в частности для настройки kafka.cluster.bootstrap-servers
замените localhost на ip адрес машины с развернутой kafka, если это требуется.
9. Запустите java -jar ConsumersApp-1.0-SNAPSHOT.jar
10. Добавьте в таблицы users, orders данные с помощью скриптов, аналогичных п. 5.
11. В консоли приложения ConsumersApp должны появляться записи о прочтенных в kafka сообщениях.