LangHelper - это простой API плагин, который позволяет получить любую переведенную строку клиента игры Minecraft.

На данный момент поддерживается все версии начиная с 1.12.2 до 1.16.5.
Поддерживается только русский язык, однако без проблем можно добавить остальные.
При первом включении, плагин скачает с сервера mojang поддерживаемые языки в папку LangHelper/languages/'версия'/

API достаточно простой, все что нужно сделать, это получить экземпляр плагина - LangHelper.getInstance() и вызвать соответвующие методы.

Пример использования:
```
String translateItem = LangHelper.getInstance().getItemTranslate(new ItemStack(Material.STONE), LangType.ru); // Получить русский перевод каменного блока
String translateEnchant = LangHelper.getInstance().getEnchantmentName(Enchantment.LUCK, LangType.ru); // Получить русский перевод зачарования на удачу
String translateEntity = LangHelper.getInstance().getEntityTranslate(EntityType.CREEPER, LangType.ru); // Получить русский перевод сущности крипера
System.out.println("Переводы: " + translateItem + " " + translateEnchant + " " + translateEntity);
```
Все методы никогда не вернут null. Если по каким-то причинам перевода нет, будет возвращена отформатированная строка содержащая английский перевод от самого сервера.
