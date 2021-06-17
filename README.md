LangHelper - это простой API плагин, который позволяет получить любую переведенную строку клиента игры Minecraft.

На данный момент поддерживается все версии начиная с 1.12.2 до 1.17.
Плагин поддерживает все языки которые поддерживает сам Minecraft.
При первом включении, плагин скачает с сервера mojang поддерживаемые языки в папку LangHelper/languages/'версия'/

API достаточно простой, все что нужно сделать, это получить экземпляр плагина - LangHelper.getInstance() и вызвать соответствующие методы.

Пример использования:
```
String translateItem = LangHelper.getInstance().getItemTranslate(new ItemStack(Material.STONE), LangType.RU_RU); // Получить русский перевод каменного блока
String translateEnchant = LangHelper.getInstance().getEnchantmentName(Enchantment.LUCK, LangType.RU_RU); // Получить русский перевод зачарования на удачу
String translateEntity = LangHelper.getInstance().getEntityTranslate(EntityType.CREEPER, LangType.RU_RU); // Получить русский перевод сущности крипера
System.out.println("Переводы: " + translateItem + " " + translateEnchant + " " + translateEntity);
```
Все методы никогда не вернут null. Если по каким-то причинам перевода нет, будет возвращена отформатированная строка содержащая английский перевод от самого сервера.
