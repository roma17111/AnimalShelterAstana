<h1 align="center">Telegram bot Asha</h1>



<h2 align="center"><a  href="https://t.me/skypro_s5_1_shelter_bot">Meet Asha</a></h2>
<p align="center">
<img src="https://media.giphy.com/media/Rdx8SHjHhiVUI/giphy.gif" width="20%"></p>

## About

Asha is a test Spring-Boot application integrated with a telegram bot, which we made as part of the "Java Developer 2.0"
training course from SkyPro.

## How to use

### Press /start on the bot menu and select a command for the bot, then follow its directions to find your fluffy friend.

- **Asha** will tell you about the animal shelter in Astana and its inhabitants.
- **Asha** will explain how to behave with animals.
- **Asha** will explain how to visit the animals and how to take care of them.
- **Asha** can issue the rules for getting to know the dog before you can pick it up from the shelter.
- **Asha** can issue a list of documents required in order to adopt a dog or cat from a shelter.
- **Asha** can issue a list of recommendations for transporting the animal.
- **Asha** can give a list of recommendations for home improvement for a puppy.
- **Asha** can give a list of recommendations for home improvement for an adult animal.
- **Asha** can issue a list of recommendations for home improvement for a animal with disabilities (vision, movement).
- **Asha** can give advice to the cynologist on the initial communication with the animal.
- **Asha** can issue recommendations on proven dog handlers for further referral to them.
- **Asha** can accept and record contact information for communication.
- 
  Команды волонтёров:
- 
- /admin - Список всех команд волонтёров
- 
- /users - посмотреть список пользователей
- 
  /send - Отправить сообщение о плохом отчёте всем пользователям,
  у которых текст сообщений в отчётах очень короткий. Отчёты нухно писать подробно.
- 
  /allquestions - Посмотреть список вопросов от пользователей
- 
  /allreports - Посмотреть список отчётов от усыновителей питомцев
- 
  /badreport{id} - отправить шаблон сообщения о конкретном отчёте - плохой отчёт.
  id - id отчёта из базы данных.
  Пример: /badreport 1
- 
  /message{id} - Отправить сообщение пользователю из базы по id
  Пример: /message12 Привет. Как дела?
- 
  /answer{id} - Ответить пользователю по id оставленного вопроса из базы
  Пример: /answer12 Пока этой породы нет.
- 
  /adddog - Добавить собаку
- 
  /addcat - Добавить кошку/кота
- 
  /congratulations {id} Поздравить усыновителя с успешным прохождением испытательного срока
  Где id - id отчёта из БД
  пример: /congratulations 1
- 
  /continue14 {id} - Продлить испытательный срок на 14 дней
  Где id - id отчёта из БД
  Пример: /continue14 25
- 
  /continue30 {id} - Продлить испытательный срок на 30 дней
  Где id - id отчёта из БД
  Пример: /continue14 25
- 
  /takepet {id} - Отправить сообщение об изъятии собаки у усыновителя
  Где id - id отчёта из БД
  Пример: /takepet 25
Д
## Our development team

- [Роман Якименко](https://github.com/roma17111)
- [Андрей Зинченко](https://github.com/astrekoi)
- [Вероника Якименко](https://github.com/verkin78)
- [Владимир Сясин](https://github.com/Vsvvn)
- [Людмила Скобелева](https://github.com/SkobelevaLuda)
- [Андрей Назаренко](https://github.com/Altanim)

___

## Used in the project

- **Java 11**
- **Maven**
- **Spring Boot**
- **Spring Web**
- **Stream API**
- **Spring Data**
- **Spring JPA**
- **Hibernate**
- **PostgreSQL**

<p align="center">
<img src="https://media.giphy.com/media/12RZ46nUQ57Ihq/giphy.gif" width="20%"></p>
