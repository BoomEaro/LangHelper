## LangHelper

LangHelper - это простой API плагин, который позволяет получить любую переведенную строку клиента игры Minecraft.

Поддерживаются версии Minecraft:
1.12.2,
1.13.2,
1.14.4,
1.15.2,
1.16.5,
1.17.1,
1.18.2,
1.19.4,
1.20,
1.20.1,
1.20.2,
1.20.3,
1.20.4,
1.20.5,
1.20.6,
1.21,
1.21.1,
1.21.2,
1.21.3,
1.21.4,
1.21.5,
1.21.6,
1.21.7,
1.21.8,
1.21.9,
1.21.10,
1.21.11

Плагин поддерживает все языки которые поддерживает сам Minecraft.
При первом включении, плагин скачает с сервера Mojang поддерживаемые языки в папку LangHelper/languages/'версия'/

## API

API достаточно простой, все что нужно сделать, это получить TranslateManager через которого можно извлекать переводы.

Пример использования:

```
TranslateManager translateManager = LangHelperApi.getTranslateManager();
String translateItem = translateManager.getItemNameSafe(new ItemStack(Material.STONE), LangType.RU_RU); // Получить русский перевод каменного блока
String translateEnchant = translateManager.getEnchantmentNameSafe(Enchantment.LUCK, LangType.RU_RU); // Получить русский перевод зачарования на удачу
String translateEntity = translateManager.getEntityNameSafe(EntityType.CREEPER, LangType.RU_RU); // Получить русский перевод сущности крипера
String translatePotionEffect = translateManager.getPotionEffectNameSafe(PotionEffectType.REGENERATION, LangType.RU_RU); // Получить русский перевод эффекта зелья регенерации
String translateBiome = translateManager.getBiomeNameSafe(Biome.DESERT, LangType.RU_RU); // Получить русский перевод биома пустыни

System.out.println("Предмет: " + translateItem);
System.out.println("Зачарование: " + translateEnchant);
System.out.println("Сущность: " + translateEntity);
System.out.println("Эффект зелья: " + translatePotionEffect);
System.out.println("Биом: " + translateBiome);
```

Все методы никогда не вернут null. Если по каким-то причинам перевода нет, будет возвращена отформатированная строка
содержащая английский перевод от самого сервера.

## Компиляция

Проект использует систему сборки Gradle и версию java 17.

Для компиляции достаточно использовать команду:

```
gradlew build
```

Скомпилированный плагин будет находиться в директории LangHelper/plugin/build/libs/LangHelper.jar


