## Задание 1.Развёртывание и настройка Kafka-кластера в Yandex Cloud

В ходе выполнения работы, был развернут кластер 'Managed Service for Kafka' из 3 брокеров в Yandex Cloud.

Характеристики кластера:

![cluster.png](Task7_1/images/cluster.png)

![cluster2.png](Task7_1/images/cluster2.png)

- Был создан топик, настроено время жизни сегмента лога, размер файла сегмента:

![topic.png](Task7_1/images/topic.png)

- Были созданы пользователи, им назначены права на топик test-topic:

![users.png](Task7_1/images/users.png)

- Зарегистрирована схема данных:

![scheme.png](Task7_1/images/scheme.png)

- Ответы get запросов схем:

![scheme_versions.png](Task7_1/images/scheme_versions.png)

![scheme_1.png](Task7_1/images/scheme_1.png)

![scheme_2.png](Task7_1/images/scheme_2.png)

- Были реализованы простой консьюмер и продьюсер, читающий и записывающие соответственно данные в кластер, развернутый в Yandex Cloud.
- Лог продсьюсера:

![producer.png](Task7_1/images/producer.png)

- Лог консьюмера:
![consumer.png](Task7_1/images/consumer.png)

### Выводы: Yandex Cloud предлагает удобные и мощные инструменты с простым и понятным UI для разворачивания инфраструктуры в облаке.
Данные инструменты существенно сокращают трудозатраты на развертывание, сопровождение и поддержку систем, а также сокращают требования
к квалификации задействованного в обслуживании систем персонала.

## Задание 2. Интеграция Kafka с внешними системами (Apache NiFi / Hadoop)

Для выполнения второй работы была выбрана технология Apache NiFi. 
Выполнена процедура ее развертывания, настройки и подключения к кластеру
Kafka в Yandex Cloud. Создан отдельный топик.
Написан простой консьюмер читающий данные, полученные из csv файла.

Запущенный сервис NiFi:
![Nifi.png](Task7_2/Images/Nifi.png)

Характеристики процессора, зачитывающего csv файл:
![GetFileProcessor.png](Task7_2/Images/GetFileProcessor.png)

Характеристики процессора, отправляющего данные в kafka:
![KafkaProcessor.png](Task7_2/Images/KafkaProcessor.png)

Сервисы контроллера:
![Services.png](Task7_2/Images/Services.png)

Запущенный консьюмер:
![Consumer.png](Task7_2/Images/Consumer.png)

Факт переданного сообщения:
![MessageFact.png](Task7_2/Images/MessageFact.png)

Наличие сообщений в топике-2:

![MessageInTopic2.png](Task7_2/Images/MessageInTopic2.png)

### Выводы:
Примененный инструмент NiFi позволяет осуществить передачу данных в Kafka из различных источников, например текстового файла.
Что может быть полезно при необходимости отправки сообщений из систем, отправка данных из которых напрямую в Kafka невозможна.