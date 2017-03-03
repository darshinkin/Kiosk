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