Порядок запуска проекта Task1:

1. Разворачиваем кластер kafka с помощью команды 'sudo EXTERNAL_IP=localhost docker compose up -d'.
Если kafka будет развернута на сервере, то вместо localhost указываем ip адрес сервера.
2. Проверяем логи всех нод kafka с помощью docker logs <container_name>, не должно быть ошибок
3. Проверяем доступность UI и брокеров: http://localhost:8080/ui/clusters/kraft/brokers
4. Создаем топик с помощью команды указанной в файле ./Infrastructure/topic.txt
5. 

Для развертывания кластера kafka требуется:

1. Получить KAFKA_KRAFT_CLUSTER_ID. Для этого запустить команду docker run --rm apache/kafka:3.7.0 kafka-storage.sh random-uuid
2. Записать полученыый KAFKA_KRAFT_CLUSTER_ID в docker-compose файл. 
3. Выполнить docker compose up -d в директории с созданным файлом docker-compose.yml
4. Проверить логи контейнеров с kafka с помощью команды docker logs
5. Проверить доступность brokers на странице с kafka ui.
6. Проверить то, что кластер обозначен, как online на странице с kafka ui.

Порядок запуска приложений для проверки работоспособности:

1. Запускаем docker-compose up -d в папке с докер файлом - поднимаются контейнеры кафки;
2. Запускаем Task1_Producer из под IDE (при необходимости меняем Id брокеров);
3. Собираем Task1_Consumer с помощью комадны D:\Repos\kafka-learning\Task1_Consumer\Task1_Consumer> "D:\Programs\maven-mvnd-1.0.2-windows-amd64\bin\mvnd.exe" clean package
   предварительно перейдя в папку D:\Repos\kafka-learning\Task1_Consumer\Task1_Consumer. Пути при этом подменить на свои (в том числе путь к maven);
   Аналогично п.2 - при необходимости меняем Id брокеров.
5. Аналогично собираем Task1_BatchConsumer;
6. Запускаем Task1_Consumer так D:\Repos\kafka-learning\Task1_Consumer\Task1_Consumer>"C:\Program Files\Java\jdk-24\bin\java.exe" -jar target/Task1_Consumer-1.0-SNAPSHOT.jar
   предварительно перейдя в D:\Repos\kafka-learning\Task1_Consumer\Task1_Consumer>
7. Аналогично запускаем Task1_BatchConsumer;
8. Аналогично запускаем вторые экземпляры консьюмеров;
9. Продьюсим сообщение запросом через браузер http://localhost:8080/api/produce-message
10. Смотрим в консоли консьюмеров как идет обработка сообщений.
