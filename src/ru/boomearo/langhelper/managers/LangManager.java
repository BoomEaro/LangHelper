package ru.boomearo.langhelper.managers;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.objects.LangData;
import ru.boomearo.langhelper.objects.LangType;
import ru.boomearo.langhelper.objects.LangVersion;
import ru.boomearo.langhelper.objects.Translate;

public class LangManager {

	private static ConcurrentMap<LangVersion, LangData> language = new ConcurrentHashMap<LangVersion, LangData>();

	public static void load() {
		try {
			File folders = new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
			if (!folders.exists()) {
				folders.getParentFile().mkdirs();
			}

			if (!folders.isDirectory()) {
				return;
			}
			LangHelper.getInstance().getLogger().info("Загружаем языки..");
			long start = System.currentTimeMillis();
			ConcurrentMap<LangVersion, LangData> lang = new ConcurrentHashMap<LangVersion, LangData>();
			File[] versions = folders.listFiles();
			if (versions != null) {
				for (File ver : versions) {
					if (ver.isDirectory()) {
						LangVersion lv = null;
						try {
							lv = LangVersion.valueOf(ver.getName());
						}
						catch (Exception e) {}
						if (lv != null) {
							LangHelper.getInstance().getLogger().info("Загрузка языков версии '" + lv.getName() + "'");
							ConcurrentMap<LangType, Translate> types = new ConcurrentHashMap<LangType, Translate>();
							File[] type = ver.listFiles();
							if (type != null) {
								for (File t : type) {
									if (t.isFile()) {
										LangType lt = null;
										try {
											lt = LangType.valueOf(t.getName());
										}
										catch (Exception e) {}
										if (lt != null) {
											LangHelper.getInstance().getLogger().info("Загрузка языка '" + lt.getName() + "' версии '" + lv.getName() + "'");
											ConcurrentMap<String, String> translates = new ConcurrentHashMap<String, String>();
											if (lv.getProtocol() <= LangVersion.V1_12_2.getProtocol()) {
												for (String line : com.google.common.io.Files.readLines(t, StandardCharsets.UTF_8)) {
													if (line.isEmpty()) {
														continue;
													}

													String[] args = line.split("=");
													if (args.length >= 2) {
														translates.put(args[0].toLowerCase(), args[1]);
													}
												}
											}
											else {
												JSONParser jsonParser = new JSONParser();
												try (FileReader reader = new FileReader(t)) {

													Object obj = jsonParser.parse(reader);

													if (obj instanceof JSONObject) {
														JSONObject o = (JSONObject) obj;


														@SuppressWarnings("unchecked")
														Set<Entry<Object, Object>> s = (Set<Entry<Object, Object>>) o.entrySet();
														for (Entry<Object, Object> f : s) {
															translates.put(f.getKey().toString(), f.getValue().toString());
														}
													}

												} 
												catch (Exception e) {
													e.printStackTrace();
												}
											}
											types.put(lt, new Translate(translates));
											LangHelper.getInstance().getLogger().info("Загружено " + translates.size() + " строк языка '" + lt.getName() + "' версии '" + lv.getName() + "'");
										}
									}
								}
							}
							lang.put(lv, new LangData(types));
						}
					}
				}
			}
			language = lang;
			long end = System.currentTimeMillis();
			LangHelper.getInstance().getLogger().info("Загрузка успешно завершена за " + (end - start) + " мс!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getItemName(ItemStack item, LangVersion version, LangType type) {
		net.minecraft.server.v1_12_R1.ItemStack itemStack = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);

		String name = itemStack.getItem().a(itemStack) + ".name";
		return getTranslate(name, version, type);
	}

	public static String getEntityName(EntityType entity, LangVersion version, LangType type) {
		String name = "entity." + entity.name() + ".name";
		return getTranslate(name, version, type);
	}

	//Похоже что на 1.12 название в файле и на сервере отличаются, поэтому до 1.13 не рекомендуется юзать
	public static String getEnchantName(Enchantment enchant, LangVersion version, LangType type) {
		String name = "enchantment." + enchant.getName();
		return getTranslate(name, version, type);
	}

	public static String getEnchantLevelName(int level, LangVersion version, LangType type) {
		String name = "enchantment.level." + level;
		return getTranslate(name, version, type);
	}

	private static String getTranslate(String name, LangVersion version, LangType type) {
		LangData data = language.get(version);
		if (data != null) {
			Translate tr = data.getTranslate(type);
			if (tr != null) {
				return tr.getTranstale(name.toLowerCase().replace("_", ""));
			}
		}
		return null;
	}
}
