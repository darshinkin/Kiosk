### Настройки config.properties

#### getVersion
* /getVersion/sbg/ios --> ios.sbg.release
* /getVersion/friend/ios --> ios.friend.release
* /getVersion/friend/android --> android.friend.release

#### file
* file/ios/sbg --> ios.sbg.plist
* file/android/sbg --> нет файла
* file/android/friend --> android.friend
* file/ios/friend --> ios.friend.plist

Есть рест сервис по урлу https://{host}/rest/file/ios/sbg/ipa
Он закачивает файл из файловой системы сервера
Путь файла мапится к ключу в формате os.app.filetype
Пример: ios.sbg.ipa=/FriendKiosk/sbg/ios/sbg.ipa

Сборка выполняется командой мавена из корня проекта:
mvn clean package -Pear
Деплой в WAS производиться копирование собранного ear файла в специальную директорию для приложений.
Например, собранный архив AppKoiskEAR.ear нужно скопировать на сервер sbt-oapou-034.sigma.sbrf.ru в директорию D:\deploy\servers\server1
Это касается для остальных архивов: KioskFriendDevWeb.ear, KioskFriendWeb.ear, SbgWeb.ear
