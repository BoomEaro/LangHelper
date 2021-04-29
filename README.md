LangHelper - это простой API плагин, который позволяет получить любую переведенную строку клиента игры Minecraft.
На данный момент поддерживается только 1.12.2 и 1.16.5 версии а также только русский и английский язык (не проблема добавить еще)

Чтобы найти файлы этих самых переведенных строк, откройте корневую папку игры '../.minecraft/', откройте папку 'indexes', перед вами появятся .json файлы установленных вами версий игры.
Откровайте нужный, желательно через notepad++, ищите строку "ru_ru" или соответсвущий перевод. После этой строки будет строка hash, копируйте его содержимое.
Переходите в папку 'objects', ищите файл который называется этим самым хешом. Это и будет тот самый файл перевода. 
Чтобы плагин смог воспользоваться переводом, скопируйте его в папку /LangHelper/languages/'версия'/'язык'. 

Плагин из под коробки уже имеет существующие переводы под текущие поддерживаемые версии. 

API достаточно простой, все что нужно сделать, это получить экземпляр плагина - LangHelper.getInstance() и вызвать соответвующие методы.

Пример использования:
```
String translateItem = LangHelper.getInstance().getItemTranslate(new ItemStack(Materia.STONE), LangType.ru); // Получить русский перевод каменного блока
String translateEnchant = LangHelper.getInstance().getEnchantmentName(Enchantment.LUCK, LangType.ru); // Получить русский перевод зачарования на удачу
String translateEntity = LangHelper.getInstance().getEntityTranslate(EntityType.CREEPER, LangType.ru); // Получить русский перевод сущности крипера
System.out.println("Переводы: " + translateItem + " " + translateEnchant + " " + translateEntity);
```
Все методы никогда не вернут null. Если по каким то причинам перевода нет, будет возвращено отформатированное название английского перевода.
